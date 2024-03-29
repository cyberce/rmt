package org.teiath.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.UserDAO;
import org.teiath.data.domain.User;
import org.teiath.data.email.IMailManager;
import org.teiath.data.properties.EmailProperties;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.util.PasswordService;

@Service("createUserService")
@Transactional
public class CreateUserServiceImpl
		implements CreateUserService {

	@Autowired
	UserDAO userDAO;
	@Autowired
	private IMailManager mailManager;
	@Autowired
	private EmailProperties emailProperties;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(User user)
			throws ServiceException {
		try {
			user.setPassword(PasswordService.encrypt(user.getPassword()));
			userDAO.save(user);

			//Create and send Email
			String mailSubject = emailProperties.getUserCreateSubject();
			String mailBody = emailProperties.getUserCreateBody();
			mailManager.sendMail(emailProperties.getFromAddress(), user.getEmail(), mailSubject, mailBody);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}
}
