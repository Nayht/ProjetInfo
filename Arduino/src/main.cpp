#include <Arduino.h>
#include "MIDI/MIDI.h"

#include "SparkFun_APDS9960.h"

// Pins
#define LEFT_GESTURE_PIN    24 // Needs to be an interrupt pin
#define RIGHT_GESTURE_PIN 25

void leftGestureDetection();
void rightGestureDetection();
void handleGesture(SparkFun_APDS9960);
void cancerMIDI();

// Constants

// Global Variables
SparkFun_APDS9960 leftGestureSensor = SparkFun_APDS9960("Left: ");
SparkFun_APDS9960 rightGestureSensor = SparkFun_APDS9960("Right: ");

int leftGestureFlag = 0;
int rightGestureFlag = 0;
uint8_t proximity_data = 0;

MIDI_CREATE_DEFAULT_INSTANCE();

void setup() {
    pinMode(LED_BUILTIN,OUTPUT);
    digitalWrite(LED_BUILTIN,HIGH);
    delay(2000);
    digitalWrite(LED_BUILTIN,LOW);

    // Set interrupt pin as input
    pinMode(LEFT_GESTURE_PIN, INPUT);
    pinMode(RIGHT_GESTURE_PIN, INPUT);

    // Initialize Serial port
    Serial.begin(115200);

    //Initialize MIDI
//    MIDI.begin();

    // Initialize interrupt service routine
    attachInterrupt(LEFT_GESTURE_PIN, leftGestureDetection, FALLING);
    attachInterrupt(RIGHT_GESTURE_PIN, rightGestureDetection,FALLING);


    // Initialize APDS-9960 (configure I2C and initial values)
    if ( leftGestureSensor.init() ) {
        Serial.println(F("Left sensor initialization complete"));
    } else {
        Serial.println(F("Left sensor initialization failed"));
    }

    if ( rightGestureSensor.init(1) ) {
        Serial.println(F("Right sensor initialization complete"));
    } else {
        Serial.println(F("Right sensor initialization failed"));
    }

    // Start running the APDS-9960 gesture sensor engine
    if ( leftGestureSensor.enableGestureSensor(true) ) {
        Serial.println(F("Left gesture sensor is now running"));
    } else {
        Serial.println(F("Left gesture initilization failed"));
    }

    if ( rightGestureSensor.enableGestureSensor(true) ) {
        Serial.println(F("Right gesture sensor is now running"));
    } else {
        Serial.println(F("Right gesture initilization failed"));
    }

    // Adjust the Proximity sensor gain
    if ( !leftGestureSensor.setProximityGain(PGAIN_1X) ) {
        Serial.println(F("Something went wrong trying to set PGAIN on left sensor"));
    }

    if ( !rightGestureSensor.setProximityGain(PGAIN_1X) ) {
        Serial.println(F("Something went wrong trying to set PGAIN on right sensor"));
    }

    // Start running the APDS-9960 proximity sensor (no interrupts)
    if ( rightGestureSensor.enableProximitySensor(false) ) {
        Serial.println(F("Left proximity sensor is now running"));
    } else {
        Serial.println(F("Right proximity sensor initialization failed"));
    }
}

void loop() {
    if(leftGestureFlag || rightGestureFlag)
    {
        digitalWrite(LED_BUILTIN,HIGH);
        if(leftGestureFlag)
        {
            detachInterrupt(LEFT_GESTURE_PIN);
            handleGesture(leftGestureSensor);
            leftGestureFlag = 0;
            attachInterrupt(LEFT_GESTURE_PIN,leftGestureDetection,FALLING);
        }
        if(rightGestureFlag)
        {
            detachInterrupt(RIGHT_GESTURE_PIN);
            handleGesture(rightGestureSensor);
            rightGestureFlag = 0;
            attachInterrupt(RIGHT_GESTURE_PIN,rightGestureDetection,FALLING);
        }
        digitalWrite(LED_BUILTIN,LOW);
    }
}

void leftGestureDetection() {
    leftGestureFlag = 1;
}

void rightGestureDetection() {
    rightGestureFlag = 1;
}

void handleGesture(SparkFun_APDS9960 sensor) {
//    Serial.println("Handle");
    if (sensor.isGestureAvailable()) {
//        Serial.println("Available");
        switch (sensor.readGesture()) {
            case DIR_UP:
                Serial.print(sensor.getPrefix());
                Serial.println("UP");
                break;
            case DIR_DOWN:
                Serial.print(sensor.getPrefix());
                Serial.println("DOWN");
                break;
            case DIR_LEFT:
                Serial.print(sensor.getPrefix());
                Serial.println("LEFT");
                break;
            case DIR_RIGHT:
                Serial.print(sensor.getPrefix());
                Serial.println("RIGHT");
                break;
            default:
                /*if (!apds.readProximity(proximity_data)) {
                    Serial.println("Error reading proximity value");
                } else {
                    Serial.print("Proximity: ");
                    Serial.println(proximity_data);
                }
                // Wait 250 ms before next reading (originalement)
                */
                delay(10);
        }
    }
}

void cancerMIDI()
{
    long note = random(10,66);
    MIDI.sendProgramChange(random(0,200),1);
    MIDI.sendNoteOn(note,200,1);
    delay(500);
    for(float f = 0.2f;f<=0.6;f+=0.1f)
    {
        MIDI.sendPitchBend(f,1);
        delay(100);
    }
    delay(500);
    MIDI.sendControlChange(64,127,1);
    for(int i = 15;i<=80;i+=5)
    {
        MIDI.sendControlChange(91,i,1);

    }
    delay(2000);
    MIDI.sendControlChange(64,0,1);
    delay(500);
    for(float f = 0.6f;f>=-0.6;f-=0.2f)
    {
        MIDI.sendPitchBend(f,1);
        delay(100);
    }
    MIDI.sendControlChange(91,0,1);
    MIDI.sendNoteOff(note,200,1);
    MIDI.sendPitchBend(0,1);
    delay(1000);
}