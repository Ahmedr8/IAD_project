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
            AgentController wearableDeviceAgent = container.createNewAgent("WearableDeviceAgent", WearableDeviceAgent.class.getName(), agentArgs);
            AgentController wearableDeviceAgent2 = container.createNewAgent("WearableDeviceAgent2", WearableDeviceAgent.class.getName(), agentArgs2);
            AgentController doctorAgent = container.createNewAgent("DoctorAgent", DoctorAgent.class.getName(), null);
            AgentController emergencyResponseAgent = container.createNewAgent("EmergencyResponseAgent", EmergencyResponseAgent.class.getName(), null);
            AgentController centralCoordinatorAgent = container.createNewAgent("CentralCoordinatorAgent", CentralCoordinatorAgent.class.getName(), null);
            AgentController historyAgent = container.createNewAgent("HistoryAgent", HistoryAgent.class.getName(), null);

            wearableDeviceAgent.start();
            wearableDeviceAgent2.start();
            doctorAgent.start();
            emergencyResponseAgent.start();
            centralCoordinatorAgent.start();
            historyAgent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
