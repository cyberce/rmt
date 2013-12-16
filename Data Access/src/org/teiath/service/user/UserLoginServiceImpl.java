package org.teiath.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.AccommodationDAO;
import org.teiath.data.dao.UserDAO;
import org.teiath.data.dao.UserRoleDAO;
import org.teiath.data.domain.User;
import org.teiath.data.domain.UserRole;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.ldap.LdapProperties;
import org.teiath.service.exceptions.AuthenticationException;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.util.PasswordService;

import java.util.ArrayList;
import java.util.Date;

@Service("userLoginService")
@Transactional
public class UserLoginServiceImpl
		implements UserLoginService {

	@Autowired
	UserDAO userDAO;
	@Autowired
	UserRoleDAO userRoleDAO;
	@Autowired
	AccommodationDAO accommodationDAO;
	@Autowired
	LdapProperties ldapProperties;

	@Override
	public User login(String username, String password)
			throws ServiceException, AuthenticationException {
		User user, ldapUser;
		try {
			if (ldapProperties.isLdapEnabled()) {
				ldapUser = userDAO.authorizeUserLDAP(username, password);
				if (ldapUser == null) {
					user = userDAO.authorize(username, password);
					user.getRoles().iterator();
				} else {
					user = userDAO.findByUsername(username);
					if (user == null) {
						user = new User();
						user.setRegistrationDate(new Date());
						user.setUserName(ldapUser.getUserName());
						user.setPassword(password);
						user.setFirstName(ldapUser.getFirstName());
						user.setLastName(ldapUser.getLastName());
						user.setEmail(ldapUser.getEmail());
						// todo check the following values. Initialize them properly
						user.setBirthDate(new Date());
						user.setGender(1);
						user.setUserType(1);
						user.setRoles(new ArrayList<UserRole>());
						user.getRoles().add(userRoleDAO.findById(4));
						userDAO.save(user);
					} else {
						user.setFirstName(ldapUser.getFirstName());
						user.setLastName(ldapUser.getLastName());
						user.setEmail(ldapUser.getEmail());
						userDAO.save(user);
						user.getRoles().iterator();
					}
				}
			} else {
				user = userDAO.authorize(username, PasswordService.encrypt(password));
				if (user != null) {
					user.getRoles().iterator();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(AuthenticationException.WRONG_CREDENTIALS_ERROR);
		}
		return user;
	}

	@Override
	public Accommodation getUserAccommodation(User user)
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

	@Override
	public User ssoLogin(User user) throws ServiceException, AuthenticationException {
		User localUser = null;
		try {
			localUser = userDAO.findByUsername(user.getUserName());
			if (localUser == null) {
				localUser = new User();
				localUser.setRegistrationDate(new Date());
				localUser.setUserName(user.getUserName());
				localUser.setFirstName(user.getFirstName());
				localUser.setLastName(user.getLastName());
				localUser.setEmail(user.getEmail());
				localUser.setUserType(user.getUserType());
				localUser.setRoles(new ArrayList<UserRole>());
				localUser.getRoles().add(userRoleDAO.findById(User.USER_ROLE_SIMPLE_USER));
				userDAO.save(localUser);
			} else {
				localUser.setFirstName(user.getFirstName());
				localUser.setLastName(user.getLastName());
				localUser.setEmail(user.getEmail());
				userDAO.save(localUser);
				localUser.getRoles().iterator();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(AuthenticationException.WRONG_CREDENTIALS_ERROR);
		}

		return localUser;
	}
}
