package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("accommodationsExcelService")
@Transactional
public class AccommodationsExcelServiceImpl
		implements AccommodationsExcelService {

	@Autowired
	AccommodationDAO accommodationDAO;

	@Override
	public Collection<Accommodation> getAccommodationsByAccommodationType(int accommodationTypeId)
			throws ServiceException {
		Collection<Accommodation> accommodations;

		try {
			accommodations = accommodationDAO.findAccommodationsByAccommodationType(accommodationTypeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodations;
	}

	@Override
	public Collection<Accommodation> getAccommodationsByNumberOfBedrooms(Integer numberFrom, Integer numberTo)
			throws ServiceException {
		Collection<Accommodation> accommodations;

		try {
			accommodations = accommodationDAO.findAccommodationsByNumberOfBedrooms(numberFrom, numberTo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodations;
	}
}
