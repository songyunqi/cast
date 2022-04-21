package com.foo.cast;

import com.foo.cast.script.WavCombiner;
import com.foo.cast.script.WavGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class ScriptTTSController {

    @FXML
    private Label scriptFilePath;
    @FXML
    private Button tts;

    @FXML
    protected void onFileChooseClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("epub Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            scriptFilePath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    protected void onTTSClick() throws Exception {
        String xlsxFilePathStr = scriptFilePath.getText();
        File xlsxFile = new File(xlsxFilePathStr);
        if (!xlsxFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请选择xlsx文件");
            alert.show();
            return;
        }
        WavGenerator wavGenerator = new WavGenerator(xlsxFilePathStr);
        wavGenerator.genWav();
    }

    @FXML
    protected void onTTSCombineClick() throws Exception {
        String xlsxFilePathStr = scriptFilePath.getText();
        File file = new File(xlsxFilePathStr);
        String dir = file.getParent();
        WavCombiner wavCombiner = new WavCombiner(dir);
        wavCombiner.combine();
    }
}
