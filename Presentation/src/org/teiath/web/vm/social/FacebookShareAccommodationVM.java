package org.teiath.web.vm.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import org.apache.log4j.Logger;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.social.FacebookShareService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class FacebookShareAccommodationVM
		extends BaseVM {

	static Logger log = Logger.getLogger(FacebookShareAccommodationVM.class.getName());

	@WireVariable
	private FacebookShareService facebookShareService;

	private String post;
	private String token;

	private Accommodation accommodation;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {

		token = Executions.getCurrent().getParameter("token");

		try {
			accommodation = facebookShareService
					.getAccommodationById(Integer.parseInt(Executions.getCurrent().getParameter("state")));
		} catch (ServiceException e) {
			log.error(e.getMessage());
		}

		if (accommodation != null) {
			//TODO post content
			post = accommodation.getAddress() + " " + accommodation.getAccommodationType().getName();
		}
	}

	@Command
	public void onPost() {
		//Facebook Post

		FacebookClient facebookClient = new DefaultFacebookClient(token);
		if (accommodation != null) {

			FacebookType publishMessageResponse = facebookClient
					.publish("me/feed", FacebookType.class, Parameter.with("message", post));
			Messagebox.show(MessageBuilder
					.buildErrorMessage("Το μήνυμα δημοσιεύτηκε επιτυχώς στο προφίλ σας", "Facebook"), "Social Networks",
					Messagebox.OK, Messagebox.INFORMATION);
			ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
		}
	}

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
}
