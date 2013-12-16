package org.teiath.data.dao;

import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;

import java.util.Collection;

public interface AccommodationDAO {

	public void save(Accommodation accommodation);

	public Accommodation findById(Integer id);

	public SearchResults<Accommodation> search(AccommodationSearchCriteria criteria);

	public Accommodation findByUser(User user);

	/*public Collection<Accommodation> findAccommodationByUser(User user);   */

	public Collection<Accommodation> findAccommodationsByAccommodationType(int accommodationTypeId);

	public Collection<Accommodation> findAccommodationsByNumberOfBedrooms(Integer numberFrom, Integer numberTo);
}
