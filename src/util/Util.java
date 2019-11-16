package util;

import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/21/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static int[] sort(final double[] array) {
        double[] a = array.clone();
        int[] idx = new int[array.length];

        for (int i = 0; i < a.length; i++) {
            int minIndex = 0;
            for (int j = 0; j < a.length; j++) {
                if (a[minIndex] > a[j]) {
                    minIndex = j;
                }
            }
            a[minIndex] = Double.MAX_VALUE;
            idx[i] = minIndex;
        }

        return idx;
    }

    public static List set2list(Set set) {
        List list = new ArrayList(set.size());
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            list.add(it.next());
        }
        return list;
    }
}
