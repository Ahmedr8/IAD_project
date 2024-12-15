package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class EmergencyResponseAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " is ready.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println("Emergency Alert: " + msg.getContent());
                    System.out.println("Notifying hospital...");
                    // Simulate alert action
                } else {
                    block();
                }
            }
        });
    }
}
