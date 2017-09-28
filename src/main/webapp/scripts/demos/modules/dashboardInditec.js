angular.module('theme.demos.dashboard.indi', [
  'angular-skycons',
  'theme.demos.forms',
  'theme.demos.tasks'
])
  .controller('DashboardControllerInditech', ['$scope', '$timeout', '$window', '$modal', 'serviceApi', '$filter', '$rootScope', function ($scope, $timeout, $window, $modal, serviceApi, $filter, $rootScope) {
    'use strict';
    $scope.totalServerItemsForComplaints = 0;
    $scope.pagingOptionsForComplaints = {
      pageSizes: [10, 20, 50],
      pageSize: 10,
      currentPage: 1
    };
    
    $scope.showDasboardForInditech=false;
    $scope.showDasboardForCompany=false;
    $scope.showDasboardForOthers=false;
    if($rootScope.loggedInUserInfo){
    	if($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1){
    		$scope.showDasboardForInditech= true;
    		$scope.showDasboardForOthers=false;
    	}else{
    		$scope.showDasboardForOthers=true;
    		$scope.showDasboardForInditech=false;
    	}
    }else{
    	$scope.showDasboardForOthers=true;
    }

    $scope.technicianData = {
      totalTechnicians: {
        title: 'Total',
        text: '0',
        color: 'red'
      },
      activeTechnicians: {
        title: 'Active',
        text: '0',
        color: 'amber'
      },
      inactiveTechnicians: {
        title: 'Inactive',
        text: '0',
        color: 'blue'
      }
    };

    $scope.complaintsData = {
      totalComplaints: {
        title: 'Total',
        text: '0',
        color: 'red'
      },
      totalUnassignedComplaints: {
        title: 'Total Unassigned',
        text: '0',
        color: 'amber'
      },
      totalAssignedComplaints: {
        title: 'Total Assigned',
        text: '0',
        color: 'blue'
      },
      totalResolvedComplaints: {
        title: 'Total Resolved',
        text: '0',
        color: 'green'
      },
      totalPendingComplaints: {
        title: 'Total Pending',
        text: '0',
        color: 'indigo'
      },
      avgLogPerDay: {
        title: 'Avg Log Per Day',
        text: '0',
        color: 'grey'
      },
      todaysTotalComplaints: {
        title: 'Todays Total',
        text: '0',
        color: 'red'
      },
      todaysUnassignedComplaints: {
        title: 'Todays Unassigned',
        text: '0',
        color: 'amber'
      },
      todaysAssignedComplaints: {
        title: 'Todays Assigned',
        text: '0',
        color: 'blue'
      },
      todaysResolvedComplaints: {
        title: 'Todays Resolved',
        text: '0',
        color: 'green'
      },
      todaysPandingComplaints: {
        title: 'Todays Pending',
        text: '0',
        color: 'indigo'
      },
      avgResolvedPerDayRegistered: {
        title: 'Avg Resolved Per Day',
        text: '0',
        color: 'grey'
      }
    };

    $scope.pendingComplaints = {
      title: 'Pending Complaints',
      text: '0',
      color: 'red'
    };

    $scope.assignedComplaints = {
      title: 'Assigned Complaints',
      text: '0',
      color: 'amber'
    };

    $scope.attemptedTodayComplaints = {
      title: 'Complaints Attempted Today',
      text: '0',
      color: 'blue'
    };
    $scope.resolvedComplaints = {
      title: 'Resolved Complaints',
      text: '0',
      color: 'green'
    };
    $scope.totalComplaints = {
      title: 'Total Complaints',
      text: '0',
      color: 'indigo'
    };
    $scope.newCustomerRegistered = {
      title: 'New Customers Registered',
      text: '0',
      color: 'grey'
    };
    $scope.gridOptionsForComplaints = {
      data: 'myComplaintsData',
      rowHeight: 40,
      enablePaging: true,
      showFooter: true,
      totalServerItems: 'totalServerItemsForComplaints',
      pagingOptions: $scope.pagingOptionsForComplaints,
      filterOptions: $scope.filterOptionsForModal,
      multiSelect: false,
      gridFooterHeight: 35,
      enableRowSelection: true,
      selectedItems: [],
      afterSelectionChange: function (rowItem, event) {
      }
    };

    $scope.filterOptionsForModal = {
      filterText: '',
      useExternalFilter: true
    };

    $scope.todaysDate = new Date();
    $scope.todaysDate.setHours(0, 0, 0, 0);
    $scope.getComplaintsCount = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');
      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
            '/RLMS/dashboard/getListOfComplaintsForDashboard',
            dataToSend)
            .then(
            function (
              largeLoad) {
              if (complaintStatusArray.includes('2') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.complaintsData.totalPendingComplaints.text = largeLoad.length;
                $scope.complaintsData.totalUnassignedComplaints.text = largeLoad.length;
                $scope.todaysUnassignedComplaints = largeLoad.filter(function (item) {
                  return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                });
                if ($scope.todaysUnassignedComplaints.length > 0) {
                  $scope.complaintsData.todaysUnassignedComplaints.text = $scope.todaysUnassignedComplaints.length;
                }
                $scope.todaysPendingComplaints = largeLoad.filter(function (item) {
                  return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                });
                if ($scope.todaysPendingComplaints.length > 0) {
                  $scope.complaintsData.todaysPandingComplaints.text = $scope.todaysPendingComplaints.length;
                }
              }
              if (complaintStatusArray.includes('3') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.complaintsData.totalAssignedComplaints.text = largeLoad.length;
                $scope.todaysAssignedComplaints = largeLoad.filter(function (item) {
                  return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                });
                if ($scope.todaysAssignedComplaints.length > 0) {
                  $scope.complaintsData.todaysAssignedComplaints.text = $scope.todaysAssignedComplaints.length;
                }
              }
              if (complaintStatusArray.includes('4') && complaintStatusArray.length == 1 && largeLoad.length > 0) {
                $scope.complaintsData.totalResolvedComplaints.text = largeLoad.length;
                $scope.todaysResolvedComplaints = largeLoad.filter(function (item) {
                  return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                });
                if ($scope.todaysResolvedComplaints.length > 0) {
                  $scope.complaintsData.todaysResolvedComplaints.text = $scope.todaysResolvedComplaints.length;
                }
              }
              if (complaintStatusArray.includes('2') && complaintStatusArray.length == 3 && largeLoad.length > 0) {
                $scope.complaintsData.totalComplaints.text = largeLoad.length;
                $scope.todaysTotalComplaints = largeLoad.filter(function (item) {
                  return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                });
                if ($scope.todaysTotalComplaints.length > 0) {
                  $scope.complaintsData.todaysTotalComplaints.text = $scope.todaysTotalComplaints.length;
                }
              }
            });
        }, 100);
    };

    $scope.getComplaintsCountForSiteVisited = function (complaintStatus) {
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');

      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      setTimeout(
        function () {
          var dataToSend = $scope
            .construnctObjeToSend(complaintStatusArray);
          serviceApi
            .doPostWithData(
            '/RLMS/dashboard/getListOfComplaintsForSiteVisited',
            dataToSend)
            .then(
            function (
              largeLoad) {
              if (largeLoad.length > 0) {
                $scope.attemptedTodayComplaints.text = largeLoad.length;
              }
            });
        }, 100);
    };

    $scope.getComplaintsCount('2');
    $scope.getComplaintsCount('3');
    $scope.getComplaintsCount('4');
    $scope.getComplaintsCount('2,3,4');
    $scope.getComplaintsCountForSiteVisited('2,3,4');

    $scope.todaysComplaintsList = function (title) {
      var emptyComplaintsArray = [];
      $scope.myComplaintsData = emptyComplaintsArray;
      $scope.pagingOptionsForComplaints.currentPage = 1;
      $scope.totalServerItemsForComplaints = 0;
      if (title == "todaysTotalPending") {
        $scope.tempComplaintsData = $scope.todaysPendingComplaints;
      } else if (title == "todaysUnassigned") {
        $scope.tempComplaintsData = $scope.todaysUnassignedComplaints;
      } else if (title == "todaysResolved") {
        $scope.tempComplaintsData = $scope.todaysResolvedComplaints;
      } else if (title == "todaysAssigned") {
        $scope.tempComplaintsData = $scope.todaysAssignedComplaints;
      } else if (title == "todaysTotal") {
        $scope.tempComplaintsData = $scope.todaysTotalComplaints;
      }
      var userDetails = [];
      var userDetailsObj = {};
      var i = 0;
      for (i = 0; i < $scope.tempComplaintsData.length; i++) {
        userDetailsObj["No"] = $scope.tempComplaintsData[i].complaintNumber;
        userDetailsObj["CompanyName"] = $scope.tempComplaintsData[i].companyName;
        userDetailsObj["Title"] = $scope.tempComplaintsData[i].title;
        userDetailsObj["City"] = $scope.tempComplaintsData[i].city;
        userDetails.push(userDetailsObj);
      }
      $scope.setPagingDataForComplaints(userDetails, $scope.pagingOptionsForComplaints.currentPage, $scope.pagingOptionsForComplaints.pageSize);
      $scope.modalInstance = $modal.open({
        templateUrl: 'demoModalContent.html',
        scope: $scope
      });
    };

    $scope.openDemoModal = function (currentModelOpen, complaintStatus, headerValue,isTodaysData) {
      var emptyComplaintsArray = [];
      $scope.myComplaintsData = emptyComplaintsArray;
      $scope.pagingOptionsForComplaints.currentPage = 1;
      $scope.totalServerItemsForComplaints = 0;
      $scope.filterOptionsForModal.filterText='';
      $scope.currentModel = currentModelOpen;
      $scope.modalHeaderVal = headerValue;
      $scope.isTodaysData=isTodaysData;
      var complaintStatusArray = [];
      var str_array = complaintStatus.split(',');
      for (var i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
        complaintStatusArray.push(str_array[i]);
      }
      $scope.currentComplaintStatus = complaintStatusArray;
      $scope.getPagedDataAsyncForComplaints($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "", complaintStatusArray, currentModelOpen,isTodaysData);
      $scope.complaintStatusValue = complaintStatusArray;
      $scope.modalInstance = $modal.open({
        templateUrl: 'demoModalContent.html',
        scope: $scope
      });
    };

    $scope.$watch('pagingOptionsForComplaints', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        $scope.getPagedDataAsyncForComplaints($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, $scope.filterOptionsForModal.filterText, $scope.currentComplaintStatus, $scope.currentModel);
      }
    }, true);
    $scope
      .$watch(
      'filterOptionsForModal',
      function (newVal, oldVal) {
        if (newVal !== oldVal) {
          $scope
            .getPagedDataAsyncForComplaints(
            $scope.pagingOptionsForComplaints.pageSize,
            $scope.pagingOptionsForComplaints.currentPage,
            $scope.filterOptionsForModal.filterText,
            $scope.currentComplaintStatus,
            $scope.currentModel,
            $scope.isTodaysData);
        }
      }, true);

    $scope.setPagingDataForComplaints = function (data, page, pageSize) {
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        $scope.myComplaintsData = pagedData;
        $scope.totalServerItemsForComplaints = data.length;
        if (!$scope.$$phase) {
          $scope.$apply();
        }
      };
      
    $scope.getPagedDataAsyncForComplaints = function (pageSize,
      page, searchText, complaintStatus, callingModel,isTodaysData) {
      var url;
      if (callingModel == 'attemptedToday') {
        url = '/RLMS/dashboard/getListOfComplaintsForSiteVisited';
      } else {
        url = '/RLMS/dashboard/getListOfComplaintsForDashboard';
      }
      setTimeout(
        function () {
          var data;
          if (searchText) {
            var ft = searchText
              .toLowerCase();
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData(url, dataToSend)
              .then(
              function (largeLoad) {
                $scope.complaints = largeLoad;
                $scope.showTable = true;
                var userDetails = [];
                if(isTodaysData){
                	largeLoad=largeLoad.filter(function (item) {
                        return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                      });
                }
                for (var i = 0; i < largeLoad.length; i++) {
                  var userDetailsObj = {};
                  if (!!largeLoad[i].complaintNumber) {
                    userDetailsObj["No"] = largeLoad[i].complaintNumber;
                  } else {
                    userDetailsObj["No"] = " - ";
                  }
                  if (!!largeLoad[i].title) {
                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
                  } else {
                    userDetailsObj["CompanyName"] = " - ";
                  }
                  if (complaintStatus.includes("2") && complaintStatus.includes("3") && complaintStatus.includes("4")) {
                      if (!!largeLoad[i].status) {
                        userDetailsObj["Status"] = largeLoad[i].status;
                      } else {
                        userDetailsObj["Status"] = " - ";
                      }
                    } else {
                    if (!!largeLoad[i].title) {
                      userDetailsObj["Title"] = largeLoad[i].title;
                    } else {
                      userDetailsObj["Title"] = " - ";
                    }
                  }
                  if (!!largeLoad[i].title) {
                    userDetailsObj["Title"] = largeLoad[i].title;
                  } else {
                    userDetailsObj["Title"] = " - ";
                  }
                  if (!!largeLoad[i].city) {
                    userDetailsObj["City"] = largeLoad[i].city;
                  } else {
                    userDetailsObj["City"] = " - ";
                  }
                  userDetails
                    .push(userDetailsObj);
                }
                
                data = userDetails
                  .filter(function (
                    item) {
                    return JSON
                      .stringify(
                      item)
                      .toLowerCase()
                      .indexOf(
                      ft) !== -1;
                  });
                $scope
                  .setPagingDataForComplaints(
                  data,
                  page,
                  pageSize);
              });
          } else {
            var dataToSend = $scope
              .construnctObjeToSend(complaintStatus);
            serviceApi
              .doPostWithData(url,
              dataToSend)
              .then(
              function (
                largeLoad) {
                $scope.complaints = largeLoad;
                $scope.showTable = true;
                var userDetails = [];
                if(isTodaysData){
                	largeLoad=largeLoad.filter(function (item) {
                        return (new Date(item.updatedDate)).getTime() === $scope.todaysDate.getTime();
                      });
                }
                for (var i = 0; i < largeLoad.length; i++) {
                  var userDetailsObj = {};
                  if (!!largeLoad[i].complaintNumber) {
                    userDetailsObj["No"] = largeLoad[i].complaintNumber;
                  } else {
                    userDetailsObj["No"] = " - ";
                  }
                  if (!!largeLoad[i].title) {
                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
                  } else {
                    userDetailsObj["CompanyName"] = " - ";
                  }
                  if (complaintStatus.includes("2") && complaintStatus.includes("3") && complaintStatus.includes("4")) {
                    if (!!largeLoad[i].status) {
                      userDetailsObj["Status"] = largeLoad[i].status;
                    } else {
                      userDetailsObj["Status"] = " - ";
                    }
                  } else {
                    if (!!largeLoad[i].title) {
                      userDetailsObj["Title"] = largeLoad[i].title;
                    } else {
                      userDetailsObj["Title"] = " - ";
                    }
                  }
                  if (!!largeLoad[i].city) {
                    userDetailsObj["City"] = largeLoad[i].city;
                  } else {
                    userDetailsObj["City"] = " - ";
                  }
                  userDetails
                    .push(userDetailsObj);
                }
                $scope
                  .setPagingDataForComplaints(
                  userDetails,
                  page,
                  pageSize);
              });

          }
        }, 100);
    };

    $scope.cancel = function () {
      $scope.modalInstance.dismiss('cancel');
    };

    $scope.construnctObjeToSend = function (complaintStatus) {
      var dataToSend = {
        statusList: [],
        companyId: $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
      };
      dataToSend["statusList"] = complaintStatus;
      return dataToSend;
    };
    
    $scope.openDemoModalForTechnician = function (currentModelOpen, headerValue, activeFlag) {
        var emptyComplaintsArray = [];
        $scope.myComplaintsData = emptyComplaintsArray;
        $scope.pagingOptionsForComplaints.currentPage = 1;
        $scope.totalServerItemsForComplaints = 0;
        $scope.currentModel = currentModelOpen;
        $scope.modalHeaderVal = headerValue;
        $scope.getPagedDataAsyncForTechnician($scope.pagingOptionsForComplaints.pageSize, $scope.pagingOptionsForComplaints.currentPage, "",currentModelOpen,activeFlag); 
        $scope.modalInstance = $modal.open({
          templateUrl: 'demoModalContent.html',
          scope: $scope
        });
      };
    
    $scope.getPagedDataAsyncForTechnician = function (pageSize,
    	      page, searchText, callingModel, activeFlag) {
    	      var url;
    	      url = '/RLMS/dashboard/getListOfTechniciansForDashboard';
    	      setTimeout(
    	        function () {
    	          var data;
    	          if (searchText) {
    	            var ft = searchText
    	              .toLowerCase();
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(url, dataToSend)
    	              .then(
    	              function (largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
    	                  var userDetailsObj = {};
    	                  if (!!largeLoad[i].userId) {
    	                    userDetailsObj["No"] = largeLoad[i].userId;
    	                  } else {
    	                    userDetailsObj["No"] = " - ";
    	                  }
    	                  if (!!largeLoad[i].title) {
    	                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
    	                  } else {
    	                    userDetailsObj["CompanyName"] = " - ";
    	                  }
    	                  if (!!largeLoad[i].city) {
    	                    userDetailsObj["City"] = largeLoad[i].city;
    	                  } else {
    	                    userDetailsObj["City"] = " - ";
    	                  }
    	                  userDetails
    	                    .push(userDetailsObj);
    	                }
    	                
    	                data = userDetails
    	                  .filter(function (
    	                    item) {
    	                    return JSON
    	                      .stringify(
    	                      item)
    	                      .toLowerCase()
    	                      .indexOf(
    	                      ft) !== -1;
    	                  });
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  data,
    	                  page,
    	                  pageSize);
    	              });
    	          } else {
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(url,
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                $scope.complaints = largeLoad;
    	                $scope.showTable = true;
    	                var userDetails = [];
    	                if (activeFlag=="Active") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 1;
      	                  });
      	                }
    	                if (activeFlag=="InActive") {
    	                	largeLoad = largeLoad.filter(function (item) {
      	                    return item.activeFlag === 0;
      	                  });
      	                }
    	                for (var i = 0; i < largeLoad.length; i++) {
      	                  var userDetailsObj = {};
      	                  if (!!largeLoad[i].userId) {
      	                    userDetailsObj["No"] = largeLoad[i].userId;
      	                  } else {
      	                    userDetailsObj["No"] = " - ";
      	                  }
      	                  if (!!largeLoad[i].title) {
      	                    userDetailsObj["CompanyName"] = largeLoad[i].companyName;
      	                  } else {
      	                    userDetailsObj["CompanyName"] = " - ";
      	                  }
      	                  if (!!largeLoad[i].city) {
      	                    userDetailsObj["City"] = largeLoad[i].city;
      	                  } else {
      	                    userDetailsObj["City"] = " - ";
      	                  }
      	                  userDetails
      	                    .push(userDetailsObj);
      	                }
    	                $scope
    	                  .setPagingDataForComplaints(
    	                  userDetails,
    	                  page,
    	                  pageSize);
    	              });

    	          }
    	        }, 100);
    	    };
    	    $scope.getTechnicianCount = function (technicianStatus) {
    	        var complaintStatusArray = [];
    	        
    	        setTimeout(
    	          function () {
    	            var dataToSend = $scope
    	              .construnctObjeToSendForTechnician();
    	            serviceApi
    	              .doPostWithData(
    	              '/RLMS/dashboard/getListOfTechniciansForDashboard',
    	              dataToSend)
    	              .then(
    	              function (
    	                largeLoad) {
    	                if (technicianStatus=="Active") {
    	                  $scope.activeTechnicians = largeLoad.filter(function (item) {
    	                    return item.activeFlag === 1;
    	                  });
    	                  $scope.technicianData.activeTechnicians.text=$scope.activeTechnicians.length;
    	                }
    	                if(technicianStatus=="InActive"){
    	                	$scope.inactiveTechnicians = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 0;
        	                  });
        	                  $scope.technicianData.inactiveTechnicians.text=$scope.inactiveTechnicians.length;
    	                }
    	                if(technicianStatus=="TotalTechnician"){
    	                	$scope.totalTechnicians = largeLoad.filter(function (item) {
        	                    return item.activeFlag === 1;
        	                  });
        	                  $scope.technicianData.totalTechnicians.text=$scope.totalTechnicians.length;
    	                }
    	              });
    	          }, 100);
    	      };
    	      
    $scope.getTechnicianCount("Active");
    $scope.getTechnicianCount("InActive");
    $scope.getTechnicianCount("TotalTechnician");
    
    $scope.construnctObjeToSendForTechnician = function () {
        var dataToSend = {
          companyId: $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
        };
        return dataToSend;
      };
  }]);