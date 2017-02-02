(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCompanyCtrl', ['$scope', '$filter','serviceApi','$route','utility','pinesNotifications','$timeout','$window', function($scope, $filter,serviceApi,$route,utility,pinesNotifications,$timeout,$window) {
		initAddCompany();
		$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Company.',close:true };
		//function to initialize addCompany Model
		$scope.showAlert = false;
		function initAddCompany(){
			$scope.addCompany={
					companyName:'',
					address:'',
					contactNumber:'',
					emailId:"",
					panNumber:'',
					tinNumber:'',
					vatNumber:'',	
			}
		};
		//Post call
		$scope.submitAddCompany = function(){
			serviceApi.doPostWithData("/RLMS/admin/addNewCompany",$scope.addCompany)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				//utility.showMessage('Company Added',successMessage,'success');
				initAddCompany();
				$scope.addCompanyForm.$setPristine();
				$scope.addCompanyForm.$setUntouched();
				//$route.reload();
					
				
				//utility.showMessage('Added Company',successMessage,'success');
				
			},function(response){
				$scope.showAlert = true;
				$scope.alert.msg = response.exceptionMessage;
				$scope.alert.type = "danger";
			})
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			$scope.showAlert = false;
			initAddCompany();
			$scope.addCompanyForm.$setPristine();
			$scope.addCompanyForm.$setUntouched();
			//$route.reload();
		};
		$scope.backPage =function(){
			 $window.history.back();
		}
		
	}]);
})();
