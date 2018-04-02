//
// Created by elektron on 27/03/18.
//


#include <Arduino.h>


void setup() {
    Serial.begin(9600);
    pinMode(13,OUTPUT);
}

void loop() {
    digitalWrite(13,HIGH);
    Serial.println("test");
    delay(1000);
    digitalWrite(13,LOW);
    Serial.println("test");
    delay(1000);
}