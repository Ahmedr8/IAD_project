import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class WearableDeviceAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " is ready.");

        addBehaviour(new TickerBehaviour(this, 5000) { // Send data every 5 seconds
            protected void onTick() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(getAID("DoctorAgent")); // Replace with actual doctor agent name
                double heartRate = 60 + Math.random() * 90; // Simulate heart rate
                double temperature = 36 + Math.random();  // Simulate temperature
                msg.setContent("HeartRate:" + heartRate + ",Temperature:" + temperature);
                send(msg);
                System.out.println("Data sent: " + msg.getContent());
            }
        });
    }
}
