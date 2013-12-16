package org.teiath.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;

import java.util.Collection;

@Repository("accommodationNotificationCriteriaDAO")
public class AccommodationNotificationCriteriaDAOImpl
		implements AccommodationNotificationCriteriaDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(AccommodationNotificationCriteria accommodationNotificationCriteria) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(accommodationNotificationCriteria);
	}

	@Override
	public void delete(AccommodationNotificationCriteria accommodationNotificationCriteria) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(accommodationNotificationCriteria);
	}

	@Override
	public Collection<AccommodationNotificationCriteria> checkCriteria(Accommodation accommodation) {
		Session session = sessionFactory.getCurrentSession();
		Collection<AccommodationNotificationCriteria> accommodationNotificationCriteria;
		Criteria criteria = session.createCriteria(AccommodationNotificationCriteria.class);

		//AccommodationType restriction
		criteria.add(Restrictions
				.or(Restrictions.and(Restrictions.isNull("accommodationType"), Restrictions.isNotNull("id")),
						Restrictions.eq("accommodationType", accommodation.getAccommodationType())));

		//Floor restriction
		criteria.add(Restrictions.or(Restrictions.and(Restrictions.isNull("floor"), Restrictions.isNotNull("id")),
				Restrictions.eq("floor", accommodation.getFloor())));

		//Heating Type restriction
		criteria.add(Restrictions.or(Restrictions.and(Restrictions.isNull("heatingType"), Restrictions.isNotNull("id")),
				Restrictions.eq("heatingType", accommodation.getHeatingType())));

		//Pets restriction
		criteria.add(Restrictions.or(Restrictions.and(Restrictions.isNull("petsAllowed"), Restrictions.isNotNull("id")),
				Restrictions.eq("petsAllowed", accommodation.isPetsAllowed())));

		//Smoking restriction
		criteria.add(Restrictions
				.or(Restrictions.and(Restrictions.isNull("smokingAllowed"), Restrictions.isNotNull("id")),
						Restrictions.eq("smokingAllowed", accommodation.isSmokingAllowed())));

		//Contribution amount restriction
		criteria.add(Restrictions.or(Restrictions.and(Restrictions.isNull("maxAmount"), Restrictions.isNotNull("id")),
				Restrictions.ge("maxAmount", accommodation.getAskedAmount())));

		//Get distinct values
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		accommodationNotificationCriteria = criteria.list();

		return accommodationNotificationCriteria;
	}
}
