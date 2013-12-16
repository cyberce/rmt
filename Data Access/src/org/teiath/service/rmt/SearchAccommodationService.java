package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface SearchAccommodationService {

	public SearchResults<Accommodation> searchAccommodation(AccommodationSearchCriteria criteria)
			throws ServiceException;

	public Collection<AccommodationType> getAccommodationType()
			throws ServiceException;
}
