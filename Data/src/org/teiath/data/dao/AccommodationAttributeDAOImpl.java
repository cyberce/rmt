package org.teiath.data.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.teiath.data.domain.rmt.AccommodationAttribute;

import java.util.Collection;

@Repository("accommodationAttributeDAO")
public class AccommodationAttributeDAOImpl
		implements AccommodationAttributeDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Collection<AccommodationAttribute> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Collection<AccommodationAttribute> accommodationAttributes;

		String hql = "from AccommodationAttribute";
		Query query = session.createQuery(hql);

		accommodationAttributes = query.list();

		return accommodationAttributes;
	}

	@Override
	public void save(AccommodationAttribute accommodationAttribute) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(accommodationAttribute);
	}

	@Override
	public AccommodationAttribute findById(Integer id) {
		AccommodationAttribute accommodationAttribute;

		Session session = sessionFactory.getCurrentSession();
		accommodationAttribute = (AccommodationAttribute) session.get(AccommodationAttribute.class, id);

		return accommodationAttribute;
	}

	@Override
	public void delete(AccommodationAttribute accommodationAttribute) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(accommodationAttribute);
		session.flush();
	}
}
