package net.rethfam.checksumChecker.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rethfam.ultis.alertWindows;
import rethfam.ultis.fileUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class MainScreenController {

    private final ObservableList<String> typeList = FXCollections.observableArrayList("MD5", "SHA-1", "SHA-256", "SHA-512");
    @FXML
    private TextField txtFile;
    @FXML
    private TextArea txtFileCheckSum, txtIntCheckSum;
    @FXML
    private Label lblFile, lblResult, lblIntCheckSum, lblFileCheckSum;
    @FXML
    private Button btnFile, btnValidate, btnClose, btnReset;
    @FXML
    private ChoiceBox<String> cbType;
    private Stage currentStage;
    private String selectedItem;


    /**
     * Reset the application
     *
     * @param event calling event
     */
    @FXML
    private void handleReset(ActionEvent event) {
        txtFile.setText("");
        txtFileCheckSum.setText("");
        txtIntCheckSum.setText("");
        lblResult.setVisible(false);
        cbType.setValue("");
        cbType.getSelectionModel().selectFirst();
    }

    /**
     * Close the application
     */
    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Open the FileChooser and allow the user to select the desired file
     *
     * @param event calling event
     */
    @FXML
    private void handleFileChooser(ActionEvent event) {

        //Create new FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        //Show the FileChooser on the current stage and set a File object as the selected file
        File file = fileChooser.showOpenDialog(currentStage);

        //Check if file was selected
        if (file != null) {
            //Set the File Text field to the file path
            txtFile.setText(file.toString());
        }
    }

    /**
     * Generate the checksum and validate against provided checksum
     *
     * @param event calling event
     */
    @FXML
    private void handleValidate(ActionEvent event) {
        if (!(txtFile.getText().trim().isEmpty() && txtFile.getText().trim().equals(""))) {
            String checkSum = null;
            try {
                checkSum = fileUtils.fileDigest(txtFile.getText(), selectedItem);
            } catch (NoSuchAlgorithmException e) {
                new alertWindows().showError(e, "Algorithm Error");
            } catch (NullPointerException ex) {
                new alertWindows().showError("File Not Found", "The selected file is invalid. Please correct and try again");
            } catch (IOException e) {
                new alertWindows().showError(e, "IOException Error");
            }
            txtFileCheckSum.setText(checkSum);


            if (!(txtIntCheckSum.getText().trim().isEmpty() && txtIntCheckSum.getText().trim().equals(""))) {

                String validSum = txtIntCheckSum.getText().trim().toUpperCase();

                System.out.println(validSum);
                System.out.println(checkSum);

                //Set the Result and Result color if matches green else red
                if (checkSum.equals(validSum)) {
                    lblResult.setText("MATCH");
                    lblResult.setStyle("-fx-text-fill: green");
                } else {
                    lblResult.setText("ERROR");
                    lblResult.setStyle("-fx-text-fill: red");
                }

                lblResult.setVisible(true);
            }
        }
    }

    /**
     * Prepares the form and set the Checkbox for the Checksum Type
     *
     * @param currentStage stage object created to display the this form in
     */
    public void initForm(Stage currentStage) {
        this.currentStage = currentStage;

        //Add available items to the choice box
        cbType.setItems(typeList);

        //Set the ChoiceBox to the first item and set that item as the selected item
        cbType.getSelectionModel().selectFirst();
        selectedItem = cbType.getSelectionModel().getSelectedItem();

        //Add listener to see when user selects a new choice
        cbType.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //Set the selected item to the item user choose
                        selectedItem = cbType.getItems().get((Integer) newValue);
                    }
                });
    }
}
