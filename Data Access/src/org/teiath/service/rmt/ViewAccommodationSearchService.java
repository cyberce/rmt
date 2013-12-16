package org.teiath.service.rmt;

import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface ViewAccommodationSearchService {

	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException;

	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException;
}
