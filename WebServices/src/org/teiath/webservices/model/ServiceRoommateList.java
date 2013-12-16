package org.teiath.webservices.model;

import org.codehaus.jackson.map.annotate.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="roommates")
@JsonRootName(value = "roommates")
public class ServiceRoommateList {

	private List<ServiceRoommate> roommates;

	public ServiceRoommateList() {

	}

	public ServiceRoommateList(List<ServiceRoommate> roommates) {
		this.roommates = roommates;
	}

	@XmlElement(name="roommate")
	public List<ServiceRoommate> getServiceRoommates() {
		return this.roommates;
	}

	public void setServiceRoommatess(List<ServiceRoommate> roommates) {
		this.roommates = roommates;
	}

}
