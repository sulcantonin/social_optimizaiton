package ontology.actions;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/21/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakeFriendship implements AgentAction {
    private AID aid;

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }
}
