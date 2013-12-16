package org.teiath.service.rmt;

import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface AccommodationViewService {

	public Accommodation getAccommodationByUser(User user)
			throws ServiceException;

	public void disableAccommodation(Accommodation accommodation)
			throws ServiceException;

	public void enableAccommodation(Accommodation accommodation)
			throws ServiceException;

	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException;
}


