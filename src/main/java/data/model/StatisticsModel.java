package data.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StatisticsModel
{
    private BoardModel board;

    public StatisticsModel(BoardModel board){
        this.board = board;
    }

    public BoardModel getBoard(){ return board; }

    public double countCompletedStoryPoints() {
        double storyPointsCount = 0.0;
        //Go thorugh all the completed columns of the board
        for (ColumnModel col : board.getCompletedColumns()) {
            //Go through all the cards in the "completed" columns
            for (CardModel card : col.getCards()) {
                storyPointsCount += card.getStoryPoint();
            }
        }
        return storyPointsCount;
    }

    public double getOverallVelocity() {
        double activeWeeks = (double) ChronoUnit.WEEKS.between(board.getCreationDate(), LocalDate.now()) + 1;
        double storyPointsCount = countCompletedStoryPoints();
        if(storyPointsCount == 0) return -1;
        return storyPointsCount / activeWeeks;
    }

    public double getLeadTime() {
        double leadTimes = 0;
        double cardCount = 0;

        for(ColumnModel col : board.getCompletedColumns()){
            for (CardModel card : col.getCards()) {
                leadTimes += ChronoUnit.WEEKS.between(card.getCreationDate(), card.getCompletedDate());
                cardCount++;
            }
        }
        System.out.println("Card count: "+cardCount);
        if(cardCount == 0) return -1;
        return leadTimes / cardCount;
    }

    public double getAverageWIP() {
        double activeWeeks = (double) ChronoUnit.WEEKS.between(board.getCreationDate(), LocalDate.now()) + 1;
        double storyPoints = countCompletedStoryPoints();
        //Add story points currently in WIP
        for(ColumnModel col : board.getWIPColumns()){
            for (CardModel card : col.getCards()) {
                storyPoints += card.getStoryPoint();
            }
        }
        if(storyPoints == 0) return -1;
        return storyPoints / activeWeeks;

    }
}
