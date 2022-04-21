package com.foo.cast;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button epubButton;

    @FXML
    private Button ttsButton;

    @FXML
    protected void onEpubButtonClick() throws IOException {
        //加载epub转换界面
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("epub-trans.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 2, 240 * 2);
        stage.setTitle("epub转换");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onTTSButtonClick() throws IOException {
        //加载tts合成页面
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("script-tts.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320 * 2, 240 * 2);
        stage.setTitle("tts合成");
        stage.setScene(scene);
        stage.show();
    }
}
