(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('reportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initConfigLift();
		function initConfigLift(){
			$scope.a={};
			$scope.bd={};
			$scope.p={};
			$scope.rtc={};
			$scope.a.homeLandingFloorNumber = 0;
			$scope.a.disableHomelanding = false;
			$scope.a.homeLandingTime = '';
			$scope.a.fireModeFloorSelection = "";
			Output contactor off delay time at lift stopping
			
		}
		
	}]);
})();
