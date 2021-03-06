(function() {
	'use strict';
	angular
			.module('rlmsApp')
			.controller(
					'complaiantManagementCtrl',
					[
							'$scope',
							'$filter',
							'serviceApi',
							'$route',
							'$http',
							'utility',
							'$rootScope',
							'$modal',
							'$log',
							function($scope, $filter, serviceApi, $route,
									$http, utility, $rootScope,$modal,$log) {
								initCustomerList();
								$scope.showCompany = false;
								$scope.showBranch = false;
								
								$scope.goToAddComplaint = function() {
									window.location.hash = "#/add-complaint";
								};
								
								function initCustomerList() {
									$scope.date = {
								        startDate: moment().subtract(1, "days"),
								        endDate: moment()
								    };
									$scope.alert = { type: 'success', msg: 'You successfully Added Complaint.',close:true };
									$scope.showAlert = false;
									$scope.selectedCompany = {};
									$scope.selectedBranch = {};
									$scope.selectedCustomer = {};
									$scope.selectedLifts = {};
									$scope.branches = [];
									$scope.selectedlifts = {};
									$scope.selectedStatus = {};
									$scope.selectedTechnician = {};
									$scope.dateRange={};
									$scope.isAssigned=true;
									var today = new Date().toISOString().slice(0, 10);
									$scope.dateRange.date = {"startDate": today, "endDate": today};
									$scope.status = [ {
										id : 2,
										name : 'Pending'
									}, {
										id : 3,
										name : 'Assigned'
									}, {
										id : 4,
										name : 'Completed'
									} ];
									$scope.lifts = [];
									$scope.showAdvanceFilter = false;
									$scope.showTable = false;
								}
								function loadCompanyData() {
									serviceApi
											.doPostWithoutData(
													'/RLMS/admin/getAllApplicableCompanies')
											.then(function(response) {
												$scope.companies = response;
											});
								}
								$scope.loadBranchData = function() {
									var companyData = {};
									if ($scope.showCompany == true) {
										companyData = {
											companyId : $scope.selectedCompany.selected.companyId
										}
									} else {
										companyData = {
											companyId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyMaster.companyId
										}
									}
									serviceApi
											.doPostWithData(
													'/RLMS/admin/getAllBranchesForCompany',
													companyData)
											.then(function(response) {
												$scope.branches = response;

											});
								}
								$scope.loadCustomerData = function() {
									var branchData = {};
									if ($scope.showBranch == true) {
										branchData = {
											branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId
										}
									} else {
										branchData = {
											branchCompanyMapId : $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
										}
									}
									serviceApi
											.doPostWithData(
													'/RLMS/admin/getAllCustomersForBranch',
													branchData)
											.then(
													function(customerData) {
														var tempAll = {
															branchCustomerMapId : -1,
															firstName : "All"
														}
														$scope.cutomers = customerData;
														$scope.cutomers
																.unshift(tempAll);
													})
								}
								$scope.loadLifts = function() {
									var dataToSend = {
										branchCompanyMapId : $scope.selectedBranch.selected.companyBranchMapId,
										branchCustomerMapId : $scope.selectedCustomer.selected.branchCustomerMapId
									}
									serviceApi.doPostWithData('/RLMS/complaint/getAllApplicableLifts',dataToSend)
											.then(function(liftData) {
												$scope.lifts = liftData;
											})
								}
								// Toggle Advance Filter
								$scope.toggleAdvanceFilter = function() {
									if ($scope.showAdvanceFilter == true) {
										$scope.showAdvanceFilter = false;
									} else {
										$scope.showAdvanceFilter = true;
										$scope.loadLifts();
									}
								};
								$scope.filterOptions = {
									filterText : '',
									useExternalFilter : true
								};
								$scope.totalServerItems = 0;
								$scope.pagingOptions = {
									pageSizes : [ 10, 20, 50 ],
									pageSize : 10,
									currentPage : 1
								};
								$scope.setPagingData = function(data, page,
										pageSize) {
									var pagedData = data.slice((page - 1)
											* pageSize, page * pageSize);
									$scope.myData = pagedData;
									$scope.totalServerItems = data.length;
									if (!$scope.$$phase) {
										$scope.$apply();
									}
								};
								$scope.getPagedDataAsync = function(pageSize,
										page, searchText) {

									setTimeout(
											function() {
												var data;
												if (searchText) {
													var ft = searchText
															.toLowerCase();
													var dataToSend = $scope
															.construnctObjeToSend();
													serviceApi
															.doPostWithData('/RLMS/complaint/getListOfComplaints',dataToSend)
															.then(
																	function(largeLoad) {
																		$scope.complaints = largeLoad;
																		$scope.showTable = true;
																		var userDetails = [];
																		for (var i = 0; i < largeLoad.length; i++) {
																			var userDetailsObj = {};
																			if (!!largeLoad[i].complaintNumber) {
																				userDetailsObj["Complaint_Number"] = largeLoad[i].complaintNumber;
																			} else {
																				userDetailsObj["Complaint_Number"] = " - ";
																			}
																			if (!!largeLoad[i].title) {
																				userDetailsObj["Title"] = largeLoad[i].title;
																			} else {
																				userDetailsObj["Title"] = " - ";
																			}
																		
																			if (!!largeLoad[i].registrationDate) {
																				userDetailsObj["Registration_Date"] = largeLoad[i].registrationDate;
																			} else {
																				userDetailsObj["Registration_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDate) {
																				userDetailsObj["Service_StartDate"] = largeLoad[i].serviceStartDate;
																			} else {
																				userDetailsObj["Service_StartDate"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_Start_Date"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_Start_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceEndDateStr) {
																				userDetailsObj["Service_End_Date"] = largeLoad[i].serviceEndDateStr;
																			} else {
																				userDetailsObj["Service_End_Date"] = " - ";
																			}
																			if (!!largeLoad[i].liftAddress) {
																				userDetailsObj["Address"] = largeLoad[i].liftAddress;
																			} else {
																				userDetailsObj["Address"] = " - ";
																			}
																			if (!!largeLoad[i].city) {
																				userDetailsObj["City"] = largeLoad[i].city;
																			} else {
																				userDetailsObj["City"] = " - ";
																			}
																			if (!!largeLoad[i].status) {
																				userDetailsObj["Status"] = largeLoad[i].status;
																			} else {
																				userDetailsObj["Status"] = " - ";
																			}
																			if (!!largeLoad[i].technicianDtls) {
																				userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
																			} else {
																				userDetailsObj["Technician"] = " - ";
																			}
																			if (!!largeLoad[i].technicianDtls) {
																				userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
																			} else {
																				userDetailsObj["Technician"] = " - ";
																			}
																			if (!!largeLoad[i].complaintId) {
																				userDetailsObj["complaintId"] = largeLoad[i].complaintId;
																			} else {
																				userDetailsObj["complaintId"] = " - ";
																			}
																			
																			userDetails
																					.push(userDetailsObj);
																		}
																		data = userDetails
																				.filter(function(
																						item) {
																					return JSON
																							.stringify(
																									item)
																							.toLowerCase()
																							.indexOf(
																									ft) !== -1;
																				});
																		$scope
																				.setPagingData(
																						data,
																						page,
																						pageSize);
																	});
												} else {
													var dataToSend = $scope
															.construnctObjeToSend();
													serviceApi
															.doPostWithData(
																	'/RLMS/complaint/getListOfComplaints',
																	dataToSend)
															.then(
																	function(
																			largeLoad) {
																		$scope.complaints = largeLoad;
																		$scope.showTable = true;
																		var userDetails = [];
																		for (var i = 0; i < largeLoad.length; i++) {
																			var userDetailsObj = {};
																			if (!!largeLoad[i].complaintNumber) {
																				userDetailsObj["Number"] = largeLoad[i].complaintNumber;
																			} else {
																				userDetailsObj["Number"] = " - ";
																			}
																			if (!!largeLoad[i].title) {
																				userDetailsObj["Title"] = largeLoad[i].title;
																			} else {
																				userDetailsObj["Title"] = " - ";
																			}
																			
																			if (!!largeLoad[i].registrationDateStr) {
																				userDetailsObj["Registration_Date"] = largeLoad[i].registrationDateStr;
																			} else {
																				userDetailsObj["Registration_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_StartDate"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_StartDate"] = " - ";
																			}
																			if (!!largeLoad[i].serviceStartDateStr) {
																				userDetailsObj["Service_Start_Date"] = largeLoad[i].serviceStartDateStr;
																			} else {
																				userDetailsObj["Service_Start_Date"] = " - ";
																			}
																			if (!!largeLoad[i].serviceEndDateStr) {
																				userDetailsObj["Service_End_Date"] = largeLoad[i].serviceEndDateStr;
																			} else {
																				userDetailsObj["Service_End_Date"] = " - ";
																			}
																			if (!!largeLoad[i].liftAddress) {
																				userDetailsObj["Address"] = largeLoad[i].liftAddress;
																			} else {
																				userDetailsObj["Address"] = " - ";
																			}
																			if (!!largeLoad[i].city) {
																				userDetailsObj["City"] = largeLoad[i].city;
																			} else {
																				userDetailsObj["City"] = " - ";
																			}
																			if (!!largeLoad[i].status) {
																				userDetailsObj["Status"] = largeLoad[i].status;
																			} else {
																				userDetailsObj["Status"] = " - ";
																			}
																			if (!!largeLoad[i].technicianDtls) {
																				userDetailsObj["Technician"] = largeLoad[i].technicianDtls;
																			} else {
																				userDetailsObj["Technician"] = " - ";
																			}
																			if (!!largeLoad[i].complaintId) {
																				userDetailsObj["complaintId"] = largeLoad[i].complaintId;
																			} else {
																				userDetailsObj["complaintId"] = " - ";
																			}
																			userDetails
																					.push(userDetailsObj);
																		}
																		$scope
																				.setPagingData(
																						userDetails,
																						page,
																						pageSize);
																	});

												}
											}, 100);
								};
								$scope.construnctObjeToSend = function() {
									var dataToSend = {
											branchCompanyMapId:0,
											branchCustomerMapId:0,
											listOfLiftCustoMapId:[],
											statusList:[]
											
									};
									if ($scope.showBranch == true) {
										dataToSend["branchCompanyMapId"] = $scope.selectedBranch.selected.companyBranchMapId
									} else {
										dataToSend["branchCompanyMapId"] = $rootScope.loggedInUserInfo.data.userRole.rlmsCompanyBranchMapDtls.companyBranchMapId
									}
									dataToSend["branchCustomerMapId"] = $scope.selectedCustomer.selected.branchCustomerMapId;
									
									if($scope.showAdvanceFilter){
										var tempLiftIds = [];
										for (var i = 0; i < $scope.selectedlifts.selected.length; i++) {
											tempLiftIds
													.push($scope.selectedlifts.selected[i].liftId);
										}
										var tempStatus = [];
										for (var j = 0; j < $scope.selectedStatus.selected.length; j++) {
											tempStatus
													.push($scope.selectedStatus.selected[j].id);
										}
										dataToSend["listOfLiftCustoMapId"] = tempLiftIds;
										dataToSend["statusList"] = tempStatus;
										//dataToSend["fromDate"]=$scope.dateRange.date.startDate;
										//dataToSend["toDate"]=$scope.dateRange.date.endDate;
									}
									return dataToSend;
								}
								$scope.loadComplaintsList = function() {
									$scope.getPagedDataAsync(
											$scope.pagingOptions.pageSize,
											$scope.pagingOptions.currentPage);
								}
								$scope.resetComplaintList = function() {
									initCustomerList();
								};
								// showCompnay Flag
								if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel == 1) {
									$scope.showCompany = true;
									loadCompanyData();
								} else {
									$scope.showCompany = false;
									$scope.loadBranchData();
								}

								// showBranch Flag
								if ($rootScope.loggedInUserInfo.data.userRole.rlmsSpocRoleMaster.roleLevel < 3) {
									$scope.showBranch = true;
								} else {
									$scope.showBranch = false;
								}

								$scope
										.$watch(
												'pagingOptions',
												function(newVal, oldVal) {
													if (newVal !== oldVal) {
														$scope
																.getPagedDataAsync(
																		$scope.pagingOptions.pageSize,
																		$scope.pagingOptions.currentPage,
																		$scope.filterOptions.filterText);
													}
												}, true);
								$scope
										.$watch(
												'filterOptions',
												function(newVal, oldVal) {
													if (newVal !== oldVal) {
														$scope
																.getPagedDataAsync(
																		$scope.pagingOptions.pageSize,
																		$scope.pagingOptions.currentPage,
																		$scope.filterOptions.filterText);
													}
												}, true);

								$scope.gridOptions = {
									data : 'myData',
									rowHeight : 40,
									enablePaging : true,
									showFooter : true,
									totalServerItems : 'totalServerItems',
									pagingOptions : $scope.pagingOptions,
									filterOptions : $scope.filterOptions,
									multiSelect : false,
									gridFooterHeight : 35,
									enableRowSelection: true,
									selectedItems: [],
									afterSelectionChange:function(rowItem, event){
										//$scope.showAlert = false;
										//console.log(rowItem);
										//console.log(event);
										//var selected = $filter('filter')($scope.complaints,{complaintId:$scope.gridOptions.selectedItems[0].complaintId});
//										if(selected[0].Status == "Assigned"){
//											$scope.isAssigned = true;
//										}else{
//											$scope.isAssigned = false;
//										}
									},
									columnDefs : [ {
										field : "Number",
										displayName:"Number",
										width : 120
									}, {
										field : "Title",
										displayName:"Title",
										width : 120
									}, {
										field : "Remark",
										displayName:"Details",
										width : 120
									}, {
										field : "Registration_Date",
										displayName:"Registration Date",
										width : 120
									}
									, {
										field : "Service_StartDate",
										displayName:"Service Start Date",
										width : 160
									}, {
										field : "Service_End_Date",
										displayName:"Service End Date",
										width : 120
									}
									, {
										field : "Address",
										displayName:"Address",
										width : 120
									}
									, {
										field : "City",
										displayName:"City",
										width : 120
									}, {
										field : "Status",
										displayName:"Status",
										width : 120
									}
									, {
										field : "Technician",
										displayName:"Technician",
										width : 120
									},{
										field : "complaintId",
										displayName:"complaintId",
										visible: false,
									}
									]
								};
//								 $scope.$watch('gridOptions.selectedItems', function(oldVal , newVal) {
//								     console.log("________")
//								    		 console.log(newVal);
//								    });
								$scope.assignComplaint =function(){
									//var selected = $filter('filter')($scope.complaints,{complaintId:$scope.gridOptions.selectedItems[0].complaintId});
									if($scope.gridOptions.selectedItems[0].Status == "Pending"){
										$scope.selectedComplaintId = $scope.gridOptions.selectedItems[0].complaintId;
										var dataToSend ={
												complaintId:$scope.selectedComplaintId
										}
										serviceApi.doPostWithData('/RLMS/complaint/getAllTechniciansToAssignComplaint',dataToSend)
										.then(function(data) {
											$scope.technicians = data;
										})
										$scope.modalInstance = $modal.open({
									        templateUrl: 'assignComplaintTemplate',
									        scope:$scope
									     })
									}else{
										alert("Already Assigned Complaint");
									}
										
							}
				
									
								$scope.submitAssign = function() {
									var dataToSend ={
											complaintId:$scope.selectedComplaintId,
											userRoleId:$scope.selectedTechnician.selected.userRoleId
									}
									serviceApi.doPostWithData('/RLMS/complaint/assignComplaint',dataToSend)
									.then(function(response) {
										$scope.showAlert = true;
										var key = Object.keys(response);
										var successMessage = response.response;
										$scope.alert.msg = successMessage;
										$scope.alert.type = "success";
										$scope.loadComplaintsList();
									})
									setTimeout(function(){ $scope.modalInstance.dismiss(); }, 1000);
									
						            
						          };
						          $scope.cancelAssign = function(){
						        	  $scope.modalInstance.dismiss('cancel');
						          }
							}]);
	
})();
