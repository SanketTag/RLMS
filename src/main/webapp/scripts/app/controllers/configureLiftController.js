(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('configurLiftController', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initConfigLift();
		$scope.changeTab = function(targetIndex){
			angular.element('#tab'+targetIndex).trigger('click');
		}
		function initConfigLift(){
			$scope.a={};
			$scope.bd={};
			$scope.p={};
			$scope.rtc={};
			$scope.a.homeLandingFloorNumber = false;
			$scope.a.disableHomelanding = false;
			$scope.a.homeLandingTime = '';
			$scope.a.fireModeFloorSelection = "";
			$scope.$watch(
                    "a.disableHomelanding",
                    function handleChange( newValue, oldValue ) {
                    	if(newValue == true){
                    		$scope.a.homeLandingFloorNumber = 99;
                    	}else{
                    		$scope.a.homeLandingFloorNumber = 0;
                    	}
                    }
                );
		}
		
	}]);
})();
