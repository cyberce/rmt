package org.teiath.web.vm.ntf;

import org.apache.log4j.Logger;
import org.teiath.data.domain.Notification;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.ntf.ViewNotificationService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class ViewNotificationVM
		extends BaseVM {

	static Logger log = Logger.getLogger(ViewNotificationVM.class.getName());

	@WireVariable
	private ViewNotificationService viewNotificationService;

	private Notification notification;

	@AfterCompose
	@NotifyChange("notification")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);
		try {
			notification = viewNotificationService
					.getNotificationById((Integer) ZKSession.getAttribute("notificationId"));
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(e.getMessage(), Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onBack() {
		ZKSession.sendRedirect(PageURL.NOTIFICATIONS_LIST);
	}

	@Command
	public void onTransition() {

        if (notification.getType() == Notification.TYPE_ROOMMATES) {
			ZKSession.setAttribute("accommodationId", notification.getAccommodation().getId());
			ZKSession.sendRedirect(PageURL.SEARCH_ACCOMMODATION_VIEW);
		}
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}
}
