package org.teiath.service.ntf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.NotificationCriteriaDAO;
import org.teiath.data.domain.NotificationCriteria;
import org.teiath.service.exceptions.ServiceException;

@Service("viewNotificationCriteriaService")
@Transactional
public class ViewNotificationCriteriaServiceImpl
		implements ViewNotificationCriteriaService {

	@Autowired
	NotificationCriteriaDAO notificationCriteriaDAO;

	@Override
	public NotificationCriteria getNotificationCriteriaById(Integer notificationId)
			throws ServiceException {
		NotificationCriteria notificationCriteria;
		try {
			notificationCriteria = notificationCriteriaDAO.findById(notificationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
		return notificationCriteria;
	}
}
