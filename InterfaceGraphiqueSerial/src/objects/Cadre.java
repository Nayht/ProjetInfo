package objects;

import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.AbstractObject;

public class Cadre extends AbstractObject{
    private double width;
    private double height;
    private double arcWidth;
    private double arcHeight;

    /** Crée un cadre
     * @param x coordonnée x du début du cadre
     * @param y coordonnée y du début du cadre
     * @param width largeur du cadre
     * @param height hauteur du cadre
     * @param arcWidth largeur de l'arrondi des coins du cadre
     * @param arcHeight hauteur de l'arrondi des coins du cadre
     * @param gc contexte graphique
     */
    public Cadre(double x, double y, double width, double height, double arcWidth, double arcHeight, GraphicsContext gc){
        super(x,y,gc);
        this.width=width;
        this.height=height;
        this.arcWidth=arcWidth;
        this.arcHeight=arcHeight;
    }

    /** Affiche le cadre
     */
    @Override
    public void display(){
        super.gc.strokeRoundRect(this.x,this.y,this.width,this.height,this.arcWidth,this.arcHeight);
    }

    /** Fonction ici pour satisfaire la classe abstraite
     */
    public void updateData(){}

}
