// Simulation happens here
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

public class SolarSystem implements Algorithm {

	//Defaults
	final double sunRadius = 3.0;

	//The planets
	Body sun;
	Body earth;
	Body mercury, venus, mars, jupiter, saturn, uranus, neptune;

	ArrayList<Body> bodies;


	public SolarSystem() {

		//Initialize planets
		initPlanets();
	}


	public void initPlanets() {
		//load images
		Image sunImg = new Image("images/sunmap.jpg");
		Image mercuryImg = new Image("images/mercurymap.jpg");
		Image venusImg = new Image("images/venusmap.jpg");
		Image earthImg = new Image("images/earthmap1k.jpg");
		Image marsImg = new Image("images/mars_1k_color.jpg");
		Image jupiterImg = new Image("images/jupitermap.jpg");
		Image saturnImg = new Image("images/saturnmap.jpg");
		Image uranusImg = new Image("images/uranusmap.jpg");
		Image neptuneImg = new Image("images/neptunemap.jpg");

		sun = new Body(0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.989 * Math.pow(10, 30), 0.0, 0.0, sunImg, "Sun", false);
		mercury = new Body(1, 5.79 * Math.pow(10, 10), 0.0, 0.0, 0.1, 5.79 * Math.pow(10, 11), 3.302 * Math.pow(10, 23), 0.0, 47873, mercuryImg, "Mercury", false);
		venus = new Body(2, 1.082 * Math.pow(10, 11), 0.0, 0.0, 0.1, 1.082 * Math.pow(10, 11), 4.869 * Math.pow(10, 24), 0.0, 35021, venusImg, "Venus", false);
		earth = new Body(3, 1.496 * Math.pow(10, 11), 0.0, 0.0, 0.1, 1.496 * Math.pow(10, 11), 5.974 * Math.pow(10, 24), 0.0, 29786, earthImg, "Earth", false);
		mars = new Body(4, 2.279 * Math.pow(10, 11), 0.0, 0.0, 0.075, 2.279 * Math.pow(10, 11), 6.419 * Math.pow(10, 23), 0.0, 24131, marsImg, "Mars", false);
		jupiter = new Body(5, 7.783 * Math.pow(10, 11), 0.0, 0.0, 2, 7.783 * Math.pow(10, 11), 1.899 * Math.pow(10, 27), 0.0, 13070, jupiterImg, "Jupiter", false);
		saturn = new Body(6, 1.427 * Math.pow(10, 12), 0.0, 0.0, 1.5, 1.427 * Math.pow(10, 12), 5.685 * Math.pow(10, 26), 0.0, 9672, saturnImg, "Saturn", true);
		uranus = new Body(7, 2.871 * Math.pow(10, 12), 0.0, 0.0, 1.25, 2.871 * Math.pow(10, 12), 8.685 * Math.pow(10, 25), 0.0, 6835, uranusImg, "Uranus", false);
		neptune = new Body(8, 4.497 * Math.pow(10, 12), 0.0, 0.0, 1.25, 4.497 * Math.pow(10, 12), 1.024 * Math.pow(10, 26), 0.0, 5478, neptuneImg, "Neptune", false);

		//In AU
		// mercury = new Body(1, 0.387, 0.0, 0.0, 0.1, 0.387, 3.302 * Math.pow(10, 23), 0.0, mToAu(47873), Color.GREY, "Mercury");
		// venus = new Body(2, 0.723, 0.0, 0.0, 0.1, 0.723, 4.869 * Math.pow(10, 24), 0.0, mToAu(35021), Color.ORANGE, "Venus");
		// earth = new Body(3, 1.0, 0.0, 0.0, 0.1, 1.0, 5.974 * Math.pow(10, 24), 0.0, mToAu(29786), Color.BLUE, "Earth");

		bodies = new ArrayList<Body>(); //an array to hold the bodies
		bodies.add(sun);
		bodies.add(mercury);
		bodies.add(venus);
		bodies.add(earth);
		bodies.add(mars);
		bodies.add(jupiter);
		bodies.add(saturn);
		bodies.add(uranus);
		bodies.add(neptune);

	}

	//Interface methods
	//This is the main animation function - it is called at specific intervals by the GUI
	//We just make it return the array ob objects we want to be drawn
	public Object processAlgorithm(int iteration) {
		//System.out.println(iteration);
		//update body positions
		for (Body b: bodies) {
			b.updatePosition(iteration, bodies);
			b.finalizePosition();
		}

		//finalize positions and create spheres that can be drawn for the bodies
		//we shift the x position for the planets by the sun radius to get better visual product
		ArrayList<Object> drawBodies = new ArrayList<Object>();
		for (int i = 0; i < bodies.size(); i++) {
			//get body
			Body b = bodies.get(i);
			double offset = 0.0;
			if (b.id != 0) offset = sunRadius;

			double pX = mToAu(b.x);
			double pY = mToAu(b.y);
			double pZ = mToAu(b.z);

			//make label
			Text3D label = new Text3D(pX, pY, pZ + (b.radius * 1.1), 0.0, 180.0, 0.0, b.name);
			label.setColor(Color.WHITE);

			//make sphere
			PhongMaterial material = new PhongMaterial();
			material.setDiffuseMap(b.img);
			Sphere3D sphere = new Sphere3D(pX, pY, pZ, b.radius, material);
			drawBodies.add(sphere);
			drawBodies.add(label);

			if (b.isRinged) {
				PhongMaterial mat = new PhongMaterial(Color.YELLOW);
				//System.out.println("Adding rings");
				// Image i1 = new Image("images/saturnringcolor.jpg");
				// Image i2 = new Image("images/saturnringpattern.gif");
				// mat.setDiffuseMap(i1);
				// mat.setSpecularMap(i2);
				Cylinder3D rings = new Cylinder3D(b.x, b.y, b.z, 3.0, 1.0, mat);
				drawBodies.add(rings);
			}



		}

		Object[] drawing = new Object[drawBodies.size()];
		drawing = drawBodies.toArray();

		return drawing;
	}

	public PhongMaterial getMaterial(int iteration) {
		return new PhongMaterial();
	}

	public boolean isDrone() {
		return false;
	}

	public boolean doClear() {
		return true;
	}

	public long getDelay() {
		return 100;
	}

	public int getIterations() {
		return 5000;
	}

	public double[] getInfo() {
		double[] a = new double[10];
		return a;
	}

	public void setInfo(double[] info) {
		System.out.println("The info method stuff:");
		System.out.println(info);
	}

	public void setId(int id) {
		System.out.println("Id: " + id);
	}

	public int getId() {
		return 1;
	}

	//Utility methods
	public double mToAu(double m) {
		//return km * (6.6846 * Math.pow(10, -12));
		return m / (1.496 * Math.pow(10, 11));
	}

}
