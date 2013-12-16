package org.teiath.service.values;

import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.DeleteViolationException;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface ListValuesService {


	public Collection<AccommodationAttribute> getAccommodationAttributes()
			throws ServiceException;

	public void deleteAccommodationAttribute(AccommodationAttribute accommodationAttribute)
			throws ServiceException, DeleteViolationException;

	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException;

	public void deleteAccommodationType(AccommodationType accommodationType)
			throws ServiceException, DeleteViolationException;

}

