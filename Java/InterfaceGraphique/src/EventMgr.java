import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.SetOfObjects;
import objects.abstracts.TextObject;
import objects.games.Snake;
import objects.misc.Weather;
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
    private int snakePanelHorizontal=1;
    private int snakePanelVertical=0;
    private Snake snake;

    private int weatherPanelHorizontal=0;
    private int weatherPanelVertical=0;
    private Weather weather;
    
    private Needle cpuLoad;

    public EventMgr(GraphicsContext gc, SetOfObjects setOfObjects) {
        this.gc=gc;
        this.setOfObjects = setOfObjects;
        this.interfaceLocked=false;
        setUpSetOfObjects();
    }


    public void manage(String event) {
        if (!this.interfaceLocked) {
            String[] eventSplitted = event.split(" ");
            for (String str :eventSplitted) {
                System.out.println(str);
            }
            System.out.println();
            if (eventSplitted[0].equals("R")) {
                if (eventSplitted[1].equals("Left")) {
                    this.setOfObjects.setToSlideLeft(true);
                }
                else if (eventSplitted[1].equals("Right")) {
                    this.setOfObjects.setToSlideRight(true);
                }
                else if (eventSplitted[1].equals("Up")){
                    this.setOfObjects.setToSlideUp(true);
                }
                else if (eventSplitted[1].equals("Down")){
                    this.setOfObjects.setToSlideDown(true);
                }
            } else if (eventSplitted[0].equals("L")) {
                if (this.setOfObjects.getCurrentPanelHorizontal() == this.snakePanelHorizontal
                        && this.setOfObjects.getCurrentPanelVertical() == this.snakePanelVertical) {
                    if (eventSplitted[1].equals("Right")) {
                        this.snake.changeDirection("right");
                    }
                    else if (eventSplitted[1].equals("Left")) {
                        this.snake.changeDirection("left");
                    }
                    else if (eventSplitted[1].equals("Down")) {
                        this.snake.changeDirection("down");
                    }
                    else if (eventSplitted[1].equals("Up")) {
                        this.snake.changeDirection("up");
                    }
                }
                if (this.setOfObjects.getCurrentPanelHorizontal() == this.weatherPanelHorizontal
                        && this.setOfObjects.getCurrentPanelVertical() == this.weatherPanelVertical){
                    if (eventSplitted[1].equals("Right")){
                        this.weather.changeDay(true);
                    }
                    else if (eventSplitted[1].equals("Left")){
                        this.weather.changeDay(false);
                    }
                }
            }
        }
    }

    private void setUpSetOfObjects(){

        //PAGE 0 Horizontal 1 Vertical
        this.setOfObjects.addPanelVertical();
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,0,1);

        //PAGE 1 Horizontal 1 Vertical
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,1,1);


        //PAGE 0 Horizontal 0 Vertical
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,0,0); //on ajoute un cadre à ce panel
        this.setOfObjects.appendObjectPercent(new Clock(0,0, gc), 0.05, 0.08, 0,0); //on ajoute une horloge au premier panel
        this.setOfObjects.appendObjectPercent(new Date(0,0, gc), 0.80, 0.08, 0,0); //on ajoute une horloge au premier panel
        this.setOfObjects.appendObjectPercent(new Calendar(0,0,gc), 0.1, 0.20,0,0); //on ajoute une horloge au premier panel

        //PAGE 1 Horizontal 0 Vertical
        this.setOfObjects.addPanelHorizontal(); //on ajoute un panel
        this.setOfObjects.appendCadrePercent(0.025,0.03,0.95, 0.94, 0.05,0.05,1,0);


        //Objets dynamiques
        //Snake
        this.snake = new Snake(0,0,gc);
        this.setOfObjects.appendObjectPercent(this.snake,0.05,0.1,this.snakePanelHorizontal,this.snakePanelVertical);

        //Moniteur CPU
        CpuMonitor cpuMonitor = new CpuMonitor();
        this.cpuLoad= new Needle(0,0,gc,0,100){

            @Override
            public void updateData(){
                double load = cpuMonitor.getCpuLoad();
                if (load!=-1) {
                    //System.out.println(load);
                    setValue(load);
                }
            }
        };
        this.setOfObjects.appendObjectPercent(new TextObject(0,0, gc,this.gc.getCanvas().getWidth()*0.04,"CPU"),0.83,0.92,1,1);
        this.setOfObjects.appendObjectPercent(this.cpuLoad,0.87,0.87,1,1);

        this.weather=new Weather(0,0,gc,"Evry");
        this.setOfObjects.appendObjectPercent(this.weather,0.6,0.175,this.weatherPanelHorizontal,this.weatherPanelVertical);
    }


}
