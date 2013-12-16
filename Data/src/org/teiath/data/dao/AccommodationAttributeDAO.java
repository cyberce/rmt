package org.teiath.data.dao;

import org.teiath.data.domain.rmt.AccommodationAttribute;

import java.util.Collection;

public interface AccommodationAttributeDAO {

	public Collection<AccommodationAttribute> findAll();

	public void save(AccommodationAttribute accommodationAttribute);

	public AccommodationAttribute findById(Integer id);

	public void delete(AccommodationAttribute accommodationAttribute);
}
