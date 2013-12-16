package org.teiath.data.domain.rmt;

import org.teiath.data.domain.NotificationCriteria;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rmt_accommodation_notifications_criteria")
@PrimaryKeyJoinColumn(name = "rmt_criteria_id")
public class AccommodationNotificationCriteria
		extends NotificationCriteria {

	@Column(name = "accommodation_notifications_criteria_floor", nullable = true)
	private Integer floor;
	@Column(name = "accommodation_notifications_criteria_maxAmount", precision = 6, scale = 2, nullable = true)
	private BigDecimal maxAmount;
	@Column(name = "accommodation_notifications_criteria_heatingType", nullable = true)
	private String heatingType;
	@Column(name = "accommodation_notifications_criteria_smokingAllowed", nullable = true)
	private Boolean smokingAllowed;
	@Column(name = "accommodation_notifications_criteria_petsAllowed", nullable = true)
	private Boolean petsAllowed;
	@Column(name = "accommodation_notifications_criteria_roommateGenre", nullable = true)
	private Integer roommateGenre;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accommodation_type_id", nullable = true)
	private AccommodationType accommodationType;

	public AccommodationNotificationCriteria() {

	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getHeatingType() {
		return heatingType;
	}

	public void setHeatingType(String heatingType) {
		this.heatingType = heatingType;
	}

	public Boolean getSmokingAllowed() {
		return smokingAllowed;
	}

	public void setSmokingAllowed(Boolean smokingAllowed) {
		this.smokingAllowed = smokingAllowed;
	}

	public Boolean getPetsAllowed() {
		return petsAllowed;
	}

	public void setPetsAllowed(Boolean petsAllowed) {
		this.petsAllowed = petsAllowed;
	}

	public Integer getRoommateGenre() {
		return roommateGenre;
	}

	public void setRoommateGenre(Integer roommateGenre) {
		this.roommateGenre = roommateGenre;
	}

	public AccommodationType getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(AccommodationType accommodationType) {
		this.accommodationType = accommodationType;
	}
}
