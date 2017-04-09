(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('reportCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initReport();
		$scope.cutomers=[];
		$scope.goToAddAMC = function(){
			window.location.hash = "#/add-amc";
		}
		function initReport(){
			 $scope.selectedCustomer = {};	
			 $scope.lifts=[];
			 $scope.selectedCustomer = {};
			 $scope.selectedLift = {};
			 $scope.selectedAmc = {};
			 $scope.showMembers = false;
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
		//Show Member List
		$scope.loadReportList = function(){
			$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
			$scope.showMembers = true;
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
	  	    	
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        var dataToSend = constructDataToSend();
	  	        serviceApi.doPostWithData('/RLMS/report/getSiteVisitReport',dataToSend)
	  	         .then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var detailsObj={};
	  	        		if(!!largeLoad[i].customerName){
	  	        			detailsObj["customerName"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			detailsObj["customerName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			detailsObj["liftNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].status){
	  	        			detailsObj["status"] =largeLoad[i].status;
	  	        		}else{
	  	        			detailsObj["status"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcAmount){
	  	        			detailsObj["amcAmount"] =largeLoad[i].amcAmount;
	  	        		}else{
	  	        			detailsObj["amcAmount"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			detailsObj["amcTypeStr"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			detailsObj["amcTypeStr"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDate){
	  	        			detailsObj["amcStartDate"] =largeLoad[i].amcStartDate;
	  	        		}else{
	  	        			detailsObj["amcStartDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dueDate){
	  	        			detailsObj["dueDate"] =largeLoad[i].dueDate;
	  	        		}else{
	  	        			detailsObj["dueDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			detailsObj["area"] =largeLoad[i].area;
	  	        		}else{
	  	        			detailsObj["area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			detailsObj["city"] =largeLoad[i].city;
	  	        		}else{
	  	        			detailsObj["city"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            data = details.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	
	  	        	var dataToSend = constructDataToSend();
		  	    	
	  	        	serviceApi.doPostWithData('/RLMS/report/getTechnicianWiseReport',dataToSend).then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var detailsObj={};
	  	        		if(!!largeLoad[i].customerName){
	  	        			detailsObj["customerName"] =largeLoad[i].customerName;
	  	        		}else{
	  	        			detailsObj["customerName"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].liftNumber){
	  	        			detailsObj["liftNumber"] =largeLoad[i].liftNumber;
	  	        		}else{
	  	        			detailsObj["liftNumber"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].status){
	  	        			detailsObj["status"] =largeLoad[i].status;
	  	        		}else{
	  	        			detailsObj["status"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcAmount){
	  	        			detailsObj["amcAmount"] =largeLoad[i].amcAmount;
	  	        		}else{
	  	        			detailsObj["amcAmount"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcTypeStr){
	  	        			detailsObj["amcTypeStr"] =largeLoad[i].amcTypeStr;
	  	        		}else{
	  	        			detailsObj["amcTypeStr"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].amcStartDate){
	  	        			detailsObj["amcStartDate"] =largeLoad[i].amcStartDate;
	  	        		}else{
	  	        			detailsObj["amcStartDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].dueDate){
	  	        			detailsObj["dueDate"] =largeLoad[i].dueDate;
	  	        		}else{
	  	        			detailsObj["dueDate"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].area){
	  	        			detailsObj["area"] =largeLoad[i].area;
	  	        		}else{
	  	        			detailsObj["area"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			detailsObj["city"] =largeLoad[i].city;
	  	        		}else{
	  	        			detailsObj["city"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            $scope.setPagingData(details, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
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
	  	      gridFooterHeight:35,
	  	      groupBy:'customerName',
	  	      columnDefs : [ {
				field : "amcAmount",
				displayName:"AMC Amount",
				width : 120
	  	      },{
					field : "amcStartDate",
					displayName:"AMC Start Date",
					width : 120
		  	  },{
					field : "amcTypeStr",
					displayName:"AMC Type",
					width : 120
		  	  },{
					field : "area",
					displayName:"Area",
					width : 120
		  	  },{
					field : "city",
					displayName:"City",
					width : 120
		  	  },{
					field : "customerName",
					displayName:"Customer Name",
					width : 120
		  	  },{
					field : "dueDate",
					displayName:"Due Date",
					width : 120
		  	  },{
					field : "liftNumber",
					displayName:"Lift Number",
					width : 120
		  	  },{
					field : "status",
					displayName:"Status",
					width : 120
		  	  }
	  	      ]
	  	    };
	  	  $scope.loadLifts = function() {
				
				var dataToSend = {
					//branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
					branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
				}
				serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
						.then(function(liftData) {
							$scope.lifts = liftData;
						})
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
	  	  $scope.resetReportList = function(){
	  		initReport();
	  	  }
	  	  function constructDataToSend(){
	  		var tempLiftIds = [];
//			for (var i = 0; i < $scope.selectedLift.selected.length; i++) {
//				tempLiftIds.push($scope.selectedLift.selected[i].liftId);
//			}
//			var tempCusto = [];
//			for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
//				tempCusto.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
//			}
			var tempStatus =[];
			for (var j = 0; j < $scope.selectedAmc.selected.length; j++) {
				tempStatus.push($scope.selectedAmc.selected[j].id);
			}
//			var data = {
//	        			liftCustomerMapId:tempLiftIds,
//	        			listOfBranchCustomerMapId:tempCusto,
//	        			listOFStatusIds:tempStatus
//  	    	}
//			var tempStatus =[]
//			return data;
	  		var tempListOfUserRoleIds = [];
	  		tempListOfUserRoleIds[0] = 2;
	  		var statusIds = [];
	  		statusIds[0] = 2;
	  		statusIds[1] = 3;
	  		statusIds[2] = 4;
	  		statusIds[3] = 5;
	  		statusIds[4] = 9;
	  		var data = {
	  				branchCompanyMapId:8,
	  				companyId:9,
	  				listOfUserRoleIds:tempListOfUserRoleIds,
//	  				fromDate:"",
//	  				toDate:"",
//	  				listOfBranchCustoMapIds:""
	  		};
	  		return data;
	  	  }
	}]);
})();
