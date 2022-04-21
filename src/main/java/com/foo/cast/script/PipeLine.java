package com.foo.cast.script;

import com.foo.cast.epub.EpubTransformer;

import java.io.IOException;

public class PipeLine {
    private String epubFilePath;
    private String scriptFilePath;

    public PipeLine(String epubFilePath) {
        this.epubFilePath = epubFilePath;
    }

    public void epubTransform() throws IOException {
        EpubTransformer epubTransformer = new EpubTransformer(epubFilePath);
        scriptFilePath = epubTransformer.genScript();
    }

    public void play() {
        WavGenerator play = new WavGenerator(scriptFilePath);
        play.genWav();
    }

    public void play(String scriptFilePath) {
        WavGenerator play = new WavGenerator(scriptFilePath);
        play.genWav();
    }

    public static void main(String[] args) throws IOException {
        String epubFilePath = "/Volumes/SD/download/阅微草堂笔记.epub";
        PipeLine pipeLine = new PipeLine(epubFilePath);
        pipeLine.epubTransform();

        //todo
        /*
         * 1.将epub转换为script,不必串流程，应为文件较大
         * 2.编辑脚本文件指定朗读者
         * 3.生成语音文件
         * 4.合并语音文件
         * 5.删除语音素材文件
         *
         * */
    }
}

