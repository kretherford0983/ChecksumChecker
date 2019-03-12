package net.rethfam.checksumChecker.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainScreenController {

    @FXML
    private TextField txtFile, txtIntCheckSum, txtFileCheckSum;

    @FXML
    private Label lblFile, lblResult, lblIntCheckSum, lblFileCheckSum;

    @FXML
    private Button btnFile, btnValidate, btnClose, btnReset;

    @FXML
    private ChoiceBox<String> cbCSype;

    private Stage currentStage;
    private final ObservableList<String> typeList = FXCollections.observableArrayList("MD5", "SHA-1", "SHA-256", "SHA-512");
    private String selectedItem;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();


    /**
     * Reset the application
     * @param event calling event
     */
    @FXML
    private void handleReset(ActionEvent event){
        txtFile.setText("");
        txtFileCheckSum.setText("");
        txtIntCheckSum.setText("");
        lblResult.setVisible(false);
        cbCSype.setValue("");
        cbCSype.getSelectionModel().selectFirst();
    }

    /**
     * Close the application
     */
    @FXML
    private void handleClose(ActionEvent event){
        System.exit(0);
    }

    /**
     * Open the FileChooser and allow the user to select the desired file
     * @param event calling event
     */
    @FXML
    private void handleFileChooser(ActionEvent event){

        //Create new FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        //Show the FileChooser on the current stage and set a File object as the selected file
        File file = fileChooser.showOpenDialog(currentStage);

        //Check if file was selected
        if(file != null){
            //Set the File Text field to the file path
            txtFile.setText(file.toString());
        }
    }

    /**
     * Generate the checksum and validate against provided checksum
     * @param event calling event
     */
    @FXML
    private void handleValidate(ActionEvent event){
        if(!(txtFile.getText().trim().isEmpty() && txtFile.getText().trim().equals(""))) {
            String checkSum = null;
            try {
                checkSum = fileDigest(txtFile.getText(), selectedItem);
            } catch (Exception e) {
                e.printStackTrace();
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
        } else {
            return;
        }
    }

    /**
     * Prepares the form and set the Checkbox for the Checksum Type
     * @param currentStage stage object created to display the this form in
     */
    public void initForm(Stage currentStage){
        this.currentStage = currentStage;

        //Add available items to the choice box
        cbCSype.setItems(typeList);

        //Set the ChoiceBox to the first item and set that item as the selected item
        cbCSype.getSelectionModel().selectFirst();
        selectedItem = cbCSype.getSelectionModel().getSelectedItem();

        //Add listener to see when user selects a new choice
        cbCSype.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //Set the selected item to the item user choose
                        selectedItem = cbCSype.getItems().get((Integer) newValue);
                    }
                });
    }

    /**
     * Generate a hashed checksum from a passed file and return the hex has back to the calling method
     * @param file File to have checksum generated from
     * @param digestType type of checksum to be generated
     * @return the hashed checksum of the file
     */
    public final String fileDigest(String file, String digestType){

        try {
            byte[] buffer = new byte[8192];
            MessageDigest md = MessageDigest.getInstance(digestType);

            DigestInputStream dis = null;
            try {
                dis = new DigestInputStream(new FileInputStream(new File(file)), md);

                while (dis.read(buffer) != -1) ;
            } finally {
                dis.close();
            }

            byte[] bytes = md.digest();

            // bytesToHex-method
            //Update the bytes to a Hex
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }

            return new String(hexChars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
