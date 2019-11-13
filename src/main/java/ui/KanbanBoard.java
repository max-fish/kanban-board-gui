package ui;

import com.jfoenix.controls.JFXDecorator;
import controllers.KanbanBoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KanbanBoard extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("layouts/kanban_board_ui.fxml"));
        Parent root = fxmlLoader.load();
        KanbanBoardController kanbanBoardController = fxmlLoader.getController();
        //kanbanBoardController.init();
        JFXDecorator jfxDecorator = new JFXDecorator(primaryStage, root);
        jfxDecorator.setCustomMaximize(true);
        jfxDecorator.setTitle("Kanban Board");
        Scene scene = new Scene(jfxDecorator, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("styling/scene_styling.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
