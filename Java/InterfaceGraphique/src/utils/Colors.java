package utils;


import javafx.scene.paint.Color;

public enum Colors {


    WHITE("white",1,1,1,1),
    RED("red",1,0,0,1),
    GREEN("green",0,1,0,1),
    BLUE("blue",0,0,1,1),
    YELLOW("yellow",1,1,0,1),
    PURPLE("purple",1,0,1,1),
    CYAN("cyan",0,1,1,1);

    private String name;
    private double r;
    private double g;
    private double b;
    private double alpha;
    private Color color;

    Colors(String name, double r, double g, double b){
        this(name,r,g,b,1);
    }

    Colors(String name, double r, double g, double b, double alpha){
        this.name=name;
        this.r=r;
        this.g=g;
        this.b=b;
        this.alpha=alpha;
        this.color=new Color(r,g,b,alpha);
    }

    public double getR(){
        return this.r;
    }
    public double getG() {
        return this.g;
    }
    public double getB() {
        return this.b;
    }
    public Color getColor() {
        return this.color;
    }
}
