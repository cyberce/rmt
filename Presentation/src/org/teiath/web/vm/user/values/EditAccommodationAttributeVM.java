package org.teiath.web.vm.user.values;

import org.apache.log4j.Logger;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.values.EditAccommodationAttributeService;
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
public class EditAccommodationAttributeVM
		extends BaseVM {

	static Logger log = Logger.getLogger(EditAccommodationAttributeVM.class.getName());

	@WireVariable
	private EditAccommodationAttributeService editAccommodationAttributeService;

	private AccommodationAttribute accommodationAttribute;

	@AfterCompose
	@NotifyChange("accommodationAttribute")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		accommodationAttribute = new AccommodationAttribute();
		try {
			accommodationAttribute = editAccommodationAttributeService
					.getAccommodationAttributeById((Integer) ZKSession.getAttribute("accommodationAttributeId"));
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
			editAccommodationAttributeService.saveAccommodationAttribute(accommodationAttribute);
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

	public AccommodationAttribute getAccommodationAttribute() {
		return accommodationAttribute;
	}

	public void setAccommodationAttribute(AccommodationAttribute accommodationAttribute) {
		this.accommodationAttribute = accommodationAttribute;
	}
}
