import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import com.google.gson.Gson;

public class WearableDeviceAgent extends Agent {
    private Gson gson = new Gson();
    private String patientId;
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            patientId = (String) args[0];
            System.out.println("WearableDeviceAgent " + patientId + " is monitoring health data...");
        } else {
            System.out.println("No device ID specified. Terminating.");
            doDelete();
            return;
        }

        addBehaviour(new TickerBehaviour(this, 5000) { // Send data every 5 seconds
            protected void onTick() {

                double heartRate = 60 + Math.random() * 90; // Simulate heart rate
                double temperature = 36 + Math.random()* 5;  // Simulate temperature
                ACLMessage hmsg = new ACLMessage(ACLMessage.INFORM);
                hmsg.addReceiver(new AID("HistoryAgent", AID.ISLOCALNAME));
                hmsg.setContent(gson.toJson(new HealthData(patientId, heartRate, temperature)));
                send(hmsg);
                System.out.println("wearable device :history Data sent: " + hmsg.getContent());
                if(heartRate > 120 || temperature > 38) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(getAID("DoctorAgent"));
                    msg.setContent(gson.toJson(new HealthData(patientId, heartRate, temperature)));                    send(msg);
                    System.out.println("wearable device :emergency Data sent to doctor: " + msg.getContent());
                }
            }
        });
    }
}
