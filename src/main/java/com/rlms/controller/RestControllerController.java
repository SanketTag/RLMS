package com.rlms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




























import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.LoginDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsUserRoles;
import com.rlms.service.ComplaintsService;
import com.rlms.service.CustomerService;
import com.rlms.service.LiftService;
import com.rlms.service.MessagingServiceImpl;
import com.rlms.service.UserService;
import com.rlms.utils.PropertyUtils;



@RestController
@RequestMapping(value="/API")
public class RestControllerController  extends BaseController {

	@Autowired
    protected AuthenticationManager authenticationManager;
	
	@Autowired
	private ComplaintsService ComplaintsService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LiftService liftService;
	
	private static final Logger log = Logger.getLogger(RestControllerController.class);
	   
    
    @RequestMapping("/loginIntoApp")
    public @ResponseBody LoginDtlsDto loginIntoApp(@RequestBody LoginDtlsDto loginDtlsDto, HttpServletRequest request, HttpServletResponse response) {
    
    	 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDtlsDto.getUserName(), loginDtlsDto.getPassword());

    	 LoginDtlsDto dto = new LoginDtlsDto();
    	 UserMetaInfo userMetaInfo = null;
         // generate session if one doesn't exist
         request.getSession();

         token.setDetails(new WebAuthenticationDetails(request));
         try{
        	    Authentication auth = authenticationManager.authenticate(token);

        	    SecurityContextHolder.getContext().setAuthentication(auth);
        	    
        	    SecurityContext context = new SecurityContextImpl();
        	    context.setAuthentication(auth);
        	    
        	    
        	    userMetaInfo =  this.getMetaInfo();
        	    dto.setCompanyId(userMetaInfo.getUserRole().getRlmsCompanyMaster().getCompanyId());
        	    dto.setUserId(userMetaInfo.getUserId());
        	    dto.setUserRoleId(userMetaInfo.getUserRole().getUserRoleId());
        	} catch(Exception e){
        	        e.printStackTrace();
        	}
         
         return dto;
        
    }
    
    @RequestMapping("/isLoggedIn")
    public @ResponseBody String isUserAlreadyLoggedIn()
    {
    	 try{
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
	    	if (!(auth instanceof AnonymousAuthenticationToken)) {
	    	    /* The user is logged in :) */
	    	    return "1";
	    	}else{
	    		return "0";
	    	}
      }catch(Exception e)
    	 {
    	    return "0";
    	 }
    }   
    
    @RequestMapping(value = "/getAllComplaintsAssigned", method = RequestMethod.POST, produces="application/json")
    public @ResponseBody ResponseDto getAllComplaintsAssigned(@RequestBody LoginDtlsDto loginDtlsDto) {
    //17
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
    	
    	List<ComplaintsDto> listOfAllAssignedComplaints = null;
    	 List<Integer> statusList = new ArrayList<Integer>();
    	 statusList.add(Status.ASSIGNED.getStatusId());
    	 statusList.add(Status.INPROGESS.getStatusId());
    	 statusList.add(Status.COMPLETED.getStatusId());
    	 
    	 try {
    		 listOfAllAssignedComplaints =  this.ComplaintsService.getAllComplaintsAssigned(Integer.valueOf(loginDtlsDto.getUserRoleId()), statusList);
    		 if(null != listOfAllAssignedComplaints && !listOfAllAssignedComplaints.isEmpty()){
	    		 dto.setStatus(true);    
	    		
	    		 dto.setResponse(mapper.writeValueAsString(listOfAllAssignedComplaints));
    		 }else{
    			 dto.setStatus(false);
    			 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.NO_COMPLAINT_ASSIGNED.getMessage()));
    		 }
    	 }catch(Exception e){
    		 dto.setStatus(false);
    		 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
    		 log.error("some Unknown exception occurs.");
    	 }
    	 
    	 return dto;
        
    }
    
    @RequestMapping(value = "/updateComplaintStatus", method = RequestMethod.POST)
    public @ResponseBody ResponseDto updateComplaintStatus(@RequestBody ComplaintsDto complaintsDto) {
    
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto dto = new ResponseDto();
    	
    	 try {
    		 
    		 dto.setResponse(mapper.writeValueAsString(this.ComplaintsService.updateComplaintStatus(complaintsDto)));
    		 dto.setStatus(true);
    	 }catch(Exception e){
    		 dto.setStatus(false);
    		 log.error("some Unknown exception occurs.");
    		 dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
    	 }
    	 
    	 return dto;
        
    }
    	
    @RequestMapping(value = "/complaint/validateAndRegisterNewComplaint", method = RequestMethod.POST)
    public @ResponseBody ResponseDto validateAndRegisterNewComplaint(@RequestBody ComplaintsDtlsDto dto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method :: validateAndRegisterNewComplaint");
        	reponseDto.setResponse(this.ComplaintsService.validateAndRegisterNewComplaint(dto, this.getMetaInfo()));
        	reponseDto.setStatus(true);
        }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        	
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	
        }
 
        return reponseDto;
    }
    
    @RequestMapping(value = "/register/registerMemeberDeviceByMblNo", method = RequestMethod.POST)
    public @ResponseBody ResponseDto registerMemeberDeviceByMblNo(@RequestBody MemberDtlsDto memberDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: registerMemeberDeviceByMblNo");
        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	MemberDtlsDto dto = this.customerService.registerMemeberDeviceByMblNo(memberDtlsDto, metaInfo);
        	Gson gSon = new Gson();
        	reponseDto.setJsonElement(gSon.toJsonTree(dto, new TypeToken<MemberDtlsDto>() {}.getType()));
        	
        	reponseDto.setStatus(true);
        	
        }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        	
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
 
        return reponseDto;
    }
    
    @RequestMapping(value = "/register/registerTechnicianDeviceByMblNo", method = RequestMethod.POST)
    public @ResponseBody ResponseDto registerTechnicianDeviceByMblNo(@RequestBody UserDtlsDto userDtlsDto) throws ValidationException, RunTimeException{
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: registerTechnicianDeviceByMblNo");
        	RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(1);
        	UserMetaInfo metaInfo = new UserMetaInfo();
        	metaInfo.setUserId(userRoles.getRlmsUserMaster().getUserId());
        	metaInfo.setUserName(userRoles.getRlmsUserMaster().getFirstName());
        	metaInfo.setUserRole(userRoles);
        	reponseDto.setResponse(mapper.writeValueAsString(this.userService.registerTechnicianDeviceByMblNo(userDtlsDto, metaInfo)));
        	reponseDto.setStatus(true);
        	
        }catch(ValidationException vex){
        	log.error(ExceptionUtils.getFullStackTrace(vex));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(vex.getExceptionMessage());
        }
        catch(Exception e){
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
 
        return reponseDto;
    }
    
    @RequestMapping(value = "/lift/getAllLiftsForMember", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllLiftsForMember(@RequestBody MemberDtlsDto memberDtlsDto){
    	List<LiftDtlsDto> listOfLiftdtls = null;
    	ObjectMapper mapper = new ObjectMapper();
    	ResponseDto reponseDto = new ResponseDto();
         try{
         	log.info("Method :: getAllLiftsForMember");
         	 listOfLiftdtls =  this.customerService.getAllLiftsForMember(memberDtlsDto.getMemberId());
         	reponseDto.setResponse(mapper.writeValueAsString(listOfLiftdtls));
         	reponseDto.setStatus(true);
         }
         catch(Exception e){
         	log.error(ExceptionUtils.getFullStackTrace(e));
         	reponseDto.setStatus(false);
         	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
         	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
         }
         
         return reponseDto;
    }
    
    @RequestMapping(value = "/complaints/getAllComplaintsByMember", method = RequestMethod.POST)
    public @ResponseBody ResponseDto getAllComplaintsByMember(@RequestBody MemberDtlsDto memberDtlsDto){
    	List<ComplaintsDto> listOfAllComplaints = null;
    	ResponseDto reponseDto = new ResponseDto();
    	ObjectMapper mapper = new ObjectMapper();
        try{
        	log.info("Method :: getAllComplaintsByMembers");
        	listOfAllComplaints =  this.ComplaintsService.getAllComplaintsByMember(memberDtlsDto.getMemberId());
        	reponseDto.setResponse(mapper.writeValueAsString(listOfAllComplaints));
        	reponseDto.setStatus(true);
        }
        catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
        
        return reponseDto;
    }
    
    @RequestMapping(value = "/lift/uploadPhoto", method = RequestMethod.POST)
    public @ResponseBody ResponseDto uploadPhoto(@RequestBody LiftDtlsDto dto){
    	ResponseDto reponseDto = new ResponseDto();
        try{
        	log.info("Method :: uploadPhoto");
        	reponseDto.setResponse(this.liftService.uploadPhoto(dto));        	
       
        }catch(Exception e){
        	log.error(ExceptionUtils.getFullStackTrace(e));
        	reponseDto.setStatus(false);
        	reponseDto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        
        }
 
        return reponseDto;
    }
    
}