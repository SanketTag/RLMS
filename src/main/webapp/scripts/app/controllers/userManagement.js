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
		$scope.editUser =function(){
			window.location.hash = "#/edit-user";
		};
		$rootScope.editUser={};
		$scope.editThisUser=function(row){
			var fullName=row.Name.replace(/-/g, '').split(" ");
			$rootScope.editUser.userId=row.Id;
			$rootScope.editUser.firstName=fullName[0];
			$rootScope.editUser.lastName=fullName[1];
			$rootScope.editUser.company=row.Company.replace(/-/g, '');
			$rootScope.editUser.contactnumber=row.Contact_Number.replace(/-/g, '');
			$rootScope.editUser.address=row.Address.replace(/-/g, '');
			$rootScope.editUser.area=row.Area;
			$rootScope.editUser.city=row.City.replace(/-/g, '');
			$rootScope.editUser.pincode=row.PinCode;
			$rootScope.editUser.branch=row.Branch.replace(/-/g, '');
			$rootScope.editUser.role=row.Role.replace(/-/g, '');
			$rootScope.editUser.emailid=row.Email_Id.replace(/-/g, '');
			window.location.hash = "#/edit-user";
			/*
			$rootScope.editComplaint.complaintsNumber=row.Number.replace(/-/g, '');
			$rootScope.editComplaint.complaintsTitle=row.Title.replace(/-/g, '');
			$rootScope.editComplaint.complaintsAddress=row.Address.replace(/-/g, '');
			$rootScope.editComplaint.complaintsCity=row.City.replace(/-/g, '');
			$rootScope.editComplaint.regDate=row.Registration_Date;
			$rootScope.editComplaint.serviceEndDate=row.Service_End_Date;
			$rootScope.editComplaint.serviceStartDate=row.Service_StartDate;
			$rootScope.selectedComplaintStatus=row.Status;
			//$rootScope.editComplaint.complaintsStatus=row.Status.replace(/-/g, '');
			var dataToSend ={
					complaintId:row.Number
			}
			serviceApi.doPostWithData('/RLMS/complaint/getAllTechniciansToAssignComplaint',dataToSend)
			.then(function(data) {
				$rootScope.techniciansForEditComplaints = data;
				var technicianArray=$rootScope.techniciansForEditComplaints;
				technicianArray.forEach(function(technician) {
					if(row.Technician.includes(technician.name)){
						$rootScope.selectedTechnician=technician;
					}
				});
				window.location.hash = "#/edit-complaint";
			});*/
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
	  	        		if(!!largeLoad[i].userId){
	  	        			userDetailsObj["Id"] =largeLoad[i].userId;
	  	        		}else{
	  	        			userDetailsObj["Id"] =" - ";
	  	        		}
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
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userRoleName){
	  	        			userDetailsObj["Role"] =largeLoad[i].userRoleName;
	  	        		}else{
	  	        			userDetailsObj["Role"] =" - ";
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
		  	        	if(!!largeLoad[i].userId){
	  	        			userDetailsObj["Id"] =largeLoad[i].userId;
	  	        		}else{
	  	        			userDetailsObj["Id"] =" - ";
	  	        		}
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
	  	        		if(!!largeLoad[i].area){
	  	        			userDetailsObj["Area"] =largeLoad[i].area;
	  	        		}
	  	        		if(!!largeLoad[i].city){
	  	        			userDetailsObj["City"] =largeLoad[i].city;
	  	        		}else{
	  	        			userDetailsObj["City"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].pinCode){
	  	        			userDetailsObj["PinCode"] =largeLoad[i].pinCode;
	  	        		}
	  	        		if(!!largeLoad[i].branchName){
	  	        			userDetailsObj["Branch"] =largeLoad[i].branchName;
	  	        		}else{
	  	        			userDetailsObj["Branch"] =" - ";
	  	        		}
	  	        		if(!!largeLoad[i].userRoleName){
	  	        			userDetailsObj["Role"] =largeLoad[i].userRoleName;
	  	        		}else{
	  	        			userDetailsObj["Role"] =" - ";
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
	  	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
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
	  	      gridFooterHeight:35,
	  	      columnDefs : [ {
				field : "Name",
				displayName:"Name"
			}, {
				field : "Company",
				displayName:"Company"
			}, {
				field : "Contact_Number",
				displayName:"Contact_Number"
			}, {
				field : "Address",
				displayName:"Address"
			}
			, {
				field : "City",
				displayName:"City"
			}, {
				field : "Branch",
				displayName:"Branch"
			}
			, {
				field : "Role",
				displayName:"Role"
			}
			, {
				field : "Email_Id",
				displayName:"Email_Id"
			},{
				cellTemplate :  
		             '<button ng-click="$event.stopPropagation(); editThisUser(row.entity);" title="Edit" style="margin-top: 6px;height: 24px;" class="btn-sky"><span class="glyphicon glyphicon-pencil"></span></button>',
				width : 30
			}
			]
	  	    };
		
	}]);
})();
