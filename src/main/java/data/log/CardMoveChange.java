package data.log;

import data.model.CardModel;
import data.model.ColumnModel;

public class CardMoveChange extends CardChange{
    private int prevPosition;
    private transient ColumnModel prevColumn;
    private int prevColumnId;

    private int newPosition;
    private transient ColumnModel newColumn;
    private int newColumnId;

    public CardMoveChange(CardModel cardModel, int prevPosition, ColumnModel prevColumn,
                                                int newPosition, ColumnModel newColumn)
    {
        super(cardModel);
        this.prevPosition = prevPosition;
        this.prevColumn = prevColumn;
        this.prevColumnId = prevColumn.getId();

        this.newPosition = newPosition;
        this.newColumn = newColumn;
        this.newColumnId = newColumn.getId();
    }

    public void apply()
    {
        if(applied)
            return;

        prevColumn.getGUI().getController().removeCard(cardModel.getGUI());
        newColumn.getGUI().getController().insertCard(cardModel, newPosition);
        applied = true;
    }

    public void revert()
    {
        if(!applied)
            return;

        newColumn.getGUI().getController().removeCard(cardModel.getGUI());
        prevColumn.getGUI().getController().insertCard(cardModel, prevPosition);
        applied = false;
    }

    @Override
    public void init()
    {
        this.cardModel = CardModel.getCardModelById(cardId);
        this.prevColumn = ColumnModel.getColumnModelById(prevColumnId);
        this.newColumn = ColumnModel.getColumnModelById(newColumnId);
    }

    public String toString()
    {
        return cardModel.getTitle() + " moved to " + newColumn.getName();
    }
}
