package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_event")
public class RlmsEventDtls implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer eventId;
	private String eventType;
	private String eventDescription;
	private Integer liftCustomerMapId;
	private Date generatedDate;
	private Integer generatedBy;
	private Date updatedDate;
	private Integer updatedBy;
	private Integer activeFlag;
	
	public RlmsEventDtls() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "event_id", unique = true, nullable = false)
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	@Column(name = "event_type", unique = true, nullable = false)
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Column(name = "event_description")
	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	@Column(name = "lift_customer_map_id")
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}

	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}

	@Column(name = "generated_date")
	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	@Column(name = "generated_by")
	public Integer getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(Integer generatedBy) {
		this.generatedBy = generatedBy;
	}

	@Column(name = "updated_date")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by")
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "active_flag")
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
