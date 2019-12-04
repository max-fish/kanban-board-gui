package utils;

import com.jfoenix.controls.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;
import controllers.ColumnController;
import data.model.KanbanModel;

public class ComponentMaker {
    public static StackPane makeBoardCard(Label title) {
        StackPane boardCard = new StackPane();
        boardCard.setPrefWidth(185);
        boardCard.setPrefHeight(80);
        boardCard.setMaxWidth(185);
        boardCard.setMaxHeight(80);

        title.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 19));
        title.setTextFill(Color.WHITE);

        boardCard.getChildren().add(title);
        StackPane.setAlignment(title, Pos.TOP_LEFT);

        boardCard.setBackground(new Background(new BackgroundFill(MaterialColors.colorPrimary, new CornerRadii(5), Insets.EMPTY)));

        JFXRippler jfxRippler = new JFXRippler(boardCard);
        jfxRippler.setRipplerFill(Color.rgb(176, 133, 245, 0.26));
        return jfxRippler;
    }

    public static JFXButton makeAddButton() {
        JFXButton jfxButton = new JFXButton();
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        jfxButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(100), Insets.EMPTY)));
        jfxButton.setStyle("-fx-padding: 5");

        FontIcon fontIcon = new FontIcon();
        fontIcon.setIconColor(MaterialColors.colorPrimary);
        fontIcon.setIconLiteral("gmi-add");
        fontIcon.setIconSize(30);

        jfxButton.setGraphic(fontIcon);
        jfxButton.setMaxHeight(30);
        jfxButton.setMaxWidth(30);
        jfxButton.setMinHeight(30);
        jfxButton.setMinWidth(30);
        return jfxButton;
    }

    public static JFXPopup makeColumnMenu(ColumnController controller)
    {
        JFXButton addCard = new JFXButton("Add card");
        FontIcon addCardIcon = new FontIcon();
        addCardIcon.setIconColor(MaterialColors.colorPrimary);
        addCardIcon.setIconLiteral("gmi-add");
        addCardIcon.setIconSize(19);
        addCard.setGraphic(addCardIcon);
        addCard.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 15));
        addCard.setAlignment(Pos.BASELINE_LEFT);
        addCard.setMinWidth(120);
        addCard.setOnMouseClicked(event -> controller.makeNewCard());

        JFXButton deleteColumn = new JFXButton("Delete");
        FontIcon deleteColumnIcon = new FontIcon();
        deleteColumnIcon.setIconColor(MaterialColors.colorPrimary);
        deleteColumnIcon.setIconLiteral("gmi-delete");
        deleteColumnIcon.setIconSize(17);
        deleteColumn.setGraphic(deleteColumnIcon);
        deleteColumn.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 15));
        deleteColumn.setAlignment(Pos.BASELINE_LEFT);
        deleteColumn.setMinWidth(120);
        deleteColumn.setOnMouseClicked(controller::deleteColumn);

        VBox container = new VBox(addCard, deleteColumn);

        JFXPopup menu = new JFXPopup();
        menu.setPopupContent(container);

        return menu;
    }

    public static JFXPopup makeFileMenu()
    {
        JFXButton importJSON = new JFXButton("Import from JSON");
        FontIcon importIcon = new FontIcon();
        importIcon.setIconColor(MaterialColors.colorPrimary);
        importIcon.setIconLiteral("gmi-file-download");
        importIcon.setIconSize(17);
        importJSON.setGraphic(importIcon);
        importJSON.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 15));
        importJSON.setAlignment(Pos.BASELINE_LEFT);
        importJSON.setMinWidth(165);
        importJSON.setOnMouseClicked(KanbanModel.instance()::loadJSON);

        JFXButton exportJSON = new JFXButton("Export to JSON");
        FontIcon exportIcon = new FontIcon();
        exportIcon.setIconColor(MaterialColors.colorPrimary);
        exportIcon.setIconLiteral("gmi-file-upload");
        exportIcon.setIconSize(17);
        exportJSON.setGraphic(exportIcon);
        exportJSON.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 15));
        exportJSON.setAlignment(Pos.BASELINE_LEFT);
        exportJSON.setMinWidth(165);
        exportJSON.setOnMouseClicked(KanbanModel.instance()::saveJSON);

        VBox container = new VBox(importJSON, exportJSON);

        JFXPopup menu = new JFXPopup();
        menu.setPopupContent(container);

        return menu;
    }

    public static JFXButton makeStatisticsButton(){
        JFXButton jfxButton = new JFXButton();
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        //jfxButton.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(100), Insets.EMPTY)));
        jfxButton.setStyle("-fx-padding: 5");

        FontIcon fontIcon = new FontIcon();
        fontIcon.setIconColor(Color.WHITE);
        fontIcon.setIconLiteral("gmi-insert-chart");
        fontIcon.setIconSize(30);

        jfxButton.setGraphic(fontIcon);
        jfxButton.setMaxHeight(50);
        jfxButton.setMaxWidth(50);
        jfxButton.setMinHeight(50);
        jfxButton.setMinWidth(50);

        return jfxButton;
    }

    public static JFXButton makeColumnRoleOption(String role, ColumnController column){
        JFXButton roleButton = new JFXButton(role);
        roleButton.setFont(Font.loadFont(ComponentMaker.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 15));
        roleButton.setAlignment(Pos.BASELINE_LEFT);
        roleButton.setMinWidth(165);
        roleButton.setOnMouseClicked(column::setRole);

        return roleButton;
    }

    public static JFXPopup makeColumnRoleDropDown(ColumnController column){

        VBox options = new VBox();
        options.getChildren().addAll(
                makeColumnRoleOption("Backlog", column),  makeColumnRoleOption("Work In Progress", column),
                makeColumnRoleOption("On Hold", column), makeColumnRoleOption("Completed Work", column),
                makeColumnRoleOption("Info only", column)
                );
        JFXButton backlog = makeColumnRoleOption("Backlog", column);

        options.getChildren().add(backlog);
        JFXPopup dropDown = new JFXPopup();
        dropDown.setPopupContent(options);
        return dropDown;
    }
}
