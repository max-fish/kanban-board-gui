package model;

import javax.smartcardio.Card;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BoardModel {
    private String name;
    private List<ColumnModel> columnModels;
    private ArrayList<LocalDate> cardsDate;
    private LocalDate creationDate;

    /**
     *
     */
    public BoardModel(String name)
    {
        this.name = name;
        columnModels = new ArrayList<>();
        cardsDate = new ArrayList<>();
        creationDate = LocalDate.now();
    }

    public void addColumn(ColumnModel columnModel)
    {
        columnModels.add(columnModel);
    }

    public void deleteColumn(ColumnModel columnModel)
    {
        columnModels.remove(columnModel);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void addedCard(LocalDate date) { cardsDate.add(date); }

    public ArrayList<LocalDate> getCardDates() { return cardsDate; }

    public LocalDate getCreationDate() { return creationDate; }
}
