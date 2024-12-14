// Data model
import java.time.Instant;
public class HealthData {
    //private Instant timestamp;
    private String patientId;
    private double heartRate;
    private double temperature;

    public HealthData(String patientId, double heartRate, double temperature) {
        this.patientId = patientId;
        //this.timestamp = Instant.now() ;
        this.heartRate = heartRate;
        this.temperature = temperature;
    }

    public String getPatientId() {
        return patientId;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public double getTemperature() {
        return temperature;
    }
}
