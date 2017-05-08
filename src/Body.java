// Physical bodies for the simulation
import org.edisonwj.draw3d.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javafx.scene.image.Image;

public class Body {
        // Coordinates
        double x, y, z;

        // Physical dimensions
        double mass, radius;
        Color color;

        // Orbit Information
        double period;
        double e;


        public Body(double x, double y, double z, double radius, double mass, Color c) {
          this.x = x;
          this.y = y;
          this.z = z;
          this.mass = mass;
          this.radius = radius;
          this.color = c;
        }

        public Sphere3D createSphere() {
          PhongMaterial material = new PhongMaterial(color);
          return new Sphere3D(x, y, z, radius, material);
        }

}
