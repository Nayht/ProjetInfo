//
// Created by trotfunky on 16/05/18.
//

#ifndef ARDUINO_ENUMS_H
#define ARDUINO_ENUMS_H

#include <map>

enum sides {
    LEFT        = 0b010,
    RIGHT       = 0b100,
    LEFT_LEFT   = LEFT|0b1,
    RIGHT_RIGHT = RIGHT|0b1,
    LEFT_RIGHT  = LEFT|RIGHT,
    NONE        = 0b0
};

enum directions{
    DIR_LEFT            = 0b00,
    DIR_RIGHT           = 0b01,
    DIR_UP              = 0b10,
    DIR_DOWN            = 0b11,

    DIR_LEFT_LEFT       = 4*(0b100+DIR_LEFT)+DIR_LEFT,
    DIR_RIGHT_RIGHT     = 4*(0b100+DIR_RIGHT)+DIR_RIGHT,
    DIR_UP_UP           = 4*(0b100+DIR_UP)+DIR_UP,
    DIR_DOWN_DOWN       = 4*(0b100+DIR_DOWN)+DIR_DOWN,
    DIR_UP_DOWN         = 4*(0b100+DIR_UP)+DIR_DOWN,

    DIR_UP_LEFT         = 4*(0b100+DIR_UP)+DIR_LEFT,
    DIR_UP_RIGHT        = 4*(0b100+DIR_UP)+DIR_RIGHT,
    DIR_DOWN_LEFT       = 4*(0b100+DIR_DOWN)+DIR_LEFT,
    DIR_DOWN_RIGHT      = 4*(0b100+DIR_DOWN)+DIR_RIGHT,
    DIR_LEFT_RIGHT      = 4*(0b100+DIR_LEFT)+DIR_RIGHT,
    DIR_DOWN_UP         = 4*(0b100+DIR_DOWN)+DIR_UP,

    DIR_LEFT_UP         = 4*(0b100+DIR_LEFT)+DIR_UP,
    DIR_RIGHT_UP        = 4*(0b100+DIR_RIGHT)+DIR_UP,
    DIR_LEFT_DOWN       = 4*(0b100+DIR_LEFT)+DIR_DOWN,
    DIR_RIGHT_DOWN      = 4*(0b100+DIR_RIGHT)+DIR_DOWN,
    DIR_RIGHT_LEFT      = 4*(0b100+DIR_RIGHT)+DIR_LEFT,

    DIR_NONE            = 0b100000,

    DIR_NEAR,
    DIR_FAR,
    DIR_ALL
};

const std::map<sides,String> sideStringLookup {
        {LEFT,"L"},{RIGHT,"R"},
        {LEFT_LEFT,"L-L"},{RIGHT_RIGHT,"R-R"},
        {LEFT_RIGHT,"L-R"},
        {NONE,"None"}
};

const std::map<directions,String> directionStringLookup {
        {DIR_LEFT,"Left"},{DIR_RIGHT,"Right"},
        {DIR_LEFT_LEFT,"Double Left"},{DIR_RIGHT_RIGHT,"Double Right"},
        {DIR_LEFT_RIGHT,"Left-Right"},{DIR_RIGHT_LEFT,"Right-Left"},
        {DIR_LEFT_UP,"Left-Up"},{DIR_LEFT_DOWN,"Left-Down"},
        {DIR_RIGHT_UP,"Right-Up"},{DIR_RIGHT_DOWN,"Right-Down"},
        {DIR_UP,"Up"},{DIR_DOWN,"Down"},
        {DIR_UP_UP,"Double Up"},{DIR_DOWN_DOWN,"Double Down"},
        {DIR_UP_DOWN,"Up-Down"},{DIR_DOWN_UP,"Down-Up"},
        {DIR_UP_LEFT,"Up-Left"},{DIR_UP_RIGHT,"Up-Right"},
        {DIR_DOWN_LEFT,"Down-Left"},{DIR_DOWN_RIGHT,"Down-Right"},
        {DIR_NONE,"None"}
};

#endif //ARDUINO_ENUMS_H
