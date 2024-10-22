package org.example.lc;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class Test {

    private List<String> readPdfTables(String filename, String startMarker, String endMarker) throws Exception {
        PDDocument document = Loader.loadPDF(new File(filename));
        List<String> tableRows = new ArrayList<>();
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
        PageIterator pi = new ObjectExtractor(document).extract();
        boolean endFlag = false;
        while (pi.hasNext()) {
            // iterate over the pages of the document
            Page page = pi.next();
            List<Table> table = sea.extract(page);
            // iterate over the tables of the page
            for (Table tables : table) {
                if (endFlag) {
                    break;
                }
                boolean containsStartMarker = false;
                boolean containsEndMarker = false;
                List<List<RectangularTextContainer>> rows = tables.getRows();
                // iterate over the rows of the table
                for (List<RectangularTextContainer> cells : rows) {
                    String rowStr = cells.stream()
                            .map(item -> item.getText().replace("\r", " ") + "|")
                            .collect(Collectors.joining());
                    if (rowStr.contains(startMarker)) {
                        containsStartMarker = true;
                        containsEndMarker = false;
                    }
                    if (rowStr.contains(endMarker)) {
                        containsStartMarker = false;
                        containsEndMarker = true;
                    }
                    if (containsStartMarker) {
                        tableRows.add(rowStr);
                    }
                    if (containsEndMarker) {
                        tableRows.add(rowStr);
                        endFlag = true;
                        break;
                    }
                }
            }
        }
        return tableRows;
    }

    public static String generatePipeString(int numberOfPipes) {
        if (numberOfPipes <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfPipes; i++) {
            sb.append("-"); // 添加横线
            if (i < numberOfPipes - 1) {
                sb.append("|"); // 添加竖线，除了最后一个
            }
        }

        return sb.toString();
    }

    public static int countOccurrences(String str, String ch) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        // 使用 split 方法分割字符串，并计算分割出的部分数量
        String[] parts = str.split(ch, -1);
        return parts.length - 1; // 出现次数等于分割部分数量减去 1
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Test t = new Test();
        String filePath = "C:\\Users\\Acer\\Desktop\\新员工资料\\3、项目\\1、邮储POC\\迭代资料\\工元致远2021年第一期汽车分期绿色资产证券化信托受托机构报告2022年第7期(总第10期).pdf";
        String startMarker = "利息兑付情况";
        String endMarker = "31,087,874.27|31,087,874.27";
        try {
            List<String> rows = t.readPdfTables(filePath, startMarker, endMarker);
            if (rows.size() > 0) {
                //根据第一行内容，添加markdown表格分隔符，放置到第二行
                String pipeCharacter = "\\|";
                // 统计字符串中 | 的数量
                int count = countOccurrences(rows.get(0), pipeCharacter);
                rows.add(1, generatePipeString(count));
            }
            for (String row : rows) {
                System.out.println(row);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}