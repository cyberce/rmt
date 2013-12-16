package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.*;
import org.teiath.data.domain.Notification;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.data.email.IMailManager;
import org.teiath.data.properties.EmailProperties;
import org.teiath.data.properties.NotificationProperties;
import org.teiath.service.exceptions.ServiceException;
import org.zkoss.image.AImage;
import org.zkoss.zul.ListModelList;

import java.util.Collection;
import java.util.Date;

@Service("updateAccommodationService")
@Transactional
public class UpdateAccommodationServiceImpl
		implements UpdateAccommodationService {

	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	private NotificationProperties notificationProperties;
	@Autowired
	NotificationDAO notificationDAO;
	@Autowired
	private IMailManager mailManager;
	@Autowired
	private EmailProperties emailProperties;
	@Autowired
	AccommodationNotificationCriteriaDAO accommodationNotificationCriteriaDAO;
	@Autowired
	AccommodationAttributeDAO accommodationAttributeDAO;
	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;
	@Autowired
	ApplicationImageDAO applicationImageDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteAccommodationPhotos(Accommodation accommodation)
			throws ServiceException {
		try {
			applicationImageDAO.deleteAll(accommodation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveAccommodation(Accommodation accommodation, ListModelList<AImage> uploadedImages)
			throws ServiceException {
		Collection<AccommodationNotificationCriteria> accommodationNotificationCriteria;

		try {
			accommodationDAO.save(accommodation);

			//save Images
			for (AImage image : uploadedImages) {
				ApplicationImage applicationImage = new ApplicationImage();
				applicationImage.setAccommodation(accommodation);
				applicationImage.setImageBytes(image.getByteData());
				applicationImageDAO.save(applicationImage);
			}

			if (accommodation.isActive()) {

				accommodationNotificationCriteria = accommodationNotificationCriteriaDAO.checkCriteria(accommodation);
				for (AccommodationNotificationCriteria matchedCriteria : accommodationNotificationCriteria) {
					//Create and save Notification
					Notification notification = new Notification();
					notification.setUser(matchedCriteria.getUser());
					notification.setAccommodation(accommodation);
					notification.setType(Notification.TYPE_ROOMMATES);
					notification.setSentDate(new Date());
					notification.setTitle("Ενημέρωση στοιχείων φοιτητικής στέγης");
					String body = notificationProperties.getNotificationBody()
							.replace("$1", accommodation.getUser().getFullName()).replace("$2", "στέγη");
					notification.setBody(body);
					notification.setNotificationCriteria(matchedCriteria);
					notificationDAO.save(notification);

					///Create and send Email
					String mailSubject = emailProperties.getRouteCreateSubject()
							.replace("$1", "[Υπηρεσία αναζήτησης συγκατοίκων]:").replace("$2", "φοιτητικής στέγης");
					//TODO mail: currently only content
					StringBuilder htmlMessageBuiler = new StringBuilder();
					htmlMessageBuiler.append("<html> <body>");
					String mailBody = emailProperties.getRouteCreateBody()
							.replace("$1", accommodation.getUser().getFullName()).replace("$2", "φοιτητική στέγη");
					htmlMessageBuiler.append(mailBody);
					htmlMessageBuiler.append("<br>");
					htmlMessageBuiler.append("<br>Διεύθυνση: " + accommodation.getAddress());
					htmlMessageBuiler.append("<br>Τύπος κατοικίας: " + accommodation.getAccommodationType().getName());
					htmlMessageBuiler.append("<br>Ποσό συνεισφοράς: " + accommodation.getAskedAmount());
					htmlMessageBuiler.append("<br>Τετραγωνικά: " + accommodation.getSquareMeters());
					htmlMessageBuiler.append("<br>Όροφος: " + accommodation.getFloor());
					htmlMessageBuiler.append("<br>Ιδιοκτήτης: " + accommodation.getUser().getFullName());
					htmlMessageBuiler.append("</body> </html>");
					mailManager.sendMail(emailProperties.getFromAddress(), matchedCriteria.getUser().getEmail(),
							mailSubject, htmlMessageBuiler.toString());
				}
			}
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
	public Accommodation getAccommodationById(Integer accommodationId)
			throws ServiceException {
		Accommodation accommodation;
		try {
			accommodation = accommodationDAO.findById(accommodationId);
			accommodation.getAttributes().iterator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodation;
	}

	@Override
	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException {
		Collection<ApplicationImage> images;

		try {
			images = applicationImageDAO.findByAccommodation(accommodation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return images;
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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteApplicationImage(ApplicationImage image)
			throws ServiceException {
		try {
			applicationImageDAO.delete(image);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}
}
