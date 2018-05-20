import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.SetOfObjects;
import objects.abstracts.TextObject;
import objects.games.Snake;
import objects.time.Calendar;
import objects.time.Clock;
import objects.time.Date;
import objects.vumetre.Needle;
import utils.CpuMonitor;

public class EventMgr {
    private SetOfObjects setOfObjects;
    private GraphicsContext gc;

    private boolean interfaceLocked;

    //Objets dynamiques
    private int snakePanel=1;
    private Snake snake;
    private Needle cpuLoad;

    public EventMgr(GraphicsContext gc, SetOfObjects setOfObjects) {
        this.gc=gc;
        this.setOfObjects = setOfObjects;
        this.interfaceLocked=false;
        setUpSetOfObjects();
    }



    public void manage(String event) {
        if (!this.interfaceLocked) {
            if (event.equals("LEFT")) {
                this.setOfObjects.setToSlideLeft(true);
            } else if (event.equals("RIGHT")) {
                this.setOfObjects.setToSlideRight(true);
            } else if (this.setOfObjects.getCurrentPanel() == this.snakePanel) {
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
    }


    private void setUpSetOfObjects(){
        //PAGE 1
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,0); //on ajoute un cadre à ce panel
        this.setOfObjects.appendObjectPercent(new Clock(0,0, gc), 0.05, 0.08, 0); //on ajoute une horloge au premier panel
        this.setOfObjects.appendObjectPercent(new Date(0,0, gc), 0.80, 0.08, 0); //on ajoute une horloge au premier panel
        this.setOfObjects.appendObjectPercent(new Calendar(0,0,gc), 0.05, 0.1,0); //on ajoute une horloge au premier panel

        //PAGE 2
        this.setOfObjects.addPanel(); //on ajoute un panel
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,1);

        //PAGE 3
        this.setOfObjects.addPanel();
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,2);

        //Objets dynamiques
        //Snake
        this.snake = new Snake(0,0,gc);
        this.setOfObjects.appendObjectPercent(this.snake,0.05,0.1,this.snakePanel);

        //Moniteur CPU
        CpuMonitor cpuMonitor = new CpuMonitor();
        this.cpuLoad= new Needle(0,0,gc,0.5,0,100,100){
            @Override
            public void updateData(){
                double load = cpuMonitor.getCpuLoad();
                if (load!=-1) {
                    System.out.println(load);
                    setValue(load);
                }
            }
        };
        this.setOfObjects.appendObjectPercent(new TextObject(0,0,gc,"CPU"),0.825,0.92,0);
        this.setOfObjects.appendObjectPercent(this.cpuLoad,0.87,0.87,0);
    }


}
