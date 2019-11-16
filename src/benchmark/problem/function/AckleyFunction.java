package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/21/13
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class AckleyFunction extends Function {

    @Override
    public double valueAt(double[] at) {
        at[0] = at[0] - 1;
        at[1] = at[1] + 1;
        double sum1 = 0.0;
        double sum2 = 0.0;

        for (int i = 0; i < at.length; i++) {
            sum1 += (at[i] * at[i]);
            sum2 += (Math.cos(2 * Math.PI * at[i]));
        }

        return (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / ((double) at.length))) -
                Math.exp(sum2 / ((double) at.length)) + 20.0 + Math.E);
    }

    @Override
    public double functionMin() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double functionMax() {
        return 20; //
    }

}
