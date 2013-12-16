package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

public interface CreateAccommodationTypeService {

	public void saveAccommodationType(AccommodationType accommodationType)
			throws ServiceException;
}
