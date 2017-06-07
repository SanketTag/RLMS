(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('configurLiftController', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initConfigLift();
		$scope.changeTab = function(targetIndex){
			angular.element('#tab'+targetIndex).trigger('click');
		}
		//Time Picker Start
			$scope.mytime = new Date();
		
		    $scope.hstep = 1;
		    $scope.mstep = 15;
		
		    $scope.options = {
		      hstep: [1, 2, 3],
		      mstep: [1, 5, 10, 15, 25, 30]
		    };
		
		    $scope.ismeridian = true;
		    $scope.toggleMode = function() {
		      $scope.ismeridian = !$scope.ismeridian;
		    };
		
		    $scope.update = function() {
		      var d = new Date();
		      d.setHours(14);
		      d.setMinutes(0);
		      $scope.mytime = d;
		    };
		
		    $scope.changed = function() {
		      console.log('Time changed to: ' + $scope.mytime);
		    };
		
		    $scope.clear = function() {
		      $scope.mytime = null;
		    };
		//Time Picker End
		function initConfigLift(){
			$scope.a={};
			$scope.bd={};
			$scope.p={};
			$scope.rtc={};
			$scope.a.homeLandingFloorNumber = false;
			$scope.a.disableHomelanding = false;
			$scope.a.homeLandingTime = '';
			$scope.a.fireModeFloorSelection = "";
			$scope.a.Date='';
			$scope.a.flag2 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
			
			 
			$scope.a.flag3 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
						$scope.a.flag4 = {
						bit0 : 0,
						bit1 : 0,
						bit2 : 0,
						bit3 : 0,
						bit4 : 0,
						bit5 : 0
			}
			
			
			$scope.$watch("a.disableHomelanding",
                    function handleChange( newValue, oldValue ) {
                    	if(newValue == true){
                    		$scope.a.homeLandingFloorNumber = 99;
                    	}else{
                    		$scope.a.homeLandingFloorNumber = 0;
                    	}
                    }
                );
            $scope.openFlag={
					Date:false,
			}
			$scope.open = function($event,which) {
			      $event.preventDefault();
			      $event.stopPropagation();
			      if($scope.openFlag[which] != true)
			    	  $scope.openFlag[which] = true;
			      else
			    	  $scope.openFlag[which] = false;
			  }
		}
		
	}]);
})();
