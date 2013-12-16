package org.teiath.data.dao;

import org.teiath.data.domain.rmt.AccommodationType;

import java.util.Collection;

public interface AccommodationTypeDAO {

	public Collection<AccommodationType> findAll();

	public void save(AccommodationType accommodationType);

	public AccommodationType findById(Integer id);

	public void delete(AccommodationType accommodationType);
}
