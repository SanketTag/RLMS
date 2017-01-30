(function () {
    'use strict';
	angular.module('rlmsApp')
	.controller('addBranchCtrl', ['$scope', '$filter','serviceApi','$route','$http','utility', function($scope, $filter,serviceApi,$route,$http,utility) {
		//initialize add Branch
		initAddBranch();
		loadCompayInfo();
		//loadBranchListInfo();
		function initAddBranch(){
			$scope.selectedCompany = {};
			$scope.addBranch={
					companyId:'',
					branchName:'',
					branchAddress:''	
			};	
		    $scope.companies = [];
		    $scope.branchList={};
		}
		//load compay dropdown data
		function loadCompayInfo(){
			serviceApi.doPostWithoutData('/RLMS/admin/getAllApplicableCompanies')
		    .then(function(response){
		    		$scope.companies = response;
		    });
		};
		//Post call add branch
		$scope.submitAddBranch = function(){
			$scope.addBranch.companyId = $scope.selectedCompany.selected.companyId;
			serviceApi.doPostWithData("/RLMS/admin/addNewBranchInCompany",$scope.addBranch)
			.then(function(response){
				var key = Object.keys(response);
				var successMessage = response[key[0]];
				initAddBranch();
				utility.showMessage('Branch Added',successMessage,'success');
				$route.reload();
			});
		}
		//rese add branch
		$scope.resetAddBranch = function(){
			initAddBranch();
			$route.reload();
		}

//	    
//	    var data ={
//	    		companyId:1
//	    }
//	    serviceApi.doPostWithData('/RLMS/admin/getAllBranchesForCompany',data)
//	    .then(function(response){
//	    	console.log("getAllBranchesForCompany");
//	    	console.log(response);
//	    	console.log("__________________");
//	    });
//	    
//	    serviceApi.doPostWithoutData('/RLMS/admin/getAllUsersForCompany',data)
//	    .then(function(response){
//	    	console.log("getAllUsersForCompany");
//	    	console.log(response);
//	    	console.log("__________________");
//	    });
	    
		
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
	  	      setTimeout(function() {
	  	        var data;
	  	        if (searchText) {
	  	          var ft = searchText.toLowerCase();
	  	          $http.post('/RLMS/admin/getListOfBranchDtls').success(function(largeLoad) {
	  	        	  var branchDetails=[];
	  	        	  var brachDetailsObj={};
	  	        	  for(var i=0;i<largeLoad.length;i++){
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
	  	          $http.post('/RLMS/admin/getListOfBranchDtls').success(function(largeLoad) {
	  	        	var branchDetails=[];
	  	        	  var brachDetailsObj={};
	  	        	  for(var i=0;i<largeLoad.length;i++){
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
