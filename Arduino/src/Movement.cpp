//
// Created by trotfunky on 16/05/18.
//

#include "Movement.h"

Movement::Movement()
{
    movementSide = NONE;
    movementDirection = DIR_NONE;
}

Movement::Movement(sides side, directions direction)
{
    movementSide = side;
    movementDirection = direction;
}

std::pair<sides,directions> Movement::getMovement()
{
    return(std::make_pair(movementSide,movementDirection));
}

String Movement::getString()
{
    return(sideStringLookup.at(movementSide)+" "+directionStringLookup.at(movementDirection));
}

Movement::operator bool()
{
    return(!(movementSide == NONE && movementDirection == DIR_NONE));
}

void Movement::operator +=(Movement& operand)
{
    std::pair<sides,directions> tempPair = operand.getMovement();

    if(movementSide == LEFT)
    {
        movementDirection = static_cast<directions>(4*(0b100+movementDirection)+tempPair.second);
    }
    else if(movementSide == RIGHT)
    {
        movementDirection = static_cast<directions>(4*(0b100+tempPair.second)+movementDirection);
    }

    movementSide = static_cast<sides>((movementSide|tempPair.first)|(movementSide==tempPair.first));
}