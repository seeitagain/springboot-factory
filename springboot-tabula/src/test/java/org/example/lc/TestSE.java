package org.example.lc;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TestSE {
    public static void main(String[] args) {
        String pdfPath = "C:\\Users\\Acer\\Desktop\\新员工资料\\3、项目\\1、邮储POC\\迭代资料\\工元致远2021年第一期汽车分期绿色资产证券化信托受托机构报告2022年第7期(总第10期).pdf";
        String startMarker = "";
        String endMarker = "";

        try {
            List<Table> extractedTables = extractTablesWithMarkers(pdfPath, startMarker, endMarker);
            // 输出提取到的表格
            for (Table table : extractedTables) {
                System.out.println(table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Table> extractTablesWithMarkers(String pdfPath, String startMarker, String endMarker) throws IOException {
        List<Table> result = new ArrayList<>();
        boolean inRange = false;

        PDDocument document = Loader.loadPDF(new File(pdfPath));
        ObjectExtractor extractor = new ObjectExtractor(document);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            Page page = extractor.extract(i);
            List<Table> tables = sea.extract(page);

            for (Table table : tables) {
                boolean containsStartMarker = false;
                boolean containsEndMarker = false;
                for (List<RectangularTextContainer> row : table.getRows()) {


                    String rowStr = row.stream()
                            .map(RectangularTextContainer::getText)
                            .reduce("", (acc, text) -> acc + " " + text);

                    if (rowStr.contains(startMarker)) {
                        inRange = true;
                        containsStartMarker = true;
                    }

                    if (rowStr.contains(endMarker)) {
                        inRange = false;
                        containsEndMarker = true;
                    }

                    if (inRange || containsStartMarker || containsEndMarker) {
                        result.add(table);
                        break;
                    }
                }
            }
        }

        document.close();
        return result;
    }

//    private void readPdfTables(String filename) throws Exception {
//        PDDocument document = Loader.loadPDF(new File(filename));
//        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
//        PageIterator pi = new ObjectExtractor(document).extract();
//        while (pi.hasNext()) {
//            // iterate over the pages of the document
//            Page page = pi.next();
//            List<Table> table = sea.extract(page);
//            // iterate over the tables of the page
//            for (Table tables : table) {
//                List<List<RectangularTextContainer>> rows = tables.getRows();
//                // iterate over the rows of the table
//                for (List<RectangularTextContainer> cells : rows) {
//                    // print all column-cells of the row plus linefeed
//                    for (RectangularTextContainer content : cells) {
//                        // Note: Cell.getText() uses \r to concat text chunks
//                        String text = content.getText().replace("\r", " ");
//                        System.out.print(text + "|");
//                    }
//                    System.out.println();
//                }
//            }
//        }
//    }

//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        TestSE t = new TestSE();
//        try {
//            t.readPdfTables("C:\\Users\\Acer\\Desktop\\新员工资料\\3、项目\\1、邮储POC\\迭代资料\\工元致远2021年第一期汽车分期绿色资产证券化信托受托机构报告2022年第7期(总第10期).pdf");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}