package objects.Time;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.FontWeight;
import objects.abstracts.TextObject;

import java.time.LocalDateTime;

public class Date extends TextObject {

    private int day;
    private int month;
    private int year;

    /** Crée une horloge, avec la police de base
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     */
    public Date(double x, double y, GraphicsContext gc){
        this(x,y,gc, "Arial", FontWeight.SEMI_BOLD, 48);
    }
    /** Crée une horloge, avec une police personnalisée
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param familyFont FamilyFont de l'horloge
     * @param fontWeight FontWeight de l'horloge
     * @param size taille de l'horloge
     */
    public Date(double x, double y, GraphicsContext gc, String familyFont, FontWeight fontWeight, double size){
        super(x,y,gc, familyFont, fontWeight, size, "");
        this.day=0;
        this.month=0;
        this.year=0;
        this.text="";
    }

    /** Affiche l'horloge
     * Permet d'avoir un affichage joli, avec un police personnalisée
     */
    @Override
    public void display(){
        String strDay= Integer.toString(this.day);
        if (this.day<10){
            strDay="0"+this.day;
        }
        String strMonth= Integer.toString(this.month);
        if (this.month<10){
            strMonth="0"+this.month;
        }
        String strYear= Integer.toString(this.year);
        this.text=strDay+"/"+strMonth+"/"+strYear;
        super.setText(this.text);
        super.display();
    }

    /** Met à jour l'horloge
     */
    @Override
    public void updateData(){
        LocalDateTime timeNow = LocalDateTime.now();
        this.day=timeNow.getDayOfMonth();
        this.month=timeNow.getMonthValue();
        this.year=timeNow.getYear();
    }
}
