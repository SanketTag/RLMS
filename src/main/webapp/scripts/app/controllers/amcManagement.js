(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('amcManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initAMCList();
		$scope.cutomers=[];
		function initAMCList(){
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
		$scope.loadAMCList = function(){
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
	  	        serviceApi.doPostWithData('/RLMS/report/getAMCDetailsForLifts',dataToSend)
	  	         .then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var detailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			detailsObj["Name"] =largeLoad[i].firstName + " " +largeLoad[i].lastName;
	  	        		}else{
	  	        			detailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			detailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			detailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			detailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			detailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			detailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			detailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["Branch"] =" - ";
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
		  	    	
	  	        	serviceApi.doPostWithData('/RLMS/report/getAMCDetailsForLifts',dataToSend).then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var detailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			detailsObj["Name"] =largeLoad[i].firstName + " " + largeLoad[i].lastName;
	  	        		}else{
	  	        			detailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			detailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			detailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			detailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			detailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			detailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			detailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			detailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			detailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["Branch"] =" - ";
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
	  	      gridFooterHeight:35
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
	  	  $scope.resetAMCList = function(){
	  		initAMCList();
	  	  }
	  	  function constructDataToSend(){
	  		var tempLiftIds = [];
			for (var i = 0; i < $scope.selectedLift.selected.length; i++) {
				tempLiftIds
						.push($scope.selectedLift.selected[i].liftId);
			}
			var tempCusto = [];
			for (var j = 0; j < $scope.selectedCustomer.selected.length; j++) {
				tempCusto
						.push($scope.selectedCustomer.selected[j].branchCustomerMapId);
			}
			var data = {
	        			liftCustomerMapId:tempLiftIds,
	        			listOfBranchCustomerMapId:tempCusto
  	    	}
			return data;
	  	  }
	}]);
})();
