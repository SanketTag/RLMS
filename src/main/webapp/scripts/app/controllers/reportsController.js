(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('reportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.goToAddAMC = function(){
			window.location.hash = "#/add-amc";
		}
		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			 $scope.lifts=[];
			 $scope.branches = [];
			 $scope.selectedCustomer = {};
			 $scope.selectedStatus = {};
			 $scope.selectedLift = {};
			 $scope.selectedAmc = {};
			 $scope.showMembers = false;
			 $scope.status = [ {
					id : 2,
					name : 'Pending'
				}, {
					id : 3,
					name : 'Assigned'
				}, {
					id : 4,
					name : 'Completed'
				}, {
					id : 5,
					name : 'Resolved'
				} ];
			 
		} 
		// showCompnay Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
			$scope.showCompany = true;
			loadCompanyData();
		} else {
			$scope.showCompany = false;
			$scope.loadBranchData();
		}
		
		// showBranch Flag
		if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
			$scope.showBranch = true;
		} else {
			$scope.showBranch = false;
		}
		function loadCompanyData() {
			serviceApi
					.doPostWithoutData(
							'/RLMS/admin/getAllApplicableCompanies')
					.then(function(response) {
						$scope.companies = response;
					});
		}
		$scope.loadBranchData = function() {
			var companyData = {};
			if ($scope.showCompany == true) {
				companyData = {
					companyId : $scope.selectedCompany.selected.companyId
				}
			} else {
				companyData = {
					companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
				}
			}
			serviceApi
					.doPostWithData(
							'/RLMS/admin/getAllBranchesForCompany',
							companyData)
					.then(function(response) {
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
		}
		//Show Member List
		$scope.loadReportList = function(){
 	         var dataToSend = constructDataToSend();
 	         serviceApi.doPostWithData('/RLMS/report/getSiteVisitReport',dataToSend)
 	         .then(function(data) {
 	        	 $scope.siteViseReport = data;
 	         })
			$scope.showMembers = true;
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
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		var tempStatus = [];
	  		if($scope.selectedStatus.selected.length){
	  			for (var j = 0; j < $scope.selectedStatus.selected.length; j++) {
					tempStatus.push($scope.selectedStatus.selected[j].id);
				}
	  		}
	  		
			
	  		var tempbranchCustomerMapIds = [];
			if($scope.selectedCustomer.selected.length > 0){
				for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
					tempbranchCustomerMapIds.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
				}
			}
			

	  		var data = {
	  				companyBranchMapId:$scope.selectedBranch.selected.companyBranchMapId,
	  				//companyId:9,
	  				//listOfUserRoleIds:tempListOfUserRoleIds,
	  				listOfStatusIds:tempStatus,
//	  				fromDate:"",
//	  				toDate:"",
	  				listOfBranchCustoMapIds:tempbranchCustomerMapIds
	  		};
	  		return data;
	  	  }
	}]);
})();
