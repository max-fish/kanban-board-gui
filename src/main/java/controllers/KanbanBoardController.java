package controllers;

import callbacks.DeleteColumnDataCallback;
import callbacks.DeleteColumnPopupCallback;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import data.model.BoardModel;
import data.model.CardModel;
import data.model.ColumnModel;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.layout.StackPane;
import ui.*;
import data.model.StatisticsModel;
import utils.AnimationMaker;
import utils.GUIMaker;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class KanbanBoardController implements Initializable {
    @FXML
    private BorderPane rootPane;
    @FXML
    private JFXTextField boardTitle;
    @FXML
    private HBox columns;

    private BoardModel boardModel;

    private JFXButton addButton;
    @FXML
    private JFXButton statisticsButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boardTitle.textProperty().addListener((observable, oldValue, newValue) -> boardModel.setName(newValue));

        statisticsButton.setOnMouseClicked(event -> getStatistics());

        addButton = GUIMaker.makeAddButton();
        addButton.setOnMouseClicked(event -> makeNewColumn());
        addButton.setId("addColumn");

        columns.getChildren().add(addButton);
    }

    @FXML
    public void makeNewColumn() {
        makeNewColumn(new ColumnModel());
    }

    /**
     * Makes a new column with a specific {@link ColumnModel}
     * @param newColumnModel - a data model for Columns
     */
    public void makeNewColumn(ColumnModel newColumnModel) {
        KanbanColumn toInsert = new KanbanColumn((KanbanBoard) rootPane);
        toInsert.getController().fillWithData(newColumnModel);

        TranslateTransition slideIn = AnimationMaker.makeAddColumnSlideInAnimation(toInsert);
        TranslateTransition addButtonSlideIn = AnimationMaker.makeAddColumnSlideInAnimation(addButton);

        columns.getChildren().set(columns.getChildren().size() - 1, toInsert);
        columns.getChildren().add(addButton);

        AnimationMaker.playAnimations(slideIn, addButtonSlideIn);

        HBox.setMargin(toInsert, new Insets(10));

        if (!boardModel.contains(newColumnModel))
            boardModel.addColumn(newColumnModel);

        if (newColumnModel.hasCards())
            createCards(newColumnModel, toInsert);
    }

    private void createCards(ColumnModel columnModel, KanbanColumn column) {
        List<CardModel> cards = columnModel.getCards();
        for (CardModel card : cards)
            column.getController().makeNewCard(card);
    }

    /**
     * Asks for user confirmation to delete a column
     * @param kanbanColumn - the {@link KanbanColumn} that needs to be deleted
     * @param callback - describes what do depending on what the user selects (delete/cancel)
     */
    void askToDeleteColumn(KanbanColumn kanbanColumn, DeleteColumnDataCallback callback) {
        KanbanBoard board = (KanbanBoard) rootPane;
        BorderPane homePane = board.getHomePage();
        DeleteConfirmationPopup deleteConfirmationPopup = new DeleteConfirmationPopup(new DeleteColumnPopupCallback() {
            @Override
            public void onStart(StackPane stackPane) {
                homePane.setCenter(stackPane);
            }

            @Override
            public void onDelete() {
                callback.onDelete();
                homePane.setCenter(board);
                deleteColumn(kanbanColumn);
            }

            @Override
            public void onCancel() {
                homePane.setCenter(board);
            }
        }, homePane.getCenter());

        deleteConfirmationPopup.show();
    }

    private void deleteColumn(KanbanColumn column) {
        ParallelTransition parallelTransition = AnimationMaker.makeDeleteColumnParallelAnimation(columns, column);
        if (parallelTransition != null) {
            parallelTransition.play();
        }
        columns.getChildren().remove(column);
    }

    private void getStatistics() {
        //add info ofr creating sttistics as parameters and keep record on fields
        StatisticsPopup statPopup = new StatisticsPopup();
        StatisticsModel model = new StatisticsModel(boardModel);
        statPopup.getController().setStatisticsModel(model);
        statPopup.getController().displayStats();
        statPopup.show(statisticsButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
    }


    /**
     * inflate the {@link KanbanCard} with data from a {@link CardModel}
     * @param boardModel - {@link BoardModel}
     */
    public void fillWithData(BoardModel boardModel) {
        this.boardModel = boardModel;
        boardTitle.setText(boardModel.getName());
    }


    /**
     * Return the data associated with this ui component
     * @return boardModel
     */
    public BoardModel getBoardModel() {
        return boardModel;
    }

    /**
     * Swaps columns while they are being dragged across the board
     * @param idx1 - the index of the {@link KanbanColumn} being dragged/swapped
     * @param idx2 - the index of the {@link KanbanColumn} being dragged/swapped
     */
    public void swapColumns(int idx1, int idx2){
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(columns.getChildren());
        Collections.swap(workingCollection, idx1, idx2);
        columns.getChildren().setAll(workingCollection);
        Collections.swap(boardModel.getColumns(), idx1, idx2);
    }

}
