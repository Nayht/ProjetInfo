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
/**
 * @brief Compose deux mouvements pour obtenir un mouvement double
 *
 * La logique de la composition des directions est la même que celle expliquée dans "enums.h",
 * elle est directement appliquée aux directions des deux opérandes (this et operand) et conservée dans
 * this.
 * Pour le côté du mouvement, on effectue un OR bit à bit sur les deux côtés de provenance. Sur le résultat
 * on effectue un autre OR avec le résultat du test d'égalité des deux côtés de provenance. En effet, le
 * booléen résultant de la comparaison est par défaut à 1 si il est vrai et à 0 sinon. Ainsi on peut obtenir
 * les valeurs définies dans "enums.h"
 *
 * @param operand : Mouvement avec lequel on va composer
 */
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