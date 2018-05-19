import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.FontWeight;
import objects.abstracts.SetOfObjects;
import objects.games.Snake;
import objects.time.Calendar;
import objects.time.Clock;
import objects.time.Date;
import objects.vumetre.Needle;
import objects.vumetre.Vertical;

public class EventMgr {
    private SetOfObjects setOfObjects;
    private GraphicsContext gc;
    private double fenWidth;
    private double fenHeight;
    private int snakePanel=4;
    private Snake snake;

    public EventMgr(GraphicsContext gc, SetOfObjects setOfObjects) {
        this.gc=gc;
        this.setOfObjects = setOfObjects;
        this.fenWidth=gc.getCanvas().getWidth();
        this.fenHeight=gc.getCanvas().getHeight();
        setUpSetOfObjects();
    }

    public void manage(String event) {
        if (event.equals("LEFT")) {
            setOfObjects.setToSlideLeft(true);
        } else if (event.equals("RIGHT")) {
            setOfObjects.setToSlideRight(true);
        }
        else if (this.setOfObjects.getCurrentPanel()==this.snakePanel) {
            if (event.equals("RIGHT-SNAKE")) {
                this.snake.changeDirection("right");
            } else if (event.equals("LEFT-SNAKE")) {
                this.snake.changeDirection("left");
            } else if (event.equals("DOWN-SNAKE")) {
                this.snake.changeDirection("down");
            } else if (event.equals("UP-SNAKE")) {
                this.snake.changeDirection("up");
            }
        }
    }


    private void setUpSetOfObjects(){
        //PAGE 1
        setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,0); //on ajoute un cadre à ce panel
        setOfObjects.appendObjectPercent(new Clock(0,0, gc, "Helvetica", FontWeight.SEMI_BOLD,this.fenWidth*0.025), 0.05, 0.08, 0); //on ajoute une horloge au premier panel
        setOfObjects.appendObjectPercent(new Date(0,0, gc, "Helvetica",FontWeight.SEMI_BOLD,this.fenWidth*0.025), 0.80, 0.08, 0); //on ajoute une horloge au premier panel
        setOfObjects.appendObjectPercent(new Calendar(0.05*this.fenWidth,0.1*this.fenHeight, gc,this.fenWidth*0.0015), 0.05, 0.1,0); //on ajoute une horloge au premier panel

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

        //Objets dynamiques
        this.snake = new Snake(0,0,gc);
        setOfObjects.appendObjectPercent(this.snake,0.05,0.1,this.snakePanel);
    }
}
