package agents.visualisation;

import benchmark.problem.function.Function;
import jade.core.AID;
import ontology.concepts.Position;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.XYZDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContourGraphPanel extends JPanel {

    private final Function function;
    private ChartPanel chartPanel;
    private final Dataset dataset;
    private static final int COUNT_POINTS = 10000;

    public ContourGraphPanel(Function function) {
        this.function = function;
        this.dataset = new Dataset(function, COUNT_POINTS);
        this.chartPanel = new ChartPanel(createChart(dataset));
        this.add(chartPanel);

    }

    private JFreeChart createChart(XYZDataset dataset) {
        NumberAxis xAxis = new NumberAxis("X");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);
        NumberAxis yAxis = new NumberAxis("Y");
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);
        XYBlockRenderer renderer = new XYBlockRenderer();
        PaintScale scale = new GrayPaintScale(this.function.functionMin(), this.function.functionMax());
        renderer.setPaintScale(scale);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(Color.white);
        JFreeChart chart = new JFreeChart(function.getName(), plot);
        chart.removeLegend();
        chart.setBackgroundPaint(Color.white);
        return chart;

    }

    public void renderAgents(List<Position> positions, List<jade.util.leap.List> friends) {
        this.dataset.setAgentPosition(positions);
        this.chartPanel.getChart().getXYPlot().setDataset(this.dataset);

        XYBlockRenderer render = (XYBlockRenderer) this.chartPanel.getChart().getXYPlot().getRenderer();
        render.removeAnnotations();

        for (int i = 0; i < positions.size(); i++) {
            XYTextAnnotation textAnnotation = new XYTextAnnotation(
                    positions.get(i).getAid().getLocalName(),
                    positions.get(i).getX(),
                    positions.get(i).getY());
            textAnnotation.setPaint(Color.red);

            render.addAnnotation(textAnnotation);
        }

        Stroke stroke = new BasicStroke(0.5f);
        for (int i = 0; i < friends.size(); i++) {
            Position sourcePosition = positions.get(i);
            jade.util.leap.List friendsList = friends.get(i);
            for (int j = 0; j < friendsList.size(); j++) {
                AID destinationAgent = (AID) friendsList.get(j);
                Position destinationPosition = null;
                for (int k = 0; k < positions.size(); k++) {
                    if (destinationAgent.equals(positions.get(k).getAid())) {
                        destinationPosition = positions.get(k);
                        break;
                    }
                }

                if (destinationPosition != null) {
                    XYLineAnnotation lineAnnotation = new XYLineAnnotation(
                            sourcePosition.getX(), sourcePosition.getY(),
                            destinationPosition.getX(), destinationPosition.getY(),
                            stroke, Color.yellow
                    );

                    render.addAnnotation(lineAnnotation);
                }

            }
        }


        validate();
        repaint();
    }


}
