package sensors;

import com.google.gson.*;
import common.SensorPayload;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/** This class is a basic abstract representation of a sensor implementation. All sensor types should implement this class
 * for basic standard functionality.
 */
public abstract class BasicSensor {

    /** Sensor configuration object **/
    protected SensorConfig sensorConfig;

    /** GSON serializer with date class compatibility **/
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();

    /** HTTP Client for sending commands/data to server back end or other sensors **/
    protected CloseableHttpClient client = HttpClients.custom().setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).build();

    /** Light weight http server implementation for receiving commands/data **/
    private Undertow server;

    public abstract void sensorOn();

    public abstract void sensorOff();

    /** Sensor data collection data loop **/
    public abstract void sensorDataLoop();

    /** Override to configure sensor listening behavior **/
    public abstract HttpHandler configureHandlers();

    /** Configure sensor settings **/
    public void configure(SensorConfig config) {
        this.sensorConfig = config;
    }

    /** Retrieve sensor log **/
    public abstract List<String> getLog();

    /**
     * Default implementation to send a given piece of data to the sensor server backend
     **/

    protected void sendData(SensorPayload payload) throws IOException {
        HttpPost httpPost = new HttpPost(sensorConfig.getServerHost() + ":" + sensorConfig.getServerPort() + sensorConfig.getServerDataEndPoint());
        try {
            httpPost.setEntity(new StringEntity(gson.toJson(payload)));
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Unexpected response from backend server");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            httpPost.releaseConnection();
        }
    }

    /**
     * Default implementation for listening to incoming commands
     **/
    protected void startSensorListener() {
        server = Undertow.builder()
                .addHttpListener(sensorConfig.getListenerPort(), "localhost", configureHandlers())
                .setHandler(configureHandlers())
                .build();
        server.start();
    }
}
