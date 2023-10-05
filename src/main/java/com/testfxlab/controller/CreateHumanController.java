package com.testfxlab.controller;

import com.testfxlab.model.Human;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Objects;

public class CreateHumanController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private DatePicker birthdayPicker;

    private Stage dialogStage;
    private Human human;
    private boolean humanUpdated;

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setHuman(Human human) {
        this.human = human;
        firstNameField.setText(human.getFirstName());
        lastNameField.setText(human.getLastName());
        birthdayPicker.setValue(human.getBirthdate());
    }

    public boolean isHumanUpdated() {
        return humanUpdated;
    }

    @FXML
    private void save() {
        if (human != null && isInputValid()) {
            humanUpdated = !Objects.equals(human.getFirstName(), firstNameField.getText())
                    || !Objects.equals(human.getLastName(), lastNameField.getText())
                    || !Objects.equals(human.getBirthdate(), birthdayPicker.getValue());
            human.setFirstName(firstNameField.getText());
            human.setLastName(lastNameField.getText());
            human.setBirthdate(birthdayPicker.getValue());
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        VBox vBox = new VBox();
        if (isTextNullOrEmpty(firstNameField)) {
            vBox.getChildren().add(new Label("First name is empty!"));
        }
        if (isTextNullOrEmpty(lastNameField)) {
            vBox.getChildren().add(new Label("Last name is empty!"));
        }
        if (birthdayPicker.getValue() == null || birthdayPicker.getValue().isAfter(LocalDate.now())) {
            vBox.getChildren().add(new Label("Birthday is empty or invalid!"));
        }
        if (!vBox.getChildren().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Empty Fields");
            alert.setHeaderText("Please fill empty fields");
            alert.getDialogPane().setContent(vBox);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean isTextNullOrEmpty(TextField field) {
        return field.getText() == null || field.getText().isEmpty();
    }

    @FXML
    private void cancel() {
        dialogStage.close();
    }
}
