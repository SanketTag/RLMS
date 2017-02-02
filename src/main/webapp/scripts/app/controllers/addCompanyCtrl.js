(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCompanyCtrl', ['$scope', '$filter','serviceApi','$route','utility','pinesNotifications','$timeout','$window', function($scope, $filter,serviceApi,$route,utility,pinesNotifications,$timeout,$window) {
		initAddCompany();
		$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Company.' };
		//function to initialize addCompany Model
		function initAddCompany(){
			$scope.showAlert = false;
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
				utility.showMessage('Company Added',successMessage,'success');
				initAddCompany();
				$route.reload();
					
				
				//utility.showMessage('Added Company',successMessage,'success');
				
			})
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			initAddCompany();
			$route.reload();
		};
		$scope.backPage =function(){
			 //window.history.back();
		}
		
	}]);
})();
