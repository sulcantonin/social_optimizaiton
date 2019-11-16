package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RosenbrockFunction extends Function {
    @Override
    public double valueAt(double[] at) {
        return Math.pow(1 - at[0], 2) + 100 * Math.pow(at[1] - Math.pow(at[0], 2), 2);
    }

    @Override
    public double functionMin() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double functionMax() {
        return 2000;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
