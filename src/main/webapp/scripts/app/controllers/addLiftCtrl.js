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
					amcType : 0,
					amcAmount : 0,


					doorType : 0,
					noOfStops : 0,
					engineType : 0,
					machineMake : '',
					machineCapacity : '',
					machineCurrent : '',
					breakVoltage : '',
					panelMake : '',
					ard : '',
					noOfBatteries :0,
					batteryCapacity : '',
					batteryMake : '',
					copMake : '',
					lopMake : '',
					collectiveType : 0,
					simplexDuplex : 0,
					autoDoorMake : '',
					wiringShceme : 0,
					fireMode : 0,
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
			$scope.showWizard = false;
			//$scope.addLift.serviceStartDate = new Date();
		}
		$scope.openFlag={
				serviceStartDate:false,
				serviceEndDate:false,
				dateOfInstallation :false,
				amcStartDate :false,
		}
		$scope.open = function($event,which) {
		      $event.preventDefault();
		      $event.stopPropagation();
		      if($scope.openFlag[which] != true)
		    	  $scope.openFlag[which] = true;
		      else
		    	  $scope.openFlag[which] = false;
		    };
		//load compay dropdown data
		//Post call add branch
		function parseBase64(){
			if($scope.addLift.machinePhoto != ''){
				$scope.addLift.machinePhoto = $scope.addLift.machinePhoto.base64;
			}
			if($scope.addLift.machinePhoto != ''){
				$scope.addLift.machinePhoto = $scope.addLift.machinePhoto.base64;
			}
			if($scope.addLift.panelPhoto != ''){
				$scope.addLift.panelPhoto = $scope.addLift.panelPhoto.base64;
			}
			if($scope.addLift.ardPhoto != ''){
				$scope.addLift.ardPhoto = $scope.addLift.ardPhoto.base64;
			}
			if($scope.addLift.lopPhoto != ''){
				$scope.addLift.lopPhoto = $scope.addLift.lopPhoto.base64;
			}
			if($scope.addLift.copPhoto != ''){
				$scope.addLift.copPhoto = $scope.addLift.copPhoto.base64;
			}
			if($scope.addLift.cartopPhoto != ''){
				$scope.addLift.cartopPhoto = $scope.addLift.cartopPhoto.base64;
			}
			if($scope.addLift.cartopPhoto != ''){
				$scope.addLift.cartopPhoto = $scope.addLift.cartopPhoto.base64;
			}
			if($scope.addLift.autoDoorHeaderPhoto != ''){
				$scope.addLift.autoDoorHeaderPhoto = $scope.addLift.autoDoorHeaderPhoto.base64;
			}
			if($scope.addLift.wiringPhoto != ''){
				$scope.addLift.wiringPhoto = $scope.addLift.wiringPhoto.base64;
			}
			if($scope.addLift.lobbyPhoto != ''){
				$scope.addLift.lobbyPhoto = $scope.addLift.lobbyPhoto.base64;
			}
		}
		$scope.submitAddLift = function(){
			parseBase64();
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
		$scope.showWizardFun = function(){
			$scope.showWizard = true;
		}
		//rese add branch
		$scope.resetAddLift = function(){
			$scope.showAlert = false;
			initAddLift();
			$scope.addLiftForm.$setPristine();
			$scope.addLiftForm.$setUntouched();
		}
		
	}]);
})();
