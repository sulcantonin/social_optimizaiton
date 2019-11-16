package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SchafferFunctionN2 extends Function {
    @Override
    public double valueAt(double[] at) {
        double x = at[0];
        double y = at[1];
        return 0.5 + (Math.pow(Math.sin(Math.sqrt(x * x - y * y)), 2) - 0.5) / Math.pow(1 + 0.001 * (x * x + y * y), 2);

    }

    @Override
    public double functionMin() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double functionMax() {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
