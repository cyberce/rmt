package org.teiath.service.ntf;

import org.teiath.data.domain.NotificationCriteria;
import org.teiath.service.exceptions.ServiceException;

public interface ViewNotificationCriteriaService {

	public NotificationCriteria getNotificationCriteriaById(Integer notificationCriteriaId)
			throws ServiceException;
}


