package sensors;

import java.util.List;

public interface CommonSensorInterface {
    /** Turns the sensor ON **/
    void sensorOn();

    /** Turns the sensor OFF **/
    void sensorOff();

    /** Configures the sensor based on certain parameters **/
    void configure(SensorConfig config);

    /** Get logs from the sensor **/
    List<String> getLog();
}
