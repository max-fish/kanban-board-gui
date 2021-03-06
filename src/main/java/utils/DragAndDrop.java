package utils;

import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import ui.KanbanBoard;

/**
 * Sets up onMousePressed, onMouseDragged, and onMouseReleased listeners for columns
 * in one dimension
 */
public class DragAndDrop {
    private double orgSceneX;
    private double orgTranslateX;
    private KanbanBoard kanbanBoard;
    private HBox board;
    private double boundX;
    private BorderPane itemBeingDragged;

    public void setDragAnimation(BorderPane rootPane, JFXButton dragButton, KanbanBoard kanbanBoard) {
        itemBeingDragged = rootPane;
        this.kanbanBoard = kanbanBoard;

        board = (HBox) ((ScrollPane) kanbanBoard.getCenter()).getContent();

        boundX = board.getChildren().size() * rootPane.getWidth();

        dragButton.setOnMousePressed(onMousePressed);
        dragButton.setOnMouseDragged(onMouseDragged);
        dragButton.setOnMouseReleased(onDragOver);
    }

    private EventHandler<MouseEvent> onMousePressed =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    orgSceneX = event.getSceneX();
                    orgTranslateX = itemBeingDragged.getTranslateX();
                    boundX = board.getChildren().size() * itemBeingDragged.getWidth();

                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(5);
                    dropShadow.setOffsetX(7);
                    dropShadow.setOffsetY(7);
                    dropShadow.setColor(Color.WHITE);
                    dropShadow.setBlurType(BlurType.GAUSSIAN);
                    itemBeingDragged.setEffect(dropShadow);

                    Rotate tilt = new Rotate();
                    tilt.setAngle(5);
                    tilt.setPivotX(itemBeingDragged.getWidth() / 2);
                    tilt.setPivotY(itemBeingDragged.getHeight() / 2);

                    itemBeingDragged.getTransforms().add(tilt);
                }
            };

    private EventHandler<MouseEvent> onMouseDragged =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double offsetX = event.getSceneX() - orgSceneX;
                    double newTranslateX = orgTranslateX + offsetX;

                    int itemBeingDraggedIndex = board.getChildren().indexOf(itemBeingDragged);
                    if (offsetX < -100 || offsetX > 100) {
                        if (offsetX < -100) {
                            if (itemBeingDraggedIndex - 1 >= 0) {
                                kanbanBoard.getController().swapColumns(itemBeingDraggedIndex, itemBeingDraggedIndex - 1);
                                orgSceneX = event.getSceneX();
                                newTranslateX = 0;
                            }
                        } else {
                            if (itemBeingDraggedIndex + 1 < board.getChildren().size()) {
                                if (itemBeingDraggedIndex != board.getChildren().size() - 2) {
                                    kanbanBoard.getController().swapColumns(itemBeingDraggedIndex, itemBeingDraggedIndex + 1);
                                    orgSceneX = event.getSceneX();
                                    newTranslateX = 0;
                                }
                            }
                        }
                    }
                    if ((event.getSceneX() > 0 && event.getSceneX() < boundX)) {
                        if (itemBeingDraggedIndex == board.getChildren().size() - 2) {
                            if (offsetX <= 0) {
                                itemBeingDragged.setTranslateX(newTranslateX);
                            }
                        } else if (itemBeingDraggedIndex == 0) {
                            if (offsetX >= 0) {
                                itemBeingDragged.setTranslateX(newTranslateX);
                            }
                        } else {
                            itemBeingDragged.setTranslateX(newTranslateX);
                        }
                    }
                }
            };

    private EventHandler<MouseEvent> onDragOver =
            event -> {
                itemBeingDragged.setTranslateX(0);
                itemBeingDragged.setEffect(null);
                itemBeingDragged.getTransforms().clear();
            };
}
