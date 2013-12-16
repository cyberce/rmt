package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("accommodationsByAccommodationTypeDialogService")
@Transactional
public class AccommodationsByAccommodationTypeDialogServiceImpl
		implements AccommodationsByAccommodationTypeDialogService {

	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;

	@Override
	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException {
		Collection<AccommodationType> accommodationTypes;

		try {
			accommodationTypes = accommodationTypeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodationTypes;
	}
}
