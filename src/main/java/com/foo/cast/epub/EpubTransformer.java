package com.foo.cast.epub;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpubTransformer {
    private static final String DIR = "/Volumes/SD/epub_script/";

    private Metadata metadata;
    private List<Resource> contents;

    public EpubTransformer(String epubBook) {
        File file = new File(epubBook);
        EpubReader reader;
        Book book;
        try (InputStream in = new FileInputStream(file)) {
            reader = new EpubReader();
            book = reader.readEpub(in);
            this.metadata = book.getMetadata();
            this.contents = book.getContents();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //获取资源内容
    public Document getDocument(Resource resource) {
        String content = null;
        try {
            content = new String(resource.getData(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = null;
        if (null != content) {
            document = Jsoup.parse(content);
        }
        return document;
    }

    //生成脚本
    public String genScript() throws IOException {
        //判断目标目录是否存在
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String title = metadata.getFirstTitle();
        StringBuilder authors = new StringBuilder();
        int authorCount = 0;
        if (metadata.getAuthors() != null) {
            authorCount = metadata.getAuthors().size();
        }
        for (int i = 0; i < authorCount; i++) {
            Author author = metadata.getAuthors().get(i);
            String tmpName = author.toString().replace(",", "").trim();
            if (i < authorCount - 1) {
                authors.append(tmpName).append(",");
            } else {
                authors.append(tmpName);
            }
        }
        try (
                OutputStream out = new FileOutputStream(DIR + title + "[" + authors + "]" + ".xlsx");
                Workbook workbook = WorkbookFactory.create(true)
        ) {
            int size = 0;
            if (contents != null) {
                size = contents.size();
            }
            for (int i = 0; i < size; i++) {
                Resource resource = contents.get(i);
                String titleName = resource.getTitle();
                titleName = titleName == null ? "page-" + i : titleName;
                Sheet sheet = workbook.createSheet(titleName);
                Document document = getDocument(resource);
                Elements elements = null;
                //有段落就按段落输出
                if (document != null) {
                    elements = document.getElementsByTag("p");
                }
                if (CollectionUtils.isEmpty(elements)) {
                    sheet.createRow(0).createCell(0).setCellValue(document.text());
                } else {
                    for (int j = 0; j < elements.size(); j++) {
                        String text = elements.get(j).text();
                        sheet.createRow(j).createCell(0).setCellValue(text);
                    }
                }
            }
            workbook.write(out);
            return DIR + title + "[" + authors + "]" + ".xlsx";
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
