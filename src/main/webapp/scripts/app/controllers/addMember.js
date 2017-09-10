(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addMemberCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope) {
	initAddMember();
			loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Member.',close:true };
			//loadBranchListInfo();
			$scope.showAlert = false;
			$scope.companies = [];
			$scope.branches = [];
			function initAddMember(){
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.selectedCustomer = {};
				$scope.addMember={
						firstName:'',
						lastName:'',
						address:'',
						city:'',
						area:'',
						pinCode:'',
						emailId:'',
						contactNumber:0,
						branchCustoMapId:0
						
				};	
			}
			//load compay dropdown data
			function loadCompayInfo(){
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
		    		$scope.selectedBranch.selected=undefined;
		    		$scope.selectedCustomer.selected=undefined;
			    	
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
	 	        	$scope.selectedCustomer.selected=undefined;
	 	         })
			}
			//Post call add customer
			$scope.submitAddCustomer = function(){
				$scope.addMember.branchCustoMapId = $scope.selectedCustomer.selected.branchCustomerMapId;
				serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewMember",$scope.addMember)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddMember();
					$scope.addMemberForm.$setPristine();
					$scope.addMemberForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			 //showCompnay Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				loadCompayInfo();
			}else{
				$scope.showCompany= false;
				$scope.loadBranchData();
			}
		  	
		  	//showBranch Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
				$scope.showBranch= true;
			}else{
				$scope.showBranch=false;
			}
			//rese add branch
			$scope.resetAddMember = function(){
				initAddMember();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
