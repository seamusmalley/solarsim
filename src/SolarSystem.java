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
		Image sunImg     = new Image("images/sunmap.jpg");
		Image mercuryImg = new Image("images/mercurymap.jpg");
		Image venusImg   = new Image("images/venusmap.jpg");
		Image earthImg   = new Image("images/earthmap1k.jpg");
		Image marsImg    = new Image("images/mars_1k_color.jpg");
		Image jupiterImg = new Image("images/jupitermap.jpg");
		Image saturnImg  = new Image("images/saturnmap.jpg");
		Image uranusImg  = new Image("images/uranusmap.jpg");
		Image neptuneImg = new Image("images/neptunemap.jpg");
		Image plutoImg = new Image("images/plutomap1k.jpg");
		Image satRingImg = new Image("images/saturn_ring.png");
		Image uranusRingImg = new Image("image/uranus_ring");


		sun     = new Body(00, 0.0,                      0.0, 0.0, null, 0.0,      1.989 * Math.pow(10, 30), sunImg,     "Sun",     false);
		mercury = new Body(01, 5.790 * Math.pow(10, 10), 0.0, 0.0, sun,  2.4400 * Math.pow(10, 6),  3.302 * Math.pow(10, 23), mercuryImg, "Mercury", false, null);
		venus   = new Body(02, 1.082 * Math.pow(10, 11), 0.0, 0.0, sun,  6.0540 * Math.pow(10, 6),  4.869 * Math.pow(10, 24), venusImg,   "Venus",   false, null);
		earth   = new Body(03, 1.496 * Math.pow(10, 11), 0.0, 0.0, sun,  6.3780 * Math.pow(10, 6),  5.974 * Math.pow(10, 24), earthImg,   "Earth",   false, null);
		mars    = new Body(04, 2.279 * Math.pow(10, 11), 0.0, 0.0, sun,  3.3960 * Math.pow(10, 6),  6.419 * Math.pow(10, 23), marsImg,    "Mars",    false, null);
		jupiter = new Body(05, 7.783 * Math.pow(10, 11), 0.0, 0.0, sun,  7.1492 * Math.pow(10, 7), 1.899 * Math.pow(10, 27), jupiterImg, "Jupiter", false, null);
		saturn  = new Body(06, 1.427 * Math.pow(10, 12), 0.0, 0.0, sun,  6.0286 * Math.pow(10, 7), 5.685 * Math.pow(10, 26), saturnImg,  "Saturn",  true, satRingImg);
		uranus  = new Body(07, 2.871 * Math.pow(10, 12), 0.0, 0.0, sun,  2.5557 * Math.pow(10, 7), 8.685 * Math.pow(10, 25), uranusImg,  "Uranus",  true, uranusRingImg);
		neptune = new Body(08, 4.497 * Math.pow(10, 12), 0.0, 0.0, sun,  2.4766 * Math.pow(10, 7), 1.024 * Math.pow(10, 26), neptuneImg, "Neptune", false, null);
    pluto   = new Body(09, 5.906 * Math.pow(10, 12), 0.0, 0.0, sun,  1.1850 * Math.pow(10, 6),  1.460 * Math.pow(10, 22), plutoImg,   "Pluto",   false, null);
                // moon    = new Body(10,


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
			label.setColor(Color.BLACK);

			//make sphere
			PhongMaterial material = new PhongMaterial();
			material.setDiffuseMap(b.img);
			Sphere3D sphere = new Sphere3D(pX, pY, pZ, b.radius, material);
			drawBodies.add(sphere);
			drawBodies.add(label);

			if (b.isRinged) {
				PhongMaterial mat = new PhongMaterial();
				//System.out.println("Adding rings");
				mat.setDiffuseMap(b.ringImg);
				Cylinder3D ring = new Cylinder3D(pX, pY, pZ, 2.0 * b.radius, 0.05, mat);
				drawBodies.add(ring);
			}



		}

		Object[] drawBodyArray = new Object[drawBodies.size()];
		drawBodyArray = drawBodies.toArray();

		return drawBodyArray;
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
