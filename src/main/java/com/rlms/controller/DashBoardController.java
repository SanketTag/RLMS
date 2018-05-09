package com.rlms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.EventDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsEventDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsSiteVisitDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.CompanyService;
import com.rlms.service.CustomerService;
import com.rlms.service.DashboardService;
import com.rlms.service.LiftService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController extends BaseController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CompanyService companyService;
	@Autowired
	private LiftDao liftDao;

	@Autowired
	ComplaintsDao complaintsDao;

	@Autowired
	private LiftService liftService;

	@Autowired
	private CustomerService customerService;

	private static final Logger logger = Logger
			.getLogger(ComplaintController.class);

	@RequestMapping(value = "/getAMCDetails", method = RequestMethod.POST)
	public @ResponseBody
	List<AMCDetailsDto> getAMCDetailsForDashboard(
			@RequestBody AMCDetailsDto amcDetailsDto) throws RunTimeException,
			ValidationException {

		List<AMCDetailsDto> listOFAmcDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchIds = new ArrayList<>();
		
		try {
			RlmsUserRoles userRole = this.getLoggedInUser();
			Integer branchId = null;
			if(null != userRole.getRlmsCompanyBranchMapDtls()){
				branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
			}
			
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService
					.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}

			List<CustomerDtlsDto> allCustomersForBranch = dashboardService
					.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dto = new LiftDtlsDto();
				dto.setBranchCustomerMapId(customerDtlsDto
						.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService
						.getAllLiftsForBranchsOrCustomer(dto);
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap
							.getLiftCustomerMapId());
				}
			}
			listOFAmcDtls = this.dashboardService.getAMCDetailsForDashboard(
					liftCustomerMapIds, amcDetailsDto);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}
		return listOFAmcDtls;
	}

	@RequestMapping(value = "/getListOfTechniciansForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<UserRoleDtlsDTO> getListOfTechnicians(
			@RequestBody ComplaintsDtlsDto dto) throws RunTimeException {
		List<UserRoleDtlsDTO> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchMapIds = new ArrayList<>();
		List<Integer> branchCustomerMapIds = new ArrayList<>();
		
		RlmsUserRoles userRole = this.getLoggedInUser();
		Integer branchId = null;
		if(null != userRole.getRlmsCompanyBranchMapDtls()){
			branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
		}
		
		logger.info("Method :: getAllBranchesForCompany");
		listOfAllBranches = this.companyService
				.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}

		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService
					.getListOfTechnicians(companyBranchMapIds);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}

		return listOfComplaints;
	}

	@RequestMapping(value = "/getListOfComplaintsForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<ComplaintsDto> getListOfComplaints(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchMapIds = new ArrayList<>();
		List<Integer> branchCustomerMapIds = new ArrayList<>();
		RlmsUserRoles userRole = this.getLoggedInUser();
		Integer branchId = null;
		if(null != userRole.getRlmsCompanyBranchMapDtls()){
			branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
		}
		
		logger.info("Method :: getAllBranchesForCompany");
		listOfAllBranches = this.companyService
				.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
		
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}

		List<CustomerDtlsDto> allCustomersForBranch = dashboardService
				.getAllCustomersForBranch(companyBranchMapIds);

		List<Integer> liftCustomerMapIds = new ArrayList<>();
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto
					.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService
					.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap
						.getLiftCustomerMapId());
			}
		}

		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}

		return listOfComplaints;
	}

	@RequestMapping(value = "/getListOfComplaintsForSiteVisited", method = RequestMethod.POST)
	public @ResponseBody
	List<ComplaintsDto> getListOfComplaintsForSiteVisited(
			@RequestBody ComplaintsDtlsDto dto) throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
		Set<Integer> siteVisitedTodayComplaintIds = new HashSet<>();

		List<Integer> companyBranchMapIds = new ArrayList<>();
		List<Integer> branchCustomerMapIds = new ArrayList<>();
		RlmsUserRoles userRole = this.getLoggedInUser();
		Integer branchId = null;
		if(null != userRole.getRlmsCompanyBranchMapDtls()){
			branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
		}
		
		logger.info("Method :: getAllBranchesForCompany");
		listOfAllBranches = this.companyService
				.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}

		List<CustomerDtlsDto> allCustomersForBranch = dashboardService
				.getAllCustomersForBranch(companyBranchMapIds);

		List<Integer> liftCustomerMapIds = new ArrayList<>();
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto
					.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService
					.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap
						.getLiftCustomerMapId());
			}
		}

		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);
			List<Integer> listOfComplaintIds = new ArrayList<>();
			for (ComplaintsDto complaintsDto : listOfComplaints) {
				listOfComplaintIds.add(complaintsDto.getComplaintId());
			}
			Date todayDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			for (Integer complaintId : listOfComplaintIds) {
				RlmsComplaintTechMapDtls rlmsTechMapId = dashboardService
						.getComplTechMapObjByComplaintId(complaintId);
				if (rlmsTechMapId != null) {
					List<RlmsSiteVisitDtls> allVisits = dashboardService
							.getAllVisitsForComnplaints(rlmsTechMapId
									.getComplaintTechMapId());
					for (RlmsSiteVisitDtls rlmsSiteVisitDtls : allVisits) {
						if (sdf.format(rlmsSiteVisitDtls.getCreatedDate())
								.equals(sdf.format(todayDate))) {
							siteVisitedTodayComplaintIds.add(complaintId);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}
		List<ComplaintsDto> finalListOfComplaints = new ArrayList<>();
		for (ComplaintsDto complaintsDto : listOfComplaints) {
			if (siteVisitedTodayComplaintIds.contains(complaintsDto
					.getComplaintId())) {
				finalListOfComplaints.add(complaintsDto);
			}
		}
		return finalListOfComplaints;
	}

	@RequestMapping(value = "/getAllAMCDetails", method = RequestMethod.POST)
	public @ResponseBody
	List<AMCDetailsDto> getAMCForDashboard(
			@RequestBody AMCDetailsDto amcDetailsDto) throws RunTimeException,
			ValidationException {

		List<AMCDetailsDto> listOFAmcDtls = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchIds = new ArrayList<>();

		try {
			logger.info("Method :: getAllBranchesForCompany");
			RlmsUserRoles userRole = this.getLoggedInUser();
			Integer branchId = null;
			if(null != userRole.getRlmsCompanyBranchMapDtls()){
				branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
			}
			
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService
					.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}

			List<CustomerDtlsDto> allCustomersForBranch = dashboardService
					.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dto = new LiftDtlsDto();
				dto.setBranchCustomerMapId(customerDtlsDto
						.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService
						.getAllLiftsForBranchsOrCustomer(dto);
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap
							.getLiftCustomerMapId());
				}
			}
			listOFAmcDtls = this.dashboardService.getAllAMCDetails(
					liftCustomerMapIds, amcDetailsDto);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}
		return listOFAmcDtls;
	}

	@RequestMapping(value = "/getLiftStatus", method = RequestMethod.POST)
	public @ResponseBody
	List<LiftDtlsDto> getLiftStatus(@RequestBody LiftDtlsDto liftDtlsDto)
			throws RunTimeException, ValidationException {

		List<LiftDtlsDto> listOfLifts = new ArrayList<LiftDtlsDto>();
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchIds = new ArrayList<>();

		try {
			logger.info("Method :: getAllBranchesForCompany");
			RlmsUserRoles userRole = this.getLoggedInUser();
			Integer branchId = null;
			if(null != userRole.getRlmsCompanyBranchMapDtls()){
				branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
			}
			
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService
					.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			listOfLifts = liftService.getLiftStatusForBranch(companyBranchIds,
					this.getMetaInfo());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}
		return listOfLifts;
	}

	@RequestMapping(value = "/getListOfCustomerForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<CustomerDtlsDto> getListOfCustomerDtls(
			@RequestBody CustomerDtlsDto customerDtlsDto)
			throws RunTimeException {
		List<CustomerDtlsDto> listOfCustomers = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchIds = new ArrayList<>();

		try {
			logger.info("Method :: getAllBranchesForCompany");
			RlmsUserRoles userRole = this.getLoggedInUser();
			Integer branchId = null;
			if(null != userRole.getRlmsCompanyBranchMapDtls()){
				branchId = userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchId();
			}
			
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService
					.getAllApplicableBranches(userRole.getRlmsCompanyMaster().getCompanyId(), branchId);
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}
			listOfCustomers = this.customerService
					.getAllApplicableCustomersForDashboard(companyBranchIds,
							this.getMetaInfo());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));

		}

		return listOfCustomers;
	}

	@RequestMapping(value = "/getAllCompanyDetailsForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<CompanyDtlsDTO> getAllCompanyDetailsForDashboard()
			throws RunTimeException {
		List<CompanyDtlsDTO> listOfApplicableCompaniesDetails = null;

		try {
			logger.info("Method :: getAllCompanyDetails");
			listOfApplicableCompaniesDetails = this.companyService
					.getAllCompanyDetailsForDashboard(this.getMetaInfo());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}

		return listOfApplicableCompaniesDetails;
	}

	@RequestMapping(value = "/getAllBranchesForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(
			@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.dashboardService
					.getAllBranchesForDashBoard(companyDtlsDTO.getCompanyId());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}

		return listOfAllBranches;
	}

	@RequestMapping(value = "/getListOfBranchDtlsForDashboard", method = RequestMethod.POST)
	public @ResponseBody
	List<BranchDtlsDto> getListOfBranchDtls(@RequestBody BranchDtlsDto dto)
			throws RunTimeException {
		List<BranchDtlsDto> listOfBranches = null;

		try {
			logger.info("Method :: getListOfBranchDtls");
			listOfBranches = this.dashboardService
					.getListOfBranchDtlsForDashboard(dto.getCompanyId(),
							this.getMetaInfo());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));

		}

		return listOfBranches;
	}

	@RequestMapping(value = "/getAllInOutEventsData", method = RequestMethod.POST)
	public @ResponseBody
	List<EventDtlsDto> getAllInOutEventsData(@RequestBody EventDtlsDto dto)
			throws RunTimeException {
		List<EventDtlsDto> listOfEvents = null;

		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchIds = new ArrayList<>();
		try {
			logger.info("Method :: getAllBranchesForCompany");
			listOfAllBranches = this.companyService
					.getAllBranches(dto.getCompanyId());
			for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
				companyBranchIds.add(companyBranchMap.getCompanyBranchMapId());
			}

			List<CustomerDtlsDto> allCustomersForBranch = dashboardService
					.getAllCustomersForBranch(companyBranchIds);
			List<Integer> liftCustomerMapIds = new ArrayList<>();
			for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
				LiftDtlsDto dtoTemp = new LiftDtlsDto();
				dtoTemp.setBranchCustomerMapId(customerDtlsDto
						.getBranchCustomerMapId());
				List<RlmsLiftCustomerMap> list = dashboardService
						.getAllLiftsForBranchsOrCustomer(dtoTemp);
				for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
					liftCustomerMapIds.add(rlmsLiftCustomerMap
							.getLiftCustomerMapId());
				}
			}
			logger.info("Method :: getAllBranchesForCompany");
			listOfEvents = this.dashboardService.getListOfEvetnDetails(
					liftCustomerMapIds, this.getMetaInfo());

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));

		}

		return listOfEvents;
	}
	
	@RequestMapping(value = "/getListOfAmcServiceCalls", method = RequestMethod.POST)
	public @ResponseBody
	List<ComplaintsDto> getListOfAmcServiceCalls(@RequestBody ComplaintsDtlsDto dto)
			throws RunTimeException {
		List<ComplaintsDto> listOfComplaints = null;
		List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;

		List<Integer> companyBranchMapIds = new ArrayList<>();
		List<Integer> branchCustomerMapIds = new ArrayList<>();
		listOfAllBranches = this.companyService.getAllBranches(dto
				.getCompanyId());
		for (RlmsCompanyBranchMapDtls companyBranchMap : listOfAllBranches) {
			companyBranchMapIds.add(companyBranchMap.getCompanyBranchMapId());
		}

		List<CustomerDtlsDto> allCustomersForBranch = dashboardService
				.getAllCustomersForBranch(companyBranchMapIds);

		List<Integer> liftCustomerMapIds = new ArrayList<>();
		for (CustomerDtlsDto customerDtlsDto : allCustomersForBranch) {
			LiftDtlsDto dtoToGetLifts = new LiftDtlsDto();
			dtoToGetLifts.setBranchCustomerMapId(customerDtlsDto
					.getBranchCustomerMapId());
			List<RlmsLiftCustomerMap> list = dashboardService
					.getAllLiftsForBranchsOrCustomer(dtoToGetLifts);
			for (RlmsLiftCustomerMap rlmsLiftCustomerMap : list) {
				liftCustomerMapIds.add(rlmsLiftCustomerMap
						.getLiftCustomerMapId());
			}
		}

		dto.setListOfLiftCustoMapId(liftCustomerMapIds);
		try {
			logger.info("Method :: getListOfComplaints");
			listOfComplaints = this.dashboardService.getListOfComplaintsBy(dto);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new RunTimeException(
					ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(),
					PropertyUtils
							.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS
									.getMessage()));
		}

		return listOfComplaints;
	}
}
