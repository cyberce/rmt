package org.teiath.webservices.viewers;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.teiath.webservices.model.*;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Item;

public class RssViewer
		extends AbstractRssFeedView {

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
		feed.setTitle("RSS Web Service");
		feed.setDescription("Feed provided by T.E.I of Athens");
		feed.setLink("www.teiath.gr");
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Item> items = new ArrayList<>();
		response.setContentType("application/xml;charset=UTF-8");

		if (model.get("serviceRoommateList") instanceof ServiceRoommateList) {
			ServiceRoommateList serviceRoommateList = (ServiceRoommateList) model.get("serviceRoommateList");

			if (serviceRoommateList != null) {
				// Create feed items
				Item item;
				for (ServiceRoommate serviceRoommate: serviceRoommateList.getServiceRoommates()) {
					item = new Item();
					item.setAuthor(serviceRoommate.getRoommateName());
					item.setTitle(serviceRoommate.getAccommodationTypeName()+","+serviceRoommate.getAddress()+","+serviceRoommate.getAskedAmount().toString());
					item.setPubDate(serviceRoommate.getAccommodationCreationDate());
					//todo link
					item.setLink("http://195.130.100.180/teiath/zul/rmt/rmt_roommate_search.zul");
					items.add(item);
				}
			}
		}

		return items;
	}
}
