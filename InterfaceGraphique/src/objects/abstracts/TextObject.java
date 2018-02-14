package objects.abstracts;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextObject extends AbstractObject {
    protected Font font;
    protected String familyFont;
    protected FontWeight fontWeight;
    protected double size;
    protected String text;

    /** Crée un objet de type TEXTE, avec la police de base
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param text texte
     */
    public TextObject(double x, double y, GraphicsContext gc, String text){
        super(x,y,gc);
        this.familyFont="Arial";
        this.fontWeight=FontWeight.SEMI_BOLD;
        this.text=text;
        this.size=48;
        this.font=Font.font(familyFont,fontWeight, size);
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

    /** Affiche le texte
     * Permet d'avoir un font personnalisé
     */
    @Override
    public void display(){
        Font oldFont = super.gc.getFont();
        super.gc.setFont(this.font);
        super.gc.strokeText(this.text, this.x, this.y);
        super.gc.setFont(oldFont);
    }

    /** Set le texte à afficher
     * @param text texte à afficher
     */
    public void setText(String text){
        this.text=text;
    }

    @Override
    public void updateData(){}
}
