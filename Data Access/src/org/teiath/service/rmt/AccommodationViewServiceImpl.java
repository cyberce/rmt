package org.teiath.service.rmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.dao.AccommodationNotificationCriteriaDAO;
import org.teiath.data.dao.ApplicationImageDAO;
import org.teiath.data.dao.NotificationDAO;
import org.teiath.data.domain.Notification;
import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;
import org.teiath.data.email.IMailManager;
import org.teiath.data.properties.EmailProperties;
import org.teiath.data.properties.NotificationProperties;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;
import java.util.Date;

@Service("accommodationViewService")
@Transactional
public class AccommodationViewServiceImpl
		implements AccommodationViewService {

	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	ApplicationImageDAO imageDAO;
	@Autowired
	AccommodationNotificationCriteriaDAO accommodationNotificationCriteriaDAO;
	@Autowired
	private NotificationProperties notificationProperties;
	@Autowired
	NotificationDAO notificationDAO;
	@Autowired
	private EmailProperties emailProperties;
	@Autowired
	private IMailManager mailManager;

	@Override
	public Accommodation getAccommodationByUser(User user)
			throws ServiceException {
		Accommodation accommodation;

		try {
			accommodation = accommodationDAO.findByUser(user);
			if (accommodation != null) {
				accommodation.getAttributes().iterator();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return accommodation;
	}

	@Override
	public void disableAccommodation(Accommodation accommodation)
			throws ServiceException {
		try {

			accommodationDAO.save(accommodation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public void enableAccommodation(Accommodation accommodation)
			throws ServiceException {
		Collection<AccommodationNotificationCriteria> accommodationNotificationCriteria;

		try {
			accommodationDAO.save(accommodation);
			accommodationNotificationCriteria = accommodationNotificationCriteriaDAO.checkCriteria(accommodation);
			for (AccommodationNotificationCriteria matchedCriteria : accommodationNotificationCriteria) {
				//Create and save Notification
				Notification notification = new Notification();
				notification.setUser(matchedCriteria.getUser());
				notification.setAccommodation(accommodation);
				notification.setType(Notification.TYPE_ROOMMATES);
				notification.setSentDate(new Date());
				notification.setTitle("Νεα φοιτητική στέγη");
				String body = notificationProperties.getNotificationBody()
						.replace("$1", accommodation.getUser().getFullName()).replace("$2", "φοιτητική στέγη");
				notification.setBody(body);
				notification.setNotificationCriteria(matchedCriteria);
				notificationDAO.save(notification);

				//Create and send Email
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
				mailManager
						.sendMail(emailProperties.getFromAddress(), matchedCriteria.getUser().getEmail(), mailSubject,
								htmlMessageBuiler.toString());
			}

			//TODO FB auth
			/*			//Facebook Post
						FacebookClient facebookClient = new DefaultFacebookClient(
								"CAAGq0CTyO7UBAGZAJA4Gnzn8P481ldhkZCfrCKSjNbNoH2JiIuPxKkH5h9ukZBUWOfUYl7zJngWNWTTrpFPKxxrHwOGTaHckE0d7onBs77z6n9DeNbfpjZAIJRVZABfCCZAOC3CTwIPZCBLS31CDmb9U1DnwvXNZA0flPIDKx2ExZAgZDZD");
						FacebookType publishMessageResponse = facebookClient.publish("373423529440729/feed", FacebookType.class,
								Parameter.with("message",
										"Νεα δράση: " + event.getEventTitle() + " " + event.getEventLocation() + " " + event
												.getDateFrom()));       */

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public Collection<ApplicationImage> getImages(Accommodation accommodation)
			throws ServiceException {
		Collection<ApplicationImage> images;

		try {
			images = imageDAO.findByAccommodation(accommodation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return images;
	}
}
