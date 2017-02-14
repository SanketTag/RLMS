(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addCustomerCtrl', ['$scope', '$filter','serviceApi','$route','utility', function($scope, $filter,serviceApi,$route,utility) {
	initAddCustomer();
			loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Branch.',close:true };
			//loadBranchListInfo();
			$scope.showAlert = false;
			$scope.companies = [];
			$scope.branches = [];
			function initAddCustomer(){
				$scope.selectedCompany = {};
				$scope.selectedBranch = {};
				$scope.addCustomer={
						firstName:'',
						lastName:'',
						address:'',
						vatNumber:'',
						tinNumber:'',
						panNumber:'',
						customerType:'',
						emailID:'',
						cntNumber:'',
						branchName:'',
						companyName:'',
						totalNumberOfLifts:'',
						branchCompanyMapId:''
						
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
			    	
			    });
			}
			//Post call add customer
			$scope.submitAddCustomer = function(){
				$scope.addCustomer.companyName = $scope.selectedCompany.selected.companyName;
				$scope.addCustomer.branchName = $scope.selectedBranch.selected.rlmsBranchMaster.branchName;
				$scope.addCustomer.branchCompanyMapId = $scope.selectedBranch.selected.companyBranchMapId;
				serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewCustomer",$scope.addCustomer)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddCustomer();
					$scope.addCustomerForm.$setPristine();
					$scope.addCustomerForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			//rese add branch
			$scope.resetAddCustomer = function(){
				initAddCustomer();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
