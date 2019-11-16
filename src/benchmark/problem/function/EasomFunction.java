package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class EasomFunction extends Function {
    @Override
    public double valueAt(double[] at) {
        double x1 = at[0];
        double x2 = at[1];
        return -Math.cos(x1) * Math.cos(x2) * Math.exp(-Math.pow(x1 - Math.PI, 2) - Math.pow(x2 - Math.PI, 2));
    }

    @Override
    public double functionMin() {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double functionMax() {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
