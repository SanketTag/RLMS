(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('memberManagementCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		initMemberList();
		$scope.showCompany = false;
		$scope.showBranch = false;
		$scope.goToAddMember = function(){
			window.location.hash = "#/add-member";
		}
		function initMemberList(){
			 $scope.selectedCompany={};
			 $scope.selectedBranch = {};
			 $scope.selectedCustomer = {};	
			 $scope.branches=[];
			 $scope.showMembers = false;
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
		//Show Member List
		$scope.showMemberList = function(){
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
	  	    	var dataToSend = {
	  	    			branchCustoMapId:0
	  	    	}
	  	    	dataToSend.branchCustoMapId= $scope.selectedCustomer.selected.branchCustomerMapId
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        serviceApi.doPostWithData('/RLMS/admin/getListOfAllMemberDtls',dataToSend)
	  	         .then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var detailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			detailsObj["Name"] =largeLoad[i].firstName;
	  	        		}else{
	  	        			detailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].cntNumber){
	  	        			detailsObj["Contact_Number"] =largeLoad[i].cntNumber;
	  	        		}else{
	  	        			detailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailID){
	  	        			detailsObj["Email_Id"] =largeLoad[i].emailID;
	  	        		}else{
	  	        			detailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			detailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			detailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			detailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			detailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].totalNumberOfLifts){
	  	        			detailsObj["Lifts_Count"] =largeLoad[i].totalNumberOfLifts;
	  	        		}else{
	  	        			detailsObj["Lifts_Count"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            data = details.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	var dataToSend = {
	  	        			branchCustoMapId:0
		  	    	}
		  	    	dataToSend.branchCustoMapId= $scope.selectedCustomer.selected.branchCustomerMapId
	  	        	serviceApi.doPostWithData('/RLMS/admin/getListOfAllMemberDtls',dataToSend).then(function(largeLoad) {
	  	        	  var details=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var detailsObj={};
	  	        		if(!!largeLoad[i].firstName){
	  	        			detailsObj["Name"] =largeLoad[i].firstName;
	  	        		}else{
	  	        			detailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].cntNumber){
	  	        			detailsObj["Contact_Number"] =largeLoad[i].cntNumber;
	  	        		}else{
	  	        			detailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailID){
	  	        			detailsObj["Email_Id"] =largeLoad[i].emailID;
	  	        		}else{
	  	        			detailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			detailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			detailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			detailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			detailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			detailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			detailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].totalNumberOfLifts){
	  	        			detailsObj["Lifts_Count"] =largeLoad[i].totalNumberOfLifts;
	  	        		}else{
	  	        			detailsObj["Lifts_Count"] =" - ";
	  	        		}
	  	        		details.push(detailsObj);
	  	        	  }
	  	            $scope.setPagingData(details, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
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
	  	      gridFooterHeight:35
	  	    };
		
	}]);
})();