package benchmark.problem.function;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/18/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuadraticFunction extends Function {


    final double[] coefficients;

    public QuadraticFunction(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public QuadraticFunction(int dimension) {
        coefficients = new double[dimension];
        Arrays.fill(coefficients, 1);
    }

    @Override
    public double valueAt(double[] at) {
        double val = 1;
        for (int i = 0; i < at.length; i++) {
            val += coefficients[i] * Math.pow(at[i], 2);
        }
        return val;
    }

    @Override
    public double functionMin() {
        return 0;
    }

    @Override
    public double functionMax() {
        return valueAt(new double[]{MAX, MAX});
    }

}
