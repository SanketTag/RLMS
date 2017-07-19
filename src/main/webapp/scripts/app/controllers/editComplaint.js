(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('editComplaintCtrl', ['$scope', '$filter','serviceApi','$route','utility','$window','$rootScope','$modal', function($scope, $filter,serviceApi,$route,utility,$window,$rootScope,$modal) {
			//loadCompayInfo();
			$scope.alert = { type: 'success', msg: 'You successfully Added Complaint.',close:true };
			$scope.showAlert = false;
	
			$scope.editFlag={
					regDate:false,
					serviceStartDate:false,
					serviceEndDate:false
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.editFlag[which] != true)
			    	  $scope.editFlag[which] = true;
			      else
			    	  $scope.editFlag[which] = false;
			  }
			//Post call add customer
			$scope.submitAddComplaint = function(){
				$scope.addComplaint.liftCustomerMapId = $scope.selectedLift.selected.liftId;
				$scope.addComplaint.registrationType = 31;
				serviceApi.doPostWithData("/RLMS/complaint/validateAndRegisterNewComplaint",$scope.addComplaint)
				.then(function(response){
					$scope.showAlert = true;
					var key = Object.keys(response);
					var successMessage = response[key[0]];
					$scope.alert.msg = successMessage;
					$scope.alert.type = "success";
					initAddComplaint();
					$scope.addComplaintForm.$setPristine();
					$scope.addComplaintForm.$setUntouched();
				},function(error){
					$scope.showAlert = true;
					$scope.alert.msg = error.exceptionMessage;
					$scope.alert.type = "danger";
				});
			}
			/* //showCompnay Flag
		  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
				$scope.showCompany= true;
				//loadCompayInfo();
			}else{
				$scope.showCompany= false;
				//$scope.loadBranchData();
			}*/
		  	
			$scope.resetaddComplaint = function(){
				initAddComplaint();
			}
			$scope.backPage =function(){
				 $window.history.back();
			}
	}]);
})();
