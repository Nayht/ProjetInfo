package objects.games;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.abstracts.AbstractObject;

import java.util.ArrayList;


public class Snake extends AbstractObject {

    ArrayList<int[]> coordsList=new ArrayList<>();
    String direction;
    String newDirection;
    private double size;
    private double tileSize;
    private long lastUpdateTimeStamp;
    private int updateInterval;
    private int nbTilePerSide;
    private double squareSideLength;
    private boolean hasEatenSomething;
    private int[] foodPosition;

    public Snake(int x, int y, GraphicsContext gc){
        this(x,y,gc, gc.getCanvas().getWidth()*0.025);
    }

    public Snake(int x, int y, GraphicsContext gc, double size) {
        super(x,y,gc);
        this.size=size;
        this.updateInterval=750;
        this.nbTilePerSide=10;
        this.squareSideLength=20*this.size;
        this.tileSize=this.squareSideLength/(this.nbTilePerSide+1);
        setUp();
    }

    private void setUp(){
        this.lastUpdateTimeStamp =System.currentTimeMillis();
        this.coordsList=new ArrayList<>();
        this.coordsList.add(new int[]{2,5});
        this.coordsList.add(new int[]{3,5});
        this.coordsList.add(new int[]{4,5});
        this.coordsList.add(new int[]{5,5});
        generateFoodPosition();
        this.direction="right";
        this.newDirection="right";
        this.hasEatenSomething=false;
    }

    @Override
    public void display() {
        gc.strokeRoundRect(this.x,this.y,this.squareSideLength,this.squareSideLength,20,20);
        gc.setFill(Color.RED);
        gc.fillRect(this.x+this.foodPosition[0]*this.tileSize,this.y+this.foodPosition[1]*this.tileSize,this.tileSize,this.tileSize);
        gc.setFill(Color.WHITE);
        for (int[] tile : this.coordsList){
            gc.fillRect(this.x+tile[0]*this.tileSize,this.y+tile[1]*this.tileSize,this.tileSize,this.tileSize);
        }
    }

    @Override
    public void updateData() {
        if (System.currentTimeMillis()>this.lastUpdateTimeStamp+this.updateInterval) {
            this.direction=this.newDirection;
            this.lastUpdateTimeStamp=System.currentTimeMillis();
            int[] lastStep = this.coordsList.get(this.coordsList.size() - 1);
            int[] nextStep;
            if (direction.equals("right")) {
                nextStep = new int[]{lastStep[0] + 1, lastStep[1]};
                if (lastStep[0]==this.nbTilePerSide){
                    nextStep[0]=0;
                }
                moveSnake(nextStep);
            } else if (direction.equals("left")) {
                nextStep = new int[]{lastStep[0] - 1, lastStep[1]};
                if (lastStep[0]==0){
                    nextStep[0]=this.nbTilePerSide;
                }
                moveSnake(nextStep);
            } else if (direction.equals("up")) {
                nextStep = new int[]{lastStep[0], lastStep[1] - 1};
                if (lastStep[1]==0){
                    nextStep[1]=this.nbTilePerSide;
                }
                moveSnake(nextStep);
            } else if (direction.equals("down")) {
                nextStep = new int[]{lastStep[0], lastStep[1] + 1};
                if (lastStep[1]==this.nbTilePerSide){
                    nextStep[1]=0;
                }
                moveSnake(nextStep);
            }
        }
    }


    private void generateFoodPosition() {
        boolean foodPositionInSnake = true;
        while (foodPositionInSnake) {
            foodPositionInSnake = false;
            this.foodPosition = new int[]{(int) Math.round(Math.random()*this.nbTilePerSide), (int) Math.round(Math.random()*this.nbTilePerSide)};
            for (int i = 0; i < this.coordsList.size() - 1; i++) {
                if (this.coordsList.get(i)[0] == this.foodPosition[0] && this.coordsList.get(i)[1] == this.foodPosition[1]) {
                    foodPositionInSnake = true;
                    break;
                }
            }
        }
    }

    public void changeDirection(String newDirection){
        if (!(newDirection.equals("right") && this.direction.equals("left"))){
            if (!(newDirection.equals("left") && this.direction.equals("right"))){
                if (!(newDirection.equals("up") && this.direction.equals("down"))){
                    if (!(newDirection.equals("down") && this.direction.equals("up"))){
                        this.newDirection = newDirection;
                    }
                }
            }
        }
    }

    private void moveSnake(int[] nextStep){
        if (this.foodPosition[0]==nextStep[0] && this.foodPosition[1]==nextStep[1]){
            this.hasEatenSomething=true;
        }
        if (!hasEatenSomething) {
            for (int i = 0; i < this.coordsList.size() - 1; i++) {
                if (this.coordsList.get(i)[0]==nextStep[0] && this.coordsList.get(i)[1]==nextStep[1]){
                    setUp();
                    return;
                }
                this.coordsList.set(i, this.coordsList.get(i+1));
            }
            this.coordsList.set(this.coordsList.size() - 1, nextStep);
        }
        else{
            this.coordsList.add(nextStep);
            generateFoodPosition();
            this.hasEatenSomething=false;
        }
    }
}
