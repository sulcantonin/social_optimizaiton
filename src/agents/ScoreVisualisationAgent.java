package agents;


import agents.visualisation.ContourGraphPanel;
import agents.visualisation.ScoreVisualisationPanel;
import benchmark.problem.function.Function;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.actions.TellPosition;
import ontology.concepts.Position;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sulcanto
 * Date: 6/4/13
 * Time: 1:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScoreVisualisationAgent extends AbstractEntityAgent {
    private JFrame frame;
    private ScoreVisualisationPanel graphPanel;
    private Function function;
    public void setup(){


        System.out.println(this.getClass().getSimpleName() + " started " + getAID().toString());
        registerDF("higher-entity", this);
        manager.registerLanguage(codec);
        manager.registerOntology(ontology);

        this.function = (Function) getArguments()[0];
        this.graphPanel = new ScoreVisualisationPanel(function);

        this.frame = new JFrame();
        frame.setTitle(this.getClass().getName());
        frame.add(this.graphPanel);
        frame.setSize(640, 480);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // RECEIVE : TellPosition,
        // sensing positions of agents
        this.addBehaviour(new CyclicBehaviour(this) {
            int state = 0;
            List<AID> humanAgents;
            List<Position> positions;
            List<jade.util.leap.List> friends;

            @Override
            public void action() {

                switch (state) {
                    case 0:
                        this.humanAgents = new ArrayList<AID>();
                        this.positions = new ArrayList<Position>();
                        this.friends = new ArrayList<jade.util.leap.List>();
                        humanAgents.addAll(Arrays.asList(getServiceProviderAgents("human-being", getAgent())));
                        state = 1;
                    case 1:
                        ACLMessage message = receive(informTemplate);
                        if (message != null) {
                            ContentElement element = null;
                            try {
                                element = manager.extractContent(message);
                                Concept action = ((jade.content.onto.basic.Action) element).getAction();
                                if (action instanceof TellPosition) {
                                    humanAgents.remove(message.getSender());
                                    Position position = ((TellPosition) action).getPosition();
                                    jade.util.leap.List friends = ((TellPosition) action).getFriends();
                                    this.positions.add(position);
                                    this.friends.add(friends);
                                }

                            } catch (Codec.CodecException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (OntologyException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                        } else {
                            block();
                        }

                        if (humanAgents.isEmpty()) {
                            graphPanel.addPositions(positions);
                            state = 0;
                        }

                }

            }
        });

    }
}
