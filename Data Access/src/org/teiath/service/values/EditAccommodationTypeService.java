package org.teiath.service.values;

import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

public interface EditAccommodationTypeService {

	public void saveAccommodationType(AccommodationType accommodationType)
			throws ServiceException;

	public AccommodationType getAccommodationTypeById(Integer AccommodationTypeId)
			throws ServiceException;
}
