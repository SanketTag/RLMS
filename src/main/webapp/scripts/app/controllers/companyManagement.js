(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('companyManagement', ['$scope', '$filter','serviceApi','$route','$http','utility', function($scope, $filter,serviceApi,$route,$http,utility) {
		$scope.goToAddCompany =function(){
			window.location.hash = "#/add-company";
		};
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
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var companyDetailsObj={};
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfBranches){
	  	        			companyDetailsObj["Total_Branches"] =largeLoad[i].numberOfBranches;
	  	        		}else{
	  	        			companyDetailsObj["Total_Branches"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfTech){
	  	        			companyDetailsObj["Total_Technicians"] =largeLoad[i].numberOfTech;
	  	        		}else{
	  	        			companyDetailsObj["Total_Technicians"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfLifts){
	  	        			companyDetailsObj["Total_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		}else{
	  	        			companyDetailsObj["Total_Lifts"] =" - ";
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
	  	        	  for(var i=0;i<largeLoad.length;i++){
		  	        	var companyDetailsObj={};
	  	        		if(!!largeLoad[i].companyName){
	  	        			companyDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		}else{
	  	        			companyDetailsObj["Company_Name"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].address){
	  	        			companyDetailsObj["Address"] =largeLoad[i].address;
	  	        		}else{
	  	        			companyDetailsObj["Address"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].contactNumber){
	  	        			companyDetailsObj["Contact_Number"] =largeLoad[i].contactNumber;
	  	        		}else{
	  	        			companyDetailsObj["Contact_Number"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].emailId){
	  	        			companyDetailsObj["Email_Id"] =largeLoad[i].emailId;
	  	        		}else{
	  	        			companyDetailsObj["Email_Id"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfBranches){
	  	        			companyDetailsObj["Total_Branches"] =largeLoad[i].numberOfBranches;
	  	        		}else{
	  	        			companyDetailsObj["Total_Branches"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfTech){
	  	        			companyDetailsObj["Total_Technicians"] =largeLoad[i].numberOfTech;
	  	        		}else{
	  	        			companyDetailsObj["Total_Technicians"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].numberOfLifts){
	  	        			companyDetailsObj["Total_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		}else{
	  	        			companyDetailsObj["Total_Lifts"] =" - ";
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
