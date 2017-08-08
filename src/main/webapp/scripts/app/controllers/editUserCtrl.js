(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editUserCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope) {
		//initialize add Branch
		initAddUser();
		loadCompayInfo();
		//loadBranchListInfo();
		$scope.backPage = function(){
			$window.history.back();
		}
		$scope.alert = { type: 'success', msg: 'You successfully Added new user.',close:true };
		$scope.showAlert = false;
		function initAddUser(){
			$scope.selectedCompany = {};
			$scope.addUser={
				firstName:'',
				lastName:'',
				address:'',
				contactNumber:'',
				emailId:'',
				companyId:'',
				city:'',
				area:'',
				pinCode:''
			};	
		    //$scope.companies = [];
		    $scope.userList={};
		}
		
		$scope.alert = { type: 'success', msg: 'You successfully edited user.',close:true };
		$scope.showAlert = false;
		//load compay dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		
		/*$rootScope.editUser.userId=row.Id;
		$rootScope.editUser.name=row.Name.replace(/-/g, '');
		$rootScope.editUser.company=row.Company.replace(/-/g, '');
		$rootScope.editUser.contactnumber=row.Contact_Number.replace(/-/g, '');
		$rootScope.editUser.address=row.Address.replace(/-/g, '');
		$rootScope.editUser.city=row.City.replace(/-/g, '');
		$rootScope.editUser.branch=row.Branch.replace(/-/g, '');
		$rootScope.editUser.role=row.Role.replace(/-/g, '');
		$rootScope.editUser.emailid=row.Email_Id.replace(/-/g, '');*/
		$scope.submitEditUser = function(){
			var userData = {};
			var fullName=$scope.editUser.name.split(" ");
			userData = {
					userId: $rootScope.editUser.userId,
					firstName:fullName[0],
					lastName:fullName[1],
					address:$scope.editUser.address,
					contactNumber:$scope.editUser.contactnumber,
					emailId:$scope.editUser.emailid,
					city:$scope.editUser.city
			};
			serviceApi.doPostWithData("/RLMS/admin/validateAndUpdateUser",userData)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		
		//Post call add branch
		$scope.submitAddUser = function(){
			$scope.addUser.companyId = $scope.selectedCompany.selected.companyId;
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewUser",$scope.addUser)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				//utility.showMessage("",successMessage,"success");
				initAddUser();
				$scope.addUserForm.$setPristine();
				$scope.addUserForm.$setUntouched();
				
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
		$scope.resetAddUser = function(){
			$scope.showAlert = false;
			initAddUser();
			$scope.addUserForm.$setPristine();
			$scope.addUserForm.$setUntouched();
		}
		
	}]);
})();