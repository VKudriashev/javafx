package com.testfxlab.controller;

import com.testfxlab.MainApi;
import com.testfxlab.model.Human;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class MainController {

    @FXML
    private TreeTableView<Human> mainTable;

    @FXML
    private TreeTableColumn<Human, String> firstNameColumn;

    @FXML
    private TreeTableColumn<Human, String> lastNameColumn;

    @FXML
    private TreeTableColumn<Human, Integer> ageColumn;

    @FXML
    private TreeTableColumn<Human, LocalDate> birthdayColumn;

    private MainApi api;
    private Stage mainStage;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory((cellData) -> cellData.getValue().getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory((cellData) -> cellData.getValue().getValue().lastNameProperty());
        ageColumn.setCellValueFactory((cellData) -> cellData.getValue().isLeaf()
                ? Optional.ofNullable(cellData.getValue().getValue().ageProperty())
                .map(IntegerProperty::asObject)
                .orElse(null)
                : null);
        birthdayColumn.setCellValueFactory((cellData) -> cellData.getValue().getValue().birthdateProperty());
        mainTable.setRowFactory((param) -> {
            TreeTableRow<Human> row = new TreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (event.getClickCount() == 2) {
                    showBirthdayDialog(row.getItem());
                }

            });
            return row;
        });
        mainTable.setRoot(new TreeItem<>(new Human("People")));
        mainTable.getRoot().setExpanded(true);
    }

    private void showBirthdayDialog(Human item) {
        LocalDate birthDate = item.getBirthdate();
        LocalDate today = LocalDate.now();
        if (birthDate != null && birthDate.getMonth().equals(today.getMonth()) && birthDate.getDayOfMonth() == today.getDayOfMonth()) {
            showWarning(AlertType.INFORMATION, "Birthday Dialog", null, "Today is " + item.getFullName() + "'s birthday!");
        }

    }

    @FXML
    private void delete() {
        TreeItem<Human> item = mainTable.getSelectionModel().getSelectedItem();
        if (item != null && item.isLeaf()) {
            api.delete(item.getValue().getHumanId());
        } else {
            showNoHumanWarning();
        }
    }

    @FXML
    private void edit() {
        TreeItem<Human> item = mainTable.getSelectionModel().getSelectedItem();
        if (item != null && item.isLeaf()) {
            Optional.ofNullable(api.showEditDialog(item.getValue())).ifPresent(updatedHuman -> api.update(updatedHuman));
        } else {
            showNoHumanWarning();
        }

    }

    @FXML
    private void create() {
        Optional.ofNullable(api.showEditDialog(new Human())).ifPresent(updatedHuman -> {
            api.create(updatedHuman);
            mainTable.sort();
        });
    }

    private void showNoHumanWarning() {
        showWarning(AlertType.WARNING, "No Selection", "No Human Selected", "Please select or create a person in the table.");
    }

    private void showWarning(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(mainStage);
        alert.showAndWait();
    }

    public void setUp(MainApi api, Stage mainStage, ObservableList<Human> people) {
        this.api = api;
        this.mainStage = mainStage;
        people.forEach(human -> mainTable.getRoot().getChildren().add(new TreeItem<>(human)));
        people.addListener((ListChangeListener<Human>) change -> {
            if (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item -> mainTable.getRoot().getChildren().add(new TreeItem<>(item)));
                    mainTable.getSelectionModel().selectLast();
                } else if (change.wasRemoved()) {
                    mainTable.getRoot().getChildren().remove(change.getFrom());
                    mainTable.getSelectionModel().clearSelection(change.getFrom());
                }
            }
        });
    }
}
