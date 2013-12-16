package org.teiath.service.social;

import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

public interface FacebookShareService {

	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException;
}
