package com.example.translate;

import com.example.translate.controller.PrimaryStageController;
import com.example.translate.view.PrimaryStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranslateApplication extends AbstractJavaFxApplicationSupport {

    @Autowired PrimaryStageController primaryStageController;

    public static void main(String[] args) {
        launchApp(TranslateApplication.class, PrimaryStageView.class, args);
        SpringApplication.run(TranslateApplication.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.setX(1200);
        stage.setY(500);
    }
}

