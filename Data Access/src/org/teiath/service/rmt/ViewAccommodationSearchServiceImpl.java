package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.dao.ApplicationImageDAO;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("viewAccommodationSearchService")
@Transactional
public class ViewAccommodationSearchServiceImpl
		implements ViewAccommodationSearchService {

	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	ApplicationImageDAO imageDAO;

	@Override
	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException {
		Accommodation accommodation;

		try {
			accommodation = accommodationDAO.findById(accommodationId);
			accommodation.getAttributes().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodation;
	}

	@Override
	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException {
		Collection<ApplicationImage> images;

		try {
			images = imageDAO.findByAccommodation(accommodation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return images;
	}
}
