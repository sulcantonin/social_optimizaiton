package agents.visualisation;

import benchmark.problem.function.Function;
import ontology.concepts.Position;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: sulcanto
 * Date: 6/4/13
 * Time: 1:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreVisualisationPanel extends JPanel {
    private ChartPanel chartPanel;
    private XYSeriesCollection agentScores;
    private final Function function;
    private static final long START_TIME = System.currentTimeMillis();

    public ScoreVisualisationPanel(Function function){
        this.function = function;
        this.agentScores = createDataset();
        this.chartPanel = createChart(agentScores);
        this.add(chartPanel);
    }

    private XYSeriesCollection createDataset(){
        return new XYSeriesCollection();
    }


    private ChartPanel createChart(XYSeriesCollection score) {
        /*
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Current Agent's value",
                "Time since start",
                "Fitness",
                score,
                true,
                true,
                false
        ); */
        final JFreeChart result = ChartFactory.createXYLineChart(
                "Current Agent's value",
                "Time since start",
                "Fitness",
                score,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        return new ChartPanel(result);
    }

    public void addPositions(List<Position> positions){
        if(this.agentScores.getSeriesCount() == 0){
            for(int i = 0; i < positions.size(); i++){
                this.agentScores.addSeries(new XYSeries(positions.get(i).getAid().getLocalName()));
            }
        }

        for(int i = 0; i < positions.size(); i++){


            this.agentScores.getSeries(
                    positions.get(i).getAid().getLocalName()).add(
                    System.currentTimeMillis()-START_TIME,function.valueAt(positions.get(i).toDoubleArray()));
        }



        this.chartPanel.updateUI();
        this.chartPanel.repaint();

    }

}
