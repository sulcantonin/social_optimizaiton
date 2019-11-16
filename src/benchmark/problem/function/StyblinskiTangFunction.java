package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class StyblinskiTangFunction extends Function {
    @Override
    public double valueAt(double[] at) {
        double x1 = at[0];
        double x2 = at[1];
        return (Math.pow(x1, 4) - 16 * Math.pow(x1, 2) + 5 * x1) / 2 + (Math.pow(x2, 4) - 16 * Math.pow(x2, 2) + 5 * x2) / 2;
    }

    @Override
    public double functionMin() {
        return -50;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double functionMax() {
        return 200;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
