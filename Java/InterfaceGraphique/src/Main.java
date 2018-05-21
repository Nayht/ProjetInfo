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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import objects.abstracts.SetOfObjects;
import serie.Serial;
import utils.Colors;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.List;

public class Main extends Application {

    final private boolean usingSerie = false;

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
        gc.setFill(Colors.WHITE.getColor());
        gc.setStroke(Colors.WHITE.getColor());
        gc.setLineWidth(3);

        //On charge l'image de fond
        Image fond = new Image("resources/fond.jpg",xCanvasSize,yCanvasSize,false,false);

        //On définit un set d'objets
        SetOfObjects setOfObjects = new SetOfObjects(canvas);

        Serial serial;
        Thread reader;
        if (usingSerie) {
            //On définit tout ce qui est nécessaire pour le port série
            serial = new Serial();
            reader = new Thread(new Serial.SerialReader(serial.getSerialInStream()));
            reader.start();
        }
        else{
            serial=null;
            reader=null;
        }
        EventMgr eventMgr = new EventMgr(gc, setOfObjects);


        /** On ajoute un listener de touches du clavier
         */
        EventHandler eHandlerPressed = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e){
                KeyCode code=e.getCode();
                switch (code) {
                    //Flèche droite
                    case RIGHT:
                        eventMgr.manage("R Left");
                        break;
                    //Flèche gauche
                    case LEFT:
                        eventMgr.manage("R Right");
                        break;
                    //Flèche haut
                    case UP:
                        break;
                    //Flèche bas
                    case DOWN:
                        break;
                    case Z:
                        eventMgr.manage("L Up");
                        break;
                    case Q:
                        eventMgr.manage("L Left");
                        break;
                    case S:
                        eventMgr.manage("L Down");
                        break;
                    case D:
                        eventMgr.manage("L Right");
                        break;
                    //Echap
                    case ESCAPE:
                        shutdown(stage, serial, reader);
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
                if (usingSerie){
                    String message = serial.readMessage();
                    if (message != null) {
                        eventMgr.manage(message);
                        serial.resetMessage();
                    }
                }
            }
        };
        timer.start(); // on démarre le timer

        stage.setOnCloseRequest(event -> shutdown(stage, serial, reader));
        stage.show(); //on affiche la scène, qui sera refresh par le timer du type AnimationTimer
    }


    private void shutdown(Stage stage, Serial serial, Thread reader){
        stage.close();
        if (serial!=null) {
            serial.flag = true;
            serial.disconnect();
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fin de la fenêtre");
    }


}
