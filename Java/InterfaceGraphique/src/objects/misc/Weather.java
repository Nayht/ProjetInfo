package objects.misc;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import objects.abstracts.AbstractObject;
import objects.abstracts.TextObject;
import utils.WebPage;

public class Weather extends AbstractObject {
    private double size;
    private int timePlusThreeHours;
    private int previousTimePlusThreeHours;
    private int maxTimePlusThreeHours;
    private WebPage webPageCurrent;
    private String webPageCurrentString;
    private WebPage webPageForecast;
    private String webPageForecastString;

    private Image clearImage;
    private Image cloudsImage;
    private Image rainImage;
    private Image snowImage;

    private String city;
    private String[] temperature;
    private String[] pressure;
    private String[] mainWeather;

    private TextObject cityTextObject;
    private TextObject mainWeatherTextObject;
    private TextObject timeTextObject;
    private TextObject temperatureTextObject;
    private TextObject pressureTextObject;


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
        this.previousTimePlusThreeHours=-1;
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
        this.clearImage = new Image("resources/clear.png");
        this.cloudsImage = new Image("resources/clouds.png");
        this.rainImage = new Image("resources/rain.png");
        this.snowImage = new Image("resources/snow.png");

        this.cityTextObject=new TextObject(0,0, this.gc,this.gc.getCanvas().getWidth()*0.03,this.city);
        this.mainWeatherTextObject=new TextObject(0,0, this.gc,this.gc.getCanvas().getWidth()*0.03,"b");
        this.timeTextObject=new TextObject(0,0, this.gc,this.gc.getCanvas().getWidth()*0.03,"c");
        this.temperatureTextObject=new TextObject(0,0, this.gc,this.gc.getCanvas().getWidth()*0.03,this.temperature[this.timePlusThreeHours]+" °C");
        this.pressureTextObject=new TextObject(0,0, this.gc,this.gc.getCanvas().getWidth()*0.03,"e");
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
        this.cityTextObject.display();
        this.timeTextObject.display();
        this.mainWeatherTextObject.display();
        this.temperatureTextObject.display();
        this.pressureTextObject.display();
        if (this.mainWeather[this.timePlusThreeHours].equals("Clear")) {
            gc.drawImage(this.clearImage, this.x + this.size * 0.02, this.y + this.size * 0.6, this.size * 0.6, this.size * 0.6);
        }
        else if(this.mainWeather[this.timePlusThreeHours].equals("Clouds")){
            gc.drawImage(this.cloudsImage, this.x + this.size * 0.02, this.y + this.size * 0.6, this.size * 0.6, this.size * 0.6);
        }
        else if(this.mainWeather[this.timePlusThreeHours].equals("Rain")){
            gc.drawImage(this.rainImage, this.x + this.size * 0.02, this.y + this.size * 0.6, this.size * 0.6, this.size * 0.6);
        }
        else if(this.mainWeather[this.timePlusThreeHours].equals("Snow")){
            gc.drawImage(this.snowImage, this.x + this.size * 0.02, this.y + this.size * 0.6, this.size * 0.6, this.size * 0.6);
        }
    }

    @Override
    public void updateData() {
        this.cityTextObject.setX(this.x+this.size*0.04);
        this.cityTextObject.setY(this.y+this.size*0.1);
        this.timeTextObject.setX(this.x+this.size*0.04);
        this.timeTextObject.setY(this.y+this.size*0.2);
        this.mainWeatherTextObject.setX(this.x+this.size*0.04);
        this.mainWeatherTextObject.setY(this.y+this.size*0.3);
        this.temperatureTextObject.setX(this.x+this.size*0.04);
        this.temperatureTextObject.setY(this.y+this.size*0.4);
        this.pressureTextObject.setX(this.x+this.size*0.04);
        this.pressureTextObject.setY(this.y+this.size*0.5);
        if (this.timePlusThreeHours!=this.previousTimePlusThreeHours){
            this.previousTimePlusThreeHours=this.timePlusThreeHours;
            if (this.timePlusThreeHours==0) {
                this.timeTextObject.setText("Now");
            }
            else{
                this.timeTextObject.setText("+" + Integer.toString(this.timePlusThreeHours * 3) + " hours");
            }
            this.mainWeatherTextObject.setText(this.mainWeather[this.timePlusThreeHours]);
            this.temperatureTextObject.setText(this.temperature[this.timePlusThreeHours]+" °C");
            this.pressureTextObject.setText(this.pressure[this.timePlusThreeHours]+" hPa");
        }
    }
}
