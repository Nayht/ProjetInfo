package objects.vumetre;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import objects.abstracts.AbstractObject;
import utils.Colors;

public class Needle extends AbstractObject {

    private double lengthNeedle;
    private double angleMin;
    private double angleMax;
    private double angle;
    private double minValue;
    private double maxValue;
    private double value; //entre 0 et 100

    public Needle(double x, double y, GraphicsContext gc, double minValue, double maxValue){
        this(x,y,gc,minValue, maxValue,gc.getCanvas().getWidth()*0.08);
    }

    public Needle(double x, double y, GraphicsContext gc, double minValue, double maxValue, double lengthNeedle){
        this(x,y,gc,1,minValue,maxValue,lengthNeedle,170,10);
    }

    public Needle(double x, double y, GraphicsContext gc, double alphaValue, double minValue, double maxValue, double lengthNeedle){
        this(x,y,gc,alphaValue,minValue,maxValue,lengthNeedle,170,10);
    }

    public Needle(double x, double y,GraphicsContext gc, double alphaValue, double minValue, double maxValue, double lengthNeedle, double angleMin, double angleMax){
        super(x,y,gc,alphaValue);
        this.minValue=minValue;
        this.maxValue=maxValue;
        this.value=minValue;
        this.lengthNeedle=lengthNeedle;
        this.angleMin=angleMin;
        this.angleMax=angleMax;
        this.angle=angleMin;
    }

    public void setValue(int value){
        if (value<0){
            value=0;
        }
        else if(value>100){
            value=100;
        }
        this.value=value/100.0;
    }

    public void setValue(float value){
        if (value<0){
            value=0;
        }
        else if(value>100){
            value=100;
        }
        this.value=value/100.0;
    }

    public void setValue(double value){
        if (value<0){
            value=0;
        }
        else if(value>100){
            value=100;
        }
        this.value=value/100.0;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void display(){
        this.angle=(angleMax-angleMin)*value+angleMin;
        //gc.setGlobalAlpha(this.alphaValue);
        gc.fillArc(this.x-this.lengthNeedle, this.y-this.lengthNeedle, this.lengthNeedle*2, this.lengthNeedle*2, angleMin, angleMax-angleMin, ArcType.ROUND);
        //gc.setGlobalAlpha(1);
        gc.setStroke(Colors.RED.getColor());
        gc.strokeLine(this.x, this.y,this.x+Math.cos(this.angle*Math.PI/180)*this.lengthNeedle,this.y-Math.sin(this.angle*Math.PI/180)*this.lengthNeedle);
        gc.setStroke(Colors.WHITE.getColor());
    }
}
