package agents;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.SocialOptimizationOntology;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractEntityAgent extends Agent {
    protected ContentManager manager = (ContentManager) getContentManager();
    protected Codec codec = new SLCodec();
    protected Ontology ontology = SocialOptimizationOntology.getInstance();

    protected static void registerDF(String type, Agent agent) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(agent.getAID().getLocalName() + type);
        dfd.addServices(sd);
        try {
            DFService.register(agent, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    protected static AID[] getServiceProviderAgents(String service, Agent agent) {
        AID[] AIDs = null;

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(service);
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] agentDescriptions = DFService.search(agent, dfAgentDescription);
            AIDs = new AID[agentDescriptions.length];
            for (int i = 0; i < agentDescriptions.length; i++) {
                AIDs[i] = agentDescriptions[i].getName();
            }
        } catch (FIPAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return AIDs;
    }

    protected final MessageTemplate informTemplate = MessageTemplate.and(
            MessageTemplate.and(
                    MessageTemplate.MatchLanguage(codec.getName()),
                    MessageTemplate.MatchOntology(ontology.getName())),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );

    protected final MessageTemplate requestTemplate = MessageTemplate.and(
            MessageTemplate.and(
                    MessageTemplate.MatchLanguage(codec.getName()),
                    MessageTemplate.MatchOntology(ontology.getName())),
            MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );

    protected final MessageTemplate cfpTemplate = MessageTemplate.and(
            MessageTemplate.and(
                    MessageTemplate.MatchLanguage(codec.getName()),
                    MessageTemplate.MatchOntology(ontology.getName())),
            MessageTemplate.MatchPerformative(ACLMessage.CFP)
    );
}
