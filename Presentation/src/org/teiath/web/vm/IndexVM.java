package org.teiath.web.vm;

import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.AuthenticationException;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.user.UserLoginService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.East;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.West;

public class IndexVM {

	static Logger log = Logger.getLogger(IndexVM.class.getName());

	@Wire("#east")
	East east;
	@Wire("#west")
	West west;

	@WireVariable
	UserLoginService userLoginService;

	private String username;
	private String password;
	private String messageLabel;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		if (ZKSession.getAttribute("AUTH_USER") != null) {
			east.setSize("0%");
			west.setSize("100%");
		}
	}

	@Command
	public void onLogin() {
		User user;
		Accommodation accommodation;

		try {
			user = userLoginService.login(username, password);
			if (user != null) {

				accommodation = userLoginService.getUserAccommodation(user);
				if (accommodation != null) {
					ZKSession.setAttribute("USER_ACCOMMODATION", accommodation);
				}

				ZKSession.setAttribute("AUTH_USER", user);
				ZKSession.sendRedirect("/zul/rmt/rmt_roommate_search.zul");
			} else {
				Messagebox.show(MessageBuilder
						.buildErrorMessage(Labels.getLabel("user.access.fail"), Labels.getLabel("user.access")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		} catch (AuthenticationException e) {
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("user.access")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			log.error(e.getMessage());
		} catch (ServiceException e) {
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("user.access")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			log.error(e.getMessage());
		}
	}

	@Command
	public void onRegister() {
		ZKSession.sendRedirect("/zul/user/user_create.zul");
	}

	@Command
	public void onSSOLogin() {
		ZKSession.sendPureRedirect("https://carpooling.teiath.gr/secure");
	}

	@Command
	public void iForgot() {
		ZKSession.sendRedirect("/reset_request.zul");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessageLabel() {
		return messageLabel;
	}

	public void setMessageLabel(String messageLabel) {
		this.messageLabel = messageLabel;
	}
}
