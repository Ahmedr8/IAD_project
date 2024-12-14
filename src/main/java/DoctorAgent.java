import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import com.google.gson.Gson;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class DoctorAgent extends Agent {
    private Gson gson = new Gson();
    private String apiKey = "OpenAI key";
    public int i=0;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " is ready.");
        // Add a single behaviour to handle incoming messages
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (msg.getPerformative() == ACLMessage.INFORM && content.startsWith("{")) {
                        System.out.println(getLocalName() + "Received data: " + msg.getContent());
                        HealthData data = gson.fromJson(content, HealthData.class);
                        double heartRate = data.getHeartRate();
                        double temperature = data.getTemperature();
                        if (heartRate > 120 || temperature > 38) {
                            // Send a request to the HistoryAgent for patient history
                            ACLMessage historyRequest = new ACLMessage(ACLMessage.REQUEST);
                            historyRequest.addReceiver(new AID("HistoryAgent", AID.ISLOCALNAME));
                            historyRequest.setContent(data.getPatientId()); // Patient ID
                            send(historyRequest);
                            //i want to display the results of the request

                            System.out.println("Critical condition detected!");
                            ACLMessage alert = new ACLMessage(ACLMessage.REQUEST);
                            alert.addReceiver(new AID("EmergencyResponseAgent", AID.ISLOCALNAME));
                            alert.setContent("Critical Alert for patient: " + msg.getContent());
                            send(alert);
                        }
                    } else if (msg.getPerformative() == ACLMessage.INFORM) {
                        String patientHistory = msg.getContent();
                        System.out.println("Patient History Received: " + content);

                        if(i==0) {
                            String analysisResponse = analyzePatientHistoryWithLLM(patientHistory);
                            System.out.println("LLM Analysis Response: " + analysisResponse);
                            i=i+1;
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }

    // Method to call the LLM API (e.g., OpenAI GPT) for analyzing patient history
    private String analyzePatientHistoryWithLLM(String patientHistory) {
        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Prepare the messages as JSON using Gson
            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", "You are a medical assistant.");

            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", "Analyze the following patient history and determine if the patient is stable or critical: '" + patientHistory + "'");

            JsonArray messagesArray = new JsonArray();
            messagesArray.add(systemMessage);
            messagesArray.add(userMessage);

            // Prepare the final payload
            JsonObject payload = new JsonObject();
            payload.addProperty("model", "gpt-3.5-turbo");
            payload.add("messages", messagesArray);

            // Create the HTTP POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response was successful
            if (response.statusCode() == 200) {
                String responseText = response.body();
                // Extracting the assistant's reply from the response
                String analysisResult = responseText.split("\"content\":\"")[1].split("\"")[0];
                return analysisResult;
            } else {
                return "Error: Received HTTP " + response.body() + " from OpenAI API.";
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error in analyzing patient history";
        }
    }
}

