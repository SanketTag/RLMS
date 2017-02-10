(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addUserCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications) {
		//initialize add Branch
		initAddLift();
		$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Lift.',close:true };
		$scope.showAlert = false;
		function initAddLift(){
			$scope.addLift={
					liftNumber : '',
					address : '',
					latitude : '',
					longitude : '',
					serviceStartDate : '',
					serviceEndDate : '',
					dateOfInstallation : '',
					amcStartDate : '',
					amcType : '',
					amcAmount : '',


					doorType : '',
					noOfStops : '',
					engineType : '',
					machineMake : '',
					machineCapacity : '',
					machineCurrent : '',
					breakVoltage : '',
					panelMake : '',
					ard : '',
					noOfBatteries : '',
					batteryCapacity : '',
					batteryMake : '',
					copMake : '',
					lopMake : '',
					collectiveType : '',
					simplexDuplex : '',
					autoDoorMake : '',
					wiringShceme : '',
					fireMode : '',
					intercomm : '',
					alarm : '',
					alarmBattery : '',
					accessControl : '',

					machinePhoto : '',
					panelPhoto : '',
					ardPhoto : '',
					lopPhoto : '',
					copPhoto : '',
					cartopPhoto : '',
					autoDoorHeaderPhoto : '',
					wiringPhoto : '',
					lobbyPhoto	 : '',
			};	
		}
		//load compay dropdown data
		//Post call add branch
		$scope.submitAddLift = function(){
			serviceApi.doPostWithData("/RLMS/admin/validateAndRegisterNewLift",$scope.addLift)
			.then(function(response){
				$scope.showAlert = true;
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				$scope.alert.msg = successMessage;
				$scope.alert.type = "success";
				initAddLift();
				$scope.addLiftForm.$setPristine();
				$scope.addLiftForm.$setUntouched();
				
			},function(error){
				$scope.showAlert = true;
				$scope.alert.msg = error.exceptionMessage;
				$scope.alert.type = "danger";
			});
		}
		//rese add branch
//		$scope.resetAddUser = function(){
//			$scope.showAlert = false;
//			initAddUser();
//			$scope.addUserForm.$setPristine();
//			$scope.addUserForm.$setUntouched();
//		}
		
	}]);
})();
