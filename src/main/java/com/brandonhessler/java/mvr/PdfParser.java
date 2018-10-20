package com.brandonhessler.java.mvr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PdfParser {

    public PdfParser() {
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


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not iterate lines in file");
        }
    }
}
