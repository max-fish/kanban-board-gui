package controllers;

import callbacks.CardDetailPopupCallback;
import callbacks.DeleteColumnPopupCallback;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

import data.model.CardModel;
import data.log.CardEditChange;

import javafx.scene.layout.StackPane;
import ui.CardDetailPopup;
import ui.DeleteConfirmationPopup;
import ui.KanbanBoard;
import ui.KanbanCard;
import utils.DragAndDropForCards;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles user input for a {@link KanbanCard}
 */
public class CardController implements Initializable {
    @FXML
    private Label cardTitle;
    @FXML
    private BorderPane rootPane;

    private CardModel cardModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DragAndDropForCards dragAnimation = new DragAndDropForCards();
        KanbanCard card = (KanbanCard) rootPane;
        dragAnimation.setDragAnimation(card);

        /*cardTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            cardModel.setTitle(newValue);
            cardModel.getParent().getParent().getActivityLogModel().addChange(new CardEditChange(cardModel, oldValue, newValue));
          });*/
    }

    /**
     * inflate the {@link KanbanCard} with data from a {@link CardModel}
     * @param cardModel - {@link CardModel}
     */
    public void fillWithData(CardModel cardModel) {
        this.cardModel = cardModel;
        cardTitle.setText(cardModel.getTitle());
    }

    /**
     * Brings up the {@link CardDetailPopup} to edit data associated
     * with the {@link CardModel}
     */
    @FXML
    private void editDetails() {
        KanbanCard card = (KanbanCard) rootPane;
        KanbanBoard board = card.getColumn().getBoard();
        BorderPane homePage = board.getHomePage();
        cardModel.setTitle(cardTitle.getText());
        CardDetailPopup cardDetailPopup = new CardDetailPopup(new CardDetailPopupCallback() {
            @Override
            public CardModel onStart(StackPane dialogContainer) {
                homePage.setCenter(dialogContainer);
                return cardModel;
            }

            @Override
            public void onSave(CardModel cardModel) {
                CardController.this.cardModel = cardModel;
                cardTitle.setText(cardModel.getTitle());
                  System.out.println(cardTitle.getText());

                homePage.setCenter(board);
            }

            @Override
            public void onCancel() {
                homePage.setCenter(board);
            }
        }, homePage.getCenter());

        cardDetailPopup.show();
    }

    @FXML
    public void deleteCard() {
        KanbanCard kanbanCardToDelete = (KanbanCard) rootPane;
        KanbanBoard board = kanbanCardToDelete.getColumn().getBoard();
        BorderPane homePage = board.getHomePage();
        DeleteConfirmationPopup deleteCardConfirmation = new DeleteConfirmationPopup(new DeleteColumnPopupCallback() {
            //init set up method
            @Override
            public void onStart(StackPane stackPane) {
                homePage.setCenter(stackPane);
            }
            //if user selects delete
            @Override
            public void onDelete() {
                kanbanCardToDelete.getColumn().getController().deleteCard(kanbanCardToDelete);
                kanbanCardToDelete.getColumn().getController().decrementCurrentWip();
                cardModel = null;
                homePage.setCenter(board);
            }
            //if user cancels
            @Override
            public void onCancel() {
                homePage.setCenter(board);
            }
        }, homePage.getCenter());

        deleteCardConfirmation.show();
    }

    /**
     * Deletes a specific card without asking for user confirmation
     * @param cardToRemove - the specific card that the user wants to delete
     */
    public void removeCard(KanbanCard cardToRemove){
        cardToRemove.getColumn().getController().deleteCard(cardToRemove);
    }

    /**
     * Return the data associated with this ui component
     * @return cardModel
     */
    public CardModel getCardModel() {
        return cardModel;
    }

    public CardModel getData() {
        return cardModel;
    }

    public Label getTitle()
    {
        return cardTitle;
    }
}
