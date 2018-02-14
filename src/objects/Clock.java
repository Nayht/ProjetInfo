package objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.FontWeight;
import objects.abstracts.TextObject;

public class Clock extends TextObject {
    private int hours;
    private int minutes;
    private int seconds;
    private String text;

    /** Crée une horloge, avec la police de base
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     */
    public Clock(double x, double y, GraphicsContext gc){
        super(x,y,gc, "Arial", FontWeight.SEMI_BOLD, 48, "");
        this.hours=0;
        this.minutes=0;
        this.seconds=0;
    }
    /** Crée une horloge, avec une police personnalisée
     * @param x coordonnée x
     * @param y coordonnée y
     * @param gc contexte graphique
     * @param familyFont FamilyFont de l'horloge
     * @param fontWeight FontWeight de l'horloge
     * @param size taille de l'horloge
     */
    public Clock(double x, double y, GraphicsContext gc, String familyFont, FontWeight fontWeight, double size){
        super(x,y,gc, familyFont, fontWeight, size, "");
        this.hours=0;
        this.minutes=0;
        this.seconds=0;
        this.text="";
    }

    /** Affiche l'horloge
     * Permet d'avoir un affichage joli, avec un police personnalisée
     */
    @Override
    public void display(){
        String strHours= Integer.toString(this.hours);
        if (this.hours<10){
            strHours="0"+this.hours;
        }
        String strMinutes= Integer.toString(this.minutes);
        if (this.minutes<10){
            strMinutes="0"+this.minutes;
        }
        String strSeconds= Integer.toString(this.seconds);
        if (this.seconds<10){
            strSeconds="0"+this.seconds;
        }
        this.text=strHours+":"+strMinutes+":"+strSeconds;
        super.setText(this.text);
        super.display();
    }

    /** Met à jour l'horloge
     */
    @Override
    public void updateData(){
        this.hours=java.time.LocalTime.now().getHour();
        this.minutes=java.time.LocalTime.now().getMinute();
        this.seconds=java.time.LocalTime.now().getSecond();
    }

}
