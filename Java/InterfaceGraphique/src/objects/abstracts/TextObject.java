package objects.abstracts;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextObject extends AbstractObject {
    protected Font font;
    protected String familyFont;
    protected FontWeight fontWeight;
    protected double size;
    protected String text;
    protected Paint color;

    /** Crée un objet de type TEXTE, avec la police de base
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param text texte
     */
    public TextObject(double x, double y, GraphicsContext gc, String text){
        this(x,y,gc,"Arial",FontWeight.SEMI_BOLD,48,text);
    }

    public TextObject(double x, double y, GraphicsContext gc, double size, String text){
        this(x,y,gc,"Arial",FontWeight.SEMI_BOLD,size,text);
    }

    /** Crée un objet de type TEXTE, avec une police personnalisée
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param familyFont FamilyFont du texte
     * @param fontWeight FontWeight du texte
     * @param size taille du texte
     * @param text texte
     */
    public TextObject(double x, double y, GraphicsContext gc, String familyFont, FontWeight fontWeight, double size, String text){
        super(x,y,gc);
        this.text=text;
        this.familyFont=familyFont;
        this.fontWeight=fontWeight;
        this.size=size;
        this.font=Font.font(familyFont,fontWeight, size);
    }

    /** Crée un objet de type TEXTE, avec une police personnalisée
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param familyFont FamilyFont du texte
     * @param fontWeight FontWeight du texte
     * @param size taille du texte
     * @param color couleur du texte
     * @param text texte
     */
    public TextObject(double x, double y, GraphicsContext gc, String familyFont, FontWeight fontWeight, double size, Color color, String text){
        super(x,y,gc);
        this.text=text;
        this.familyFont=familyFont;
        this.fontWeight=fontWeight;
        this.size=size;
        this.color=color;
        this.font=Font.font(familyFont,fontWeight, size);
    }


    /** Affiche le texte
     * Permet d'avoir un font personnalisé
     */
    @Override
    public void display(){
        Font oldFont = super.gc.getFont();
        Paint oldColor = super.gc.getFill();
        super.gc.setFont(this.font);
        super.gc.setFill(this.color);
        super.gc.fillText(this.text, this.x, this.y);
        super.gc.setFill(oldColor);
        super.gc.setFont(oldFont);
    }

    /** Set le texte à afficher
     * @param text texte à afficher
     */
    public void setText(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    @Override
    public void updateData(){}
}
