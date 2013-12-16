package org.teiath.service.values;

import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.service.exceptions.ServiceException;

public interface EditAccommodationAttributeService {

	public void saveAccommodationAttribute(AccommodationAttribute accommodationAttribute)
			throws ServiceException;

	public AccommodationAttribute getAccommodationAttributeById(Integer AccommodationAttributeId)
			throws ServiceException;
}
