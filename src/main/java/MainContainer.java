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
            AgentController wearableDeviceAgent = container.createNewAgent("WearableDeviceAgent", WearableDeviceAgent.class.getName(), null);
            AgentController doctorAgent = container.createNewAgent("DoctorAgent", DoctorAgent.class.getName(), null);
            AgentController emergencyResponseAgent = container.createNewAgent("EmergencyResponseAgent", EmergencyResponseAgent.class.getName(), null);
            AgentController centralCoordinatorAgent = container.createNewAgent("CentralCoordinatorAgent", CentralCoordinatorAgent.class.getName(), null);

            wearableDeviceAgent.start();
            doctorAgent.start();
            emergencyResponseAgent.start();
            centralCoordinatorAgent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
