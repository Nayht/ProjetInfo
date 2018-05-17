#include <Arduino.h>

#include "MIDI/MIDI.h"

#include "SparkFun_APDS9960.h"
#include "Movement.h"
#include <queue>

#undef DEBUG
#define DEBUG false

#if DEBUG
#include <Metro.h>
#endif

// Pins
#define LEFT_GESTURE_PIN    24
#define RIGHT_GESTURE_PIN   25

constexpr int doubleGestureDelayThreshold = 300;    // ms
constexpr int noteFadeTime = 1000;                  // ms


void leftGestureDetection();
void rightGestureDetection();
Movement handleGesture(SparkFun_APDS9960);
void executeGestureAction(Movement&);
void cancerMIDI();

// Global Variables
SparkFun_APDS9960 leftGestureSensor = SparkFun_APDS9960("Left: ");
SparkFun_APDS9960 rightGestureSensor = SparkFun_APDS9960("Right: ");

int leftGestureFlag = 0;
int rightGestureFlag = 0;
uint8_t proximity_data = 0;
std::queue<std::pair<uint8_t,int>> runningNotes;

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

    /*// Adjust the Proximity sensor gain
    if ( !leftGestureSensor.setProximityGain(PGAIN_1X) ) {
        Serial.println(F("Something went wrong trying to set PGAIN on left sensor"));
    }

    if ( !rightGestureSensor.setProximityGain(PGAIN_1X) ) {
        Serial.println(F("Something went wrong trying to set PGAIN on right sensor"));
    }

    // Start running the APDS-9960 proximity sensor (no interrupts)
    if ( leftGestureSensor.enableProximitySensor(false) ) {
        Serial.println(F("Left proximity sensor is now running"));
    } else {
        Serial.println(F("Left proximity sensor initialization failed"));
    }
    if ( rightGestureSensor.enableProximitySensor(false) ) {
        Serial.println(F("Right proximity sensor is now running"));
    } else {
        Serial.println(F("Right proximity sensor initialization failed"));
    }*/
}

void loop() {

    static long lastGestureTime = -1000;
    static Movement lastMovement = Movement();

#if DEBUG
    static Metro interuptCheck = Metro(1000);
    static long lastLastGestureTime = -1000;
    if(lastLastGestureTime != lastGestureTime)
    {
        Serial.println(lastGestureTime);
        lastLastGestureTime = lastGestureTime;
    }
    if(interuptCheck.check())
    {
        Serial.println("===============");
        Serial.print(digitalRead(LEFT_GESTURE_PIN));
        Serial.print(" - ");
        Serial.println(digitalRead(RIGHT_GESTURE_PIN));
        Serial.println("===============");
    }
#endif

    /**
     * Permet de gérer l'extinction des notes qui ont été lancées
     * A chaque passage de boucle, vérifie la note la plus ancienne
     * Et si son temps d'existence suffisant, envoi le message
     * d'extinction et l'enlève de la queue.
     */
    if(!runningNotes.empty())
    {
        if(millis() - runningNotes.front().second >= noteFadeTime)
        {
            usbMIDI.sendNoteOff(runningNotes.front().first,64,0);
            runningNotes.pop();
        }
    }

    /**
     * Si le temps depuis le dernier mouvement est suffisament grand,
     * l'exécute en tant que mouvement simple et réinitialise le processus
     */
    if(millis()-lastGestureTime >= doubleGestureDelayThreshold && lastMovement)
    {
        // Execute le code correspondant à lastMovement

        executeGestureAction(lastMovement);

        lastMovement = Movement();
        lastGestureTime = millis();
    }

    if(leftGestureFlag || rightGestureFlag)             // Si un mouvement est disponnible à lire
    {
        Movement results;
        digitalWrite(LED_BUILTIN,HIGH);
        if(leftGestureFlag)
        {
            results = handleGesture(leftGestureSensor); // On le récupère

            if(results)                                 // Si ce n'est pas le mouvement nul
            {
                if (millis() - lastGestureTime >= doubleGestureDelayThreshold || !lastMovement)
                {
                    lastGestureTime = millis();         // Si on a dépassé le temps d'attente ou que le mouvement
                    lastMovement = results;             // Précédent était nul, on réactualise
                }
                else if(millis()-lastGestureTime <= doubleGestureDelayThreshold)
                {
                    lastMovement += results;            // Sinon, on combine les mouvements

                    executeGestureAction(lastMovement); // Et on exécute le mouvement combiné

                    lastMovement = Movement();
                    lastGestureTime = millis();
                }
            }

            if(digitalRead(LEFT_GESTURE_PIN) == HIGH)   // Si le capteur a relâché la pin d'interruption
            {                                           // On a plus rien à lire, donc on remet le flag à 0
                leftGestureFlag = 0;
            }
        }

        if(rightGestureFlag)                            // De même, pour le capteur de droite
        {
            results = handleGesture(rightGestureSensor);

            if(results)
            {
                if (millis() - lastGestureTime >= doubleGestureDelayThreshold || !lastMovement)
                {
                    lastGestureTime = millis();
                    lastMovement = results;
                }
                else if(millis()-lastGestureTime <= doubleGestureDelayThreshold)
                {
                    lastMovement += results;

                    executeGestureAction(lastMovement);

                    lastMovement = Movement();
                    lastGestureTime = millis();
                }
            }

            if(digitalRead(RIGHT_GESTURE_PIN) == HIGH)
            {
                rightGestureFlag = 0;
            }
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

// Foutre tout ce qui concerne ce bordel dans une autre classe propre

Movement handleGesture(SparkFun_APDS9960 sensor)
/**
 * @brief Récupération de mouvement du capteur
 *
 * Gère la récupération de l'info de direction du capteur, et la combine
 * a l'information de position du capteur. Elle permet de retourner un
 * mouvement complet (côté+direction), soit le mouvement nul si aucun
 * mouvement valide ne peut être récupéré.
 *
 * @param sensor : Capteur dont on récupère les données
 * @return Mouvement complet si il est valide, mouvement nul sinon
 */
{
#if DEBUG
    Serial.println("Handle");
#endif
    sides side;
    if(sensor.getPrefix() == "Left: ")                  // Assigne le côté du capteur au mouvement
    {
        side = LEFT;
    }
    else if(sensor.getPrefix() == "Right: ")
    {
        side = RIGHT;
    }
    else
    {
        side = NONE;
    }

    if (sensor.isGestureAvailable()) {
#if DEBUG
        Serial.println("Available");
#endif
        switch (sensor.readGesture()) {                 // Lit le mouvement reconnu par le capteur
            case DIR_UP:
                return(Movement(side,DIR_UP));          // Si il est valide, retourne un mouvement complet

            case DIR_DOWN:
                return(Movement(side,DIR_DOWN));

            case DIR_LEFT:
                return(Movement(side,DIR_LEFT));

            case DIR_RIGHT:
                return(Movement(side,DIR_RIGHT));

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
    return(Movement());                                 // Sinon, retourne le mouvement nul
}

void executeGestureAction(Movement& movement)
/**
 * @brief Réalise les actions serial/MIDI correspondantes au mouvement
 *
 * Récupère le string du mouvement passé en argument et l'envoi sur la série
 * A partir de l'identifiant du mouvement, envoie une note MIDI d'une hauteur précise
 * mais de vélocité et de canal prédéfini.
 * La hauteur de la note est créée en utilisant comme point de départ l'ID du côté puis
 * en y additionnant l'ID de la direction du mouvement.
 * La note envoyée est ajoutée à la queue des notes en attente d'extinction.
 *
 * @param movement : Mouvement à exécuter
 */
{
    Serial.println(movement.getString());
    std::pair<sides,directions> actionPair = movement.getMovement();

    uint8_t baseNote = 0;

    switch (actionPair.first) {
        case LEFT:
            baseNote = 20;
            break;
        case RIGHT:
            baseNote = 90;
            break;
        case LEFT_LEFT:
            baseNote = 13;
            break;
        case LEFT_RIGHT:
            baseNote = 30;
            break;
        case RIGHT_RIGHT:
            baseNote = 47;
            break;
        default:
            Serial.println("Problème de côté dans le mouvement traité");
    }

    usbMIDI.sendNoteOn(baseNote+actionPair.second,64,0);
    runningNotes.push(std::make_pair(baseNote+actionPair.second,millis()));
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