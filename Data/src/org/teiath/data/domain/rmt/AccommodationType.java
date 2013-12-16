package org.teiath.data.domain.rmt;

import javax.persistence.*;

@Entity
@Table(name = "rmt_accommodation_types")
public class AccommodationType {

	@Id
	@Column(name = "accommodation_type_id")
	@SequenceGenerator(name = "accommodation_types_seq",
			sequenceName = "rmt_accommodation_types_accommodation_type_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accommodation_types_seq")
	private Integer id;

	@Column(name = "accommodation_type_name", length = 200, nullable = false)
	private String name;
	@Column(name = "accommodation_type_description", length = 400, nullable = false)
	private String description;

	//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "accommodationType")
	//	private Set<Accommodation> accommodations;

	public AccommodationType() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && this.id != null && obj.getClass() == AccommodationType.class && this.id
				.equals(((AccommodationType) obj).getId());
	}
}
