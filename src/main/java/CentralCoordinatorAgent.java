import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class CentralCoordinatorAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " is ready.");
        // Optionally monitor agent interactions here
    }
}
