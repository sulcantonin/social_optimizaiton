package ontology.concepts;

import jade.content.Concept;
import jade.util.leap.List;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/23/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Samples implements Concept {
    // list of positions
    private List positions;

    public List getPositions() {
        return positions;
    }

    public void setPositions(List positions) {
        this.positions = positions;
    }
}
