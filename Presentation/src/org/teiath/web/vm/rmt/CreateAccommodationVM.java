package org.teiath.web.vm.rmt;

import com.vividsolutions.jts.io.ParseException;
import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.CreateAccommodationService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.Collection;

@SuppressWarnings("UnusedDeclaration")
public class CreateAccommodationVM
		extends BaseVM {

	static Logger log = Logger.getLogger(AccommodationViewVM.class.getName());

	@Wire("#createListingWin")
	private Window win;
	@Wire("#uploadButton")
	private Button uploadButton;
	@Wire("#btnDelete")
	private Button deleteButton;
	@Wire("#btnView")
	private Button viewButton;
	@Wire("#photosListbox")
	private Listbox photosListbox;

	@WireVariable
	private CreateAccommodationService createAccommodationService;

	private Accommodation accommodation;
	private ListModelList<AccommodationType> accommodationTypes;
	private ListModelList<AccommodationAttribute> availableAttributes;
	private ListModelList<AccommodationAttribute> selectedAttributes;
	private ListModelList<AImage> uploadedImages;

	@AfterCompose
	@NotifyChange("accommodation")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		uploadedImages = new ListModelList<>();

		try {
			if (createAccommodationService.findAccommodationByUser(loggedUser) == null) {
				selectedAttributes = new ListModelList<>();
				accommodation = new Accommodation();
				accommodation.setUser(loggedUser);
				accommodation.setConstructionYear(null);
				accommodation.setExistingKitchen(true);
			} else {
				ZKSession.getAttribute("accommodationId");
				ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
			}
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR);
			ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
		}
	}

	@Command
	public void onChooseAllBtnClick() {
		selectedAttributes.addAll(availableAttributes);
		availableAttributes.clear();
	}

	@Command
	public void onChooseBtnClick() {
		for (AccommodationAttribute selectedAttribute : availableAttributes.getSelection()) {
			selectedAttributes.add(selectedAttribute);
			availableAttributes.remove(selectedAttribute);
		}
	}

	@Command
	public void onRemoveBtnClick() {
		for (AccommodationAttribute selectedAttribute : selectedAttributes.getSelection()) {
			availableAttributes.add(selectedAttribute);
			selectedAttributes.remove(selectedAttribute);
		}
	}

	@Command
	public void onRemoveAllBtnClick() {
		availableAttributes.addAll(selectedAttributes);
		selectedAttributes.clear();
	}

	@Command("upload")
	public void onImageUpload(
			@ContextParam(ContextType.BIND_CONTEXT)
			BindContext ctx) {
		UploadEvent upEvent = null;
		Object objUploadEvent = ctx.getTriggerEvent();
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}
		if ((upEvent != null) && (uploadedImages.getSize() < 4)) {
			Media media = upEvent.getMedia();
			uploadedImages.add((AImage) media);
			Listitem listitem = new Listitem();
			Listcell listcell = new Listcell();
			Image image = new Image();
			image.setContent((AImage) media);
			image.setHeight("64px");
			image.setWidth("64px");
			listcell.appendChild(image);
			listitem.appendChild(listcell);
			photosListbox.appendChild(listitem);
		}
		photosListbox.setVisible(true);
	}

	@Command("select")
	public void onSelect() {
		deleteButton.setDisabled(false);
		viewButton.setDisabled(false);
	}

	@Command("delete")
	public void onDelete() {
		uploadedImages.remove(photosListbox.getSelectedIndex());
		photosListbox.removeItemAt(photosListbox.getSelectedIndex());
		deleteButton.setDisabled(true);
		System.out.println(uploadedImages.getSize());
	}

	@Command("view")
	public void onView() {
		ZKSession.setAttribute("aImage", uploadedImages.get(photosListbox.getSelectedIndex()));
		Window window = (Window) Executions.createComponents("/zul/trg/image_view.zul", null, null);
		window.doModal();
	}

	@Command
	public void onSave(
			@ContextParam(ContextType.TRIGGER_EVENT)
			InputEvent evnt) {
		try {
			accommodation.setAttributes(selectedAttributes);

			String locationCoords = ((Textbox) evnt.getTarget()).getValue();
			if (! locationCoords.isEmpty()) {
				accommodation.materializeLocation(locationCoords);
				createAccommodationService.saveAccommodation(accommodation, uploadedImages);
				ZKSession.setAttribute("USER_ACCOMMODATION", accommodation);
				Messagebox.show(Labels.getLabel("roommates.message.success"),
						Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.INFORMATION,
						new EventListener<Event>() {
							public void onEvent(Event evt) {
								ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
							}
						});
			}
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR,
					new EventListener<Event>() {
						public void onEvent(Event evt) {
							ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
						}
					});
		} catch (ParseException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("action")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR,
					new EventListener<org.zkoss.zk.ui.event.Event>() {
						public void onEvent(org.zkoss.zk.ui.event.Event evt) {
							ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
						}
					});
		}
	}

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
	}

	public ListModelList<AccommodationType> getAccommodationTypes() {
		if (accommodationTypes == null) {
			accommodationTypes = new ListModelList<>();
			try {
				accommodationTypes.addAll(createAccommodationService.getAccommodationTypes());
			} catch (ServiceException e) {
				Messagebox
						.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home.type")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
				log.error(e.getMessage());
			}
		}

		return accommodationTypes;
	}

	public Collection<AccommodationAttribute> getAvailableAttributes() {
		if (availableAttributes == null) {
			availableAttributes = new ListModelList<>();
			try {
				availableAttributes.addAll(createAccommodationService.getAccommodationAttributes());
			} catch (ServiceException e) {
				Messagebox
						.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home.type")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
				log.error(e.getMessage());
			}
		}

		return availableAttributes;
	}

	public Accommodation getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}

	public ListModelList<AccommodationAttribute> getSelectedAttributes() {
		return selectedAttributes;
	}

	public void setSelectedAttributes(ListModelList<AccommodationAttribute> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public ListModelList<AImage> getUploadedImages() {
		return uploadedImages;
	}

	public void setUploadedImages(ListModelList<AImage> uploadedImages) {
		this.uploadedImages = uploadedImages;
	}
}
