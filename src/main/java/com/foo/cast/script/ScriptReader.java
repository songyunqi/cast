package com.foo.cast.script;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ScriptReader {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Sheet[] sheets;
    private String fileName;

    public String getFilePath() {
        return filePath;
    }

    private String filePath;

    private Workbook workbook;

    public Workbook getWorkbook() {
        return workbook;
    }

    public ScriptReader(String filePath) {
        boolean isFile = Files.isFile().apply(new File(filePath));
        if (!isFile) {
            System.out.println("文件不存在");
            throw new RuntimeException("文件不存在");
        }
        this.filePath = filePath;
        this.fileName = Files.getNameWithoutExtension(filePath);
        loadScript();
    }

    private void loadScript() {
        try (
                InputStream inputStream = java.nio.file.Files.newInputStream(Paths.get(filePath));
                Workbook workbook = WorkbookFactory.create(inputStream)
        ) {
            this.workbook = workbook;
            int numberOfSheets = workbook.getNumberOfSheets();
            sheets = new Sheet[numberOfSheets];
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                sheets[i] = sheet;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public List<CastLines> getSheetLines(Sheet sheet) {
        int rowNum;
        if (sheet == null) {
            return null;
        }
        List<CastLines> castLinesList = new ArrayList<>(16);
        rowNum = sheet.getLastRowNum();

        for (int i = 0; i <= rowNum; i++) {
            int j = 0;
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(j++);
            String text = getCellValue(cell);
            cell = row.getCell(j);
            String cast = getCellValue(cell);
            if (StringUtils.isEmpty(cast)) {
                cast = "101021";
            }
            CastLines castLines = new CastLines(Integer.valueOf(cast), text);
            castLines.setBookName(this.fileName);
            castLines.setSheetName(sheet.getSheetName());
            castLines.setRowIndex(i + "");
            castLinesList.add(castLines);
        }
        return castLinesList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String result;
        switch (cell.getCellType()) {
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                result = String.valueOf(cell.getErrorCellValue());
                break;
            case FORMULA:
                result = cell.getCellFormula();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = format.format(cell.getDateCellValue());
                } else {
                    result = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case STRING:
                result = cell.getRichStringCellValue().getString();
                break;
            default:
                result = cell.getStringCellValue();
                break;
        }
        return result;
    }
}
