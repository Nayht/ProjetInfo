#include <Arduino.h>
#include <Wire.h>
#include "MIDI/MIDI.h"

#include "SparkFun_APDS9960.h"

// Pins
#define APDS9960_INT    2 // Needs to be an interrupt pin

void interruptRoutine();
void handleGesture();
void cancerMIDI();

// Constants

// Global Variables
SparkFun_APDS9960 apds = SparkFun_APDS9960();
int isr_flag = 0;
uint8_t proximity_data = 0;

MIDI_CREATE_DEFAULT_INSTANCE();

void setup() {

    // Set interrupt pin as input
    pinMode(APDS9960_INT, INPUT);

    // Initialize Serial port
    Serial.begin(115200);

    //Initialize MIDI
    MIDI.begin();

    // Initialize interrupt service routine
    attachInterrupt(0, interruptRoutine, FALLING);

    // Initialize APDS-9960 (configure I2C and initial values)
    if ( apds.init() ) {
        Serial.println(F("Initialization complete"));
    } else {
        Serial.println(F("Something went wrong during APDS-9960 init!"));
    }

    // Start running the APDS-9960 gesture sensor engine
    if ( apds.enableGestureSensor(true) ) {
        Serial.println(F("Gesture sensor is now running"));
    } else {
        Serial.println(F("Something went wrong during gesture sensor init!"));
    }

    // Adjust the Proximity sensor gain
    if ( !apds.setProximityGain(PGAIN_1X) ) {
        Serial.println(F("Something went wrong trying to set PGAIN"));
    }

    // Start running the APDS-9960 proximity sensor (no interrupts)
    if ( apds.enableProximitySensor(false) ) {
        Serial.println(F("Proximity sensor is now running"));
    } else {
        Serial.println(F("Something went wrong during sensor init!"));
    }
}

void loop() {
    if(isr_flag==1)
    {
        detachInterrupt(0);
        handleGesture();
        isr_flag = 0;
        attachInterrupt(0,interruptRoutine,FALLING);
    }
}

void interruptRoutine() {
    isr_flag = 1;
}

void handleGesture() {
    if (apds.isGestureAvailable()) {
        switch (apds.readGesture()) {
            case DIR_UP:
                Serial.println("UP");
                break;
            case DIR_DOWN:
                Serial.println("DOWN");
                break;
            case DIR_LEFT:
                Serial.println("LEFT");
                break;
            case DIR_RIGHT:
                Serial.println("RIGHT");
                MIDI.sendNoteOn(88,120,1);
                delay(500);
                MIDI.sendNoteOff(88,120,1);
                break;
            default:
                if (!apds.readProximity(proximity_data)) {
                    Serial.println("Error reading proximity value");
                } else {
                    Serial.print("Proximity: ");
                    Serial.println(proximity_data);
                }
                // Wait 250 ms before next reading (originalement)
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