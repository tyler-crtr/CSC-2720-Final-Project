package cs;

// Import required package
import javax.swing.ImageIcon;

/**
 *This class will be used to store an image and its name
 *It will be used to help display and store all of the
 *users images inside one object array
 */

public class storeImages {

	// Create variables
	private String imageName;
	private ImageIcon I;

	// Define the constructor
	public storeImages(String name, ImageIcon image) {
		this.imageName = name;
		this.I = image;
	}

	// Change the image name
	public void changeName(String n) {
		this.imageName = n;
	}

	// Change the image 
	public void changeImage(ImageIcon image) {
		this.I = image;
	}

	// Return the image name
	public String getName() {
		return this.imageName;
	}

	// Return the image
	public ImageIcon getImage() {
		return this.I;
	}
}