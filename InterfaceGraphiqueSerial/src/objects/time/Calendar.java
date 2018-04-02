package objects.time;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import objects.abstracts.AbstractObject;
import objects.abstracts.TextObject;

import java.time.LocalDateTime;

public class Calendar extends AbstractObject{

    private int size;
    private int textSize;
    private Date date;
    private TextObject[] labels;
    private String familyFont;
    private FontWeight fontWeight;
    private String monthName;
    private TextObject monthText;
    private int lineHeight;

    public Calendar(double x, double y, GraphicsContext gc){
        this(x,y,gc,2);
    }

    public Calendar(double x, double y, GraphicsContext gc, int size){
        this(x,y,gc,size,"Arial",FontWeight.SEMI_BOLD);
    }

    public Calendar(double x, double y, GraphicsContext gc, int size, String familyFont, FontWeight fontWeight){
        super(x,y,gc);
        this.size=size;
        this.textSize=20*this.size;
        this.labels = new TextObject[6*8];
        this.familyFont=familyFont;
        this.fontWeight=fontWeight;
        createLabels();
    }

    private void createLabels(){
        LocalDateTime timeNow = java.time.LocalDateTime.now();
        int monthNumber=timeNow.getMonth().getValue();
        if (monthNumber==1){
            this.monthName="JANVIER";
        }
        else if (monthNumber==2){
            this.monthName="FEVRIER";
        }
        else if (monthNumber==3){
            this.monthName="MARS";
        }
        else if (monthNumber==4){
            this.monthName="AVRIL";
        }
        else if (monthNumber==5){
            this.monthName="MAI";
        }
        else if (monthNumber==6){
            this.monthName="JUIN";
        }
        else if (monthNumber==7){
            this.monthName="JUILLET";
        }
        else if (monthNumber==8){
            this.monthName="AOUT";
        }
        else if (monthNumber==9){
            this.monthName="SEPTEMBRE";
        }
        else if (monthNumber==10){
            this.monthName="OCTOBRE";
        }
        else if (monthNumber==11){
            this.monthName="NOVEMBRE";
        }
        else {
            this.monthName="DECEMBRE";
        }
        this.monthText=new TextObject(this.x+5*this.size, this.y+20*this.size, gc, this.familyFont, this.fontWeight, this.textSize, this.monthName);
        int nbDayOfMonth = timeNow.getDayOfMonth();
        int amountDayInThisMonth = timeNow.getMonth().length((timeNow.getYear()%4==0));
        int nbDayOfWeek = timeNow.getDayOfWeek().getValue()-1;
        int positionFirstDayOfMonth=nbDayOfWeek+7;
        int nbWeekOfMonth=(nbDayOfMonth+1)/7;
        for (int i=7; i<positionFirstDayOfMonth+1; i++){
            this.labels[i]=new TextObject(0,0, gc,"");
        }
        for (int i=0; i<=6; i++){
            String daySymbol;
            if (i==0){
                daySymbol=" L";
            }
            else if (i==1){
                daySymbol=" M";
            }
            else if (i==2){
                daySymbol=" M";
            }
            else if (i==3){
                daySymbol=" J";
            }
            else if (i==4){
                daySymbol=" V";
            }
            else if (i==5){
                daySymbol=" S";
            }
            else{
                daySymbol=" D";
            }
            this.labels[i]=new TextObject(this.x+i*30*this.size+5*this.size, this.y+30*this.size+20*this.size, gc, this.familyFont, this.fontWeight, this.textSize, daySymbol);
        }
        int counter=1;
        int counterNbDayOfWeek=(nbDayOfWeek-nbDayOfMonth)+1;
        while (counterNbDayOfWeek<0){
            counterNbDayOfWeek+=7;
        }
        while (counterNbDayOfWeek>6){
            counterNbDayOfWeek-=7;
        }
        System.out.print(counterNbDayOfWeek);
        while (counter<=amountDayInThisMonth){
            if (counterNbDayOfWeek>6){
                counterNbDayOfWeek=0;
                nbWeekOfMonth+=1;
            }
            String text="";
            if (counter<10){
                text="  "+Integer.toString(counter);
            }
            else{
                text=Integer.toString(counter);
            }
            if (nbDayOfMonth==counter){
                this.labels[counter+positionFirstDayOfMonth]=new TextObject(this.x+counterNbDayOfWeek*30*this.size+5*this.size, this.y+(nbWeekOfMonth+2)*30*this.size+20*this.size, gc, this.familyFont, this.fontWeight, this.textSize, Color.RED, text);
            }
            else{
                this.labels[counter+positionFirstDayOfMonth]=new TextObject(this.x+counterNbDayOfWeek*30*this.size+5*this.size, this.y+(nbWeekOfMonth+2)*30*this.size+20*this.size, gc, this.familyFont, this.fontWeight, this.textSize, text);
            }
            counterNbDayOfWeek+=1;
            counter+=1;
        }
        for (int i=counter+positionFirstDayOfMonth; i<6*8; i++){
            this.labels[i]=new TextObject(0,0, gc,"");
        }
        this.lineHeight=nbWeekOfMonth+1;
    }

    @Override
    public void display() {
        super.gc.strokeRoundRect(this.x, this.y, 210*this.size,30*(this.lineHeight+2)*this.size, 5*this.size, 5*this.size);
        this.monthText.display();
        for (TextObject label : labels){
            label.display();
        }
    }

    @Override
    public void move(double deltaX, double deltaY){
        this.x+=deltaX;
        this.y+=deltaY;
        this.monthText.move(deltaX, deltaY);
        for (TextObject label : labels){
            label.move(deltaX, deltaY);
        }
    }

    @Override
    public void updateData() {
    }
}
