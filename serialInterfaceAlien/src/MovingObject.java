import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MovingObject {
    String type;
    Image img;
    String text;
    int width;
    int height;
    int windowWidth;
    int windowHeight;
    double x;
    double y;
    double xspeed;
    double yspeed;

    MovingObject(String type, String name, int width, int height, int windowWidth, int windowHeight) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        if (type.equals("image")) {
            img = new Image(getClass().getResource(name).toExternalForm(), width, height, false, false);
        } else if (type.equals("text")) {
            this.text = name;
        }
    }

    public void render(GraphicsContext gc) {
        if (this.type.equals("image")) {
            gc.drawImage(this.img, this.x, this.y);
        } else if (this.type.equals("text")) {
            gc.fillText(this.text, this.x, this.y);
        }
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.text = name;
    }

    public void setSpeed(double xspeed, double yspeed) {
        this.xspeed = xspeed;
        this.yspeed = yspeed;
    }

    public void update() {
        this.x += this.xspeed;
        this.y += this.yspeed;
        this.validatePosition();
    }

    public void validatePosition() {
        if (this.x > this.windowWidth - this.width || this.x < 0) {
            this.xspeed = 0;
        }
        if (this.y > this.windowHeight - this.height || this.y < 0) {
            this.yspeed = 0;
        }
    }
}
