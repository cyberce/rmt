package org.teiath.data.dao;

import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;

import java.util.Collection;

public interface ApplicationImageDAO {

	public Collection<ApplicationImage> findByAccommodation(Accommodation accommodation);

	public ApplicationImage findByUser(User user);

	public void save(ApplicationImage applicationImage);

	public void delete(ApplicationImage applicationImage);

	public void deleteAll(Accommodation accommodation);
}
