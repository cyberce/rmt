package org.teiath.service.rmt;

import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.zkoss.image.AImage;
import org.zkoss.zul.ListModelList;

import java.util.Collection;

public interface UpdateAccommodationService {

	public void deleteAccommodationPhotos(Accommodation accommodation)
			throws ServiceException;

	public void saveAccommodation(Accommodation accommodation, ListModelList<AImage> uploadedImages)
			throws ServiceException;

	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException;

	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException;

	public Collection<AccommodationAttribute> getAccommodationAttributes()
			throws ServiceException;

	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException;

	public void saveApplicationImage(ApplicationImage image)
			throws ServiceException;

	public void deleteApplicationImage(ApplicationImage image)
			throws ServiceException;
}
