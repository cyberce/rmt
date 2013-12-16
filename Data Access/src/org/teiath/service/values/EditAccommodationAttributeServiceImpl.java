package org.teiath.service.values;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationAttributeDAO;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.service.exceptions.ServiceException;

@Service("editAccommodationAttributeService")
@Transactional
public class EditAccommodationAttributeServiceImpl
		implements EditAccommodationAttributeService {

	@Autowired
	AccommodationAttributeDAO accommodationAttributeDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAccommodationAttribute(AccommodationAttribute accommodationAttribute)
			throws ServiceException {
		try {
			accommodationAttributeDAO.save(accommodationAttribute);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public AccommodationAttribute getAccommodationAttributeById(Integer accommodationAttributeId)
			throws ServiceException {
		AccommodationAttribute accommodationAttribute;
		try {
			accommodationAttribute = accommodationAttributeDAO.findById(accommodationAttributeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodationAttribute;
	}
}
