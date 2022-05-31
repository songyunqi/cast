package com.foo.cast.tts;

import com.foo.cast.script.CastLines;
import com.google.common.io.Files;
import com.tencent.tts.model.SpeechSynthesisResponse;
import com.tencent.tts.service.SpeechSynthesisListener;
import com.tencent.tts.utils.Ttsutils;

import java.io.File;


public class ConcreteSpeechSynthesisListener extends SpeechSynthesisListener {

    //private static final String CODEC = "pcm";
    private static final int SAMPLE_RATE = 16000;

    private CastLines castLines;

    private String targetDir;

    public ConcreteSpeechSynthesisListener(CastLines castLines, String targetDir) {
        this.castLines = castLines;
        this.targetDir = targetDir;
    }

    @Override
    public void onComplete(SpeechSynthesisResponse response) {
        if (!response.getSuccess()) {
            return;
        }
        String filePath = Ttsutils.responsePcm2Wav(SAMPLE_RATE, response.getAudio(), response.getSessionId());
        //重命名文件
        File file = new File(filePath);
        String bookName = this.castLines.getBookName();
        String sheetName = this.castLines.getSheetName();
        String rowIndex = this.castLines.getRowIndex();
        //文件重命名
        String nameWithoutExtension = Files.getNameWithoutExtension(filePath);
        //目标目录+文件名重命名
        String newFilePath = this.targetDir + "/" + file.getName().replace(nameWithoutExtension, bookName + "-" + sheetName + "-" + rowIndex);
        file.renameTo(new File(newFilePath));
    }

    @Override
    public void onMessage(byte[] bytes) {

    }

    @Override
    public void onFail(SpeechSynthesisResponse response) {
        //第几个sheet,第几行报错
        System.out.println("tts 出错>>>>");
        System.out.println(this.castLines.getSheetName() + "-" + this.castLines.getRowIndex());
        System.out.println(response.getMessage());
        System.out.println("<<<<tts 出错");
    }
}
