package controllers;

import callbacks.BoardNamePopupCallBack;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles user input when the user is setting the board name for the first time
 */
public class BoardNamePopupController implements Initializable {
    @FXML
    private JFXButton okButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXTextField boardNameTextField;

    private BoardNamePopupCallBack callBack;
    private JFXDialog dialog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //if user submits text
        okButton.setOnAction(event -> {
            if (!boardNameTextField.getText().isEmpty()) {
                dialog.close();
                callBack.onValidName(boardNameTextField.getText());
            }
        });
        //if user cancels
        cancelButton.setOnAction(event -> {
            dialog.close();
            callBack.onCancel();
        });
    }

    public void setBoardNamePopupCallBack(BoardNamePopupCallBack boardNamePopupCallBack) {
        callBack = boardNamePopupCallBack;
    }

    public void setUi(JFXDialog jfxDialog) {
        dialog = jfxDialog;
    }
}
