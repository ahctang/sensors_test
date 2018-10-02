package sensors;

import common.SensorPayload;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sensor Type A is going to simulate being a sensor that generates float values
 **/
public class SensorTypeA extends BasicSensor {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(SensorTypeA.class);

    private boolean sensorState = false;

    public SensorTypeA(SensorConfig config) {
        this.sensorConfig = config;
        startSensorListener();
    }

    @Override
    public void sensorOn() {
        sensorState = true;
        executorService.execute(this::sensorDataLoop);
    }

    @Override
    public void sensorOff() {
        sensorState = false;
    }

    @Override
    public void sensorDataLoop() {
        while (sensorState) {
            try {
                //Send data at polling interval to server backend
                double fakeData = random.nextDouble();
                logger.info("{} generated double data - {}", sensorConfig.getSensorName(), fakeData);
                sendData(new SensorPayload<>(sensorConfig.getSensorName(), fakeData));

                //Periodically send data to type B sensors
                sendDataToTypeB(Double.toString(fakeData));

            } catch (IOException e) {
                logger.error("{} Error - {}", sensorConfig.getSensorName(), e.getMessage());
            } finally {
                try {
                    Thread.sleep(sensorConfig.getPollingInterval());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public HttpHandler configureHandlers() {
        return Handlers.routing()
                .get("/", exchange -> exchange.getResponseSender().send("Hello World!"))
                .post("/off", (exchange -> {
                    sensorOff();
                    exchange.getResponseSender().send("OK");
                }))
                .post("/on", (exchange -> {
                    sensorOn();
                    exchange.getResponseSender().send("OK");
                }))
                .post("/set-polling-interval/{interval}", (exchange -> {
                    int interval = Integer.parseInt(exchange.getQueryParameters().get("interval").getFirst());
                    sensorConfig.setPollingInterval(interval);
                    exchange.getResponseSender().send(String.format("Setting polling interval to: %d", interval));
                }))
                .post("/send-sample-data/{data}", (exchange -> {
                    String data = exchange.getQueryParameters().get("data").getFirst();
                    logger.error(String.format("%s Received data: %s", sensorConfig.getSensorName(), data));
                    exchange.getResponseSender().send("OK");
                }));
    }

    @Override
    public void configure(SensorConfig config) {
        this.sensorConfig = config;
    }

    @Override
    public List<String> getLog() {
        return null;
    }

    private void sendDataToTypeB(String data) throws IOException {
        HttpPost httpPost = new HttpPost(sensorConfig.getServerHost() + ":" + sensorConfig.getTargetSensorPort() + "/send-sample-data/" + data);
        try {
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Unexpected response from target sensor");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            httpPost.releaseConnection();
        }
    }
}
