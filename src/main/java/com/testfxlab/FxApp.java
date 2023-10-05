package com.testfxlab;

import com.testfxlab.controller.CreateHumanController;
import com.testfxlab.controller.MainController;
import com.testfxlab.dao.HumanDao;
import com.testfxlab.dao.HumanDaoImpl;
import com.testfxlab.model.Human;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;

public class FxApp extends Application {

    private final HumanDao humanDao = new HumanDaoImpl();

    private MainApi api;
    private Stage primaryStage;
    private ObservableList<Human> people;

    public FxApp() {
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaFX App");
        api = createMainApi();
        people = retrievePeople();
        showMainWindow();
    }

    private void showMainWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FxApp.class.getResource("/view/mainDialog.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            MainController controller = fxmlLoader.getController();
            controller.setUp(api, primaryStage, people);
            primaryStage.show();
        } catch (IOException e) {
            showExceptionDialog(e);
        }

    }

    private ObservableList<Human> retrievePeople() {
        try {
            return FXCollections.observableArrayList(humanDao.getPeople());
        } catch (SQLException e) {
            System.out.println("Failed to get people from database " + e);
            showExceptionDialog(e);
            ObservableList<Human> people = FXCollections.observableArrayList(
                    new Human(1, "Monika", "Flits", LocalDate.of(1990, 9, 4)),
                    new Human(2, "Tom", "Riddle", LocalDate.of(1980, 10, 20)),
                    new Human(3, "Jon", "Smith", LocalDate.of(2001, 8, 11)),
                    new Human(4, "Harry", "Potter", LocalDate.of(1990, 6, 22)),
                    new Human(5, "Severus", "Snape", LocalDate.of(1960, 1, 9)));
            people.sort(Comparator.comparing(Human::getFirstName));
            return people;
        }
    }

    private MainApi createMainApi() {
        return new MainApi() {

            @Override
            public void delete(int humanId) {
                try {
                    humanDao.delete(humanId);
                } catch (SQLException e) {
                    System.out.println("Failed to delete human from database " + e);
                    showExceptionDialog(e);
                } finally {
                    people.removeIf((human) -> humanId == human.getHumanId());
                }
            }

            @Override
            public void create(Human created) {
                Integer humanId = null;
                try {
                    humanId = humanDao.add(created);
                } catch (SQLException e) {
                    System.out.println("Failed to add human to database " + e);
                    showExceptionDialog(e);
                } finally {
                    created.setHumanId(humanId != null ? humanId : people.size() + 1);
                    people.add(created);
                }
            }

            @Override
            public void update(Human updated) {
                try {
                    humanDao.update(updated);
                } catch (SQLException e) {
                    System.out.println("Failed to update human in database " + e);
                    showExceptionDialog(e);
                } finally {
                    Human humanToUpdate = people.get(people.indexOf(updated));
                    humanToUpdate.setFirstName(updated.getFirstName());
                    humanToUpdate.setLastName(updated.getLastName());
                    humanToUpdate.setBirthdate(updated.getBirthdate());
                }
            }

            @Override
            public Human showEditDialog(Human selected) {
                return showCreateWindow(selected);
            }
        };
    }

    private Human showCreateWindow(Human selected) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FxApp.class.getResource("/view/humanEditDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle((selected.isNew() ? "Add new" : "Edit") + " Human");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(fxmlLoader.load()));

            Human humanToUpdate = new Human(selected);

            CreateHumanController controller = fxmlLoader.getController();
            controller.setStage(dialogStage);
            controller.setHuman(humanToUpdate);

            dialogStage.showAndWait();

            return controller.isHumanUpdated() ? humanToUpdate : null;
        } catch (IOException e) {
            showExceptionDialog(e);
            return null;
        }
    }

    private void showExceptionDialog(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception occurred!");
        alert.setContentText("Open for more details.");
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(new Label("The exception stacktrace:"), 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
