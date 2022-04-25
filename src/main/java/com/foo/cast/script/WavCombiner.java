package com.foo.cast.script;

import org.apache.commons.collections4.CollectionUtils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.SequenceInputStream;
import java.util.Vector;
import java.util.stream.Stream;

public class WavCombiner {
    private String dir;

    public WavCombiner(String dir) {
        this.dir = dir;
    }

    public void combine() throws Exception {
        File file = new File(dir);
        String[] files = file.list(
                (dir, name) -> name.endsWith(".wav") && !name.startsWith("._") && !name.endsWith("combined.wav")
        );
        if (CollectionUtils.sizeIsEmpty(files)) {
            return;
        }
        //重新排序
        files = Stream.of(files).sorted((a, b) -> {
            String[] aSplits = a.split("-");
            String[] bSplits = b.split("-");
            int aNum = Integer.parseInt(aSplits[aSplits.length - 1].replace(".wav", ""));
            int bNum = Integer.parseInt(aSplits[bSplits.length - 1].replace(".wav", ""));
            return bNum - aNum;
        }).toArray(String[]::new);
        File file0 = new File(dir, files[0]);
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(file0);
        Vector<AudioInputStream> streams = new Vector<>();
        long length = 0;
        for (String fileName : files) {
            File tmpFile = new File(dir, fileName);
            //*/AudioFileFormat tempAff = AudioSystem.getAudioFileFormat(tmpFile);
            //System.out.println("fileName:getType:" + tempAff.getType());
            AudioInputStream tmpStream = AudioSystem.getAudioInputStream(tmpFile);
            length += tmpStream.getFrameLength();
            streams.add(tmpStream);
        }
        SequenceInputStream sis = new SequenceInputStream(streams.elements());
        //解析出文件名+sheet名
        String[] splits = file0.getName().split("-");

        String targetFileName = "combined.wav";
        if (splits.length > 2) {
            targetFileName = splits[0] + "-" + splits[1];
        }
        File descFile = new File(this.dir + "/" + targetFileName + "-combined.wav");
        AudioSystem.write(new AudioInputStream(sis, aff.getFormat(), length), aff.getType(), descFile);
        for (AudioInputStream stream : streams) {
            stream.close();
        }
        //删除素材文件
        for (String fileName : files) {
            File tmpFile = new File(dir, fileName);
            tmpFile.delete();
        }
    }

    public static void main(String[] args) throws Exception {
        WavCombiner wavCombiner = new WavCombiner("/Volumes/SD/download/casting");
        wavCombiner.combine();
    }
}
