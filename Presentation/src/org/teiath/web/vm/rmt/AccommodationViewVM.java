package org.teiath.web.vm.rmt;

import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.AccommodationViewService;
import org.teiath.web.facebook.rmt.FacebookToolKitAccommodations;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.image.AImage;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.io.IOException;
import java.util.Date;

@SuppressWarnings("UnusedDeclaration")
public class AccommodationViewVM
		extends BaseVM {

	static Logger log = Logger.getLogger(AccommodationViewVM.class.getName());

	@Wire("#accommodationViewOwnerWin")
	private Window win;
	@Wire("#empty")
	private org.zkoss.zul.Label empty;
	@Wire("#enableButton")
	private Toolbarbutton enableButton;
	@Wire("#disableButton")
	private Toolbarbutton disableButton;
	@Wire("#smokingLabel")
	private Label smokingLabel;
	@Wire("#petsLabel")
	private Label petsLabel;
	@Wire("#kitchenLabel")
	private Label kitchenLabel;
	@Wire("#facebookShareButton")
	private Toolbarbutton facebookShareButton;
	@Wire("#panelChildren")
	private Panelchildren panelchildren;

	@WireVariable
	private AccommodationViewService accommodationViewService;

	private Accommodation accommodation;
	private ListModelList<ApplicationImage> images;
	private ApplicationImage selectedImage;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		try {

			accommodation = accommodationViewService.getAccommodationByUser(loggedUser);

			if (accommodation == null) {
				ZKSession.sendRedirect(PageURL.ACCOMMODATION_CREATE);
			} else {
				accommodation = accommodationViewService.getAccommodationByUser(loggedUser);

				if (accommodation.isSmokingAllowed()) {
					smokingLabel.setValue(Labels.getLabel("common.yes"));
				}
				if (accommodation.isPetsAllowed()) {
					petsLabel.setValue(Labels.getLabel("common.yes"));
				}
				if (accommodation.isExistingKitchen()) {
					kitchenLabel.setValue(Labels.getLabel("common.yes"));
				}

				if (accommodation.isActive()) {
					facebookShareButton.setDisabled(false);
					disableButton.setDisabled(false);
					enableButton.setDisabled(true);
				}

				if (images == null) {
					images = new ListModelList<>();
					try {
						images.addAll(accommodationViewService.getImages(accommodation));
						for (ApplicationImage image : images) {
							AImage aImage = new AImage("", image.getImageBytes());
							Image imageComponent = new Image();
							imageComponent.setContent(aImage);
							panelchildren.appendChild(imageComponent);
						}
					} catch (ServiceException e) {
						Messagebox.show(MessageBuilder
								.buildErrorMessage(e.getMessage(), Labels.getLabel("listing.productPhotos")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
						log.error(e.getMessage());
					} catch (IOException e) {
						Messagebox.show(MessageBuilder
								.buildErrorMessage(e.getMessage(), Labels.getLabel("listing.productPhotos")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
						log.error(e.getMessage());
					}
				}
			}
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	public void onUpdate() {
		ZKSession.setAttribute("accommodationId", accommodation.getId());
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_UPDATE);
	}

	@Command
	public void onDisable() {
		Messagebox.show(Labels.getLabel("roommates.message.disabelQuestion"),
				Labels.getLabel("common.messages.inactivate_title"), Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event evt) {
				switch ((Integer) evt.getData()) {
					case Messagebox.YES:

						try {
							if (accommodation.isActive() == true) {
								accommodation.setActive(false);
								accommodationViewService.disableAccommodation(accommodation);
								Messagebox.show(Labels.getLabel("roommates.message.disabelConfirmation"),
										Labels.getLabel("common.messages.confirm"), Messagebox.OK,
										Messagebox.INFORMATION, new EventListener<Event>() {
									public void onEvent(Event evt) {
										ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
									}
								});
							} else {
								Messagebox.show(Labels.getLabel("roommates.messages.disabled"),
										Labels.getLabel("common.messages.save_error"), Messagebox.OK, Messagebox.ERROR,
										new EventListener<Event>() {
											public void onEvent(Event evt) {
												switch ((Integer) evt.getData()) {
													case Messagebox.OK:
														ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
												}
											}
										});
							}
						} catch (ServiceException e) {
							log.error(e.getMessage());
							Messagebox.show(MessageBuilder
									.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home")),
									Labels.getLabel("common.messages.delete_title"), Messagebox.OK, Messagebox.ERROR);
							ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
						}

					case Messagebox.NO:
						break;
				}
			}
		});
	}

	@Command
	public void onEnable() {
		Messagebox.show(Labels.getLabel("roommates.message.enableQuestion"),
				Labels.getLabel("common.messages.activate_title"), Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, new EventListener<Event>() {
			public void onEvent(Event evt) {
				switch ((Integer) evt.getData()) {
					case Messagebox.YES:
						try {
							if (accommodation.isActive() == false) {
								accommodation.setActive(true);
								accommodation.setAccommodationPublishDate(new Date());
								accommodationViewService.enableAccommodation(accommodation);
								Messagebox.show(Labels.getLabel("roommates.message.enableConfirmation"),
										Labels.getLabel("common.messages.confirm"), Messagebox.OK,
										Messagebox.INFORMATION, new EventListener<Event>() {
									public void onEvent(Event evt) {
										ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
									}
								});
							} else {
								Messagebox.show(Labels.getLabel("roommates.messages.already_enabled"),
										Labels.getLabel("common.messages.save_error"), Messagebox.OK, Messagebox.ERROR,
										new EventListener<Event>() {
											public void onEvent(Event evt) {
												switch ((Integer) evt.getData()) {
													case Messagebox.OK:
														ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
												}
											}
										});
							}
						} catch (ServiceException e) {
							log.error(e.getMessage());
							Messagebox.show(MessageBuilder
									.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home")),
									Labels.getLabel("common.messages.delete_title"), Messagebox.OK, Messagebox.ERROR);
							ZKSession.sendRedirect(PageURL.ACCOMMODATION_LIST);
						}

					case Messagebox.NO:
						break;
				}
			}
		});
	}

	@Command
	public void facebookShare() {
		if (accommodation != null) {
			//stelnoume to id ws state wste na mas stalei pisw apo to facebook
			ZKSession.sendPureRedirect(
					FacebookToolKitAccommodations.getLoginRedirectURL() + "&state=" + accommodation.getId());
		}
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public Accommodation getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}

	public ApplicationImage getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(ApplicationImage selectedImage) {
		this.selectedImage = selectedImage;
	}
}
