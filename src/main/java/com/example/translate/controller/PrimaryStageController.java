package com.example.translate.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/7/18 10:03
 */
@FXMLController
public class PrimaryStageController implements Initializable {

    @FXML
    private TextArea englishTextArea;
    @FXML
    private TextArea chineseTextArea;
    @FXML
    private Button upToDown;
    @FXML
    private Button downToUp;

    @Autowired
    BaiduTranslte baiduTranslte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //剪贴板实时更新
        final Clipboard systemClipboard = Clipboard.getSystemClipboard();
        new com.sun.glass.ui.ClipboardAssistance(com.sun.glass.ui.Clipboard.SYSTEM) {
            @Override
            public void contentChanged() {
                englishTextArea.setText(systemClipboard.getString());
                translate();
            }
        };

        //启动时加载剪贴板
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String clipboardString = clipboard.getString();
        englishTextArea.setText(clipboardString);
        if (englishTextArea.getText().length() < 50)
            translate();

        //检测回车自动翻译
        englishTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    translate();
                }
            }
        });

        //英译中按钮
        upToDown.setOnAction(event -> {
            translate();
        });

    }

    public void translate() {
        int mode;
        if (isChinese(englishTextArea.getText())) mode = 2;
        else mode = 1;
        String englishText = englishTextArea.getText();
        String chineseText = baiduTranslte.translate(englishText, mode);
        chineseTextArea.setText(chineseText);
    }

    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }


}
