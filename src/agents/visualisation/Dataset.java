package agents.visualisation;

import benchmark.problem.function.Function;
import ontology.concepts.Position;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYZDataset;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dataset implements XYZDataset {

    List<Position> positions;
    final private int numAxisElements;
    final private float[] axisValues;
    final private Function function;


    public void setAgentPosition(List<Position> positions) {
        this.positions = positions;

    }

    public Dataset(Function function, int countPoints) {
        this.numAxisElements = (int) Math.sqrt(countPoints);
        this.function = function;
        double delta = (Math.abs(Function.MAX) + Math.abs(Function.MIN)) / numAxisElements;
        this.axisValues = new float[numAxisElements];
        axisValues[0] = (float) Function.MIN;
        for (int i = 1; i < numAxisElements; i++) {
            axisValues[i] = (float) (axisValues[i - 1] + delta);
        }


    }

    public int getSeriesCount() {
        return 1;
    }

    public int getItemCount(int series) {
        return (int) Math.pow(numAxisElements, 2);
    }

    public Number getX(int series, int item) {
        return getXValue(series, item);
    }

    public double getXValue(int series, int item) {
        return axisValues[(item % numAxisElements)];
    }

    public Number getY(int series, int item) {
        return getYValue(series, item);
    }

    public double getYValue(int series, int item) {
        return axisValues[(item / numAxisElements)];
    }

    public Number getZ(int series, int item) {
        return new Double(getZValue(series, item));
    }

    public double getZValue(int series, int item) {
        double x = getXValue(series, item);
        double y = getYValue(series, item);
        return function.valueAt(new double[]{x, y});
    }


    public void addChangeListener(DatasetChangeListener datasetChangeListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeChangeListener(DatasetChangeListener listener) {
        // ignore
    }

    public DatasetGroup getGroup() {
        return null;
    }

    public void setGroup(DatasetGroup group) {
        // ignore
    }

    public Comparable getSeriesKey(int series) {
        return "FITNESS";
    }

    public int indexOf(Comparable seriesKey) {
        return 0;
    }

    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }


}
