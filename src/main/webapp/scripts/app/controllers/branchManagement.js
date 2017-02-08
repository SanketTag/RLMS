(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('branchManagement', ['$scope', '$filter','serviceApi','$route','$http','utility','$rootScope', function($scope, $filter,serviceApi,$route,$http,utility,$rootScope) {
		$scope.goToAddBranch =function(){
			window.location.hash = "#/add-branch";
		};
		$scope.showTable = false;
		loadCompanyData();
		$scope.selectedCompany={};
		$scope.showCompany = false;
		function loadCompanyData(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		}
		//-------Branch Details Table---------
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
	  	          $http.post('/RLMS/admin/getListOfBranchDtls',companyData).success(function(largeLoad) {
	  	        	$scope.showTable=true;
	  	        	  var branchDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var brachDetailsObj={};
	  	        		brachDetailsObj["Branch Name"] =largeLoad[i].branchName;
	  	        		brachDetailsObj["Branch Address"] =largeLoad[i].branchAddress;
	  	        		brachDetailsObj["Company Name"] =largeLoad[i].companyName;
	  	        		brachDetailsObj["Number Of Technicians"] =largeLoad[i].numberOfTechnicians;
	  	        		brachDetailsObj["Number Of Lifts"] =largeLoad[i].numberOfLifts;
	  	        		branchDetails[i].push(brachDetailsObj);
	  	        	  }
	  	            data = branchDetails.filter(function(item) {
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
	  	          $http.post('/RLMS/admin/getListOfBranchDtls',companyData).success(function(largeLoad) {
	  	        	$scope.showTable =true;
	  	        	var branchDetails=[];
	  	        	  for(var i=0;i<largeLoad.length;i++){
	  	        		var brachDetailsObj={};
	  	        		brachDetailsObj["Branch_Name"] =largeLoad[i].branchName;
	  	        		brachDetailsObj["Branch_Address"] =largeLoad[i].branchAddress;
	  	        		brachDetailsObj["Company_Name"] =largeLoad[i].companyName;
	  	        		brachDetailsObj["Number_Of_Technicians"] =largeLoad[i].numberOfTechnicians;
	  	        		brachDetailsObj["Number_Of_Lifts"] =largeLoad[i].numberOfLifts;
	  	        		branchDetails.push(brachDetailsObj);
	  	        	  }
	  	            $scope.setPagingData(branchDetails, page, pageSize);
	  	          });
	  	          
	  	        }
	  	      }, 100);
	  	    };
	  	    
	  	  $scope.loadBranchInfo=function(){
		  	    	 $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
		  	 }
		  	if($rootScope.loggedInUserInfo.data.userRole.userRoleId == 1){
				$scope.showCompany= true;
			}else{
				$scope.loadBranchInfo();
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
	  	      enablePaging: true,
	  	      showFooter: true,
	  	      totalServerItems: 'totalServerItems',
	  	      pagingOptions: $scope.pagingOptions,
	  	      filterOptions: $scope.filterOptions
	  	    };
	}]);
})();
