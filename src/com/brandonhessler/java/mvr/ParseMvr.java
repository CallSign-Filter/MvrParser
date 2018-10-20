package com.brandonhessler.java.mvr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by Brandon on 1/8/2016.
 * This will take in the .txt file that is copied and pasted from the .pdf and will output a .xls file
 * containing the useful information
 */
public final class ParseMvr {

    private static final int BUFFER_FROM_TOP = 2;
    private static final int BUFFER_ON_BOTTOM = 2;

    private ParseMvr() {
    }

    public static void parse(File file) {

        String path = file.getAbsolutePath();
        String fileName = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
        String newFileName = path.substring(0, path.lastIndexOf(".")) + ".xlsx";

        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            CreationHelper helper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet();

            LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");

            //TODO This would be nice to do later
//            HashMap<Client, Charge> clientPaymentHashMap = new HashMap<>();
            List<List<String>> fullList = new ArrayList<>();

            /**
             * This parses in all the information form the file that I am using later
             */
            String[] totalLine = new String[4];
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (line.startsWith("Total Orders")) {
                    totalLine = line.split(" ");
                }

                if (line.startsWith("0") || line.startsWith("1")) {
                    String[] arr = line.split(" ");

                    fullList.add(Arrays.asList(arr));
                }
            }

            /**
             * Sorts the List of all transactions based on the 3rd from last value in the array,
             * this is typically the client name. Sometimes it is the last word of the client name
             * in cases where the client name is more than one word  (typically)
             */
            Collections.sort(fullList, new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    if (o1.get(o1.size() - 3) != null && o2.get(o2.size() - 3) != null)
                        return o1.get(o1.size() - 3).compareTo(o2.get(o2.size() - 3));
                    else
                        return 0;
                }
            });

            int longest = 0;
            for (List lineCharge : fullList) {
                if (lineCharge.size() > longest)
                    longest = lineCharge.size();
            }

            createSheet(sheet, fullList.size() + BUFFER_FROM_TOP + BUFFER_ON_BOTTOM, longest + 1);

            markUpTop(wb, sheet, longest, "HIS Parse Results from: " + fileName);

            /**
             * Prints the values in the cells
             */
            for (int i = 0; i < fullList.size(); i++) {
                List<String> lineCharge = fullList.get(i);
                int counter = 0;
                for (int j = lineCharge.size() - 1; j > 0; j--) {
                    String val = lineCharge.get(j);
                    if (j == lineCharge.size() - 1) {
                        sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest - 1 - counter).setCellType(Cell.CELL_TYPE_NUMERIC);
                        sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest - 1 - counter).setCellValue(Double.parseDouble(val));
                    } else {
                        sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest - 1 - counter).setCellValue(val);
                    }
                    counter ++;
                }
            }

            /**
             * Does the math for the individual totals for each client
             */
            double runningTotal  = 0;
            for (int i = 0; i < fullList.size(); i++) {
                List<String> thisLineCharge = fullList.get(i);
                runningTotal += Double.parseDouble(thisLineCharge.get(thisLineCharge.size() - 1));

                if (i != fullList.size() - 1) {
                    List<String> nextLineCharge = fullList.get(i+1);
                    if (thisLineCharge.get(thisLineCharge.size() - 3).equals(nextLineCharge.get(nextLineCharge.size() - 3))) {
                        continue;
                        //+ Double.parseDouble(nextLineCharge.get(nextLineCharge.size() - 1));
                    } else { //print it and zero out the running total
                        sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest).setCellType(Cell.CELL_TYPE_NUMERIC);
                        sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest).setCellValue(runningTotal * -1);
                        runningTotal = 0;
                    }
                } else {
                    sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest).setCellType(Cell.CELL_TYPE_NUMERIC);
                    sheet.getRow(i + BUFFER_FROM_TOP).getCell(longest).setCellValue(runningTotal * -1);
                }
            }
            /**
             * Mark up the bottom with the total
             */
            for (int i = 0; i < totalLine.length; i++) {
                String str = totalLine[i];
                if (i > 1) {
                    sheet.getRow(fullList.size() + BUFFER_FROM_TOP + BUFFER_ON_BOTTOM - 1).getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
                    if (i == totalLine.length - 1)
                        sheet.getRow(fullList.size() + BUFFER_FROM_TOP + BUFFER_ON_BOTTOM -1).getCell(i).setCellValue(Double.parseDouble(str));
                    else
                        sheet.getRow(fullList.size() + BUFFER_FROM_TOP + BUFFER_ON_BOTTOM -1).getCell(i).setCellValue(Integer.parseInt(str));
                }
                sheet.getRow(fullList.size() + BUFFER_FROM_TOP + BUFFER_ON_BOTTOM -1).getCell(i).setCellValue(str);
            }


            FileOutputStream fileOutputStream = new FileOutputStream(newFileName);
            wb.write(fileOutputStream);
            fileOutputStream.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates the excel rows and cells for the size we need
     * @param sheet  The sheet of the workbook we will be using
     * @param rows the number of rows to create
     * @param cells the number of columns in each row
     */
    private static void createSheet(Sheet sheet, int rows, int cells) {
        for (int rowNum = 0; rowNum < rows; rowNum++) {

            Row row =sheet.createRow(rowNum);

            for (int colNum = 0; colNum< cells; colNum++) {
                row.createCell(colNum);
            }
        }
    }

    /**
     * The title of the sheet
     * @param wb The workbook we are using. For some reason this ius needed to justify Center
     * @param sheet The Sheet that we want to mark up
     * @param width How many cells across to make it
     * @param markUp What words you want on top
     */
    private static void markUpTop(Workbook wb, Sheet sheet, int width, String markUp) {
        CellUtil.setAlignment(sheet.getRow(0).getCell(0), wb, CellStyle.ALIGN_CENTER);
        sheet.getRow(0).getCell(0).setCellValue(markUp);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                width  //last column  (0-based)
        ));
    }

}


