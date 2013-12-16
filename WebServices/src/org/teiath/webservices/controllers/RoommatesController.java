package org.teiath.webservices.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.SearchAccommodationService;
import org.teiath.service.rmt.ViewAccommodationSearchService;
import org.teiath.webservices.model.ServiceRoommate;
import org.teiath.webservices.model.ServiceRoommateList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
public class RoommatesController {

	@Autowired
	private SearchAccommodationService searchAccommodationService;
	@Autowired
	private ViewAccommodationSearchService viewAccommodationSearchService;

	private static final Logger logger_c = Logger.getLogger(RoommatesController.class);

	@RequestMapping(value = "/services/roommates", method = RequestMethod.GET)
	public ServiceRoommateList searchRoommates(String aType, Integer floor, Double mAmount, String hType, Boolean smoke, Boolean pets) {

		ServiceRoommateList serviceRoommateList = new ServiceRoommateList();
		serviceRoommateList.setServiceRoommatess(new ArrayList<ServiceRoommate>());
		ServiceRoommate serviceRoommate = null;

		AccommodationSearchCriteria criteria = new AccommodationSearchCriteria();
		try {
			criteria.setAccommodationTypeName(aType);
			criteria.setFloor(floor);
			criteria.setMaxAmount(mAmount != null ? new BigDecimal(mAmount) : null);
			criteria.setHeatingType(hType);
			criteria.setSmokingAllowed(smoke != null ? smoke ? 1 : 0 : null);
			criteria.setPetsAllowed(pets != null ? pets ? 1 : 0 : null);

			criteria.setPageNumber(0);
			criteria.setPageSize(Integer.MAX_VALUE);

			SearchResults<Accommodation> results = searchAccommodationService.searchAccommodation(criteria);
			System.out.println(results.getTotalRecords());
			for (Accommodation accommodation: results.getData()) {
				serviceRoommate = new ServiceRoommate();
				serviceRoommate.setAddress(accommodation.getAddress());
				serviceRoommate.setAccommodationTypeName(accommodation.getAccommodationType().getName());
				serviceRoommate.setFloor(accommodation.getFloor());
				serviceRoommate.setSquareMeters(accommodation.getSquareMeters());
				serviceRoommate.setNumberOfBedrooms(accommodation.getNumberOfBedrooms());
				serviceRoommate.setHeatingType(accommodation.getHeatingType());
				serviceRoommate.setConstructionYear(accommodation.getConstructionYear());
				serviceRoommate.setAskedAmount(accommodation.getAskedAmount());
				serviceRoommate.setSmokingAllowed(accommodation.isSmokingAllowed());
				serviceRoommate.setPetsAllowed(accommodation.isPetsAllowed());
				serviceRoommate.setComment(accommodation.getComment());
				serviceRoommate.setAccommodationCreationDate(accommodation.getAccommodationPublishDate());
				serviceRoommate.setRoommateName(accommodation.getUser().getFullName());

				serviceRoommateList.getServiceRoommates().add(serviceRoommate);
			}
		} catch (ServiceException e) {
			e.printStackTrace(); 
		}

		logger_c.debug("Returing Roommates: " + serviceRoommate);
		return serviceRoommateList;
	}

	@RequestMapping(value = "/services/roommate", method = RequestMethod.GET)
	public ServiceRoommate searchRoommateById(Integer id) {

		ServiceRoommate serviceRoommate = new ServiceRoommate();
		try {

			Accommodation accommodation = viewAccommodationSearchService.getAccommodationById(id);
			serviceRoommate.setAddress(accommodation.getAddress());
			serviceRoommate.setAccommodationTypeName(accommodation.getAccommodationType().getName());
			serviceRoommate.setFloor(accommodation.getFloor());
			serviceRoommate.setSquareMeters(accommodation.getSquareMeters());
			serviceRoommate.setNumberOfBedrooms(accommodation.getNumberOfBedrooms());
			serviceRoommate.setHeatingType(accommodation.getHeatingType());
			serviceRoommate.setConstructionYear(accommodation.getConstructionYear());
			serviceRoommate.setAskedAmount(accommodation.getAskedAmount());
			serviceRoommate.setSmokingAllowed(accommodation.isSmokingAllowed());
			serviceRoommate.setPetsAllowed(accommodation.isPetsAllowed());
			serviceRoommate.setComment(accommodation.getComment());
			serviceRoommate.setAccommodationCreationDate(accommodation.getAccommodationPublishDate());
			serviceRoommate.setRoommateName(accommodation.getUser().getFullName());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		logger_c.debug("Returing Event: " + serviceRoommate);

		return serviceRoommate;
	}
}
