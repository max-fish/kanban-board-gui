package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import ui.KanbanCard;
import ui.KanbanColumn;

import java.util.Collections;

public class DragAndDropForCards {
    private double orgSceneY;
    private double orgTranslateY;
    private double orgSceneX;
    private double orgTranslateX;
    private KanbanColumn columnContainer;
    private VBox column;
    private HBox board;
    private double boundX;
    private double boundY;
    private double columnHeaderOffset;
    private BorderPane itemBeingDragged;

    public void setDragAnimation(KanbanCard rootPane) {
        columnContainer = rootPane.getColumn();
        column = (VBox) columnContainer.getCenter();
        board = (HBox) ((ScrollPane) columnContainer.getBoard().getCenter()).getContent();
        columnHeaderOffset = ((VBox) columnContainer.getTop()).getHeight();
        boundY = column.getChildren().size() * (rootPane.getHeight()) + columnHeaderOffset + 200;
        boundX = (board.getChildren().size() - 1) * columnContainer.getWidth();

        rootPane.setOnMousePressed(onMousePressed);
        rootPane.setOnMouseDragged(onMouseDragged);
        rootPane.setOnMouseReleased(onMouseReleased);
    }

    private EventHandler<MouseEvent> onMousePressed =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    itemBeingDragged = (BorderPane) event.getSource();
                    orgSceneY = event.getSceneY();
                    orgTranslateY = itemBeingDragged.getTranslateY();
                    orgSceneX = event.getSceneX();
                    orgTranslateX = itemBeingDragged.getTranslateX();
                    boundY = column.getChildren().size() * (itemBeingDragged.getHeight()) + columnHeaderOffset + 200;
                    boundX = (board.getChildren().size() - 1) * columnContainer.getWidth();

                    itemBeingDragged.setEffect(getDropShadow());

                    Rotate tilt = new Rotate();
                    tilt.setAngle(5);
                    tilt.setPivotX(itemBeingDragged.getWidth() / 2);
                    tilt.setPivotY(itemBeingDragged.getHeight() / 2);

                    itemBeingDragged.getTransforms().add(tilt);
                }
            };

    private DropShadow getDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(7);
        dropShadow.setOffsetY(7);
        dropShadow.setColor(Color.WHITE);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        return dropShadow;
    }

    private EventHandler<MouseEvent> onMouseDragged =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double offsetY = event.getSceneY() - orgSceneY;
                    double newTranslateY = orgTranslateY + offsetY;
                    double offsetX = event.getSceneX() - orgSceneX;
                    double newTranslateX = orgTranslateX + offsetX;
                    itemBeingDragged = (BorderPane) event.getSource();

                    int itemBeingDraggedIndex = column.getChildren().indexOf(itemBeingDragged);
//                    double cardBegin = itemBeingDraggedIndex * itemBeingDragged.getHeight() + columnHeaderOffset;
//                    double cardEnd = cardBegin + itemBeingDragged.getHeight();

                    if (offsetY < -100 || offsetY > 100) {
                        if (offsetY < 100) {
                            if (itemBeingDraggedIndex - 1 >= 0) {
                                ObservableList<Node> workingCollection = FXCollections.observableArrayList(column.getChildren());
                                Collections.swap(workingCollection, itemBeingDraggedIndex - 1, itemBeingDraggedIndex);
                                column.getChildren().setAll(workingCollection);
                                orgSceneY = event.getSceneY();
                                newTranslateY = 0;
                            }
                        } else {
//                            System.out.println("pos: " + event.getSceneY());
//                            System.out.println("cardEnd: " + cardEnd);
                            if (itemBeingDraggedIndex + 1 < column.getChildren().size()) {
                                ObservableList<Node> workingCollection = FXCollections.observableArrayList(column.getChildren());
                                Collections.swap(workingCollection, itemBeingDraggedIndex, itemBeingDraggedIndex + 1);
                                column.getChildren().setAll(workingCollection);
                                orgSceneY = event.getSceneY();
                                newTranslateY = 0;
                            }
                        }
                    }
                    if (offsetX < -200 || offsetX > 200) {
                        int columnIndex = board.getChildren().indexOf(columnContainer);
                        if (offsetX > 200) {
                            if (columnIndex + 1 < board.getChildren().size() - 1) {
                                KanbanColumn nextColumnContainer = (KanbanColumn) board.getChildren().get(columnIndex + 1);
                                VBox nextColumn = (VBox) nextColumnContainer.getCenter();
                                onDragOver();
                                columnContainer.getController().deleteCard((KanbanCard) itemBeingDragged);
//                                nextColumn.getChildren().add(itemBeingDraggedIndex, itemBeingDragged);
                                nextColumnContainer.getController().makeNewCard(((KanbanCard) itemBeingDragged).getController().getCardModel());
                                columnContainer = nextColumnContainer;
                                column = nextColumn;
                                orgSceneX = event.getSceneX();
                                newTranslateX = 0;
                            }
                        } else {
                            if (columnIndex - 1 >= 0) {
                                KanbanColumn previousColumnContainer = (KanbanColumn) board.getChildren().get(columnIndex - 1);
                                VBox previousColumn = (VBox) previousColumnContainer.getCenter();
                                onDragOver();
                                columnContainer.getController().deleteCard((KanbanCard) itemBeingDragged);
//                                previousColumn.getChildren().add(itemBeingDraggedIndex, itemBeingDragged);
                                previousColumnContainer.getController().makeNewCard(((KanbanCard) itemBeingDragged).getController().getCardModel());
                                columnContainer = previousColumnContainer;
                                column = previousColumn;
                                orgSceneX = event.getSceneX();
                                newTranslateX = 0;
                            }
                        }
                    }
                    if ((event.getSceneY() > 0 && event.getSceneY() < boundY)) {
                        if (itemBeingDraggedIndex == 0) {
                            if (offsetY >= 0) {
                                itemBeingDragged.setTranslateY(newTranslateY);
                            }
                        } else if (itemBeingDraggedIndex == column.getChildren().size() - 1) {
                            if (offsetY <= 0) {
                                itemBeingDragged.setTranslateY(newTranslateY);
                            }
                        } else {
                            itemBeingDragged.setTranslateY(newTranslateY);
                        }
                    }
                    if (event.getSceneX() > 0 && event.getSceneX() < boundX) {
                        itemBeingDragged.setTranslateX(newTranslateX);
                    }
                }
            };

    private EventHandler<MouseEvent> onMouseReleased =
            event -> {
                itemBeingDragged.setTranslateY(0);
                itemBeingDragged.setTranslateX(0);
                itemBeingDragged.setEffect(null);
                itemBeingDragged.getTransforms().clear();
            };

    private void onDragOver() {
        itemBeingDragged.setEffect(null);
        itemBeingDragged.getTransforms().clear();
    }
}
