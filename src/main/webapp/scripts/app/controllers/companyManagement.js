(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('companyManagement', ['$scope', '$filter','serviceApi','$route','$http','utility', function($scope, $filter,serviceApi,$route,$http,utility) {
		$scope.goToAddCompany =function(){
			window.location.pathname = "RLMS/add-company"
		}
		//-------Company Details Table---------
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
	  	          $http.post('/RLMS/admin/getAllCompanyDetails').success(function(largeLoad) {
	  	        	  var companyDetails=[];
	  	        	  var companyDetailsObj={};
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		companyDetails.push(companyDetailsObj);
	  	        	  }
	  	            data = companyDetails.filter(function(item) {
	  	              return JSON.stringify(item).toLowerCase().indexOf(ft) !== -1;
	  	            });
	  	            $scope.setPagingData(data, page, pageSize);
	  	          });
	  	        } else {
	  	          $http.post('/RLMS/admin/getAllCompanyDetails').success(function(largeLoad) {
	  	        	  var companyDetails=[];
	  	        	  var companyDetailsObj={};
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		companyDetails.push(companyDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(companyDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };

	  	    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

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
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions
	  	    };
		
	}]);
})();
