(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addComplaintCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
		initAddComplaint();
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Complaint.',close:true };
			$scope.showAlert = false;
			$scope.companies = [];
			$scope.branches = [];
			$scope.cutomers=[];
			function initAddComplaint() {
				$scope.customerSelected = false;
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomer = {};
				$scope.selectedLift = {};
				$scope.companyName='';
				$scope.branchName='';
				$scope.addComplaint={
						branchCompanyMapId:0,
						liftCustomerMapId:0,
						branchCustomerMapId:0,
						companyId:0,
						complaintsTitle:'',
						complaintsRemark:'',
						registrationType:2,
						fromDate:'',
						toDate:''
				};
			}
			$scope.openFlag={
					fromDate:false,
					toDate:false
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.openFlag[which] != true)
			    	  $scope.openFlag[which] = true;
			      else
			    	  $scope.openFlag[which] = false;
			  }
			//load compay dropdown data
			/*function loadCompayInfo(){
				serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
			    .then(function(response){
			    		$scope.companies = response;
			    });
			};
			$scope.loadBranchData = function(){
				var data = {
					companyId : $scope.selectedCompany.selected.companyId
				}
			    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',data)
			    .then(function(response){
			    	$scope.branches = response;
			    	
			    });
			}
			$scope.loadCustomerData = function(){
				var branchData ={};
	  	    	if($scope.showBranch == true){
	  	    		branchData = {
	  	    			branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
						}
	  	    	}else{
	  	    		branchData = {
	  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
						}
	  	    	}
	  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
	 	         .then(function(customerData) {
	 	        	 $scope.cutomers = customerData;
	 	         })
			}*/
			$scope.loadLifts = function() {
				
				var dataToSend = {
					//branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})
				
				serviceApi.doPostWithData('/RLMS/complaint/getCustomerDtlsById',dataToSend)
						.then(function(data) {
							$scope.customerSelected = true;
							$scope.companyName = data.companyName;
							$scope.branchName = data.branchName
						})
			}
			//Post call add customer
			$scope.submitAddComplaint = function(){
				$scope.addComplaint.liftCustomerMapId = $scope.selectedLift.selected.liftId;
				$scope.addComplaint.registrationType = 31;
				serviceApi.doPostWithData("/RLMS/complaint/validateAndRegisterNewComplaint",$scope.addComplaint)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddComplaint();
					$scope.addComplaintForm.$setPristine();
					$scope.addComplaintForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			 //showCompnay Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				//loadCompayInfo();
			}else{
				$scope.showCompany= false;
				//$scope.loadBranchData();
			}
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
				$scope.showBranch= true;
			}else{
				$scope.showBranch=false;
			}
			//rese add branch
			$scope.resetaddComplaint = function(){
				initAddComplaint();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
			$scope.searchCustomer = function(query){
				//console.log(query);
				if(query && query.length > 1){
				 var dataToSend = {
				 	'customerName':query
				 }
					serviceApi.doPostWithData("/RLMS/complaint/getCustomerByName",dataToSend)
					.then(function(customerData){
						//console.log(customerData);
						 $scope.cutomers = customerData;
					},function(error){
						
					});
				} 
				
			}
	}]);
})();
