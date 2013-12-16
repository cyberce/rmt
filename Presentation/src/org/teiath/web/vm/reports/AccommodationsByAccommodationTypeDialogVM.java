package org.teiath.web.vm.reports;

import org.apache.log4j.Logger;
import org.teiath.data.domain.rmt.AccommodationType;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.AccommodationsByAccommodationTypeDialogService;
import org.teiath.web.reports.common.ExcelToolkit;
import org.teiath.web.reports.common.Report;
import org.teiath.web.reports.common.ReportToolkit;
import org.teiath.web.reports.common.ReportType;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class AccommodationsByAccommodationTypeDialogVM {

	static Logger log = Logger.getLogger(AccommodationsByAccommodationTypeDialogVM.class.getName());

	@WireVariable
	private AccommodationsByAccommodationTypeDialogService accommodationsByAccommodationTypeDialogService;

	private ListModelList<AccommodationType> accommodationTypes;
	private AccommodationType selectedAccommodationType;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void onPrintPDF() {
		if (selectedAccommodationType != null) {
			Report report = ReportToolkit
					.requestAccommodationsByAccommodationTypeReport(selectedAccommodationType.getId(), ReportType.PDF);
			ZKSession.setAttribute("REPORT", report);
			ZKSession.sendPureRedirect(
					"/reportsServlet?zsessid=" + ZKSession.getCurrentWinID() + "&" + ZKSession.getPWSParams(), "_self");
		} else {
			Messagebox.show("Μη έγκυρος τύπος κατοικίας", Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onPrintXLS() {
		if (selectedAccommodationType != null) {
			Report report = ReportToolkit
					.requestAccommodationsByAccommodationTypeReport(selectedAccommodationType.getId(),
							ReportType.EXCEL);
			report.setExcelReport(ExcelToolkit.ACCOMMODATIONS_BY_ACCOMMODATION_TYPE);
			ZKSession.setAttribute("REPORT", report);
			ZKSession.sendPureRedirect(
					"/reportsServlet?zsessid=" + ZKSession.getCurrentWinID() + "&" + ZKSession.getPWSParams(), "_self");
		} else {
			Messagebox.show("Μη έγκυρη ημερομηνία", Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onCancel() {
		//		ZKSession.sendRedirect(PageURL.ROUTE_LIST);
	}

	public ListModelList<AccommodationType> getAccommodationTypes() {
		if (accommodationTypes == null) {
			accommodationTypes = new ListModelList<>();
			selectedAccommodationType = new AccommodationType();
			selectedAccommodationType.setId(- 1);
			selectedAccommodationType.setName("");
			accommodationTypes.add(selectedAccommodationType);
			try {
				accommodationTypes.addAll(accommodationsByAccommodationTypeDialogService.getAccommodationTypes());
			} catch (ServiceException e) {
				log.error(e.getMessage());
				Messagebox
						.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("roommates.home.type")),
								Labels.getLabel("common.messages.read_title"), Messagebox.OK, Messagebox.ERROR);
			}
		}

		return accommodationTypes;
	}

	public AccommodationType getSelectedAccommodationType() {
		return selectedAccommodationType;
	}

	public void setSelectedAccommodationType(AccommodationType selectedAccommodationType) {
		this.selectedAccommodationType = selectedAccommodationType;
	}
}
