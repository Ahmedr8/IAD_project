import Agents.DoctorAgent;
import Agents.EmergencyResponseAgent;
import Agents.HistoryAgent;
import Agents.WearableDeviceAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainContainer {
    public static void main(String[] args) {
        try {
            // Create the JADE runtime
            Runtime runtime = Runtime.instance();

            // Create the main container
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_HOST, "localhost");
            ContainerController container = runtime.createMainContainer(profile);

            // Start agents
            String patientId = "Patient_001";
            Object[] agentArgs = new Object[] { patientId };
            String patientId2 = "Patient_002";
            Object[] agentArgs2 = new Object[] { patientId2 };
            AgentController wearableDeviceAgent = container.createNewAgent("Agents.WearableDeviceAgent", WearableDeviceAgent.class.getName(), agentArgs);
            AgentController wearableDeviceAgent2 = container.createNewAgent("WearableDeviceAgent2", WearableDeviceAgent.class.getName(), agentArgs2);
            AgentController doctorAgent = container.createNewAgent("Agents.DoctorAgent", DoctorAgent.class.getName(), null);
            AgentController emergencyResponseAgent = container.createNewAgent("Agents.EmergencyResponseAgent", EmergencyResponseAgent.class.getName(), null);
            AgentController historyAgent = container.createNewAgent("Agents.HistoryAgent", HistoryAgent.class.getName(), null);

            try {
                wearableDeviceAgent.start();
                Thread.sleep(2000); // 2-second delay between the two patient for better visualization in logs
                wearableDeviceAgent2.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doctorAgent.start();
            emergencyResponseAgent.start();
            historyAgent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
