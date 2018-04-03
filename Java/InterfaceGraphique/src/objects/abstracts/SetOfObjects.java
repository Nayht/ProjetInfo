package objects.abstracts;

import enums.Directions;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import objects.Cadre;

import java.util.ArrayList;

public class SetOfObjects {
    private double x;
    private double y;
    private int nbPanels;
    private int currentPanel;
    private int aimX;
    private int aimY;
    private int startX;
    private int startY;
    private double vx;
    private double vy;
    private double maxSpeed;
    private ArrayList<AbstractObject> objects;
    private GraphicsContext gc;
    private Canvas canvas;
    private boolean inLongMovement;
    private Directions longMovementDirection;
    private boolean toSlideLeft;
    private boolean toSlideRight;

    public SetOfObjects(Canvas canvas) {
        this.canvas = canvas;
        this.x = 0;
        this.y = 0;
        this.nbPanels = 0;
        this.currentPanel=0;
        this.aimX = 0;
        this.aimY = 0;
        this.startX = 0;
        this.startY = 0;
        this.vx = 0;
        this.vy = 0;
        this.maxSpeed=30;
        this.inLongMovement = false;
        this.toSlideLeft=false;
        this.toSlideRight=false;
        this.gc = canvas.getGraphicsContext2D();
        this.objects = new ArrayList<AbstractObject>();
    }

    /** Ajoute un nouvel objet. Les coordonnées sont définies par les coordonnées de l'objet
     * @param newObject objet à ajouter
     */
    public void appendObject(AbstractObject newObject) {
        this.objects.add(newObject);
    }

    /** Ajoute un nouvel objet. Les coordonnées sont définies par xOnPanel et yOnPanel, sur la page nbPanel
     * @param newObject objet à ajouter
     * @param xOnPanel coordonnées x de l'objet sur la page
     * @param yOnPanel coordonnées y de l'objet sur la page
     * @param nbPanel page sur laquelle doit se trouver l'objet
     */
    public void appendObject(AbstractObject newObject, int xOnPanel, int yOnPanel, int nbPanel) {
        newObject.setX(xOnPanel+nbPanel*this.canvas.getWidth());
        newObject.setY(yOnPanel);
        this.objects.add(newObject);
    }

    /** Ajoute un nouvel objet. Les coordonnées sont définies par xOnPanel et yOnPanel, sur la page nbPanel
     * @param newObject objet à ajouter
     * @param xPercentOnPanel pourcentage x de l'objet sur la page
     * @param yPercentOnPanel pourcentage y de l'objet sur la page
     * @param nbPanel page sur laquelle doit se trouver l'objet
     */
    public void appendObjectPercent(AbstractObject newObject, double xPercentOnPanel, double yPercentOnPanel, int nbPanel) {
        newObject.setX(xPercentOnPanel*this.canvas.getWidth()+nbPanel*this.canvas.getWidth());
        newObject.setY(yPercentOnPanel*this.canvas.getHeight());
        this.objects.add(newObject);
    }

    /** Ajout d'un cadre
     * @param x coordonnée x du début du cadre
     * @param y coordonnée y du début du cadre
     * @param width largeur du cadre
     * @param height hauteur du cadre
     * @param arcWidth largeur des angles arrondis
     * @param arcHeight hauteur des angles arrondis
     */
    public void appendCadre(double x, double y, double width, double height, double arcWidth, double arcHeight, int nbPanel) {
        this.objects.add(new Cadre(x+nbPanel*this.canvas.getWidth(), y, width, height, arcWidth, arcHeight, this.gc));
    }

    /** Ajout d'un cadre
     * @param percentXMin pourcentage x du début du cadre entre 0 et 1
     * @param percentYMin pourcentage y du début du cadre entre 0 et 1
     * @param percentWidth pourcentage largeur du cadre entre 0 et 1
     * @param percentHeight pourcentage hauteur du cadre entre 0 et 1
     * @param percentArcWidth pourcentage largeur des angles arrondis entre 0 et 1
     * @param percentArcHeight pourcentage hauteur des angles arrondis entre 0 et 1
     */
    public void appendCadrePercent(double percentXMin, double percentYMin, double percentWidth, double percentHeight, double percentArcWidth, double percentArcHeight, int nbPanel) {
        this.objects.add(new Cadre((percentXMin+nbPanel)*this.canvas.getWidth(), percentYMin*this.canvas.getHeight(),
                percentWidth*this.canvas.getWidth(), percentHeight*this.canvas.getHeight(),
                percentArcWidth*this.canvas.getWidth(), percentArcHeight*this.canvas.getHeight(), this.gc));
    }

    /** Permet d'ajouter un panel
     */
    public void addPanel(){
        this.nbPanels+=1;
    }

    /** Update et display tous les objets du set
     */
    public void updateAndDisplay() {
        this.update();
        this.display();
    }

    /** Update tous les objets du set
     */
    public void update(){
        for (AbstractObject object : this.objects){
            object.updateData();
        }
        this.updateCoords();
    }

    /** Update uniquement les coordonnées de tous les objets du set
     */
    public void updateCoords() {
        if (inLongMovement) {
            updateSlideSpeed();
        }
        else{
            if (toSlideRight){
                toSlideRight=false;
                this.slide(Directions.RIGHT);
            }
            if (toSlideLeft){
                toSlideLeft=false;
                this.slide(Directions.LEFT);
            }
        }
        this.x += this.vx;
        this.y += this.vy;
        for (AbstractObject object : this.objects) {
            object.move(this.vx, this.vy);
        }
    }

    /** Display tous les objets du set
     */
    public void display() {
        for (AbstractObject object : this.objects) {
            object.display();
        }
    }

    /** Le set d'objets fait un dérapage contrôlé vers une direction donnée
     * @param direction direction du slide
     */
    private void slide(Directions direction) {
        this.inLongMovement = true;
        this.startX = (int) this.x;
        this.startY = (int) this.y;
        if (direction == Directions.LEFT) {
            if (this.currentPanel<nbPanels) {
                this.currentPanel+=1;
                longMovementDirection = Directions.LEFT;
                this.aimX -= this.canvas.getWidth();
            }
        } else if (direction == Directions.RIGHT) {
            if (this.currentPanel>0) {
                this.currentPanel-=1;
                longMovementDirection = Directions.RIGHT;
                this.aimX += this.canvas.getWidth();
            }
        }
    }

    /** Fonction gérant la vitesse du slide (ie du dérapage contrôlé)
     */
    private void updateSlideSpeed() {
        if (!(this.x == this.aimX)) {
            if (this.x == this.startX) {
                this.vx = 0.5 * (this.aimX - this.x) / Math.abs(this.aimX - this.x);
            } else {
                this.vx = (this.x - this.startX) * (this.x - this.aimX) / 2000; // /613 to bouncy bouncy
                if (this.longMovementDirection == Directions.RIGHT) {
                    this.vx *= -1;
                }
            }
        } else {
            this.vx = 0;
        }
        if (!(this.y == this.aimY)) {
            if (this.y == this.startY) {
                this.vy = 0.5 * (this.aimY - this.y) / Math.abs(this.aimY - this.y);
            } else {
                this.vy = (this.y - this.startY) * (this.y - this.aimY) / 2000; // /613 to bouncy bouncy
                if (this.longMovementDirection == Directions.DOWN) {
                    this.vy *= -1;
                }
            }
        } else {
            this.vy = 0;
        }

        //On majore la vitesse en valeur absolue
        if (this.vx > this.maxSpeed) {
            this.vx = this.maxSpeed;
        } else if (this.vx < -this.maxSpeed) {
            this.vx = -this.maxSpeed;
        }
        if (this.vy > this.maxSpeed) {
            this.vy = this.maxSpeed;
        } else if (this.vy < -this.maxSpeed) {
            this.vy = -this.maxSpeed;
        }

        //On recale correctement lorsque le slide est presque fini
        if (Math.abs(this.x - this.aimX) < 1 && Math.abs(this.y - this.aimY) < 1) {
            this.inLongMovement = false;
            this.x = this.aimX;
            this.y = this.aimY;
            this.vx = 0;
            this.vy = 0;
        }
    }

    /** Fonction permettant de déclencher le slide vers la gauche
     * @param toSlideLeft valeur à mettre sur true pour slider vers la gauche
     */
    public void setToSlideLeft(boolean toSlideLeft) {
        this.toSlideLeft = true;
    }

    /** Fonction permettant de déclencher le slide vers la droite
     * @param toSlideRight valeur à mettre sur true pour slider vers la droite
     */
    public void setToSlideRight(boolean toSlideRight) {
        this.toSlideRight = true;
    }
}
