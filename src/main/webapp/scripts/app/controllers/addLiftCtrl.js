(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addUserCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$window','pinesNotifications','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$window,pinesNotifications,$rootScope) {
		//initialize add Branch
		initAddLift();
		// Date Picker
		$scope.today = function() {
		      $scope.dt = new Date();
		    };
		    $scope.today();

		    $scope.clear = function() {
		      $scope.dt = null;
		    };

		    // Disable weekend selection
		    $scope.disabled = function(date, mode) {
		      return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
		    };

		    $scope.toggleMin = function() {
		      $scope.minDate = $scope.minDate ? null : new Date();
		    };
		    $scope.toggleMin();

		    $scope.open = function($event) {
		      $event.preventDefault();
		      $event.stopPropagation();
		      if($scope.opened != true)
		          $scope.opened = true;
		      else
		          $scope.opened = false;
		    };

		    $scope.dateOptions = {
		      formatYear: 'yy',
		      startingDay: 1,
		    };

		    $scope.initDate = new Date('2016-15-20');
		    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
		    $scope.format = $scope.formats[0];
	    //Date Picker End
		$scope.alert = { type: 'success', msg: 'Well done! You successfully Added Lift.',close:true };
		$scope.showAlert = false;
		$scope.showCompany = false;
		$scope.showBranch = false;
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		$scope.loadBranchData = function(){
			var companyData={};
			if($scope.showCompany == true){
  	    		companyData = {
						companyId : $scope.selectedCompany.selected.companyId
					}
  	    	}else{
  	    		companyData = {
						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
					}
  	    	}
		    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',companyData)
		    .then(function(response){
		    	$scope.branches = response;
		    	
		    });
		}
		$scope.loadCustomerData = function(){
			var branchData ={};
  	    	if($scope.showBranch == true){
  	    		branchData = {
  	    			branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
					}
  	    	}else{
  	    		branchData = {
  	    			branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
					}
  	    	}
  	    	serviceApi.doPostWithData('/RLMS/admin/getAllCustomersForBranch',branchData)
 	         .then(function(customerData) {
 	        	 $scope.cutomers = customerData;
 	         })
		}
		//showCompnay Flag
	  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
			$scope.showCompany= true;
			loadCompanyData();
		}else{
			$scope.showCompany= false;
			$scope.loadBranchData();
		}
	  	
	  	//showBranch Flag
	  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3){
			$scope.showBranch= true;
		}else{
			$scope.showBranch=false;
		}
	  	
		function initAddLift(){
			$scope.selectedCompany={};
			$scope.selectedBranch = {};
			$scope.selectedCustomer = {};	
			$scope.addLift={
					branchCustomerMapId :'',
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
			$scope.addLift.serviceStartDate = new Date();
		}
		$scope.open = function($event) {
		      $event.preventDefault();
		      $event.stopPropagation();
		      if($scope.opened != true)
		          $scope.opened = true;
		      else
		          $scope.opened = false;
		    };
		//load compay dropdown data
		//Post call add branch
		$scope.submitAddLift = function(){
			$scope.addLift.branchCustomerMapId = $scope.selectedCustomer.selected.branchCustomerMapId
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
