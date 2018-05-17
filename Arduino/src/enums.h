//
// Created by trotfunky on 16/05/18.
//

#ifndef ARDUINO_ENUMS_H
#define ARDUINO_ENUMS_H

#include <map>


/**
 * Les côtés Left et Right ont des ID uniques, qui permettent de construire les IDs des côtés de mouvements
 * composites. Un bit est ajouté en première position pour indiquer qu'on a un mouvement double.
 */
enum sides {
    LEFT        = 0b010,
    RIGHT       = 0b100,
    LEFT_LEFT   = LEFT|0b1,
    RIGHT_RIGHT = RIGHT|0b1,
    LEFT_RIGHT  = LEFT|RIGHT,
    NONE        = 0b0
};

/**
 * Les mouvements élémentaires (Up,Down,Left,Right) ont chacun un ID unique sur 2 bits.
 * Les IDs des mouvements composites sont obtenus par concaténation des IDs des deux mouvements
 * qui le composent et l'ajout d'un 5ème bit qui indique la nature composée du mouvement.
 *
 * Exemple: le mouvement Up-Down
 *
 * On a d'une part l'ID de Up et d'autre celui de Down
 * Up : 0b10    Down : 0b11
 *
 * Pour concaténer, on va simplement décaler de deux bits vers la gauche l'ID de la première direction:
 * Up : 0b1000  Down : 0b0011
 *
 * Puis les additionner et rajouter le bit de composition:
 * Up-Down : 0b01011 + 0b10000
 * Up-Down : 0b11011
 *
 * En pratique, on ajoute directement 0b100 à la première direction.
 */
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

    DIR_NONE            = 0b100000, // N'est pas zéro pour faciliter les calculs avec les ID

    DIR_NEAR,
    DIR_FAR,
    DIR_ALL
};

// Les map de lookup permettent de récupérer directement le string associé à chaque partie du mouvement
// pour toute utilisation ultérieure et entre autre envoi dans la série

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
