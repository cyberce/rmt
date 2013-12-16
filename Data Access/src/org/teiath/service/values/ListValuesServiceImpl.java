package org.teiath.service.values;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.teiath.data.dao.*;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.DeleteViolationException;
import org.teiath.service.exceptions.ServiceException;

import java.util.Collection;

@Service("listValuesService")
@Transactional
public class ListValuesServiceImpl
		implements ListValuesService {

	@Autowired
	AccommodationAttributeDAO accommodationAttributeDAO;
	@Autowired
	AccommodationTypeDAO accommodationTypeDAO;


	@Override
	public Collection<AccommodationAttribute> getAccommodationAttributes()
			throws ServiceException {
		Collection<AccommodationAttribute> attributes;

		try {
			attributes = accommodationAttributeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return attributes;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteAccommodationAttribute(AccommodationAttribute accommodationAttribute)
			throws ServiceException, DeleteViolationException {
		try {
			accommodationAttributeDAO.delete(accommodationAttribute);
		} catch (ConstraintViolationException e) {
			throw new DeleteViolationException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}

	@Override
	public Collection<AccommodationType> getAccommodationTypes()
			throws ServiceException {
		Collection<AccommodationType> types;

		try {
			types = accommodationTypeDAO.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}

		return types;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteAccommodationType(AccommodationType accommodationType)
			throws ServiceException, DeleteViolationException {
		try {
			accommodationTypeDAO.delete(accommodationType);
		} catch (ConstraintViolationException e) {
			throw new DeleteViolationException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ServiceException.DATABASE_ERROR);
		}
	}
}
