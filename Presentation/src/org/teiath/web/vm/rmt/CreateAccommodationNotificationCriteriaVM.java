package org.teiath.web.vm.rmt;

import org.apache.log4j.Logger;
import org.teiath.data.domain.NotificationCriteria;
import org.teiath.data.domain.rmt.AccommodationNotificationCriteria;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.CreateAccommodationNotificationCriteriaService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class CreateAccommodationNotificationCriteriaVM
		extends BaseVM {

	static Logger log = Logger.getLogger(CreateAccommodationNotificationCriteriaVM.class.getName());

	@WireVariable
	private CreateAccommodationNotificationCriteriaService createAccommodationNotificationCriteriaService;

	private AccommodationNotificationCriteria accommodationNotificationCriteria;
	private ListModelList<AccommodationType> accommodationTypes;
	private AccommodationType selectedAccommodationType;

	@AfterCompose
	@NotifyChange("accommodationNotificationCriteria")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		accommodationNotificationCriteria = new AccommodationNotificationCriteria();
		accommodationNotificationCriteria.setUser(loggedUser);
		accommodationNotificationCriteria.setFloor(null);
		accommodationNotificationCriteria.setPetsAllowed(null);
		accommodationNotificationCriteria.setSmokingAllowed(null);
	}

	@Command
	public void onSave() {

		accommodationNotificationCriteria.setType(NotificationCriteria.TYPE_ROOMMATES);
		accommodationNotificationCriteria
				.setAccommodationType(selectedAccommodationType.getId() != - 1 ? selectedAccommodationType : null);
		try {
			createAccommodationNotificationCriteriaService
					.saveAccommodationNotificationCriteria(accommodationNotificationCriteria);
			Messagebox.show(Labels.getLabel("notifications.message.success"),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.INFORMATION,
					new EventListener<Event>() {
						public void onEvent(Event evt) {
							ZKSession.sendRedirect(PageURL.NOTIFICATION_CRITERIA_LIST);
						}
					});
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("notifications")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR);
			ZKSession.sendRedirect(PageURL.NOTIFICATION_CRITERIA_LIST);
		}
	}

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.NOTIFICATION_CRITERIA_LIST);
	}

	public ListModelList<AccommodationType> getAccommodationTypes() {
		if (accommodationTypes == null) {
			accommodationTypes = new ListModelList<>();
			selectedAccommodationType = new AccommodationType();
			selectedAccommodationType.setId(- 1);
			selectedAccommodationType.setName("");
			selectedAccommodationType.setDescription("");
			accommodationTypes.add(selectedAccommodationType);
			try {
				accommodationTypes.addAll(createAccommodationNotificationCriteriaService.getAccommodationTypes());
			} catch (ServiceException e) {
				log.error(e.getMessage());
				Messagebox.show(MessageBuilder
						.buildErrorMessage(e.getMessage(), Labels.getLabel("listing.transactionType")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		}
		return accommodationTypes;
	}

	public AccommodationNotificationCriteria getAccommodationNotificationCriteria() {
		return accommodationNotificationCriteria;
	}

	public void setAccommodationNotificationCriteria(
			AccommodationNotificationCriteria accommodationNotificationCriteria) {
		this.accommodationNotificationCriteria = accommodationNotificationCriteria;
	}

	public AccommodationType getSelectedAccommodationType() {
		return selectedAccommodationType;
	}

	public void setSelectedAccommodationType(AccommodationType selectedAccommodationType) {
		this.selectedAccommodationType = selectedAccommodationType;
	}
}
