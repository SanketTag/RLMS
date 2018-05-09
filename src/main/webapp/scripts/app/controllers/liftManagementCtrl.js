(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('liftManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initCustomerList();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddLift =function(){
			window.location.hash = "#/add-lift";
		};
		function initCustomerList(){
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 $scope.branches=[];
			 $scope.showTable = false;
		} 
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
		    	$scope.selectedBranch.selected=undefined;
		    	var emptyLiftArray=[];
		    	$scope.myData=emptyLiftArray;
		    	
		    });
		}
	    $scope.filterOptions = {
	  	      filterText: '',
	  	      useExternalFilter: true
	  	    };
	  	    $scope.totalServerItems = 0;
	  	    $scope.pagingOptions = {
	  	      pageSizes: [10, 20, 50],
	  	      pageSize: 10,
	  	      currentPage: 1
	  	    };
	  	    $scope.setPagingData = function(data, page, pageSize) {
	  	      var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
	  	      $scope.myData = pagedData;
	  	      $scope.totalServerItems = data.length;
	  	      if (!$scope.$$phase) {
	  	        $scope.$apply();
	  	      }
	  	    };
	  	    $scope.getPagedDataAsync = function(pageSize, page, searchText) {
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
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
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
	  	          var ft = searchText.toLowerCase();
	  	        serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcType"] =" - ";
	  	        		}
	  	        		if (!!largeLoad[i].liftId) {
							userDetailsObj["liftId"] = largeLoad[i].liftId;
						} else {
							userDetailsObj["liftId"] = " - ";
						}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            data = userDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
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
	  	        	serviceApi.doPostWithData('/RLMS/admin/getLiftDetailsForBranch',branchData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			userDetailsObj["Lift_Number"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			userDetailsObj["Lift_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].customerName){
	  	        			userDetailsObj["Customer_Name"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			userDetailsObj["Customer_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceStartDateStr){
	  	        			userDetailsObj["Service_Start_Date"] =largeLoad[i].serviceStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].serviceEndDateStr){
	  	        			userDetailsObj["Service_End_Date"] =largeLoad[i].serviceEndDateStr;
	  	        		}else{
	  	        			userDetailsObj["Service_End_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["Installation_Date"] =largeLoad[i].dateOfInstallationStr;
	  	        		}else{
	  	        			userDetailsObj["Installation_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDateStr){
	  	        			userDetailsObj["Amc_Start_Date"] =largeLoad[i].amcStartDateStr;
	  	        		}else{
	  	        			userDetailsObj["Amc_Start_Date"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dateOfInstallationStr){
	  	        			userDetailsObj["amcType"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			userDetailsObj["amcType"] =" - ";
	  	        		}
	  	        		if (!!largeLoad[i].liftId) {
							userDetailsObj["liftId"] = largeLoad[i].liftId;
						} else {
							userDetailsObj["liftId"] = " - ";
						}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	    $scope.loadCustomerInfo=function(){
	  	    	
	  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    }
	  	    $scope.resetCustomerList=function(){
	  	    	initCustomerList();
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
		  	
	  	    $scope.$watch('pagingOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
	  	      }
	  	    }, true);
	  	    $scope.$watch('filterOptions', function(newVal, oldVal) {
	  	      if (newVal !== oldVal) {
	  	        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
	  	      }
	  	    }, true);

	  	    $scope.gridOptions = {
	  	      data: 'myData',
	  	      rowHeight: 40,
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions,
	  	      multiSelect: false,
	  	    gridFooterHeight : 35,
			enableRowSelection: true,
			selectedItems: [],
			afterSelectionChange:function(rowItem, event){
				//$scope.showAlert = false;
				//console.log(rowItem);
				//console.log(event);
				//var selected = $filter('filter')($scope.complaints,{complaintId:$scope.gridOptions.selectedItems[0].complaintId});
//				if(selected[0].Status == "Assigned"){
//					$scope.isAssigned = true;
//				}else{
//					$scope.isAssigned = false;
//				}
			},
			columnDefs : [ {
				field : "Lift_Number",
				displayName:"Lift Number",
				width : 120
			}, {
				field : "Address",
				displayName:"Address",
				width : 120
			}, {
				field : "City",
				displayName:"City",
				width : 120
				
			}, {
				field : "Customer_Name",
				displayName:"Customer Name",
				width : 120
			}
			, {
				field : "Branch_Name",
				displayName:"Branch Name",
				width : 160
			}, {
				field : "Service_Start_Date",
				displayName:"Service Start Date",
				width : 120
			}
			, {
				field : "Service_End_Date",
				displayName:"Service End Date",
				width : 120
			}
			, {
				field : "Installation_Date",
				displayName:"Installation Date",
				width : 120
			}, {
				field : "Amc_Start_Date",
				displayName:"Amc Start Date",
				width : 120
			}
			, {
				field : "amcType",
				displayName:"AMC Type",
				width : 120
			},{
				field : "liftId",
				displayName:"liftId",
				visible: false,
			},{
				cellTemplate :  
		             '<button ng-click="$event.stopPropagation(); editThisRow(row.entity);" title="Edit" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
				width : 30
			}
			]
	  	    };
	  	    
	  	  $rootScope.editLift={};
	  	$scope.selectedDoorType={};
			//$rootScope.technicianDetails=[];
		//	$rootScope.complaintStatusArray=['Pending','Assigned','Completed','In Progress'];
			$scope.editThisRow=function(row){
				
				
				serviceApi.doPostWithData('/RLMS/admin/getLiftById',row.liftId)
				.then(function(data) {
					
					$rootScope.editLift = data.response;
					$scope.selectedDoorType.id= $scope.editLift.doorType; 
					$scope.selectedDoorType.selected =$scope.editLift.doorType;
					//var technicianArray=$rootScope.techniciansForEditComplaints;
					
					window.location.hash = "#/edit-lift";
				});
				
				
					/*$rootScope.editLift.liftNumber=$rootScope.outData.liftNumber;
					$rootScope.editLift.address=row.Address.replace(/-/g, '');
					$rootScope.editLift.city=row.City.replace(/-/g, '');
					$rootScope.editLift.customerName=row.Customer_Name.replace(/-/g, '');
					$rootScope.editLift.branchName=row.Branch_Name.replace(/-/g, '');
					$rootScope.editLift.serviceStartDate=row.Service_Start_Date;
					$rootScope.editLift.serviceEndDate=row.Service_End_Date;
					$rootScope.editLift.installationDate=row.Installation_Date;
					$rootScope.editLift.amcStartDate=row.Amc_Start_Date;
					$rootScope.editLift.amcType="NA";
					$rootScope.editLift.area="NA";
					$rootScope.editLift.pincode="NA";
					
					$rootScope.editLift.latitude="NA";
					$rootScope.editLift.longitude="NA";
					$rootScope.editLift.amcEndDate="NA";
					$rootScope.editLift.amcAmount="NA";
					$rootScope.editLift.noOfStops="NA";
					$rootScope.editLift.machineMake="NA";
					$rootScope.editLift.machineCurrent="NA";
					$rootScope.editLift.machineCapacity="NA";
					$rootScope.editLift.breakVoltage="NA";
					$rootScope.editLift.panelMake="NA";
					$rootScope.editLift.ard="NA";
					$rootScope.editLift.noOfBatteries="NA";
					$rootScope.editLift.batteryCapacity="NA";
					$rootScope.editLift.batteryMake="NA";
					$rootScope.editLift.copMake="NA";
					$rootScope.editLift.lopMake="NA";
					$rootScope.editLift.autoDoorMake="NA";
					$rootScope.editLift.fireMode="NA";
					$rootScope.editLift.intercomm="NA";
					$rootScope.editLift.alarm="NA";
					$rootScope.editLift.alarmBattery="NA";
					$rootScope.editLift.accessControl="NA";
					
					$rootScope.editLift.machinePhoto="NA";
					$rootScope.editLift.panelPhoto="NA";
					$rootScope.editLift.ardPhoto="NA";
					$rootScope.editLift.lopPhoto="NA";
					$rootScope.editLift.copPhoto="NA";
					$rootScope.editLift.cartopPhoto="NA";
					$rootScope.editLift.autoDoorHeaderPhoto="NA";
					$rootScope.editLift.wiringPhoto="NA";
					$rootScope.editLift.lobbyPhoto="NA";*/
					
					
					
					
				//	$rootScope.editLift.installationDate=row.Service_StartDate;
					//$rootScope.editLift.installationDate=row.Service_StartDate;
					//$rootScope.editLift.installationDate=row.Service_StartDate;
					
				//	$rootScope.selectedComplaintStatus=row.Status;
					//$rootScope.editComplaint.complaintsStatus=row.Status.replace(/-/g, '');
					
					//window.location.hash = "#/edit-lift";
				
			};
		
	}]);
})();
