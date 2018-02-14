package objects.abstracts;

import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractObject {
    protected double x;
    protected double y;
    protected GraphicsContext gc;

    /** Crée un objet abstrait
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     */
    public AbstractObject(double x, double y, GraphicsContext gc){
        this.x=x;
        this.y=y;
        this.gc=gc;
    }

    /** Renvoie la coordonnée X de l'objet
     * @return x (double)
     */
    public double getX(){ return this.x;}

    /** Renvoie la coordonnée Y de l'objet
     * @return y (double)
     */
    public double getY(){ return this.y;}

    /** Définit la coordonnée X de l'objet
     * @param x coordonnée X à définir
     */
    public void setX(double x){
        this.x=x;
    }

    /** Définit la coordonnée Y de l'objet
     * @param y coordonnée Y à définir
     */
    public void setY(double y){
        this.y=y;
    }

    /** Déplace l'objet d'un certain deltaX et deltaY
     * @param deltaX différence de position en X
     * @param deltaY différence de position en Y
     */
    public void move(double deltaX, double deltaY){
        this.x+=deltaX;
        this.y+=deltaY;
    }

    /** Classe abstraite demandant une fonction d'affichage
     */
    public abstract void display();
    /** Classe abstraite demandans une fonction de mise à jour des données
     */
    public abstract void updateData();

}
