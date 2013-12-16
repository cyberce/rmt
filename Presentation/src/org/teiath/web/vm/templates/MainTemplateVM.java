package org.teiath.web.vm.templates;

import org.teiath.data.domain.User;
import org.teiath.data.domain.UserRole;
import org.teiath.web.session.ZKSession;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class MainTemplateVM {

	@Wire("#adminMenu")
	private Menu adminMenu;
	@Wire("#newHomeMenu")
	private Menuitem newHomeMenu;
	@Wire("#mySettingsMenu")
	private Menu mySettingsMenu;
	@Wire("#myHomeMenu")
	private Menuitem myHomeMenu;

	private User user;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		if (ZKSession.getAttribute("AUTH_USER") != null) {
			user = (User) ZKSession.getAttribute("AUTH_USER");

			if (ZKSession.getAttribute("USER_ACCOMMODATION") == null) {
				newHomeMenu.setVisible(true);
				myHomeMenu.setVisible(false);
			}

			for (UserRole userRole : user.getRoles()) {
				if (userRole.getCode() == User.USER_ROLE_ADMINISTRATION_CLERK) {
					adminMenu.setVisible(true);
				}

				if (userRole.getCode() == User.USER_ROLE_EVENT_MANAGER) {
				}

				if (userRole.getCode() == User.USER_ROLE_ADMINISTRATOR) {
					adminMenu.setVisible(true);
				}
			}
		} else {
			ZKSession.invalidate();
			ZKSession.sendPureRedirect("/index.zul");
		}
	}

	@Command
	public void onMenuSelect(
			@BindingParam("selectedMenu")
			String selectedMenu) {
		ZKSession.sendRedirect("/zul" + selectedMenu + ".zul");
	}

	@Command
	public void logout() {
		Messagebox.show(Labels.getLabel("template.logout_message"), Labels.getLabel("template.logout"),
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			public void onEvent(Event evt) {
				switch ((Integer) evt.getData()) {
					case Messagebox.YES:
						ZKSession.invalidate();
						ZKSession.sendPureRedirect("/index.zul");
						break;
					case Messagebox.NO:
						break;
				}
			}
		});
	}

	@Command
	public void home() {
		ZKSession.sendRedirect("/index.zul");
	}

	@Command
	public void api() {
		ZKSession.sendRedirect("/zul/guides/apiGuide.htm");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}