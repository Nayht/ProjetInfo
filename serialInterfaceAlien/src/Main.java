import java.io.InputStream;

import gnu.io.SerialPort;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;

public class Main extends Application {

    public static void main ( String[] args )
    {
        launch(args);
    }

    public void start(Stage stage) {
        int width = 680;
        int height = 520;

        Group group = new Group();
        Scene scene = new Scene(group);
        Canvas canvas = new Canvas(width, height);
        group.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        stage.setTitle("Alien vs Pineapples");
        stage.setResizable(false);
        stage.setScene(scene);

        System.out.println("ok");

        //Image background = new Image(getClass().getResource("../space.jpg").toExternalForm(), width, height, false, false);
        //gc.drawImage(background, 0, 0);
        gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);

        Image background = new Image(getClass().getResource("space.jpg").toExternalForm(), width, height, false, false);
        //gc.fillText(serial.readMessage(), 540, 36);

        Serial serial = new Serial();
        Thread reader = new Thread(new Serial.SerialReader(serial.getSerialInStream()));
        reader.start();

        MovingObject display = new MovingObject("image", "alien.png", 62, 36, width, height);
        display.render(gc);

        AnimationTimer anim = new AnimationTimer() {
            @Override
            public void handle(long now) {
                String message = serial.readMessage();
                //System.out.println("Message reçu : " + message);

                gc.drawImage(background, 0, 0);
                if (message != null) {
                    if (message.equals("DOWN")) {
                        display.setName("DOWN");
                        display.setSpeed(0, 2);
                    } else if (message.equals("UP")) {
                        display.setName("UP");
                        display.setSpeed(0, -2);
                    } else if (message.equals("LEFT")) {
                        display.setName("LEFT");
                        display.setSpeed(-2, 0);
                    } else if (message.equals("RIGHT")) {
                        display.setName("RIGHT");
                        display.setSpeed(2, 0);
                    } else {
                        display.setName("BITE");
                        //display.setSpeed(0, 0);
                    }
                }
                display.update();
                display.render(gc);

                serial.resetMessage();
            }
        };
        anim.start();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                serial.bite = true;
                serial.disconnect();
                try {
                    reader.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("caca");
            }
        });
        stage.show();
    }
}
