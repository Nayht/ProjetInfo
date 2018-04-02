//
// Created by elektron on 27/03/18.
//


#include <Arduino.h>


void setup() {
    Serial.begin(9600);
    pinMode(13,OUTPUT);
}

void loop() {
    Serial.println("LEFT");
    delay(1000);
}