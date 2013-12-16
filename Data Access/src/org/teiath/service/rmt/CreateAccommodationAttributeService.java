package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.service.exceptions.ServiceException;

public interface CreateAccommodationAttributeService {

	public void saveAccommodationAttribute(AccommodationAttribute accommodationAttribute)
			throws ServiceException;
}
