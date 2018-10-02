package sensors;

public class SensorConfig {

    /** Backend Server for sending data **/
    private String serverHost;
    private int serverPort;
    private String serverDataEndPoint;

    /** Port that the sensor is listening on to receive data **/
    private int listenerPort;

    /** Sensor config **/
    private int pollingInterval;
    private String sensorName;

    /** Other sensors to communicate with **/
    private int targetSensorPort;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerDataEndPoint() {
        return serverDataEndPoint;
    }

    public void setServerDataEndPoint(String serverDataEndPoint) {
        this.serverDataEndPoint = serverDataEndPoint;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getTargetSensorPort() {
        return targetSensorPort;
    }

    public void setTargetSensorPort(int targetSensorPort) {
        this.targetSensorPort = targetSensorPort;
    }
}
