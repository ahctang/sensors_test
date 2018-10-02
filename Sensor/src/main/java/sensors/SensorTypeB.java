package sensors;

import common.SensorPayload;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sensor Type A is going to simulate being a sensor that generates string values
 **/
public class SensorTypeB extends BasicSensor {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(SensorTypeB.class);
    private boolean sensorState = false;

    public SensorTypeB(SensorConfig config) {
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
                String generatedString = "DummyOutput".concat(String.valueOf(random.nextInt()));
                logger.info("{} generated string data - {}", sensorConfig.getSensorName(), generatedString);
                sendData(new SensorPayload<>(sensorConfig.getSensorName(), generatedString));
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
                    logger.info(String.format("%s Received data: %s", sensorConfig.getSensorName(), data));
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
}
