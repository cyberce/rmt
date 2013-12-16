package org.teiath.web.vm.user;

import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.AccommodationAttribute;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.DeleteViolationException;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.values.ListValuesService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class ListValuesVM
		extends BaseVM {

	static Logger log = Logger.getLogger(ListValuesVM.class.getName());

	@WireVariable
	private ListValuesService listValuesService;

	private User user;
	private ListModelList<AccommodationAttribute> accommodationAttributes;
	private AccommodationAttribute selectedAccommodationAttribute;
	private ListModelList<AccommodationType> accommodationTypes;
	private AccommodationType selectedAccommodationType;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void onCreateActionCategories() {
		ZKSession.sendRedirect(PageURL.EVENT_CATEGORY_CREATE);
	}

	@Command
	public void onCreateAccommodationAttributes() {
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_ATTRIBUTE_CREATE);
	}

	@Command
	public void onCreateAccommodationTypes() {
		ZKSession.sendRedirect(PageURL.ACCOMMODATION_TYPE_CREATE);
	}

	@Command
	public void onCreateProductCategories() {
		ZKSession.sendRedirect(PageURL.PRODUCT_CATEGORY_CREATE);
	}

	@Command
	public void onCreateProductStatuses() {
		ZKSession.sendRedirect(PageURL.PRODUCT_STATUS_CREATE);
	}

	@Command
	public void onCreateTransactionTypes() {
		ZKSession.sendRedirect(PageURL.TRANSACTION_TYPE__CREATE);
	}

	@Command
	public void onEditAccommodationAttributes() {
		if (selectedAccommodationAttribute != null) {
			ZKSession.setAttribute("accommodationAttributeId", selectedAccommodationAttribute.getId());
			ZKSession.sendRedirect(PageURL.ACCOMMODATION_ATTRIBUTE_EDIT);
		}
	}

	@Command
	public void onEditAccommodationTypes() {
		if (selectedAccommodationType != null) {
			ZKSession.setAttribute("accommodationTypeId", selectedAccommodationType.getId());
			ZKSession.sendRedirect(PageURL.ACCOMMODATION_TYPE_EDIT);
		}
	}

	@Command
	public void onDeleteAccommodationAttributes() {
		if (selectedAccommodationAttribute != null) {
			Messagebox.show(Labels.getLabel("value.message.deleteQuestion"),
					Labels.getLabel("common.messages.delete_title"), Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener<Event>() {
				public void onEvent(Event evt) {
					switch ((Integer) evt.getData()) {
						case Messagebox.YES:
							try {
								listValuesService.deleteAccommodationAttribute(selectedAccommodationAttribute);
								Messagebox.show(Labels.getLabel("value.message.deleteConfirmation"),
										Labels.getLabel("common.messages.confirm"), Messagebox.OK,
										Messagebox.INFORMATION, new EventListener<Event>() {
									public void onEvent(Event evt) {
										ZKSession.sendRedirect(PageURL.VALUES_LIST);
									}
								});
							} catch (DeleteViolationException e) {
								Messagebox.show(MessageBuilder
										.buildErrorMessage(e.getMessage(), Labels.getLabel("accommodation.attributes")),
										Labels.getLabel("common.messages.delete_title"), Messagebox.OK,
										Messagebox.ERROR);
							} catch (ServiceException e) {
								log.error(e.getMessage());
								Messagebox.show(MessageBuilder
										.buildErrorMessage(e.getMessage(), Labels.getLabel("accommodation.attributes")),
										Labels.getLabel("common.messages.delete_title"), Messagebox.OK,
										Messagebox.ERROR);
							}
							break;
						case Messagebox.NO:
							break;
					}
				}
			});
		}
	}

	@Command
	public void onDeleteAccommodationTypes() {
		if (selectedAccommodationType != null) {
			Messagebox.show(Labels.getLabel("value.message.deleteQuestion"),
					Labels.getLabel("common.messages.delete_title"), Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener<Event>() {
				public void onEvent(Event evt) {
					switch ((Integer) evt.getData()) {
						case Messagebox.YES:
							try {
								listValuesService.deleteAccommodationType(selectedAccommodationType);
								Messagebox.show(Labels.getLabel("value.message.deleteConfirmation"),
										Labels.getLabel("common.messages.confirm"), Messagebox.OK,
										Messagebox.INFORMATION, new EventListener<Event>() {
									public void onEvent(Event evt) {
										ZKSession.sendRedirect(PageURL.VALUES_LIST);
									}
								});
							} catch (DeleteViolationException e) {
								Messagebox.show(MessageBuilder
										.buildErrorMessage(e.getMessage(), Labels.getLabel("accommodation.types")),
										Labels.getLabel("common.messages.delete_title"), Messagebox.OK,
										Messagebox.ERROR);
							} catch (ServiceException e) {
								log.error(e.getMessage());
								Messagebox.show(MessageBuilder
										.buildErrorMessage(e.getMessage(), Labels.getLabel("accommodation.types")),
										Labels.getLabel("common.messages.delete_title"), Messagebox.OK,
										Messagebox.ERROR);
							}
							break;
						case Messagebox.NO:
							break;
					}
				}
			});
		}
	}

	public ListModelList<AccommodationAttribute> getAccommodationAttributes() {
		if (accommodationAttributes == null) {
			accommodationAttributes = new ListModelList<>();
			try {
				accommodationAttributes.addAll(listValuesService.getAccommodationAttributes());
			} catch (ServiceException e) {
				log.error(e.getMessage());
				Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("action.theme")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		}

		return accommodationAttributes;
	}

	public ListModelList<AccommodationType> getAccommodationTypes() {
		if (accommodationTypes == null) {
			accommodationTypes = new ListModelList<>();
			try {
				accommodationTypes.addAll(listValuesService.getAccommodationTypes());
			} catch (ServiceException e) {
				log.error(e.getMessage());
				Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("action.theme")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		}

		return accommodationTypes;
	}


	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public AccommodationAttribute getSelectedAccommodationAttribute() {
		return selectedAccommodationAttribute;
	}

	public void setSelectedAccommodationAttribute(AccommodationAttribute selectedAccommodationAttribute) {
		this.selectedAccommodationAttribute = selectedAccommodationAttribute;
	}

	public AccommodationType getSelectedAccommodationType() {
		return selectedAccommodationType;
	}

	public void setSelectedAccommodationType(AccommodationType selectedAccommodationType) {
		this.selectedAccommodationType = selectedAccommodationType;
	}
}
