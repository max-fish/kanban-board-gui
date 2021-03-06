package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import data.model.StatisticsModel;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * This class holds the logic of calculating the statistics and displaying them in a {@link com.jfoenix.controls.JFXPopup}
 */
public class StatisticsController implements Initializable {

    @FXML
    private Label overallVelocity;
    @FXML
    private Label leadTime;
    @FXML
    private Label averageWIP;

    @FXML
    private LineChart<Number, Number> linechartOverallVelocity;
    @FXML
    private LineChart<Number, Number> linechartLeadTime;
    @FXML
    private LineChart<Number, Number> linechartAverageWIP;

    private StatisticsModel statisticsModel;

    private int[] overallVelocityWeekArray;
    private int[] leadTimeWeekArray;
    private double[] averageWIPWeekArray;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void setStatisticsModel(StatisticsModel statisticsModel) {
        this.statisticsModel = statisticsModel;
        this.overallVelocityWeekArray = statisticsModel.getOverallVelocity();
        this.leadTimeWeekArray = statisticsModel.getLeadTime();
        this.averageWIPWeekArray = statisticsModel.getAverageWIP();
    }

    public void displayStats(){
        if(!statisticsModel.getBoard().hasCompleteColumn()){
            overallVelocity.setText("To view the overall velocity you need to assign a completed role.");
            leadTime.setText("To view the overall velocity you need to assign a completed role.");
            averageWIP.setText("To view the overall velocity you need to assign a completed role.");
        }
        else {
            double overallVelocityVal = -1;
            if (overallVelocityWeekArray != null) {
                overallVelocityVal = overallVelocityWeekArray[0] / statisticsModel.getBoard().getActiveWeeks();
            }
            double leadTimeVal = -1;
            if (leadTimeWeekArray != null) {
                int daySum = 0;
                for (int j = 1; j < leadTimeWeekArray.length; j++) {
                    daySum += leadTimeWeekArray[j];
                }
                leadTimeVal = daySum / statisticsModel.getBoard().getActiveWeeks();
            }
            double averageWIPVal = -1;
            if (averageWIPWeekArray != null) {
                averageWIPVal = averageWIPWeekArray[0] / statisticsModel.getBoard().getActiveWeeks();
            }


            if (overallVelocityVal == -1)
                overallVelocity.setText("There's no story points on the Completed Work column yet");
            else {
                overallVelocity.setText(new DecimalFormat("#.0").format(overallVelocityVal) + " story points per week");
                displayLineChartOverallVelocity();
            }

            if (leadTimeVal == -1) leadTime.setText("There's no story points on the Completed Work column yet");
            else {
                leadTime.setText(new DecimalFormat("#.0").format(leadTimeVal) + " days per story point");
                displayLineChartLeadTime();
            }

            if (averageWIPVal == -1) averageWIP.setText("Average WIP is calculated when cards are completed");
            else {
                averageWIP.setText(new DecimalFormat("#.0").format(averageWIPVal) + " story points in WIP per week");
                displayLineChartAverageWIP();
            }
        }
    }

    public void displayLineChartOverallVelocity(){
        XYChart.Series<Number, Number> ovSeries = new XYChart.Series<>();

//        double weeks = statisticsModel.getBoard().getActiveWeeks();

        int visited = 0;
        for(int x= 1; x<overallVelocityWeekArray.length ; x++){
            ovSeries.getData().add(new XYChart.Data<>(x-1,(overallVelocityWeekArray[x]+visited)/(double)x));
            visited += overallVelocityWeekArray[x];
        }

        linechartOverallVelocity.getData().add(ovSeries);
    }

    public void displayLineChartLeadTime(){
        XYChart.Series<Number, Number> leadTimeSeries = new XYChart.Series<>();
        double week = statisticsModel.getBoard().getActiveWeeks();
        int visited = 0;

        for(int x= 1; x<= week ; x++){
            System.out.println("Week: "+ (x-1)+ ", Leadtime: "+ leadTimeWeekArray[x]);
            leadTimeSeries.getData().add(new XYChart.Data<>(x - 1, (leadTimeWeekArray[x] + visited) / (double) x));
            visited += leadTimeWeekArray[x];
        }

        linechartLeadTime.getData().add(leadTimeSeries);
    }

    public void displayLineChartAverageWIP(){
        XYChart.Series<Number, Number> wipSeries = new XYChart.Series<>();

        double week = statisticsModel.getBoard().getActiveWeeks();
        int visited = 0;
        for(int x= 1; x<= week ; x++){
            wipSeries.getData().add(new XYChart.Data<>(x - 1, (averageWIPWeekArray[x] + visited) / (double) (x)));
            visited += averageWIPWeekArray[x];
        }

        linechartAverageWIP.getData().add(wipSeries);
    }




}
