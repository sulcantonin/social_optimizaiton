package benchmark.problem.function;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/18/13
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Function {
    public static final double MIN = -15;
    public static final double MAX = 15;

    public abstract double valueAt(double[] at);

    public abstract double functionMin();

    public abstract double functionMax();

    public String getName() {
        return getClass().getSimpleName();
    }

}
