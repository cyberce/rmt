package org.teiath.service.rmt;

import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.zkoss.image.AImage;
import org.zkoss.zul.ListModelList;

import java.util.Collection;

public interface CreateAccommodationService {

	public void saveAccommodation(Accommodation accommodation, ListModelList<AImage> uploadedImages)
			throws ServiceException;

	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException;

	public Collection<AccommodationAttribute> getAccommodationAttributes()
			throws ServiceException;

	public void saveApplicationImage(ApplicationImage image)
			throws ServiceException;

	public Accommodation findAccommodationByUser(User user)
			throws ServiceException;
}
