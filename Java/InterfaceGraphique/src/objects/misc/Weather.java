package objects.misc;

import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.AbstractObject;
import utils.WebPage;

public class Weather extends AbstractObject {
    private double size;
    private int timePlusThreeHours;
    private int maxTimePlusThreeHours;
    private WebPage webPageCurrent;
    private String webPageCurrentString;
    private WebPage webPageForecast;
    private String webPageForecastString;


    private String city;
    private String[] temperature;
    private String[] pressure;
    private String[] mainWeather;


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
        this.temperature=new String[this.maxTimePlusThreeHours+1];
        this.pressure=new String[this.maxTimePlusThreeHours+1];
        this.mainWeather=new String[this.maxTimePlusThreeHours+1];
        this.webPageCurrent = new WebPage("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=bd5e378503939ddaee76f12ad7a97608");
        this.webPageCurrentString = this.webPageCurrent.downloadWebPage();
        this.webPageForecast = new WebPage("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&APPID=bd5e378503939ddaee76f12ad7a97608");
        this.webPageForecastString = this.webPageForecast.downloadWebPage();
        parseParameters(this.webPageCurrentString, true);
        parseParameters(this.webPageForecastString, false);
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

    private int giveIndiceOfNthCharacter(char character, String toFindIn, int nthChar){
        int nbChar=0;
        for (int i=0; i<toFindIn.length(); i++){
            if (toFindIn.charAt(i)==character){
                nbChar+=1;
                if (nthChar==nbChar){
                    return i;
                }
            }
        }
        return -1;
    }

    private void parseParameters(String stringToParse, boolean current) {
        String[] parsedMain = stringToParse.split('"'+"main"+'"'+':');
        String[] parsedTemp = stringToParse.split('"'+"temp"+'"'+':');
        String[] parsedPressure = stringToParse.split('"'+"pressure"+'"'+':');
        if (current){
            this.mainWeather[0]=parsedMain[1].substring(1,giveIndiceOfNthCharacter('"',parsedMain[1],2));
            this.temperature[0]=Long.toString(Math.round(Double.parseDouble(parsedTemp[1].substring(0,giveIndiceOfNthCharacter(',',parsedTemp[1],1)))-273.15));
            this.pressure[0]=Long.toString(Math.round(Double.parseDouble(parsedPressure[1].substring(0,giveIndiceOfNthCharacter(',',parsedPressure[1],1)))));
        }
        else{
            for (int i=1; i<=this.maxTimePlusThreeHours; i++) {
                this.mainWeather[i] = parsedMain[2*i].substring(1, giveIndiceOfNthCharacter('"', parsedMain[2*i], 2));
            }
            for (int i=1; i<=this.maxTimePlusThreeHours; i++) {
                this.temperature[i] = Long.toString(Math.round(Double.parseDouble(parsedTemp[i].substring(0, giveIndiceOfNthCharacter(',', parsedTemp[i], 1)))-273.15));
            }
            for (int i=1; i<=this.maxTimePlusThreeHours; i++){
                this.pressure[i] = Long.toString(Math.round(Double.parseDouble(parsedPressure[i].substring(0, giveIndiceOfNthCharacter(',', parsedPressure[i], 1)))));
            }
        }
    }


    @Override
    public void display() {
        gc.strokeRoundRect(this.x,this.y,this.gc.getCanvas().getWidth()*0.25,this.gc.getCanvas().getWidth()*0.50,this.gc.getCanvas().getWidth()*0.05,this.gc.getCanvas().getWidth()*0.05);

        gc.fillText(this.city,this.x+this.size*0.04,this.y+this.size*0.1);
        if (this.timePlusThreeHours==0) {
            gc.fillText("Now", this.x+this.size*0.04, this.y+this.size*0.2);
        }
        else{
            gc.fillText("+"+Integer.toString(this.timePlusThreeHours*3)+" hours",this.x+this.size*0.04,this.y+this.size*0.2);
        }
        gc.fillText(this.mainWeather[this.timePlusThreeHours],this.x+this.size*0.04,this.y+this.size*0.3);
        gc.fillText(this.temperature[this.timePlusThreeHours]+" °C",this.x+this.size*0.04,this.y+this.size*0.4);
        gc.fillText(this.pressure[this.timePlusThreeHours]+" hectoPascal",this.x+this.size*0.04,this.y+this.size*0.5);
    }

    @Override
    public void updateData() {

    }
}
