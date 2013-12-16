package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationNotificationCriteriaDAO;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("createAccommodationNotificationCriteriaService")
@Transactional
public class CreateAccommodationNotificationCriteriaServiceImpl
		implements CreateAccommodationNotificationCriteriaService {

	@Autowired
	AccommodationNotificationCriteriaDAO accommodationNotificationCriteriaDAO;
	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAccommodationNotificationCriteria(
			AccommodationNotificationCriteria accommodationNotificationCriteria)
			throws ServiceException {
		try {
			accommodationNotificationCriteriaDAO.save(accommodationNotificationCriteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

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
