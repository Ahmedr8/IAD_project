import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class DoctorAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " is ready.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println("Received data: " + msg.getContent());
                    String[] data = msg.getContent().split(",");
                    double heartRate = Double.parseDouble(data[0].split(":")[1]);
                    double temperature = Double.parseDouble(data[1].split(":")[1]);

                    if (heartRate > 120 || temperature > 38) {
                        System.out.println("Critical condition detected!");
                        ACLMessage alert = new ACLMessage(ACLMessage.REQUEST);
                        alert.addReceiver(getAID("EmergencyResponseAgent")); // Replace with actual emergency agent name
                        alert.setContent("Critical Alert for patient: " + msg.getContent());
                        send(alert);
                    }
                } else {
                    block();
                }
            }
        });
    }
}
