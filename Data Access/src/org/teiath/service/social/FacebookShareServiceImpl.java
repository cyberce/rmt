package org.teiath.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

@Service("facebookShareService")
@Transactional
public class FacebookShareServiceImpl
		implements FacebookShareService {

	@Autowired
	AccommodationDAO accommodationDAO;

	@Override
	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException {
		Accommodation accommodation;
		try {
			accommodation = accommodationDAO.findById(accommodationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodation;
	}
}
