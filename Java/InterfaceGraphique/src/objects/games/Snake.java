package objects.games;

import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.abstracts.AbstractObject;

import java.util.ArrayList;

public class Snake extends AbstractObject {

    ArrayList<int[]> listCoodrs;
    double size;
    double tileSize;
    int nbTilePerSide;
    int refreshInterval;
    long lastUpdateTimeStamp;
    int[] foodPosition;
    boolean hasEatenSomething;
    String direction;
    String newDirection;

    public Snake(int x, int y, GraphicsContext gc){
        this(x,y,gc,20);
    }

    public Snake(int x, int y, GraphicsContext gc, double size){
        super (x,y,gc);
        this.size=size;
        this.refreshInterval=200;
        setUp();
    }


    private void setUp(){
        this.direction="right";
        this.newDirection="right";
        this.lastUpdateTimeStamp=System.currentTimeMillis();
        this.listCoodrs=new ArrayList<>();
        this.listCoodrs.add(new int[]{2,2});
        this.listCoodrs.add(new int[]{3,2});
        this.listCoodrs.add(new int[]{4,2});
        this.listCoodrs.add(new int[]{5,2});
        this.listCoodrs.add(new int[]{6,2});
        this.listCoodrs.add(new int[]{7,2});
        generateFoodPosition();
        this.hasEatenSomething=false;
    }


    private void generateFoodPosition(){
        boolean isFoodPositionInSnake=true;
        this.foodPosition=new int[2];
        while (isFoodPositionInSnake){
            this.foodPosition[0]=(int)(Math.round((Math.random()*this.nbTilePerSide)));
            this.foodPosition[1]=(int)(Math.round((Math.random()*this.nbTilePerSide)));
            for (int[] snakeTile : this.listCoodrs){
                if (snakeTile[0]==foodPosition[0] && snakeTile[1]==foodPosition[1]){
                    isFoodPositionInSnake=true;
                }
            }
        }
    }

    private void moveSnake(int[] nextStep){
        if (nextStep[0]==this.foodPosition[0] && nextStep[1]==this.foodPosition[1]){
            this.hasEatenSomething=true;
        }
        if (!this.hasEatenSomething){
            for(int i=0; i<this.listCoodrs.size()-1; i++){
                if (this.listCoodrs.get(i)[0] == nextStep[0] && this.listCoodrs.get(i)[1] == nextStep[1]){
                    setUp();
                    return;
                }
                this.listCoodrs.set(i,this.listCoodrs.get(i+1));
            }
            this.listCoodrs.set(this.listCoodrs.size()-1,nextStep);
        }
        else{
            generateFoodPosition();
            this.listCoodrs.add(nextStep);
        }
    }

    public void changeDirection(String newDirection){
        if (!(this.direction.equals("right") && newDirection.equals("left"))){
            if (!(this.direction.equals("left") && newDirection.equals("right"))){
                if (!(this.direction.equals("up") && newDirection.equals("down"))){
                    if (!(this.direction.equals("down") && newDirection.equals("up"))){
                        this.newDirection=newDirection;
                    }
                }
            }
        }
    }


    @Override
    public void display() {
        gc.fillRoundRect(this.x,this.y,this.size*20, this.size*20, 10,10);
        gc.setFill(Color.RED);
        gc.fillRect(this.x+this.foodPosition[0]*this.tileSize,this.y+this.foodPosition[1]*this.tileSize,this.tileSize, this.tileSize);
        gc.setFill(Color.WHITE);
        for (int[] tile : this.listCoodrs){
            gc.fillRect(this.x+tile[0]*this.tileSize, this.y+tile[1]*this.tileSize, this.tileSize, this.tileSize);
        }
    }

    @Override
    public void updateData() {
        if (System.currentTimeMillis()>this.lastUpdateTimeStamp+this.refreshInterval) {
            this.lastUpdateTimeStamp=System.currentTimeMillis();
            int[] nextStep = this.listCoodrs.get(this.listCoodrs.size() - 1);
            if (this.direction.equals("right")) {
                nextStep[0] += 1;
                if (nextStep[0] > this.nbTilePerSide) {
                    nextStep[0] = 0;
                }
            } else if (this.direction.equals("left")) {
                nextStep[0] -= 1;
                if (nextStep[0] < 0) {
                    nextStep[0] = this.nbTilePerSide;
                }
            } else if (this.direction.equals("up")) {
                nextStep[1] -= 1;
                if (nextStep[1] < 0) {
                    nextStep[1] = this.nbTilePerSide;
                }
            } else if (this.direction.equals("down")) {
                nextStep[1] += 1;
                if (nextStep[1] > this.nbTilePerSide) {
                    nextStep[1] = 0;
                }
            }
            moveSnake(nextStep);
        }
    }
}
