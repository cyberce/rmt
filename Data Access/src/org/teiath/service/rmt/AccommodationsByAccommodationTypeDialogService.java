package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface AccommodationsByAccommodationTypeDialogService {

	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException;
}
