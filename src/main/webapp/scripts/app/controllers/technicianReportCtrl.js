(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('technicainReportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();

		function initReport(){
			$scope.selectedCompany = {};
			$scope.selectedBranch = {};
			 $scope.branches = [];
			 $scope.companies = [];
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
		$scope.loadReportList = function(){
 	         var dataToSend = constructDataToSend();
 	         serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',dataToSend)
 	         .then(function(data) {
 	        	 $scope.siteViseReport = data;
 	         })
			$scope.showMembers = true;
		}
	   
	  	 
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		var data = {
	  				'companyBranchMapId':$scope.selectedBranch.selected.companyBranchMapId,
	  				'companyId':$scope.selectedCompany.selected.companyId,
	  		};
	  		return data;
	  	  }
	}]);
})();
