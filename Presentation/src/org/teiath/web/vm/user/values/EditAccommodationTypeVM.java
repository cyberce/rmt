package org.teiath.web.vm.user.values;

import org.apache.log4j.Logger;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.values.EditAccommodationTypeService;
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
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class EditAccommodationTypeVM
		extends BaseVM {

	static Logger log = Logger.getLogger(EditAccommodationTypeVM.class.getName());

	@WireVariable
	private EditAccommodationTypeService editAccommodationTypeService;

	private AccommodationType accommodationType;

	@AfterCompose
	@NotifyChange("accommodationType")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		accommodationType = new AccommodationType();
		try {
			accommodationType = editAccommodationTypeService
					.getAccommodationTypeById((Integer) ZKSession.getAttribute("accommodationTypeId"));
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("value.list")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			ZKSession.sendRedirect(PageURL.VALUES_LIST);
		}
	}

	@Command
	public void onSave() {
		try {
			editAccommodationTypeService.saveAccommodationType(accommodationType);
			Messagebox.show(Labels.getLabel("value.message.success"), Labels.getLabel("common.messages.save_title"),
					Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
				public void onEvent(Event evt) {
					ZKSession.sendRedirect(PageURL.VALUES_LIST);
				}
			});
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("value.list")),
					Labels.getLabel("common.messages.edit_title"), Messagebox.OK, Messagebox.ERROR);
			ZKSession.sendRedirect(PageURL.VALUES_LIST);
		}
	}

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.VALUES_LIST);
	}

	public AccommodationType getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(AccommodationType accommodationType) {
		this.accommodationType = accommodationType;
	}
}

