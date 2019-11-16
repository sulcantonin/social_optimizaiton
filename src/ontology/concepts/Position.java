package ontology.concepts;


import jade.content.Concept;
import jade.core.AID;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Position implements Concept {
    private float x;
    private float y;
    private AID aid;

    public Position() {

    }

    public Position(double[] position) {
        this.setX((float) position[0]);
        this.setY((float) position[1]);
    }

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    public double[] toDoubleArray() {
        return new double[]{getX(), getY()};
    }
}

