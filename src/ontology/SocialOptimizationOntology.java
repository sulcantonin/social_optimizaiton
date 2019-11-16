package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.AggregateSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;
import ontology.actions.*;
import ontology.concepts.Position;
import ontology.concepts.Samples;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/20/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class SocialOptimizationOntology extends Ontology implements SocialOptimizationVocabulary {
    public static SocialOptimizationOntology getInstance() {
        return ontology;
    }

    public static final String ONTOLOGY_NAME = "Social-optimization-ontology";

    private static final SocialOptimizationOntology ontology = new SocialOptimizationOntology();

    private SocialOptimizationOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(new ConceptSchema(POSITION), Position.class);
            add(new ConceptSchema(SAMPLES), Samples.class);
            add(new AgentActionSchema(TELLPOSITION), TellPosition.class);
            add(new AgentActionSchema(MAKEFRIENDSHIP), MakeFriendship.class);
            add(new AgentActionSchema(CLOSEFRIENDSHIP), CloseFriendship.class);
            add(new AgentActionSchema(REQUESTSAMPLES), RequestSamples.class);
            add(new AgentActionSchema(SENDSAMPLES), SendSamples.class);

            // concepts
            ConceptSchema ps = (ConceptSchema) getSchema(POSITION);
            ps.add(X, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            ps.add(Y, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            ps.add(AID, (ConceptSchema) getSchema(BasicOntology.AID));

            ps = (ConceptSchema) getSchema(SAMPLES);
            ps.add(POSITIONS, (AggregateSchema) getSchema(BasicOntology.SEQUENCE));

            // actions
            AgentActionSchema acs = (AgentActionSchema) getSchema(TELLPOSITION);
            acs.add(POSITION, (ConceptSchema) getSchema(POSITION));
            acs.add(FRIENDS, (AggregateSchema) getSchema(BasicOntology.SEQUENCE));

            acs = (AgentActionSchema) getSchema(MAKEFRIENDSHIP);
            acs.add(AID, (ConceptSchema) getSchema(BasicOntology.AID));

            acs = (AgentActionSchema) getSchema(CLOSEFRIENDSHIP);
            acs.add(AID, (ConceptSchema) getSchema(BasicOntology.AID));

            acs = (AgentActionSchema) getSchema(REQUESTSAMPLES);
            acs.add(AID, (ConceptSchema) getSchema(BasicOntology.AID));

            acs = (AgentActionSchema) getSchema(SENDSAMPLES);
            acs.add(SAMPLES, (ConceptSchema) getSchema(SAMPLES));
            acs.add(AID, (ConceptSchema) getSchema(BasicOntology.AID));
            acs.add(LEADERSHIP, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
        } catch (OntologyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
