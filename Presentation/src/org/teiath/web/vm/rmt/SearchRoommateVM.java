package org.teiath.web.vm.rmt;

import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.data.paging.SearchResults;
import org.teiath.data.search.AccommodationSearchCriteria;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.SearchAccommodationService;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.Collection;

public class SearchRoommateVM
		extends BaseVM {

	static Logger log = Logger.getLogger(SearchRoommateVM.class.getName());

	@Wire("#paging")
	private Paging paging;
	@Wire("#accommodationListbox")
	private Listbox accommodationListbox;
	@Wire("#toolbar")
	private Div toolbar;
	@Wire("#contributionAmountIntbox")
	private Intbox contributionAmountIntbox;
	@Wire("#petsAllowedRG")
	private Radiogroup petsAllowedRG;
	@Wire("#smokingAllowedRG")
	private Radiogroup smokingAllowedRG;
	@Wire("#floorCombo")
	private Combobox floorCombo;

	@WireVariable
	private SearchAccommodationService searchAccommodationService;

	private AccommodationSearchCriteria accommodationSearchCriteria;
	private ListModelList<Accommodation> accommodationList;
	private ListModelList<AccommodationType> accommodationType;
	private AccommodationType selectedAccommodationType;
	private Accommodation selectedAccommodation;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		//Initial search criteria
		accommodationSearchCriteria = new AccommodationSearchCriteria();
		accommodationSearchCriteria.setPageSize(paging.getPageSize());
		accommodationSearchCriteria.setPageNumber(0);
		accommodationList = new ListModelList<>();
	}

	@Command
	@NotifyChange("selectedAccommodation")
	public void onSearch() {
		selectedAccommodation = null;
		accommodationList.clear();
		accommodationSearchCriteria.setPageNumber(0);
		accommodationSearchCriteria.setPageSize(paging.getPageSize());
		accommodationSearchCriteria.setActive(true);
		accommodationSearchCriteria.setAccommodationType(selectedAccommodationType);

		try {
			SearchResults<Accommodation> results = searchAccommodationService
					.searchAccommodation(accommodationSearchCriteria);
			Collection<Accommodation> accommodations = results.getData();
			accommodationList.addAll(accommodations);
			if (results.getTotalRecords() != 0) {
				accommodationListbox.setVisible(true);
				paging.setVisible(true);
				toolbar.setVisible(true);
			} else {
				accommodationListbox.setVisible(false);
				paging.setVisible(false);
				toolbar.setVisible(false);
				Messagebox.show(MessageBuilder
						.buildErrorMessage(Labels.getLabel("roommates.notFound"), Labels.getLabel("roommates")),
						Labels.getLabel("common.messages.search"), Messagebox.OK, Messagebox.INFORMATION);
			}
			paging.setTotalSize(results.getTotalRecords());
			paging.setActivePage(accommodationSearchCriteria.getPageNumber());
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	@NotifyChange({"selectedAccommodationType", "selectedAccommodation", "accommodationSearchCriteria"})
	public void onResetSearch() {

		floorCombo.setSelectedItem(null);
		selectedAccommodationType = accommodationType.get(0);
		contributionAmountIntbox.setRawValue(null);
		petsAllowedRG.setSelectedItem(null);
		smokingAllowedRG.setSelectedItem(null);
	}

	@Command
	public void onView() {
		if (selectedAccommodation != null) {
			ZKSession.setAttribute("accommodationId", selectedAccommodation.getId());
			ZKSession.sendRedirect(PageURL.SEARCH_ACCOMMODATION_VIEW);
		}
	}

	@Command
	@NotifyChange("selectedAccommodation")
	public void onSort(BindContext ctx) {
		Event event = ctx.getTriggerEvent();
		Listheader listheader = (Listheader) event.getTarget();

		accommodationSearchCriteria.setOrderField(listheader.getId());
		accommodationSearchCriteria.setOrderDirection(listheader.getSortDirection());
		accommodationSearchCriteria.setPageNumber(0);
		selectedAccommodation = null;
		accommodationList.clear();

		try {
			SearchResults<Accommodation> results = searchAccommodationService
					.searchAccommodation(accommodationSearchCriteria);
			Collection<Accommodation> accommodations = results.getData();
			accommodationList.addAll(accommodations);
			paging.setTotalSize(results.getTotalRecords());
			paging.setActivePage(accommodationSearchCriteria.getPageNumber());
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
					Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	@NotifyChange("selectedAccommodation")
	public void onPaging() {
		if (accommodationList != null) {
			accommodationSearchCriteria.setPageNumber(paging.getActivePage());
			try {
				SearchResults<Accommodation> results = searchAccommodationService
						.searchAccommodation(accommodationSearchCriteria);
				selectedAccommodation = null;
				accommodationList.clear();
				accommodationList.addAll(results.getData());
				paging.setTotalSize(results.getTotalRecords());
				paging.setActivePage(accommodationSearchCriteria.getPageNumber());
			} catch (ServiceException e) {
				Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates")),
						Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
				log.error(e.getMessage());
			}
		}
	}

	public ListModelList<AccommodationType> getAccommodationType() {
		if (accommodationType == null) {
			accommodationType = new ListModelList<>();
			selectedAccommodationType = new AccommodationType();
			selectedAccommodationType.setId(- 1);
			selectedAccommodationType.setName("");
			accommodationType.add(selectedAccommodationType);
			try {
				accommodationType.addAll(searchAccommodationService.getAccommodationType());
			} catch (ServiceException e) {
				Messagebox
						.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home.type")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
				log.error(e.getMessage());
			}
		}

		return accommodationType;
	}

	public AccommodationType getSelectedAccommodationType() {
		return selectedAccommodationType;
	}

	public void setSelectedAccommodationType(AccommodationType selectedAccommodationType) {
		this.selectedAccommodationType = selectedAccommodationType;
	}

	public Accommodation getSelectedAccommodation() {
		return selectedAccommodation;
	}

	public void setSelectedAccommodation(Accommodation selectedAccommodation) {
		this.selectedAccommodation = selectedAccommodation;
	}

	public ListModelList<Accommodation> getAccommodationList() {
		return accommodationList;
	}

	public void setAccommodationList(ListModelList<Accommodation> accommodationList) {
		this.accommodationList = accommodationList;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public AccommodationSearchCriteria getAccommodationSearchCriteria() {
		return accommodationSearchCriteria;
	}

	public void setAccommodationSearchCriteria(AccommodationSearchCriteria accommodationSearchCriteria) {
		this.accommodationSearchCriteria = accommodationSearchCriteria;
	}
}
