package enums;

/** Enum contenant les directions possiblement renvoyées par les capteurs
 */
public enum Directions {
    UP("up"),
    RIGHT("right"),
    DOWN("down"),
    LEFT("left"),
    BACK("back"),
    FRONT("front");

    private String direction;
    Directions(String direction){
        this.direction=direction;
    }

    @Override
    public String toString(){
        return this.direction;
    }

    /** Renvoie la direction correspondante à l'élément de l'énum sous forme de chaîne de caractère
     * @return direction (string)
     */
    public String getDirection(){
        return this.direction;
    }
}
