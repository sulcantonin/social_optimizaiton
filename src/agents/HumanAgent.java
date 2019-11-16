package agents;


import benchmark.problem.function.Function;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.SocialOptimizationVocabulary;
import ontology.actions.*;
import ontology.concepts.Position;
import ontology.concepts.Samples;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import util.CyclicStack;
import util.Functions;
import util.Util;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/18/13
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class HumanAgent extends AbstractEntityAgent implements SocialOptimizationVocabulary {

    private static final int SAMPLE_POINTS = 5;
    private static final int MAX_MEMORY = 10;

    private final double[] initialMean = new double[]
            {(float) ((Math.random() - 0.5) * (Math.abs(Function.MAX) + Math.abs(Function.MIN))),
                    (float) ((Math.random() - 0.5) * (Math.abs(Function.MAX) + Math.abs(Function.MIN)))};
    private double[][] cov = new double[][]{
            new double[]{10 * Math.random(), 0},
            new double[]{0, 10 * Math.random()}
    };

    private Function function;

    MultivariateNormalDistribution mvd;


    private Set<AID> friends;

    private double rationality;
    private double memory;
    private double leadership;
    private double individuality;
    private double sociability;
    private double creativity;

    private CyclicStack<Position> position;

    public HumanAgent() {
        this.rationality = Math.random();
        this.memory = Math.random();
        this.leadership = Math.random();
        this.individuality = Math.random();
        this.sociability = Math.random();
        this.creativity = Math.random();

        // friends
        this.friends = new HashSet<AID>();

        // setting up position
        this.position = new CyclicStack<Position>((int) (memory * MAX_MEMORY));
        this.mvd = new MultivariateNormalDistribution(initialMean, cov);
        this.position.add(new Position(mvd.sample()));
    }

    private static double[][] sample(double[] means, double[][] cov, double creativity, int numSamples) {
        double[][] newSamples = new double[numSamples][];
        RealMatrix C = MatrixUtils.createRealMatrix(cov);
        C = C.scalarMultiply(creativity);
	MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, C.getData());
        for (int s = 0; s < numSamples; s++) {
            newSamples[s] = mvd.sample();
        }
        return newSamples;

    }

    private static Samples double2SampleClass(double[][] doubleSamples, AID aid) {
        jade.util.leap.List positions = new jade.util.leap.ArrayList(doubleSamples.length);
        for (int i = 0; i < doubleSamples.length; i++) {
            Position position = new Position();
            position.setX((float) doubleSamples[i][0]);
            position.setY((float) doubleSamples[i][1]);
            position.setAid(aid);
            positions.add(position);
        }
        Samples samples = new Samples();
        samples.setPositions(positions);
        return samples;
    }

    public void setup() {
        registerDF("human-being", this);
        manager.registerLanguage(codec);
        manager.registerOntology(ontology);

        // getting arguments
        function = (Function) getArguments()[0];

        /*
        // sampling - samples new points
        addBehaviour(new TickerBehaviour(this,1000) {
            @Override
            protected void onTick() {

                // CREATIVITY: multipliing covariance matrix with
                // amount of creativity assigned to agent
                RealMatrix C = MatrixUtils.createRealMatrix(cov);
                C.scalarMultiply(creativity);
                mvd = new MultivariateNormalDistribution(position,C.getData());

                double [][] newSamples = new double[SAMPLE_POINTS][];
                double [] f = new double[SAMPLE_POINTS];
                int minIndex = 0;

                for(int i = 0; i < SAMPLE_POINTS; i++){
                    newSamples[i] = mvd.sample();
                    f[i] = function.valueAt(newSamples[i]);
                }

                int [] idx = Util.sort(f);

                // RATIONALITY : chooses index according to agent's rationality
                // the higher value of the rationality is the agent's preferences
                // the better values from sample are chosen
                int agentRationalIndex = (int) (idx.length*(1-rationality));
                position = newSamples[agentRationalIndex];

                // MEMORY : ?
                // TODO: to be implemented, don't know how to interpret it
            }
        });
        */
        // INFORM : TellPosition
        // tells other agents about presence
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                // send this to all human-being agents
                AID[] aids = getServiceProviderAgents("human-being", this.getAgent());
                for (AID aid : aids) {
                    if (!aid.equals(getAID()))  // skip agent itself
                        message.addReceiver(aid);
                }
                // inform god
                aids = getServiceProviderAgents("higher-entity", this.getAgent());
                for(int i = 0; i < aids.length; i++){
                    message.addReceiver(aids[i]);
                }

                message.setSender(getAID());
                message.setLanguage(codec.getName());
                message.setOntology(ontology.getName());

                Position positionConcept = position.peek();
                positionConcept.setAid(getAID());

                TellPosition tellPosition = new TellPosition();
                tellPosition.setPosition(positionConcept);
                tellPosition.setFriends(Util.set2list(friends));


                Action action = new Action(getAID(), tellPosition);
                try {
                    manager.fillContent(message, action);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OntologyException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                send(message);
            }
        });

        // RECEIVE : INFORM : TellPosition
        // TellPosition
        this.addBehaviour(new CyclicBehaviour(this) {

            MessageTemplate cfpReply = null;

            public void action() {

                ACLMessage message = receive(informTemplate);
                if (message != null) {

                    ContentElement element = null;
                    try {
                        element = manager.extractContent(message);
                        Concept action = ((Action) element).getAction();
                        if (action instanceof TellPosition) {
                            Position position = ((TellPosition) action).getPosition();

                            // avoiding to make friendship with agent itself
                            if (position.getAid().equals(getAID())) {
                                return;
                            }

                            // SOCIABILITY : acceptance of new friendship
                            // if sociability is high then agent accepts new friendships
                            if (sociability > Math.random()) {

                                // SOCIABILITY : agent accepted to form new friendship,
                                // CFP the other agent for friendship
                                message = message.createReply();
                                message.setPerformative(ACLMessage.CFP);
                                message.setReplyWith("" + System.currentTimeMillis());

                                MakeFriendship friendship = new MakeFriendship();
                                friendship.setAid(getAID());
                                Action friendshipAction = new Action(getAID(), friendship);
                                manager.fillContent(message, friendshipAction);
                                send(message);

                                cfpReply = MessageTemplate.MatchInReplyTo(message.getReplyWith());

                                addBehaviour(new SimpleBehaviour() {
                                    boolean receivedReply = false;

                                    @Override
                                    public void action() {
                                        ACLMessage message = receive(cfpReply);
                                        if (message != null) {
                                            try {
                                                ContentElement element = null;
                                                element = manager.extractContent(message);
                                                Concept action = ((Action) element).getAction();
                                                if (action instanceof MakeFriendship) {
                                                    // friendship accepted
                                                    if (message.getPerformative() == ACLMessage.PROPOSE) {
                                                        friends.add(((MakeFriendship) action).getAid());
                                                    } // friendship rejected
                                                    else {

                                                    }
                                                    receivedReply = true;
                                                }
                                            } catch (Codec.CodecException e) {
                                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                            } catch (OntologyException e) {
                                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                            }
                                        } else {
                                            block();
                                        }
                                    }

                                    @Override
                                    public boolean done() {
                                        return receivedReply;
                                    }
                                });
                            }
                        }

                    } catch (Codec.CodecException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (OntologyException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                } else {
                    block();
                }
            }
        });


        // SOCIABILITY
        // CFP : MakeFriendship
        // RECEIVE proposals from the other agents about their position
        this.addBehaviour(new CyclicBehaviour(this) {

            @Override
            public void action() {
                ACLMessage message = receive(cfpTemplate);
                // new
                if (message != null) {
                    ContentElement element = null;
                    try {
                        element = manager.extractContent(message);
                        Concept action = ((Action) element).getAction();
                        if (action instanceof MakeFriendship) {

                            // SOCIABILITY : agent accepted proposal for friendship
                            if (sociability > Math.random()) {
                                message = message.createReply();
                                message.setPerformative(ACLMessage.PROPOSE);

                                MakeFriendship makeFriendship = new MakeFriendship();
                                makeFriendship.setAid(getAID());
                                Action friendshipAction = new Action(getAID(), makeFriendship);
                                manager.fillContent(message, friendshipAction);
                                send(message);
                            } // SOCIABILITY : agent rejected proposal for friendship
                            else {
                                message = message.createReply();
                                message.setPerformative(ACLMessage.REFUSE);
                                MakeFriendship makeFriendship = new MakeFriendship();
                                makeFriendship.setAid(getAID());
                                Action friendshipAction = new Action(getAID(), makeFriendship);
                                manager.fillContent(message, friendshipAction);
                                send(message);
                                send(message);
                            }

                        }

                    } catch (UngroundedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (OntologyException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }


                }


            }
        });

        // INFORM : closing friendship
        // SEND
        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            protected void onTick() {
                for (Iterator<AID> aidIterator = friends.iterator(); aidIterator.hasNext(); ) {
                    if (sociability < Math.random()) {
                        AID friendsAID = aidIterator.next();
                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);

                        message.addReceiver(friendsAID);
                        message.setLanguage(codec.getName());
                        message.setOntology(ontology.getName());
                        CloseFriendship closeFriendship = new CloseFriendship();
                        closeFriendship.setAid(getAID());
                        Action action = new Action(getAID(), closeFriendship);
                        try {
                            manager.fillContent(message, action);
                            send(message);
                        } catch (Codec.CodecException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (OntologyException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
        });

        // INFOROM : CloseFriendship
        // RECEIVE
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage message = receive(informTemplate);
                if (message != null) {
                    try {
                        ContentElement element = manager.extractContent(message);
                        Concept action = ((Action) element).getAction();
                        if (action instanceof CloseFriendship) {
                            synchronized (friends) {
                                friends.remove(((CloseFriendship) action).getAid());
                            }
                        }
                    } catch (UngroundedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (OntologyException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                } else {
                    block();
                }

            }
        });


        // SEND : REQUEST : RequestSamples
        // send request for samples to friends and wait until all agent not reply
        addBehaviour(new CyclicBehaviour(this) {
            MessageTemplate messageTemplate;
            List<AID> requestedFriends = new ArrayList<AID>(friends.size());
            List<Samples> receivedSamples = new ArrayList<Samples>(friends.size());
            List<Float> leaderships = new ArrayList<Float>(friends.size());

            private int step = 0;
            private int stepCounter = 0;
            private final int MAX_STEPS = 100; // maximal amount of steps if agent receive proceudre get stucked


            @Override
            public void action() {
                switch (step) {
                    case 0:
                        if (friends.isEmpty())
                            break;
                        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                        for (Iterator<AID> it = friends.iterator(); it.hasNext(); ) {
                            AID friendAID = it.next();
                            request.addReceiver(friendAID);
                            requestedFriends.add(friendAID);
                        }
                        request.setLanguage(codec.getName());
                        request.setOntology(ontology.getName());
                        request.setReplyWith("" + System.currentTimeMillis());

                        messageTemplate = MessageTemplate.and(
                                requestTemplate,
                                MessageTemplate.MatchInReplyTo(request.getReplyWith())
                        );

                        RequestSamples requestSamples = new RequestSamples();
                        requestSamples.setAid(getAID());
                        Action requestSamplesAction = new Action(getAID(), requestSamples);
                        try {
                            manager.fillContent(request, requestSamplesAction);
                        } catch (Codec.CodecException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (OntologyException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        send(request);
                        step = 1;
                        break;

                    case 1:
                        ACLMessage reply = receive(messageTemplate);
                        if (reply != null) {
                            ContentElement element = null;
                            try {
                                element = manager.extractContent(reply);
                                Concept action = ((Action) element).getAction();
                                if (action instanceof SendSamples) {
                                    receivedSamples.add(((SendSamples) action).getSamples());
                                    leaderships.add(((SendSamples) action).getLeadership());
                                    requestedFriends.remove(((SendSamples) action).getAid());
                                    if (requestedFriends.isEmpty()) {
                                        int numSamples = SAMPLE_POINTS;
                                        for (int i = 0; i < receivedSamples.size(); i++) {
                                            numSamples += receivedSamples.get(i).getPositions().size();
                                        }

                                        double[][] thisSamples = sample(position.peek().toDoubleArray(), cov, creativity, SAMPLE_POINTS);
                                        double[][] allSamples = new double[numSamples][];
                                        double[] f = new double[numSamples];



                                        // samples of this agent
                                        int k = 0;
                                        for (int i = 0; i < thisSamples.length; i++) {
                                            allSamples[k] = thisSamples[i];
                                            f[k] = leadership * function.valueAt(allSamples[k]);
                                            k++;
                                        }


                                        // samples from friends
                                        for (int i = 0; i < receivedSamples.size(); i++) {
                                            for (int j = 0; j < receivedSamples.get(i).getPositions().size(); j++) {
                                                allSamples[k] = ((Position) receivedSamples.get(i).getPositions().get(j)).toDoubleArray();
                                                f[k] = leaderships.get(i) * function.valueAt(allSamples[k]);
                                                k++;
                                            }
                                        }
					
					double [] w = new double[allSamples.length];
					double wSum = 0;
					for (int i = 0; i < allSamples.length; i++)
					{
						wSum += f[i];
					}
					// normalization;
					for (int i = 0; i < w.length; i++)
					{
						w[i] = 1-f[i] / wSum;						
					}
					double [] wMean = new double[2];
					// weighted mean
					for (int i = 0; i < allSamples.length; i++)
					{
						wMean[0] = (1-rationality/2) * w[i] * allSamples[i][0];					
						wMean[1] = (1-rationality/2) * w[i] * allSamples[i][1];
					}
					position.add(new Position(wMean)); 
					/*
					// obsolete
                                        int[] fitnessOrderIdx = Util.sort(f);
                                        // RATIONALITY
                                        // according to agent's rationality choosing corresponding position
                                        int chosenIndex = fitnessOrderIdx[((int) ((1 - Math.abs(rationality)) * (fitnessOrderIdx.length - 1)))];
                                        position.add(new Position(allSamples[chosenIndex]));
                                        // cov = Functions.calculateWeightedCov(allSamples,f);
					*/
                                        step = 0;
                                    }
                                }
                            } catch (UngroundedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (Codec.CodecException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (OntologyException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        } else {
                            block();
                        }
                        if (requestedFriends.isEmpty()) {
                            requestedFriends.clear();
                            receivedSamples.clear();
                            step = 0;
                        }

                        if (stepCounter++ > MAX_STEPS) {
                            this.requestedFriends.clear();
                            this.receivedSamples.clear();
                            stepCounter = 0;
                            step = 0;

                        }

                        break;
                }
            }
        });

        // RECEIVE : RequestSamples and SEND : SendSamples
        // if agent receive request for samples then reply with Samples to agent
        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            public void onTick() {
                ACLMessage requestSamplesMessage = receive(requestTemplate);
                if (requestSamplesMessage != null) {

                    ContentElement element = null;
                    try {
                        element = manager.extractContent(requestSamplesMessage);
                        Concept action = ((Action) element).getAction();
                        if (action instanceof RequestSamples) {

                            requestSamplesMessage = requestSamplesMessage.createReply();
                            SendSamples sendSamples = new SendSamples();
                            sendSamples.setAid(getAID());
                            sendSamples.setSamples(HumanAgent.double2SampleClass(sample(position.peek().toDoubleArray(), cov, creativity, SAMPLE_POINTS), getAID()));
                            sendSamples.setLeadership((float) leadership);
                            Action sendSamplesAction = new Action(getAID(), sendSamples);
                            manager.fillContent(requestSamplesMessage, sendSamplesAction);
                            send(requestSamplesMessage);
                        }

                    } catch (UngroundedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (OntologyException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    block();
                }

            }
        });

        this.addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                Position first = position.first();
                Position last = position.last();
                double firstVal = function.valueAt(first.toDoubleArray());
                double lastVal = function.valueAt(last.toDoubleArray());

                double[] history = new double[position.size()];
                int i = 0;
		
                for (Iterator<Position> it = position.iterator(); it.hasNext(); history[i++] = function.valueAt(it.next().toDoubleArray()))
			;

		// calculating mean and obtaining weights
		double [] mean = new double[2];
		double [] w = new double[position.size()];
		double wSum = 0;
		i=0;
                for (Iterator<Position> it = position.iterator(); it.hasNext();){
			Position pos = it.next();
			mean[0] += pos.getX();
			mean[1] += pos.getY();
			w[i] = function.valueAt(pos.toDoubleArray());
			wSum += w[i];
			i++;
		}

		// normalizing weights
		for(i = 0; i < w.length; i++)
		{
			w[i] = w[i] / wSum;
		}

		
                rationality = (double) rationalityIterations / (++rationalityIterations);
		leadership = Functions.sigmoid(history[0] - history[history.length-1],rationalityIterations);	

		// calculating covariance
		i=0;
                for (Iterator<Position> it = position.iterator(); it.hasNext();){
			Position pos = it.next();
			cov[0][0] = Math.max(1,(1-w[i])*(pos.getX() - mean[0]) * (pos.getX() - mean[0])/position.size());
			cov[1][1] = Math.max(1,(1-w[i])*(pos.getY() - mean[1]) * (pos.getY() - mean[1])/position.size());
			i++;
		}
		
                System.out.println("rationality:" + rationality + ", lead:" + leadership + " cov x=" + cov[0][0] + ", y=" + cov[1][1]);
            }
        });

    }

    private static long rationalityIterations = 0;
}
