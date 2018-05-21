package objects.misc;

import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.AbstractObject;
import utils.WebPage;

public class Weather extends AbstractObject {
    private double size;
    private int timePlusThreeHours;
    private int timePlusThreeHoursUpdated;
    private int maxTimePlusThreeHours;
    private String city;
    private WebPage webPageCurrent;
    private String webPageCurrentString;
    private WebPage webPageForecast;

    private boolean alreadyDisplayed;


    public Weather(int x, int y, GraphicsContext gc){
        this(x,y,gc,"Evry");
    }

    public Weather(int x, int y, GraphicsContext gc, String city){
        this(x,y,gc,gc.getCanvas().getWidth()*0.4,city);
    }

    public Weather(int x, int y, GraphicsContext gc, double size, String city){
        super(x,y,gc);
        this.size=size;
        this.timePlusThreeHours=0;
        this.maxTimePlusThreeHours=8;
        this.city=city;
        this.alreadyDisplayed=false;
        this.webPageCurrent = new WebPage("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=bd5e378503939ddaee76f12ad7a97608");
        this.webPageCurrentString = this.webPageCurrent.downloadWebPage();
        this.webPageForecast = new WebPage("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&APPID=bd5e378503939ddaee76f12ad7a97608");
    }


    public void changeDay(boolean forNextDay){
        if (forNextDay){
            if (this.timePlusThreeHours<this.maxTimePlusThreeHours){
                this.timePlusThreeHours+=1;
            }
        }
        else {
            if (this.timePlusThreeHours>0){
                this.timePlusThreeHours-=1;
            }
        }
    }

    @Override
    public void display() {
        if (!this.alreadyDisplayed) {
            this.alreadyDisplayed=true;
            System.out.println(webPageCurrentString);
        }
    }

    @Override
    public void updateData() {
        if (this.timePlusThreeHoursUpdated!=this.timePlusThreeHours) {
            this.timePlusThreeHoursUpdated = this.timePlusThreeHours;
        }
    }
}
