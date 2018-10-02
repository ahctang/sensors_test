package com.ascentroboticstest.SensorServerFramework.route;

import com.ascentroboticstest.SensorServerFramework.models.SensorDoublePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensors")
public class ServerApiController {

    Logger logger = LoggerFactory.getLogger(ServerApiController.class);

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Greetings from the Sensor Server Framework");
    }

    @PostMapping("/send-data")
    public ResponseEntity<?> receiveData(@RequestBody SensorDoublePayload<Double> sensorDoublePayload) {
        logger.info("{} - Data received from {} value is \"{}\"", sensorDoublePayload.getDate(), sensorDoublePayload.getOrigin(), sensorDoublePayload.getPayload());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-string-data")
    public ResponseEntity<?> receiveStringData(@RequestBody SensorDoublePayload<String> sensorDoublePayload) {
        logger.info("{} - Data received from {} value is \"{}\"", sensorDoublePayload.getDate(), sensorDoublePayload.getOrigin(), sensorDoublePayload.getPayload());
        return ResponseEntity.ok().build();
    }
}
