package org.teiath.service.user;

import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.AuthenticationException;
import org.teiath.service.exceptions.ServiceException;

public interface UserLoginService {

	public User login(String username, String password)
			throws ServiceException, AuthenticationException;

	public Accommodation getUserAccommodation(User user)
			throws ServiceException;

	public User ssoLogin(User user) throws ServiceException, AuthenticationException;
}
