package org.teiath.data.domain.rmt;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.hibernate.annotations.Type;
import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;

@Entity
@Table(name = "rmt_accommodations")
public class Accommodation
		implements Serializable {

	public final static boolean ACTIVE_A = true;
	public final static boolean ACTIVE_APPROVED = true;
	public final static boolean ACTIVE_DISABLE = false;

	@Id
	@Column(name = "accommodation_id")
	@SequenceGenerator(name = "accommodations_seq", sequenceName = "rmt_accommodations_accommodation_id_seq",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accommodations_seq")
	private Integer id;

	@Column(name = "accommodation_address", length = 2000, nullable = false)
	private String address;
	@Column(name = "accommodation_floor", nullable = false)
	private int floor;
	@Column(name = "accommodation_square_meters", nullable = false)
	private int squareMeters;
	@Column(name = "accommodation_number_of_bedrooms", nullable = false)
	private int numberOfBedrooms;
	@Column(name = "accommodation_heating_type", length = 2000, nullable = false)
	private String heatingType;
	@Column(name = "accommodation_construction_year", nullable = false)
	private Integer constructionYear;
	@Column(name = "accommodation_asked_amount", precision = 6, scale = 2, nullable = true)
	private BigDecimal askedAmount;
	@Column(name = "accommodation_smoking_allowed", nullable = false)
	private boolean smokingAllowed;
	@Column(name = "accommodation_pets_allowed", nullable = false)
	private boolean petsAllowed;
	@Column(name = "accommodation_existing_kitchen", nullable = false)
	private boolean existingKitchen;
	@Column(name = "accommodation_comment", length = 4000, nullable = true)
	private String comment;
	@Column(name = "accommodation_photos", nullable = true)
	private File[] photos;
	@Column(name = "accommodation_active", nullable = false)
	private boolean active;
	@Column(name = "accommmodation_publish_date", nullable = true)
	private Date accommodationPublishDate;
	@Column(name = "map_location", columnDefinition = "Geometry", nullable = false)
	@Type(type = "org.hibernate.spatial.GeometryType")
	private Point location;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "accommodation")
	private Set<ApplicationImage> accommodationImages;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accommodation_type_id", nullable = false)
	private AccommodationType accommodationType;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "ACCOMMODATION_ACCOMODATION_ATTRIBUTES", joinColumns = {@JoinColumn(name = "accommodation_id")},
			inverseJoinColumns = {@JoinColumn(name = "accommodation_attribute_id")})
	private Collection<AccommodationAttribute> attributes;

	public Accommodation() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public File[] getPhotos() {
		return photos;
	}

	public void setPhotos(File[] photos) {
		this.photos = photos;
	}

	public AccommodationType getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(AccommodationType accommodationType) {
		this.accommodationType = accommodationType;
	}

	public Collection<AccommodationAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<AccommodationAttribute> attributes) {
		this.attributes = attributes;
	}

	public Set<ApplicationImage> getAccommodationImages() {
		return accommodationImages;
	}

	public void setAccommodationImages(Set<ApplicationImage> accommodationImages) {
		this.accommodationImages = accommodationImages;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Date getAccommodationPublishDate() {
		return accommodationPublishDate;
	}

	public void setAccommodationPublishDate(Date accommodationPublishDate) {
		this.accommodationPublishDate = accommodationPublishDate;
	}

	public void materializeLocation(String coords)
			throws ParseException {
		StringTokenizer st = new StringTokenizer(coords, "),(");
		if (st.hasMoreTokens()) {
			String lat = st.nextToken().trim();
			String lng = st.nextToken().trim();
			WKTReader fromText = new WKTReader();
			Geometry geom = fromText.read("POINT(" + lat + " " + lng + ")");
			geom.setSRID(4326);

			this.location = (Point) geom;
		}
	}

	public boolean isExistingKitchen() {
		return existingKitchen;
	}

	public void setExistingKitchen(boolean existingKitchen) {
		this.existingKitchen = existingKitchen;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && this.id != null && obj.getClass() == Accommodation.class && this.id
				.equals(((Accommodation) obj).getId());
	}
}
