package org.teiath.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.UserDAO;
import org.teiath.data.domain.User;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.UserSearchCriteria;
import org.teiath.service.exceptions.ServiceException;

@Service("listUsersService")
@Transactional
public class ListUsersServiceImpl
		implements ListUsersService {

	@Autowired
	UserDAO userDAO;

	@Override
	public SearchResults<User> searchUsersByCriteria(UserSearchCriteria criteria)
			throws ServiceException {
		SearchResults<User> results;

		try {
			results = userDAO.search(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
		return results;
	}
}
