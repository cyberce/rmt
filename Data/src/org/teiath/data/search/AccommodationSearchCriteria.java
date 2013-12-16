package org.teiath.data.search;

import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;

import java.math.BigDecimal;
import java.util.Date;

public class AccommodationSearchCriteria
		extends SearchCriteria {

	private AccommodationAttribute accommodationAttribute;
	private AccommodationType accommodationType;
	private String accommodationTypeName;
	private Date constructionDate;
	private BigDecimal maxAmount;
	private String heatingType;
	private Integer floor;
	private int squareMeters;
	private int numberOfBedrooms;
	private Integer smokingAllowed;
	private Integer petsAllowed;
	private String listingKeyword;
	private String comment;
	private String address;
	private boolean active;

	public AccommodationSearchCriteria() {

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getConstructionDate() {
		return constructionDate;
	}

	public void setConstructionDate(Date constructionDate) {
		this.constructionDate = constructionDate;
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

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public int getSquareMeters() {
		return squareMeters;
	}

	public void setSquareMeters(int squareMeters) {
		this.squareMeters = squareMeters;
	}

	public int getNumberOfBedrooms() {
		return numberOfBedrooms;
	}

	public void setNumberOfBedrooms(int numberOfBedrooms) {
		this.numberOfBedrooms = numberOfBedrooms;
	}

	public Integer getSmokingAllowed() {
		return smokingAllowed;
	}

	public void setSmokingAllowed(Integer smokingAllowed) {
		this.smokingAllowed = smokingAllowed;
	}

	public Integer getPetsAllowed() {
		return petsAllowed;
	}

	public void setPetsAllowed(Integer petsAllowed) {
		this.petsAllowed = petsAllowed;
	}

	public String getListingKeyword() {
		return listingKeyword;
	}

	public void setListingKeyword(String listingKeyword) {
		this.listingKeyword = listingKeyword;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public AccommodationType getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(AccommodationType accommodationType) {
		this.accommodationType = accommodationType;
	}

	public AccommodationAttribute getAccommodationAttribute() {
		return accommodationAttribute;
	}

	public void setAccommodationAttribute(AccommodationAttribute accommodationAttribute) {
		this.accommodationAttribute = accommodationAttribute;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAccommodationTypeName() {
		return accommodationTypeName;
	}

	public void setAccommodationTypeName(String accommodationTypeName) {
		this.accommodationTypeName = accommodationTypeName;
	}
}
