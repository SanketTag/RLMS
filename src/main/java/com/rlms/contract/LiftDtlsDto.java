package com.rlms.contract;

import java.util.Date;

public class LiftDtlsDto {

	private String liftNumber;
	private String address;
	private String customerName;
	private String branchName;
	
	private Integer companyId;
	private Integer branchCompanyMapId;
	private Integer branchCustomerMapId;
	
	private Integer liftId;
	private String latitude;
	private String longitude;
	private Date serviceStartDate;
	private String serviceStartDateStr;
	private Date serviceEndDate;
	private String serviceEndDateStr;
	private Date dateOfInstallation;
	private String dateOfInstallationStr;
	private Date amcStartDate;
	private String amcStartDateStr;
	private Integer amcType;
	private String amcAmount;
	private Integer doorType;
	private Integer noOfStops;
	private Integer engineType; 
	private String machineMake;
	private String machineCapacity;
	private String machineCurrent;
	private byte[] machinePhoto;
	private String breakVoltage;
	private String panelMake;
	private byte[] panelPhoto;
	private String ard;
	private byte[] ARDPhoto;
	private Integer NoOfBatteries;
	private String BatteryCapacity;
	private String BatteryMake;
	private String COPMake;
	private byte[] COPPhoto;
	private String  LOPMake;
	private byte[] LOPPhoto;
	private Integer collectiveType;
	private Integer SimplexDuplex;
	private byte[] cartopPhoto;
	private String autoDoorMake;
	private byte[] autoDoorHeaderPhoto;
	private Integer wiringShceme;
	private byte[] wiringPhoto;
	private Integer fireMode;
	private String intercomm;
	private String alarm;
	private String alarmBattery;
	private String accessControl;
	private byte[] lobbyPhoto;
	private Integer fyaTranId;
	private Integer liftCustomerMapId;
	
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	public Date getServiceEndDate() {
		return serviceEndDate;
	}
	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	public Date getDateOfInstallation() {
		return dateOfInstallation;
	}
	public void setDateOfInstallation(Date dateOfInstallation) {
		this.dateOfInstallation = dateOfInstallation;
	}
	public Date getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(Date amcStartDate) {
		this.amcStartDate = amcStartDate;
	}
	public Integer getAmcType() {
		return amcType;
	}
	public void setAmcType(Integer amcType) {
		this.amcType = amcType;
	}
	public String getAmcAmount() {
		return amcAmount;
	}
	public void setAmcAmount(String amcAmount) {
		this.amcAmount = amcAmount;
	}
	public Integer getDoorType() {
		return doorType;
	}
	public void setDoorType(Integer doorType) {
		this.doorType = doorType;
	}
	public Integer getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(Integer noOfStops) {
		this.noOfStops = noOfStops;
	}
	public Integer getEngineType() {
		return engineType;
	}
	public void setEngineType(Integer engineType) {
		this.engineType = engineType;
	}
	public String getMachineMake() {
		return machineMake;
	}
	public void setMachineMake(String machineMake) {
		this.machineMake = machineMake;
	}
	public String getMachineCapacity() {
		return machineCapacity;
	}
	public void setMachineCapacity(String machineCapacity) {
		this.machineCapacity = machineCapacity;
	}
	public String getMachineCurrent() {
		return machineCurrent;
	}
	public void setMachineCurrent(String machineCurrent) {
		this.machineCurrent = machineCurrent;
	}
	public byte[] getMachinePhoto() {
		return machinePhoto;
	}
	public void setMachinePhoto(byte[] machinePhoto) {
		this.machinePhoto = machinePhoto;
	}
	public String getBreakVoltage() {
		return breakVoltage;
	}
	public void setBreakVoltage(String breakVoltage) {
		this.breakVoltage = breakVoltage;
	}
	public String getPanelMake() {
		return panelMake;
	}
	public void setPanelMake(String panelMake) {
		this.panelMake = panelMake;
	}
	public byte[] getPanelPhoto() {
		return panelPhoto;
	}
	public void setPanelPhoto(byte[] panelPhoto) {
		this.panelPhoto = panelPhoto;
	}
	
	public byte[] getARDPhoto() {
		return ARDPhoto;
	}
	public void setARDPhoto(byte[] aRDPhoto) {
		ARDPhoto = aRDPhoto;
	}
	public Integer getNoOfBatteries() {
		return NoOfBatteries;
	}
	public void setNoOfBatteries(Integer noOfBatteries) {
		NoOfBatteries = noOfBatteries;
	}
	public String getBatteryCapacity() {
		return BatteryCapacity;
	}
	public void setBatteryCapacity(String batteryCapacity) {
		BatteryCapacity = batteryCapacity;
	}
	public String getBatteryMake() {
		return BatteryMake;
	}
	public void setBatteryMake(String batteryMake) {
		BatteryMake = batteryMake;
	}
	public String getCOPMake() {
		return COPMake;
	}
	public void setCOPMake(String cOPMake) {
		COPMake = cOPMake;
	}
	public byte[] getCOPPhoto() {
		return COPPhoto;
	}
	public void setCOPPhoto(byte[] cOPPhoto) {
		COPPhoto = cOPPhoto;
	}
	public String getLOPMake() {
		return LOPMake;
	}
	public void setLOPMake(String lOPMake) {
		LOPMake = lOPMake;
	}
	public byte[] getLOPPhoto() {
		return LOPPhoto;
	}
	public void setLOPPhoto(byte[] lOPPhoto) {
		LOPPhoto = lOPPhoto;
	}
	public Integer getCollectiveType() {
		return collectiveType;
	}
	public void setCollectiveType(Integer collectiveType) {
		this.collectiveType = collectiveType;
	}
	public Integer getSimplexDuplex() {
		return SimplexDuplex;
	}
	public void setSimplexDuplex(Integer simplexDuplex) {
		SimplexDuplex = simplexDuplex;
	}
	public byte[] getCartopPhoto() {
		return cartopPhoto;
	}
	public void setCartopPhoto(byte[] cartopPhoto) {
		this.cartopPhoto = cartopPhoto;
	}
	public String getAutoDoorMake() {
		return autoDoorMake;
	}
	public void setAutoDoorMake(String autoDoorMake) {
		this.autoDoorMake = autoDoorMake;
	}
	public byte[] getAutoDoorHeaderPhoto() {
		return autoDoorHeaderPhoto;
	}
	public void setAutoDoorHeaderPhoto(byte[] autoDoorHeaderPhoto) {
		this.autoDoorHeaderPhoto = autoDoorHeaderPhoto;
	}
	public Integer getWiringShceme() {
		return wiringShceme;
	}
	public void setWiringShceme(Integer wiringShceme) {
		this.wiringShceme = wiringShceme;
	}
	public byte[] getWiringPhoto() {
		return wiringPhoto;
	}
	public void setWiringPhoto(byte[] wiringPhoto) {
		this.wiringPhoto = wiringPhoto;
	}
	public Integer getFireMode() {
		return fireMode;
	}
	public void setFireMode(Integer fireMode) {
		this.fireMode = fireMode;
	}
	public String getIntercomm() {
		return intercomm;
	}
	public void setIntercomm(String intercomm) {
		this.intercomm = intercomm;
	}
	public String getAlarm() {
		return alarm;
	}
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getAlarmBattery() {
		return alarmBattery;
	}
	public void setAlarmBattery(String alarmBattery) {
		this.alarmBattery = alarmBattery;
	}
	public String getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
	public byte[] getLobbyPhoto() {
		return lobbyPhoto;
	}
	public void setLobbyPhoto(byte[] lobbyPhoto) {
		this.lobbyPhoto = lobbyPhoto;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public Integer getBranchCustomerMapId() {
		return branchCustomerMapId;
	}
	public void setBranchCustomerMapId(Integer branchCustomerMapId) {
		this.branchCustomerMapId = branchCustomerMapId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getLiftId() {
		return liftId;
	}
	public void setLiftId(Integer liftId) {
		this.liftId = liftId;
	}
	public Integer getFyaTranId() {
		return fyaTranId;
	}
	public void setFyaTranId(Integer fyaTranId) {
		this.fyaTranId = fyaTranId;
	}
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public String getArd() {
		return ard;
	}
	public void setArd(String ard) {
		this.ard = ard;
	}
	public String getServiceStartDateStr() {
		return serviceStartDateStr;
	}
	public void setServiceStartDateStr(String serviceStartDateStr) {
		this.serviceStartDateStr = serviceStartDateStr;
	}
	public String getServiceEndDateStr() {
		return serviceEndDateStr;
	}
	public void setServiceEndDateStr(String serviceEndDateStr) {
		this.serviceEndDateStr = serviceEndDateStr;
	}
	public String getDateOfInstallationStr() {
		return dateOfInstallationStr;
	}
	public void setDateOfInstallationStr(String dateOfInstallationStr) {
		this.dateOfInstallationStr = dateOfInstallationStr;
	}
	public String getAmcStartDateStr() {
		return amcStartDateStr;
	}
	public void setAmcStartDateStr(String amcStartDateStr) {
		this.amcStartDateStr = amcStartDateStr;
	}
	
	 
}
