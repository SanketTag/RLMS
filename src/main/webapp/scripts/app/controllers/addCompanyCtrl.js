(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCompanyCtrl', ['$scope', '$filter','serviceApi','$route','utility', function($scope, $filter,serviceApi,$route,utility) {
		initAddCompany();
		$scope.alert = { type: 'success', msg: 'Well done! You successfully read this important alert message.' };
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
				$scope.alert.msg = successMessage;
				initAddCompany();
				//utility.showMessage('Added Company',successMessage,'success');
				$route.reload();
			})
		};
		//Reset Add company form
		$scope.resetAddCompany = function(){
			initAddCompany();
			$route.reload();
		};
		
	}]);
})();
