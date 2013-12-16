package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

@Service("createAccommodationTypeService")
@Transactional
public class CreateAccommodationTypeServiceImpl
		implements CreateAccommodationTypeService {

	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAccommodationType(AccommodationType accommodationType)
			throws ServiceException {
		try {
			accommodationTypeDAO.save(accommodationType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}
}
