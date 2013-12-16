package org.teiath.web.vm.rmt;

import com.vividsolutions.jts.io.ParseException;
import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.UpdateAccommodationService;
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

import java.io.IOException;

@SuppressWarnings("UnusedDeclaration")
public class UpdateAccommodationVM
		extends BaseVM {

	static Logger log = Logger.getLogger(UpdateAccommodationVM.class.getName());

	@Wire("#uploadButton")
	private Button uploadButton;
	@Wire("#btnDelete")
	private Button deleteButton;
	@Wire("#btnView")
	private Button viewButton;
	@Wire("#photosListbox")
	private Listbox photosListbox;
	@Wire
	private Label markerData;

	@WireVariable
	private UpdateAccommodationService updateAccommodationService;

	private Accommodation accommodation;
	private ListModelList<AccommodationType> accommodationTypes;
	private ListModelList<AccommodationAttribute> availableAttributes;
	private ListModelList<AccommodationAttribute> selectedAttributes;
	private ListModelList<ApplicationImage> images;
	private ListModelList<AImage> uploadedImages;

	@AfterCompose
	@NotifyChange("accommodation")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		uploadedImages = new ListModelList<>();

		accommodation = new Accommodation();
		accommodation.setUser(loggedUser);

		accommodationTypes = new ListModelList<>();
		selectedAttributes = new ListModelList<>();
		availableAttributes = new ListModelList<>();

		try {
			accommodation = updateAccommodationService
					.getAccommodationById((Integer) ZKSession.getAttribute("accommodationId"));
			accommodationTypes.addAll(updateAccommodationService.getAccommodationTypes());
			selectedAttributes.addAll(accommodation.getAttributes());
			availableAttributes.addAll(updateAccommodationService.getAccommodationAttributes());
			availableAttributes.removeAll(accommodation.getAttributes());

			markerData.setValue(accommodation.getLocation().getX() + "|" + accommodation.getLocation().getY());
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("accommodation")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
		}

		images = this.getImages();
		if (images != null) {
			for (ApplicationImage applicationImage : images) {
				try {
					AImage aImage = new AImage("", applicationImage.getImageBytes());
					uploadedImages.add(aImage);
					Listitem listitem = new Listitem();
					Listcell listcell = new Listcell();
					Image image = new Image();
					image.setContent(aImage);
					listcell.appendChild(image);
					listitem.appendChild(listcell);
					photosListbox.appendChild(listitem);
				} catch (IOException e) {
					Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("route")),
							Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
					log.error(e.getMessage());
					ZKSession.sendRedirect(PageURL.LISTING_LIST);
				}
			}
			photosListbox.setVisible(true);
		}
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
			String locationCoords = ((Textbox) evnt.getTarget()).getValue();
			if (! locationCoords.isEmpty()) {
				accommodation.materializeLocation(locationCoords);
				accommodation.setAttributes(selectedAttributes);
				updateAccommodationService.deleteAccommodationPhotos(accommodation);
				updateAccommodationService.saveAccommodation(accommodation, uploadedImages);
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

	public ListModelList<AccommodationType> getAccommodationType() {
		if (accommodationTypes == null) {
			accommodationTypes = new ListModelList<>();
			try {
				accommodationTypes.addAll(updateAccommodationService.getAccommodationTypes());
			} catch (ServiceException e) {
				log.error(e.getMessage());
				Messagebox
						.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home.type")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		}

		return accommodationTypes;
	}

	public ListModelList<ApplicationImage> getImages() {
		if (images == null) {
			images = new ListModelList<>();
			try {
				images.addAll(updateAccommodationService.getImages(accommodation));
			} catch (ServiceException e) {
				Messagebox.show(MessageBuilder
						.buildErrorMessage(e.getMessage(), Labels.getLabel("listing.productPhotos")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
				log.error(e.getMessage());
			}
		}
		return images;
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

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
	}

	public ListModelList<AccommodationAttribute> getSelectedAttributes() {
		return selectedAttributes;
	}

	public void setSelectedAttributes(ListModelList<AccommodationAttribute> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

	public Accommodation getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}

	public ListModelList<AccommodationType> getAccommodationTypes() {
		return accommodationTypes;
	}

	public void setAccommodationTypes(ListModelList<AccommodationType> accommodationTypes) {
		this.accommodationTypes = accommodationTypes;
	}

	public ListModelList<AccommodationAttribute> getAvailableAttributes() {
		return availableAttributes;
	}

	public void setAvailableAttributes(ListModelList<AccommodationAttribute> availableAttributes) {
		this.availableAttributes = availableAttributes;
	}
}
