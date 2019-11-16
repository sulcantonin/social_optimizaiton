package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BealeFunction extends Function {

    @Override
    public double valueAt(double[] at) {
        double x = at[0];
        double y = at[1];
        return Math.pow(1.5 - x + x * y, 2) + Math.pow(2.25 - x + x * y * y, 2) + Math.pow(2.625 - x + x * y * y * y, 2);
    }

    @Override
    public double functionMin() {
        return 0;
    }


    @Override
    public double functionMax() {
        return 100;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
