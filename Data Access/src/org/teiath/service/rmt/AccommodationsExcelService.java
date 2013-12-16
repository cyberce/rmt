package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface AccommodationsExcelService {

	public Collection<Accommodation> getAccommodationsByAccommodationType(int accommodationTypeId)
			throws ServiceException;

	public Collection<Accommodation> getAccommodationsByNumberOfBedrooms(Integer numberFrom, Integer numberTo)
			throws ServiceException;
}
