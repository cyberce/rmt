package org.teiath.data.domain.rmt;

import javax.persistence.*;

@Entity
@Table(name = "rmt_accommodation_attributes")
public class AccommodationAttribute {

	@Id
	@Column(name = "accommodation_attribute_id")
	@SequenceGenerator(name = "accommodation_attributes_seq",
			sequenceName = "rmt_accommodation_attributes_accommodation_attribute_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accommodation_attributes_seq")
	private Integer id;

	@Column(name = "accommodation_attribute_code", length = 2000, nullable = false)
	private String code;
	@Column(name = "accommodation_attribute_name", length = 2000, nullable = false)
	private String name;

	public AccommodationAttribute() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && this.id != null && obj.getClass() == AccommodationAttribute.class && this.id
				.equals(((AccommodationAttribute) obj).getId());
	}
}
