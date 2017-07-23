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
			
			$scope.submitEditComplaint = function(){
				$scope.selectedTechnician;
				var complaintsData = {};
				complaintsData = {
						 complaintNumber:$scope.editComplaint.complaintsNumber,
						 complaintId:$scope.editComplaint.complaintsNumber,
						 registrationDateStr:$filter('date')($scope.editComplaint.regDate, "dd-MMM-yyyy"),
						 serviceStartDateStr:$filter('date')($scope.editComplaint.serviceStartDate, "dd-MMM-yyyy"),
						 actualServiceEndDateStr:$filter('date')($scope.editComplaint.serviceEndDate, "dd-MMM-yyyy"),
						 liftAddress:$scope.editComplaint.complaintsAddress + $scope.editComplaint.complaintsCity,
						 status:$scope.selectedComplaintStatus,
						 title:$scope.editComplaint.complaintsTitle,
						 technicianDtls:$scope.selectedTechnician.name,
						 userRoleId:$scope.selectedTechnician.userRoleId
				};
				serviceApi.doPostWithData("/RLMS/complaint/validateAndUpdateComplaint",complaintsData)
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
