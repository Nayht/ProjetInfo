//
// Created by trotfunky on 16/05/18.
//

#include <Arduino.h>
#include "enums.h"

#ifndef ARDUINO_MOVEMENT_H
#define ARDUINO_MOVEMENT_H


class Movement {
private:

    sides movementSide;
    directions  movementDirection;

public:

    Movement();
    Movement(sides,directions);

    std::pair<sides,directions> getMovement();
    String getString();
    explicit operator bool();
    void operator +=(Movement&);

};


#endif //ARDUINO_MOVEMENT_H
