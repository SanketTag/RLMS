(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addAMCCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
		initAddAMC();
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Complaint.',close:true };
			$scope.showAlert = false;
			$scope.companies = [];
			function initAddAMC() {
				$scope.customerSelected = false;
				$scope.selectedCustomer = {};
				$scope.selectedLift = {};
				$scope.selectedAmc = {};
				$scope.addAMC={
						liftCustoMapId:'',
						amcEndDate:'',
						amcStartDate:'',
						amcType:'',
						amcAmount:''
				};
				$scope.amcStatuses=[
					 {
						 name:"Under Warranty",
						 id:38
					 },
					 {
						 name:"Renewal Due",
						 id:39
					 },
					 {
						 name:"AMC Pending",
						 id:40
					 },
					 {
						 name:"Under AMC",
						 id:41
					 }
				 ]
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
			$scope.loadLifts = function() {
				
				var dataToSend = {
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
			$scope.submitaddAMC = function(){
				$scope.addAMC.liftCustomerMapId =  $scope.selectedCustomer.selected.branchCustomerMapId
				$scope.addAMC.liftCustoMapId=$scope.selectedLift.selected.liftId,
				$scope.addAMC.amcType=$scope.selectedAmc.selected.id,
				serviceApi.doPostWithData("/RLMS/report/addAMCDetailsForLift",$scope.addAMC)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddAMC();
					$scope.addAMCForm.$setPristine();
					$scope.addAMCForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			
		  
			//rese add branch
			$scope.resetaddAmc = function(){
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
