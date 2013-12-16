package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationAttributeDAO;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.dao.AccommodationTypeDAO;
import org.teiath.data.dao.ApplicationImageDAO;
import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.zkoss.image.AImage;
import org.zkoss.zul.ListModelList;

import java.util.Collection;

@Service("createAccommodationService")
@Transactional

public class CreateAccommodationServiceImpl
		implements CreateAccommodationService {

	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;
	@Autowired
	AccommodationAttributeDAO accommodationAttributeDAO;
	@Autowired
	ApplicationImageDAO applicationImageDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveAccommodation(Accommodation accommodation, ListModelList<AImage> uploadedImages)
			throws ServiceException {

		try {
			accommodationDAO.save(accommodation);

			//save Images
			for (AImage image : uploadedImages) {
				ApplicationImage applicationImage = new ApplicationImage();
				applicationImage.setAccommodation(accommodation);
				applicationImage.setImageBytes(image.getByteData());
				applicationImageDAO.save(applicationImage);
			}
			//den apostellontai eidopoihseis dioti h stegi den einai energopoihmenh
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException {
		Collection<AccommodationType> accommodationTypes;

		try {
			accommodationTypes = accommodationTypeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodationTypes;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveApplicationImage(ApplicationImage image)
			throws ServiceException {
		try {
			applicationImageDAO.save(image);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public Collection<AccommodationAttribute> getAccommodationAttributes()
			throws ServiceException {
		Collection<AccommodationAttribute> accommodationAttributes;

		try {
			accommodationAttributes = accommodationAttributeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodationAttributes;
	}

	@Override
	public Accommodation findAccommodationByUser(User user)
			throws ServiceException {
		Accommodation accommodation;

		try {
			accommodation = accommodationDAO.findByUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodation;
	}
}
