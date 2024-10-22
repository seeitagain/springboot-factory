package org.example.lc;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.ParagraphRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkdownToWordExample {

    public static void main(String[] args) throws Exception {
        // Markdown to HTML
        String markdown = "# Hello World\nThis is **Markdown** content!";
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        String html = renderer.render(parser.parse(markdown));
        System.out.println(html);


        String filePath = "C:\\project\\git-repo\\springboot-demo\\springboot-poi-tl\\template.docx";


//        // 使用 Jsoup 解析 HTML 并创建 poi-tl 数据
//        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(html);
//        Elements elements = htmlDoc.body().children();
//
//        List<Object> contents = new ArrayList<>();
//        for (Element element : elements) {
//            switch (element.tagName()) {
//                case "h1":
//                case "h2":
//                case "h3":
//                case "h4":
//                case "h5":
//                case "h6":
//                    contents.add(new ParagraphRenderData(
//                            TextRenderData.builder()
//                                    .text(element.text())
//                                    .bold(true)
//                                    .fontSize(24 - 2 * Integer.parseInt(element.tagName().substring(1)))
//                                    .build()));
//                    break;
//                case "p":
//                    contents.add(new ParagraphRenderData(TextRenderData.builder().text(element.text()).build()));
//                    break;
//                case "ul":
//                case "ol":
//                    for (Element li : element.children()) {
//                        contents.add(new ParagraphRenderData(TextRenderData.builder().text("• " + li.text()).build()));
//                    }
//                    break;
//                case "code":
//                    contents.add(new ParagraphRenderData(new TextRenderData(element.text())));
//                    break;
//                // 添加其他 HTML 元素的处理
//            }
//        }
//
//        // 渲染到 Word 模板
//        Map<String, Object> data = new HashMap<>();
//        data.put("content", new DocxRenderData(contents));
//
//        XWPFTemplate template = XWPFTemplate.compile(filePath).render(data);
//
//        // 输出 Word 文件
//        FileOutputStream out = new FileOutputStream("output.docx");
//        template.write(out);
//        out.close();
//        template.close();
//    }
    }
}