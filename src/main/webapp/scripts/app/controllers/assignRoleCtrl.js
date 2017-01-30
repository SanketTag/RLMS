(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('assignRoleCtrl', ['$scope', '$filter','serviceApi','$route','utility', function($scope, $filter,serviceApi,$route,utility) {
		initAssignRole();
		loadRolesData();
		//function to initialize addCompany Model
		function initAssignRole(){
			$scope.isRoleSelected = false;
			$scope.selectedRole = {};
			$scope.roles = [];
			
			$scope.selectedCompany = {};
			$scope.companies = [];
			
			$scope.selectedBranch = {};
			$scope.branches = [];
			
			$scope.selectedUser = {};
			$scope.users = [];
			
			$scope.assignRole={
					userId:'',
					companyId:'',
					spocRoleId:''
			}
		};
		//Load Roles Data
		function loadRolesData(){
		    serviceApi.doPostWithoutData('/RLMS/admin/getApplicableRoles')
		    .then(function(response){
		    	$scope.roles = response;
		    });
		    
		}
		//Proceed Role
		$scope.proceedRole = function(){
			loadCompanyData();
			//loadBranchData();
			$scope.isRoleSelected = true;
		}
		$scope.loadBranchData = function(){
			var data = {
				companyId : $scope.selectedCompany.selected.companyId
			}
			loadUsersData();
		    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',data)
		    .then(function(response){
		    	$scope.branches = response;
		    	
		    });
		}
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		function loadUsersData(){
			var companyId = $scope.selectedCompany.selected.companyId;
			var data = {
					companyId : companyId
				}
		    serviceApi.doPostWithData('/RLMS/admin/getAllUsersForCompany',data)
		    .then(function(response){
		    	$scope.users = response;
		    	console.log(response);
		    });
		}
		//Post call
		$scope.submitAssignRole = function(){
//			$scope.assignRole.branchId = $scope.selectedBranch.selected.rlmsBranchMaster.branchId;
			$scope.assignRole.companyId = $scope.selectedCompany.selected.companyId;
			$scope.assignRole.spocRoleId = $scope.selectedRole.selected.spocRoleId;
			$scope.assignRole.userId = $scope.selectedUser.selected.userId;
			serviceApi.doPostWithData("/RLMS/admin/assignRole",$scope.assignRole)
			.then(function(response){
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				initAssignRole();
				utility.showMessage('Assigned Company',successMessage,'success');
				$route.reload();
			})
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			initAssignRole();
			$route.reload();
		};
	}]);
})();
