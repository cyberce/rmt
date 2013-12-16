package org.teiath.web.reports.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.teiath.data.domain.rmt.Accommodation;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.rmt.AccommodationsExcelService;
import org.teiath.web.reports.common.ExcelToolkit;

import java.util.Collection;

public class AccommodationsByNumberOfBedroomsReport {

	public HSSFWorkbook exportList(AccommodationsExcelService accommodationsExcelService, Integer numberFrom,
	                               Integer numberTo) {
		HSSFWorkbook wb = new HSSFWorkbook();
		int rowDataIndex = 1;
		int rowIndex = 1;

		HSSFSheet sheet = wb.createSheet("Φοιτητικές Στέγες ανά αριθμό δωματίων");

		HSSFRow row = sheet.createRow((short) 0);
		row.setHeightInPoints((float) 25);

		HSSFFont font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 9);

		ExcelToolkit excelToolkit = new ExcelToolkit();

		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 0, "A/A", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, false, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 1, "Διεύθυνση", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, false, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 2, "Όροφος", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, false, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 3, "Τετραγωνικά μέτρα", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit
				.createCell(ExcelToolkit.GENERIC, wb, row, font, 4, "Αριθμός υπνοδωματίων", HSSFCellStyle.ALIGN_CENTER,
						HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 5, "Τύπος θέρμανσης", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 6, "Έτος κατασκευής", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.GREY_25_PERCENT.index);
		excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 7, "Ιδιοκτήτης", HSSFCellStyle.ALIGN_CENTER,
				HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.GREY_25_PERCENT.index);

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 15000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 5000);

		font = wb.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);

		try {
			Collection<Accommodation> accommodations = accommodationsExcelService
					.getAccommodationsByNumberOfBedrooms(numberFrom, numberTo);
			for (Accommodation accommodation : accommodations) {
				row = sheet.createRow((short) rowDataIndex++);
				excelToolkit
						.createCell(ExcelToolkit.GENERIC, wb, row, font, 0, rowIndex++ + "", HSSFCellStyle.ALIGN_CENTER,
								HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 1, accommodation.getAddress(),
						HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit
						.createCell(ExcelToolkit.GENERIC, wb, row, font, 2, Integer.toString(accommodation.getFloor()),
								HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, true, true,
								HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 3,
						Integer.toString(accommodation.getSquareMeters()), HSSFCellStyle.ALIGN_LEFT,
						HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 4,
						Integer.toString(accommodation.getNumberOfBedrooms()), HSSFCellStyle.ALIGN_CENTER,
						HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 5, accommodation.getHeatingType(),
						HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 6,
						accommodation.getConstructionYear().toString(), HSSFCellStyle.ALIGN_LEFT,
						HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
				excelToolkit.createCell(ExcelToolkit.GENERIC, wb, row, font, 7, accommodation.getUser().getFullName(),
						HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, true, true, HSSFColor.WHITE.index);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return wb;
	}
}
