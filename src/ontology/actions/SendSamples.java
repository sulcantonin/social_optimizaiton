package ontology.actions;

import jade.content.AgentAction;
import jade.core.AID;
import ontology.concepts.Samples;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/23/13
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SendSamples implements AgentAction {
    private Samples samples;
    private float leadership;
    private AID aid;


    public Samples getSamples() {
        return samples;
    }

    public void setSamples(Samples samples) {
        this.samples = samples;
    }

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public float getLeadership() {
        return leadership;
    }

    public void setLeadership(float leadership) {
        this.leadership = leadership;
    }
}
