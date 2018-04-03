import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import objects.time.Calendar;
import objects.time.Clock;
import objects.time.Date;
import objects.abstracts.SetOfObjects;
import objects.vumetre.Needle;
import objects.vumetre.Vertical;

import java.awt.*;
import java.util.List;

public class Main extends Application {

    /**Sert juste à lancer JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** Ce qu'il se passe lorsque JavaFX est lancé, ie quand on lance le programme
     */
    @Override
    public void start(Stage stage) throws Exception{
        //Largeur et hauteur arbitraires, utiles tout au long de la définition des objets
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double width = gd.getDisplayMode().getWidth()/1.6;
        double height = gd.getDisplayMode().getHeight()/1.2;
        //On définit la taille du canvas qui va accueillir les images
        double xCanvasSize = width;
        double yCanvasSize = height;

        //On définit la scene sur laquelle le canvas sera présent
        Group group = new Group();
        Scene scene = new Scene(group, xCanvasSize, yCanvasSize);
        stage.setTitle("Interface");
        stage.setResizable(false);
        stage.setScene(scene);

        //On définit le canvas qui va accueillir les objets
        Canvas canvas = new Canvas(xCanvasSize, yCanvasSize);
        List childrenList = group.getChildren();
        childrenList.add(canvas);

        //On récupère le contexte graphique 2D (GC) du canvas (interface permettant d'ajouter des objets)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //On définit la police de base du GC
        gc.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, width*0.025));
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);

        //On charge l'image de fond
        Image fond = new Image("resources/fond.jpg",xCanvasSize,yCanvasSize,false,false);

        //On définit un set d'objets
        SetOfObjects setOfObjects = new SetOfObjects(canvas);




        //PAGE 1
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,0); //on ajoute un cadre à ce panel
        setOfObjects.appendObjectPercent(new Clock(0,0, gc, "Helvetica",FontWeight.SEMI_BOLD,width*0.025), 0.05, 0.08, 0); //on ajoute une horloge au premier panel
        setOfObjects.appendObjectPercent(new Date(0,0, gc, "Helvetica",FontWeight.SEMI_BOLD,width*0.025), 0.80, 0.08, 0); //on ajoute une horloge au premier panel
        setOfObjects.appendObjectPercent(new Calendar(0.05*width,0.1*height, gc,width*0.0015), 0.05, 0.1,0); //on ajoute une horloge au premier panel

        //PAGE 2
        setOfObjects.addPanel(); //on ajoute un panel
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,1);
        setOfObjects.appendObjectPercent(new Clock(0,0,gc), 0.43,0.50,1); //on ajoute un objet à ce panel

        //PAGE 3
        setOfObjects.addPanel();
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,2);
        setOfObjects.appendObjectPercent(new Needle(0,0,gc,0.3,0,10,100), 0.5,0.5,2);

        //PAGE 4
        setOfObjects.addPanel();
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,3);
        setOfObjects.appendObjectPercent(new Vertical(0,0,gc,1,"up"),0.05,0.1,3);

        //PAGE 5
        setOfObjects.addPanel();
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,4);

        //PAGE 6
        setOfObjects.addPanel();
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,5);

        /** On ajoute un listener de touches du clavier
         */
        EventHandler eHandlerPressed = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e){
                KeyCode code=e.getCode();
                switch (code) {
                    //Flèche droite
                    case RIGHT:
                        setOfObjects.setToSlideLeft(true);
                        break;
                    //Flèche gauche
                    case LEFT:
                        setOfObjects.setToSlideRight(true);
                        break;
                    //Flèche haut
                    case UP:
                        break;
                    //Flèche bas
                    case DOWN:
                        break;
                    //Echap
                    case ESCAPE:
                        stage.close();
                        break;
                    default:
                }
            }
        };
        scene.setOnKeyPressed(eHandlerPressed); //on lie le listener à la scène

        /**On ajoute un timer permettant d'animer la scène
         * Tout ce qui est écrit dans la fonction handle va s'exécuter en boucle
         */
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.drawImage(fond, 0, 0); //on affiche le fond
                setOfObjects.updateAndDisplay(); //on update tous les objets
            }
        };
        timer.start(); // on démarre le timer

        stage.show(); //on affiche la scène, qui sera refresh par le timer du type AnimationTimer
    }

}
