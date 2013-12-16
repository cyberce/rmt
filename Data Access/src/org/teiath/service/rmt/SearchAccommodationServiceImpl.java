package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("searchAccommodationService")
@Transactional
public class SearchAccommodationServiceImpl
		implements SearchAccommodationService {

	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;

	@Override
	public SearchResults<Accommodation> searchAccommodation(AccommodationSearchCriteria criteria)
			throws ServiceException {
		SearchResults<Accommodation> results;

		try {
			results = accommodationDAO.search(criteria);
			for (Accommodation accommodation : results.getData()) {
				accommodation.getAttributes().iterator();
				accommodation.getUser().getRoles().iterator();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return results;
	}

	@Override
	public Collection<AccommodationType> getAccommodationType()
			throws ServiceException {
		Collection<AccommodationType> accommodations;

		try {
			accommodations = accommodationTypeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodations;
	}
}
