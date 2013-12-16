package org.teiath.service.rmt;

import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface CreateAccommodationNotificationCriteriaService {

	public void saveAccommodationNotificationCriteria(
			AccommodationNotificationCriteria accommodationNotificationCriteria)
			throws ServiceException;

	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException;
}
