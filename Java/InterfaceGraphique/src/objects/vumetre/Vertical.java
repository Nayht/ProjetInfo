package objects.vumetre;

import javafx.scene.canvas.GraphicsContext;
import objects.abstracts.AbstractObject;
import utils.Colors;

public class Vertical extends AbstractObject {

    private double width;
    private double height;
    private double value; //entre 0 et 100
    private String sens;

    public Vertical(double x, double y, GraphicsContext gc) {
        this(x, y, gc,1);
    }

    public Vertical(double x, double y, GraphicsContext gc, double transparency) {
        this(x, y, gc, transparency,gc.getCanvas().getWidth()*0.05,gc.getCanvas().getHeight()*0.70);
    }

    public Vertical (double x, double y, GraphicsContext gc, double transparency, String sens){
        this(x, y, gc, transparency,gc.getCanvas().getWidth()*0.05,gc.getCanvas().getHeight()*0.70, sens);
    }

    public Vertical (double x, double y, GraphicsContext gc, String sens){
        this(x, y, gc, 1,gc.getCanvas().getWidth()*0.05,gc.getCanvas().getHeight()*0.70, sens);
    }

    public Vertical (double x, double y, GraphicsContext gc, double transparency, double width, double height){
        this(x, y, gc, transparency, width, height, "down");
    }

    public Vertical (double x, double y, GraphicsContext gc, double transparency, double width, double height, String sens){
        super(x,y,gc,transparency);
        this.width=width;
        this.height=height;
        this.value=0.5;
        this.sens=sens;
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
    public void display() {
        gc.setGlobalAlpha(this.alphaValue);
        double threeshold1=0.6;
        double threeshold2=0.8;
        if (this.sens.equals("down")) {
            if (this.value > threeshold2) {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y, this.width, this.height * threeshold1);
                gc.setFill(Colors.YELLOW.getColor());
                gc.fillRect(this.x, this.y + this.height * threeshold1, this.width, this.height * (threeshold2 - threeshold1));
                gc.setFill(Colors.RED.getColor());
                gc.fillRect(this.x, this.y + this.height * threeshold2, this.width, this.height * (this.value - threeshold2));
            }
            else if (this.value > threeshold1) {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y, this.width, this.height * threeshold1);
                gc.setFill(Colors.YELLOW.getColor());
                gc.fillRect(this.x, this.y + this.height * threeshold1, this.width, this.height * (this.value - threeshold1));
            }
            else {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y, this.width, this.height * this.value);
            }
        }
        else if (this.sens.equals("up")){
            if (this.value > threeshold2) {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y + this.height * (1-threeshold1), this.width, this.height * threeshold1);
                gc.setFill(Colors.YELLOW.getColor());
                gc.fillRect(this.x, this.y + this.height * (1-threeshold2), this.width, this.height * (threeshold2 - threeshold1));
                gc.setFill(Colors.RED.getColor());
                gc.fillRect(this.x, this.y + this.height * (1-this.value), this.width, this.height * (this.value - threeshold2));
            }
            else if (this.value > threeshold1) {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y + this.height * (1-threeshold1), this.width, this.height * threeshold1);
                gc.setFill(Colors.YELLOW.getColor());
                gc.fillRect(this.x, this.y + this.height * (1-this.value), this.width, this.height * (this.value - threeshold1));
            }
            else {
                gc.setFill(Colors.GREEN.getColor());
                gc.fillRect(this.x, this.y+this.height * (1-this.value), this.width, this.height*this.value);
            }
        }
        gc.setFill(Colors.WHITE.getColor());
        gc.setGlobalAlpha(1);
    }

    @Override
    public void updateData() {
    }
}
