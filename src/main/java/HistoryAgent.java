import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAgent extends Agent {

    private Map<String, List<HealthData>> patientHistory = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    protected void setup() {
        System.out.println("HistoryAgent " + getLocalName() + " started.");

        // Add behavior to handle messages
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    String sender = msg.getSender().getLocalName();

                    if (msg.getPerformative() == ACLMessage.INFORM) {
                        // Receive and store patient data
                        HealthData data = gson.fromJson(content, HealthData.class);
                        storePatientData(data);
                        System.out.println("Data stored for patient: " + data.getPatientId());
                    } else if (msg.getPerformative() == ACLMessage.REQUEST) {
                        // Retrieve patient history
                        String patientId = content;
                        List<HealthData> history = getPatientHistory(patientId);
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(gson.toJson(history));
                        send(reply);
                        System.out.println("History sent to: " + sender);
                    }
                } else {
                    block();
                }
            }
        });
    }


    // Synchronized method to store patient data
    private synchronized void storePatientData(HealthData data) {
        patientHistory.putIfAbsent(data.getPatientId(), new ArrayList<>());
        patientHistory.get(data.getPatientId()).add(data);
    }


    // Synchronized method to retrieve patient history
    private synchronized List<HealthData> getPatientHistory(String patientId) {
        return patientHistory.getOrDefault(patientId, new ArrayList<>());
    }
}


