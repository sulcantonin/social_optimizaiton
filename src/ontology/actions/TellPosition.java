package ontology.actions;

import jade.content.AgentAction;
import jade.util.leap.List;
import ontology.concepts.Position;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TellPosition implements AgentAction {
    private Position position;
    private List friends;


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List getFriends() {
        return this.friends;
    }

    public void setFriends(List friends) {
        this.friends = friends;
    }

}
