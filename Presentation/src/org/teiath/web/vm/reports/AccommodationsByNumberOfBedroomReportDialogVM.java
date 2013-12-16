package org.teiath.web.vm.reports;

import org.apache.log4j.Logger;
import org.teiath.web.reports.common.ExcelToolkit;
import org.teiath.web.reports.common.Report;
import org.teiath.web.reports.common.ReportToolkit;
import org.teiath.web.reports.common.ReportType;
import org.teiath.web.session.ZKSession;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("UnusedDeclaration")
public class AccommodationsByNumberOfBedroomReportDialogVM {

	static Logger log = Logger.getLogger(AccommodationsByNumberOfBedroomReportDialogVM.class.getName());

	private Integer numberFrom;
	private Integer numberTo;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void onPrintPDF() {
		if (numberFrom != null && numberTo != null) {
			Report report = ReportToolkit
					.requestAccommodationsByNumberOfBedroomReport(numberFrom, numberTo, ReportType.PDF);
			ZKSession.setAttribute("REPORT", report);
			ZKSession.sendPureRedirect(
					"/reportsServlet?zsessid=" + ZKSession.getCurrentWinID() + "&" + ZKSession.getPWSParams(), "_self");
		} else {
			Messagebox.show("Μη έγκυροι αριθμοί", Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onPrintXLS() {
		if (numberFrom != null && numberTo != null) {
			Report report = ReportToolkit
					.requestAccommodationsByNumberOfBedroomReport(numberFrom, numberTo, ReportType.EXCEL);
			report.setExcelReport(ExcelToolkit.ACCOMMODATIONS_BY_NUMBER_OF_BEDROOMS);
			ZKSession.setAttribute("REPORT", report);
			ZKSession.sendPureRedirect(
					"/reportsServlet?zsessid=" + ZKSession.getCurrentWinID() + "&" + ZKSession.getPWSParams(), "_self");
		} else {
			Messagebox.show("Μη έγκυροι αριθμοί", Labels.getLabel("common.messages.read_title"), Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	@Command
	public void onCancel() {
		//		ZKSession.sendRedirect(PageURL.ROUTE_LIST);
	}

	public Integer getNumberFrom() {
		return numberFrom;
	}

	public void setNumberFrom(Integer numberFrom) {
		this.numberFrom = numberFrom;
	}

	public Integer getNumberTo() {
		return numberTo;
	}

	public void setNumberTo(Integer numberTo) {
		this.numberTo = numberTo;
	}
}
