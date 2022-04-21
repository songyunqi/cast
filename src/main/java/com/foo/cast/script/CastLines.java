package com.foo.cast.script;

public class CastLines {
    //
    private Integer cast;
    private String lines;

    private String bookName;

    private String sheetName;
    private String rowIndex;

    public CastLines(Integer cast, String lines) {
        this.cast = cast;
        this.lines = lines;
    }

    public Integer getCast() {
        return cast;
    }

    public void setCast(Integer cast) {
        this.cast = cast;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }
}
