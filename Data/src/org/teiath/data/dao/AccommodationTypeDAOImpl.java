package org.teiath.data.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.teiath.data.domain.rmt.AccommodationType;

import java.util.Collection;

@Repository("accommodationTypeDAO")
public class AccommodationTypeDAOImpl
		implements AccommodationTypeDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Collection<AccommodationType> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Collection<AccommodationType> accommodationTypes;

		String hql = "from AccommodationType";
		Query query = session.createQuery(hql);

		accommodationTypes = query.list();

		return accommodationTypes;
	}

	@Override
	public void save(AccommodationType accommodationType) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(accommodationType);
	}

	@Override
	public AccommodationType findById(Integer id) {
		AccommodationType accommodationType;

		Session session = sessionFactory.getCurrentSession();
		accommodationType = (AccommodationType) session.get(AccommodationType.class, id);

		return accommodationType;
	}

	@Override
	public void delete(AccommodationType accommodationType) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(accommodationType);
		session.flush();
	}
}
