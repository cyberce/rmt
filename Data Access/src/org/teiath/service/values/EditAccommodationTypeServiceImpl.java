package org.teiath.service.values;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

@Service("editAccommodationTypeService")
@Transactional
public class EditAccommodationTypeServiceImpl
		implements EditAccommodationTypeService {

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

	@Override
	public AccommodationType getAccommodationTypeById(Integer accommodationTypeId)
			throws ServiceException {
		AccommodationType accommodationType;
		try {
			accommodationType = accommodationTypeDAO.findById(accommodationTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodationType;
	}
}
