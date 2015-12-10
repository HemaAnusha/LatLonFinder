package com.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pojo.classes.GoogleResponse;
import com.pojo.classes.Result;

public class ExcelDataRead {

	public static void main(String args[]) {
		try {
			readData("C:\\Users\\Anusha\\Downloads\\PointBProviderData.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, List<List<String>>> readData(String filePath) throws Exception {

		File file = new File(filePath);

		Map<String, List<List<String>>> map = new HashMap<String, List<List<String>>>(2);
		System.out.println("Provided file exists: " + file.exists());

		FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Anusha\\Downloads\\lat_long_1.txt");

		try {
			XSSFWorkbook workbook = new XSSFWorkbook(filePath);
			int sheetCount = workbook.getNumberOfSheets();

			if (sheetCount > 0) {
				for (int i = 0; i < 2; i++) {
					System.out.println("=================================\n\n\n\n\n\n");

					map = new HashMap<String, List<List<String>>>(2);
					List<List<String>> lst = new ArrayList<List<String>>();
					XSSFSheet sheet = workbook.getSheetAt(i);
					addData(sheet, lst);
					map.put(sheet.getSheetName(), lst);
					System.out.println(map.toString());
					lst = map.get(sheet.getSheetName());
					for (List<String> list : lst) {
						String location = list.get(16) + "," + list.get(5) + "," + list.get(6);

						System.out.println(sheet.getSheetName() + "\t" + location);
						GoogleResponse res = new AddressConverter().convertToLatLong(location);
						if (res.getStatus().equals("OK")) {
							for (Result result : res.getResults()) {
								System.out.println("Lattitude of address is :" + result.getGeometry().getLocation().getLat());
								System.out.println("Longitude of address is :" + result.getGeometry().getLocation().getLng());
								System.out.println("Location is " + result.getGeometry().getLocation_type());

								fileOutputStream.write((result.getGeometry().getLocation().getLat() + "\t"
												+ result.getGeometry().getLocation().getLng() + "\t" + location + "\n").getBytes());
								fileOutputStream.flush();
							}
						} else {
							System.out.println(res.getStatus());
						}
					}

					fileOutputStream.write(("\n\n\n\n\n" + sheet.getSheetName() + "=====end======\n\n\n\n").getBytes());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	private static void addData(XSSFSheet sheet, List<List<String>> lst) {

		List<String> lstHeaders = new ArrayList<String>();
		Iterator<Row> iterator = sheet.iterator();

		Row row = null;
		int colCount = 18;

		for (; iterator.hasNext();) {
			row = iterator.next();
			// if (row.getRowNum() == 0 || row.getRowNum() == 1) {
			if (row.getRowNum() > 0) {
				// continue; // just skip the rows if row number is 0 or 1
				// } else {
				// if (row.getRowNum() == 2) {
				// for (int i = 0; i < colCount; i++) {
				// lstHeaders.add(row.getCell(i).getStringCellValue().trim());
				// }
				// } else if (row.getRowNum() > 2) {
				List<String> mp = new ArrayList<String>();
				for (int i = 0; i < 18; i++) {

					if (row.getCell(i) != null) {
						if (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC) {
							mp.add((int) row.getCell(i).getNumericCellValue() + "");
						} else {
							mp.add(row.getCell(i).getStringCellValue());
						}
					}

				}
				lst.add(mp);
			}
		}
	}
}
