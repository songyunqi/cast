package com.foo.cast;

import com.foo.cast.epub.EpubTransformer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class EpubTransController {

    @FXML
    private Label epubFilePath;

    @FXML
    private TextField scriptFilePath;

    @FXML
    protected void onFileChooseClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("epub Files", "*.epub"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            epubFilePath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    protected void onTransClick() {
        String epubFilePathStr = epubFilePath.getText();
        Alert alert;
        if (!new File(epubFilePathStr).exists()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请选择epub文件");
            alert.show();
            return;
        }
        EpubTransformer epubTransformer = new EpubTransformer(epubFilePathStr);
        try {
            String targetFile = epubTransformer.genScript();
            scriptFilePath.setText(targetFile);
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getLocalizedMessage());
            alert.show();
            return;
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("转换完成");
        alert.show();
    }
}
