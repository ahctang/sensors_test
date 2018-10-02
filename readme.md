# Ascent Robotics Sensor Test Solution

## Prerequisites
1. Please make sure you are connected to the internet
2. Java SDK v1.8.x

## Project Breakdown
- The mock sensor server backend is implemented in the ServerSensorFramework project.
- The mock sensors are implemented and orchestrated by the Sensor project.

The sensors communicate with each other and the server and receive commands using HTTP. There are two types of sensors, 
Type A and Type B. Type A sensors generate random float values and also send these values to Type B sensors. Type B 
sensors generate string values. Both types of sensors send their generate values to the server backend.

The server backend logs all incoming requests so the data flow is visible.


## Running the project
1. Open a terminal and navigate to the root folder of the project.
2. Execute the following command to start the server:

    MacOS: 
    ```
    ./gradlew bootRun
    ```

    Windows:
    ```
    gradlew.bat bootRun
   ```

3. Open a new terminal and navigate to the root folder of the project again.
4.  Execute the following command to start the sensors:
    
    MacOS: 
    ```
    ./gradlew run
    ```

    Windows:
    ```
    gradlew.bat run
    ```

## Controlling the sensors at run time
By default, there are 4 sensors and they are listening on ports 3939, 3940, 3941, 3942. You can control the sensors by issuing http requests to these ports using CURL or Postman.

e.g. POST http://localhost:3939/off will turn the sensor OFF.

### Sensor listeners
/on: Turns the sensor on

/off: Turns the sensor off

/set-polling-interval/{interval}: Changes the sensor polling interval to the provided interval in ms.

/send-sample-data/{data}: Sends a string to the sensor

## Logging
All of the logs are present in the in the logs folder in the root folder of the project. 

*sensor.log* contains the logs for all of the sensors. The log shows that sensors generate data at fixed intervals, and that they can also receive data from other sensors.  

*sensor_server_framework.log* contains the logs for the server backend. The log shows the information that is transmitted from the sensors to the server.