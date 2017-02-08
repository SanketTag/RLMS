(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('userManagement', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		loadCompanyData();
		 $scope.selectedCompany={};
		 $scope.showTable = false;
		$scope.goToAddUser =function(){
			window.location.hash = "#/add-user";
		};
		$scope.showCompany = false;
		
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
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
	  	    	var companyData ={};
	  	    	if($scope.showCompany == true){
	  	    		companyData = {
  						companyId : $scope.selectedCompany.selected.companyId
  					}
	  	    	}else{
	  	    		companyData = {
  						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
  					}
	  	    	}
		  	    
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	        serviceApi.doPostWithData('/RLMS/admin/getAllRegisteredUsers',companyData)
	  	         .then(function(largeLoad) {
	  	        	$scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var userDetailsObj={};
	  	        		if(!!largeLoad[i].userName){
	  	        			userDetailsObj["Name"] =largeLoad[i].userName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			userDetailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            data = userDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	        	var companyData ={};
		  	    	if($scope.showCompany == true){
		  	    		companyData = {
	  						companyId : $scope.selectedCompany.selected.companyId
	  					}
		  	    	}else{
		  	    		companyData = {
	  						companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
	  					}
		  	    	}
	  	        	serviceApi.doPostWithData('/RLMS/admin/getAllRegisteredUsers',companyData).then(function(largeLoad) {
	  	        		 $scope.showTable= true;
	  	        	  var userDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var userDetailsObj={};
	  	        		if(!!largeLoad[i].userName){
	  	        			userDetailsObj["Name"] =largeLoad[i].userName;
	  	        		}else{
	  	        			userDetailsObj["Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			userDetailsObj["Company"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			userDetailsObj["Company"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			userDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			userDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			userDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			userDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			userDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			userDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		userDetails.push(userDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(userDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	  $scope.loadUsersInfo=function(){
	  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
	  	    }
	  	if($rootScope.loggedInUserInfo.data.userRole.userRoleId == 1){
			$scope.showCompany= true;
		}else{
			$scope.loadUsersInfo();
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
