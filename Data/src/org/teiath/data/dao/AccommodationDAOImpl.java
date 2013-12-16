package org.teiath.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;

import java.util.Collection;

@Repository("accommodationDAO")
public class AccommodationDAOImpl
		implements AccommodationDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public SearchResults<Accommodation> search(AccommodationSearchCriteria accommodationSearchCriteria) {
		Session session = sessionFactory.getCurrentSession();
		SearchResults<Accommodation> results = new SearchResults<>();
		Criteria criteria = session.createCriteria(Accommodation.class);
		criteria.createAlias("accommodationType", "accommodation");

		//AccommodationType Restriction
		if ((accommodationSearchCriteria.getAccommodationType() != null) && (accommodationSearchCriteria
				.getAccommodationType().getId() != - 1)) {

			criteria.add(Restrictions.eq("accommodationType", accommodationSearchCriteria.getAccommodationType()));
		}

		//AccommodationType Restriction by type name
		if (accommodationSearchCriteria.getAccommodationTypeName() != null) {
			criteria.add(Restrictions.eq("accommodation.name", accommodationSearchCriteria.getAccommodationTypeName()));
		}

		//AskedAmount  restriction
		if (accommodationSearchCriteria.getMaxAmount() != null) {
			criteria.add(Restrictions.le("askedAmount", accommodationSearchCriteria.getMaxAmount()));
		}

		//getFloor restriction
		if (accommodationSearchCriteria.getFloor() != null) {
			criteria.add(Restrictions.eq("floor", accommodationSearchCriteria.getFloor()));
		}

		//NumberOfBedrooms restriction
		if (accommodationSearchCriteria.getNumberOfBedrooms() != 0) {
			criteria.add(Restrictions.le("numberOfBedrooms", accommodationSearchCriteria.getNumberOfBedrooms()));
		}

		//HeatingType restriction
		if (accommodationSearchCriteria.getHeatingType() != null) {
			criteria.add(Restrictions.le("heatingType", accommodationSearchCriteria.getHeatingType()));
		}

		//PetsAllowed restriction
		if (accommodationSearchCriteria.getPetsAllowed() != null) {
			if (accommodationSearchCriteria.getPetsAllowed() == 1) {
				criteria.add(Restrictions.eq("petsAllowed", true));
			} else {
				criteria.add(Restrictions.eq("petsAllowed", false));
			}
		}

		//Smoking
		if (accommodationSearchCriteria.getSmokingAllowed() != null) {
			if (accommodationSearchCriteria.getSmokingAllowed() == 1) {
				criteria.add(Restrictions.eq("smokingAllowed", true));
			} else {
				criteria.add(Restrictions.eq("smokingAllowed", false));
			}
		}

		//Active restriction
		if (accommodationSearchCriteria.isActive()) {
			criteria.add(Restrictions.eq("active", true));
		}

		if (accommodationSearchCriteria.getAddress() != null) {
			criteria.add(Restrictions.le("address", accommodationSearchCriteria.getAddress()));
		}

		//Keyword
		if (accommodationSearchCriteria.getListingKeyword() != null) {

			criteria.add(Restrictions.or(Restrictions
					.like("address", accommodationSearchCriteria.getListingKeyword(), MatchMode.ANYWHERE)));
		}

		//Get distinct values
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		//Total records
		results.setTotalRecords(criteria.list().size());

		//Paging
		criteria.setFirstResult(
				accommodationSearchCriteria.getPageNumber() * accommodationSearchCriteria.getPageSize());
		criteria.setMaxResults(accommodationSearchCriteria.getPageSize());

		//Sorting
		if (accommodationSearchCriteria.getOrderField() != null) {
			if (accommodationSearchCriteria.getOrderDirection().equals("ascending")) {
				criteria.addOrder(Order.asc(accommodationSearchCriteria.getOrderField()));
			} else {
				criteria.addOrder(Order.desc(accommodationSearchCriteria.getOrderField()));
			}
		}

		//Fetch data
		results.setData(criteria.list());

		return results;
	}

	@Override
	public void save(Accommodation accommodation) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(accommodation);
	}

	@Override
	public Accommodation findById(Integer id) {
		Accommodation accommodation;

		Session session = sessionFactory.getCurrentSession();
		accommodation = (Accommodation) session.get(Accommodation.class, id);

		return accommodation;
	}

	@Override
	public Accommodation findByUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		Accommodation accommodation;
		Criteria criteria = session.createCriteria(Accommodation.class);

		criteria.add(Restrictions.eq("user", user));
		accommodation = (Accommodation) criteria.uniqueResult();

		return accommodation;
	}

	@Override
	public Collection<Accommodation> findAccommodationsByAccommodationType(int accommodationTypeId) {
		Session session = sessionFactory.getCurrentSession();
		Collection<Accommodation> accommodations;

		String hql = "select accommodation " +
				"from Accommodation as accommodation " +
				"where accommodation.accommodationType.id=:accommodationTypeId ";
		Query query = session.createQuery(hql);
		query.setParameter("accommodationTypeId", accommodationTypeId);

		accommodations = query.list();

		return accommodations;
	}

	@Override
	public Collection<Accommodation> findAccommodationsByNumberOfBedrooms(Integer numberFrom, Integer numberTo) {
		Session session = sessionFactory.getCurrentSession();
		Collection<Accommodation> accommodations;

		String hql = "select accommodation " +
				"from Accommodation as accommodation " +
				"where accommodation.numberOfBedrooms >=:numberFrom " +
				"and accommodation.numberOfBedrooms <=:numberTo ";
		Query query = session.createQuery(hql);
		query.setParameter("numberFrom", numberFrom);
		query.setParameter("numberTo", numberTo);

		accommodations = query.list();

		return accommodations;
	}
}
