package com.foo.cast.script;

import com.foo.cast.tts.TencentSpeechClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.List;

public class WavGenerator {
    //private final TencentSpeechClient tencentSpeechClient = new TencentSpeechClient();
    private final ScriptReader scriptReader;

    private String scriptFilePath;
    private String targetDir;

    public WavGenerator(String scriptFilePath) {
        scriptReader = new ScriptReader(scriptFilePath);
        this.scriptFilePath = scriptReader.getFilePath();
        this.targetDir = new File(scriptFilePath).getParent();
    }

    public void genWav() {
        Workbook workbook = scriptReader.getWorkbook();
        if (workbook == null) {
            System.out.println("脚本文件不存在");
            return;
        }
        List<CastLines> lines;
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            lines = scriptReader.getSheetLines(workbook.getSheetAt(i));
            TencentSpeechClient tencentSpeechClient = new TencentSpeechClient();
            if (CollectionUtils.isEmpty(lines)) {
                continue;
            }
            for (CastLines line : lines) {
                tencentSpeechClient.tts(line, this.targetDir);
            }
        }
    }

    public static void main(String[] args) {
        new WavGenerator("/Volumes/SD/epub/阅微草堂摘录.xlsx").genWav();
    }
}
