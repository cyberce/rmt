package org.teiath.service.user;

import org.teiath.data.domain.User;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

public interface ViewUserService {

	public User getUserById(int userId)
			throws ServiceException;
}


