package org.teiath.data.dao;

import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;

import java.util.Collection;

public interface AccommodationNotificationCriteriaDAO {

	public void save(AccommodationNotificationCriteria accommodationNotificationCriteria);

	public void delete(AccommodationNotificationCriteria accommodationNotificationCriteria);

	public Collection<AccommodationNotificationCriteria> checkCriteria(Accommodation accommodation);
}
