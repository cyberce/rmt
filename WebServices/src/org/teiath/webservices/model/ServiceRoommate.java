package org.teiath.webservices.model;

import org.codehaus.jackson.map.annotate.JsonRootName;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@XmlRootElement(name="roommate")
@JsonRootName(value = "roommate")
public class ServiceRoommate {

	private String address;
	private int floor;
	private int squareMeters;
	private int numberOfBedrooms;
	private String heatingType;
	private Integer constructionYear;
	private BigDecimal askedAmount;
	private boolean smokingAllowed;
	private boolean petsAllowed;
	private boolean existingKitchen;
	private String comment;
	private boolean active;
	private String roommateName;
	private String accommodationTypeName;
	private Date accommodationCreationDate;

	public ServiceRoommate() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
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

	public String getHeatingType() {
		return heatingType;
	}

	public void setHeatingType(String heatingType) {
		this.heatingType = heatingType;
	}

	public Integer getConstructionYear() {
		return constructionYear;
	}

	public void setConstructionYear(Integer constructionYear) {
		this.constructionYear = constructionYear;
	}

	public BigDecimal getAskedAmount() {
		return askedAmount;
	}

	public void setAskedAmount(BigDecimal askedAmount) {
		this.askedAmount = askedAmount;
	}

	public boolean isSmokingAllowed() {
		return smokingAllowed;
	}

	public void setSmokingAllowed(boolean smokingAllowed) {
		this.smokingAllowed = smokingAllowed;
	}

	public boolean isPetsAllowed() {
		return petsAllowed;
	}

	public void setPetsAllowed(boolean petsAllowed) {
		this.petsAllowed = petsAllowed;
	}

	public boolean isExistingKitchen() {
		return existingKitchen;
	}

	public void setExistingKitchen(boolean existingKitchen) {
		this.existingKitchen = existingKitchen;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getRoommateName() {
		return roommateName;
	}

	public void setRoommateName(String roommateName) {
		this.roommateName = roommateName;
	}

	public Date getAccommodationCreationDate() {
		return accommodationCreationDate;
	}

	public void setAccommodationCreationDate(Date accommodationCreationDate) {
		this.accommodationCreationDate = accommodationCreationDate;
	}
}
