package org.teiath.web.reports.common;

import org.teiath.data.domain.rmt.AccommodationType;

import java.util.Date;

public class ReportToolkit {

	public static Report requestCreatedRoutesReport(Date dateFrom, Date dateTo, int reportType) {
		Report report = new Report();
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("createdRoutes" + findFileExtension(reportType));
		report.setReportLocation("/reports/routes/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("createdRoutesReport.jasper");

		return report;
	}

	public static Report requestCompletedRoutesReport(Date dateFrom, Date dateTo, int reportType) {
		Report report = new Report();
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("completedRoutes" + findFileExtension(reportType));
		report.setReportLocation("/reports/routes/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("completedRoutesReport.jasper");

		return report;
	}

	public static Report requestCreatedListingsReport(Date dateFrom, Date dateTo, int reportType) {
		Report report = new Report();
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("createdListings" + findFileExtension(reportType));
		report.setReportLocation("/reports/listings/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("createdListingsReport.jasper");

		return report;
	}

	public static Report requestListingsTransactionTypeReport(int transactionTypeId, int reportType) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("ListingsTransactionTypeReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/listings/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("ListingsTransactionTypeReport.jasper");
		report.getParameters().put("TRANSACTION_TYPE_ID", transactionTypeId);

		return report;
	}

	public static Report requestListingsProductCategoryReport(int productCategoryId, int reportType) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("ListingsProductCategoryReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/listings/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("ListingsProductCategoryReport.jasper");
		report.getParameters().put("PRODUCT_CATEGORY_ID", productCategoryId);

		return report;
	}

	public static Report requestOnGoinfActionsReport(Date dateFrom, Date dateTo, int reportType) {
		Report report = new Report();
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("onGoingActionsReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/actions/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("onGoingActionsReport.jasper");

		return report;
	}

	public static Report requestActionsByCategoryReport(int eventCategoryId, int reportType) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("actionsByCategoryReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/actions/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("actionsByCategoryReport.jasper");
		report.getParameters().put("EVENT_CATEGORY_ID", eventCategoryId);

		return report;
	}

	public static Report requestTransactionsReport(Date dateFrom, Date dateTo, int reportType) {
		Report report = new Report();
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("TransactionsReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/listings/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("TransactionsReport.jasper");

		return report;
	}

	public static Report requestAccommodationsByAccommodationTypeReport(int accommodationTypeId, int reportType) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("AccommodationsByAccommodationTypeReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/rmt/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("AccommodationsByAccommodationTypeReport.jasper");
		report.getParameters().put("ACCOMMODATION_TYPE_ID", accommodationTypeId);

		return report;
	}

	public static Report requestAccommodationsByAccommodationTypeReportXLS(AccommodationType accommodationType,
	                                                                       int reportType) {
		Report report = new Report();
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("AccommodationsByAccommodationTypeReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/rmt/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("AccommodationsByAccommodationTypeReport.jasper");
		report.getParameters().put("ACCOMMODATION_TYPE", accommodationType);

		return report;
	}

	public static Report requestAccommodationsByNumberOfBedroomReport(Integer numberFrom, Integer numberTo,
	                                                                  int reportType) {
		Report report = new Report();
		report.setNumberFrom(numberFrom);
		report.setNumberTo(numberTo);
		report.setReportType(reportType);
		report.setDisplayName("");
		report.setOutputFileName("AccommodationsByNumberOfBedroomsReport" + findFileExtension(reportType));
		report.setReportLocation("/reports/rmt/");
		report.setImagesLocation("/img/");
		report.setRootReportFile("AccommodationsByNumberOfBedroomsReport.jasper");

		return report;
	}

	private static String findFileExtension(int reportType) {
		switch (reportType) {
			case ReportType.PDF:
				return ".pdf";
			case ReportType.EXCEL:
				return ".xls";
		}

		return null;
	}
}
