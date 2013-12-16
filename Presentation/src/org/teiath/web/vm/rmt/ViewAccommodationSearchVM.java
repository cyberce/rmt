package org.teiath.web.vm.rmt;

import org.apache.log4j.Logger;
import org.teiath.data.domain.image.ApplicationImage;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.ViewAccommodationSearchService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.io.IOException;

public class ViewAccommodationSearchVM
		extends BaseVM {

	static Logger log = Logger.getLogger(ViewAccommodationSearchVM.class.getName());

	@Wire("#accommodationViewSearchWin")
	private Window win;
	@Wire("#petsAllowedLabel")
	private Label objectAllowedLabel;
	@Wire("#smokingAllowedLabel")
	private Label smokingAllowedLabel;
	@Wire("#existingKitchenLabel")
	private Label existingKitchenLabel;
	@Wire("#panelChildren")
	private Panelchildren panelchildren;
	@Wire("#photoVBox")
	private Vbox photoVBox;
	@Wire("#userPhoto")
	private Image userPhoto;

	@WireVariable
	private ViewAccommodationSearchService viewAccommodationSearchService;

	private Accommodation accommodation;
	private ListModelList<ApplicationImage> images;
	private ApplicationImage selectedImage;

	@AfterCompose
	@NotifyChange("accommodation")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		try {
			accommodation = viewAccommodationSearchService
					.getAccommodationById((Integer) ZKSession.getAttribute("accommodationId"));
			if (accommodation.getUser().getApplicationImage() == null) {
				userPhoto.setSrc("/img/default-avatar.png");
			} else {
				AImage aImage = new AImage("", accommodation.getUser().getApplicationImage().getImageBytes());
				userPhoto.setContent(aImage);
			}

			if (accommodation.isPetsAllowed()) {
				objectAllowedLabel.setValue(Labels.getLabel("common.yes"));
			}
			if (accommodation.isSmokingAllowed()) {
				smokingAllowedLabel.setValue(Labels.getLabel("common.yes"));
			}

			if (accommodation.isExistingKitchen()) {
				existingKitchenLabel.setValue(Labels.getLabel("common.yes"));
			}

			if (images == null) {
				images = new ListModelList<>();
				try {
					images.addAll(viewAccommodationSearchService.getImages(accommodation));
					for (ApplicationImage image : images) {
						AImage aImage = new AImage("", image.getImageBytes());
						Image imageComponent = new Image();
						imageComponent.setContent(aImage);
						photoVBox.appendChild(imageComponent);
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
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(e.getMessage(), Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			Messagebox.show(e.getMessage(), Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onBack() {
		ZKSession.sendRedirect(PageURL.SEARCH_ACCOMMODATION);
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
