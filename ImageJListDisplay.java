package cs;

// Import required packages
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.sun.java.swing.*;

import java.util.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This is the page which displays the images as a JList component
 * in the GUI. The user can cycle between the images based on their
 * names and click the one they want to see to appear.
 */

public class ImageJListDisplay extends JSplitPane
implements ListSelectionListener {

	// Initialize the greyCheck boolean to false
	static boolean greyCheck = false;

	// Initialize the selected image and selected picture label
	// which will be used interchangeably to display the image
	ImageIcon currentImage;
	JLabel picture;

	// Initialize the scroll pane components.
	JScrollPane pictureScrollPane, listScrollPane;

	// List of images will be the result of converting
	// an ArrayList of Strings to a JList to display
	// in the scroll pane
	JList listOfImages;

	// This is needed to get the store all the images inside
	// an ArrayList and return the displayed one when needed
	ArrayList<storeImages> pass = new ArrayList<storeImages>();

	public ImageJListDisplay(ArrayList<storeImages> users) {

		// Declare settings for the GUI, set pass equal to users.
		super(HORIZONTAL_SPLIT);
		setOneTouchExpandable(true);
		pass = users;

		// Set a new ArrayList of strings to convert the image names
		// into the JList 'listOfImages'.
		ArrayList<String>iNames = new ArrayList<String>();

		for(int x = 0; x <users.size();x++) {
			iNames.add(users.get(x).getName());
		}

		listOfImages = new JList(iNames.toArray());

		// Adjust the components accordingly then add the list of 
		// image names to the pane.
		listOfImages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listOfImages.setSelectedIndex(0);
		listOfImages.addListSelectionListener(this);

		listScrollPane = new JScrollPane(listOfImages);

		// Specify divider size
		setDividerLocation(150);
		setDividerSize(10);

		// The first image to be displayed will be index(0) of the
		// list of images. Display it in the viewing pane.
		currentImage = users.get(0).getImage();
		picture = new JLabel(currentImage);
		picture.setPreferredSize(new Dimension(currentImage.getIconWidth(),
				currentImage.getIconHeight()));
		pictureScrollPane = new JScrollPane(picture);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		pictureScrollPane.setMinimumSize(minimumSize);

		// Provide a preferred size for the split pane
		setPreferredSize(new Dimension(1200, 1200));

		// Add the two scroll panes to this split pane
		setRightComponent(pictureScrollPane);
		setLeftComponent(listScrollPane);
	}

	//Return the index of the current image on display
	public int getIndex() {
		return listOfImages.getSelectedIndex();
	}

	// The grayscale converted image is passed in, is displayed
	// in the view panel accordingly.
	public void purify(ImageIcon done) {

		// Set the image to display to be the passed ImageIcon 'done'.
		greyCheck= true;
		currentImage = done;

		// Add the grayscale image to the pane
		picture = new JLabel(currentImage);
		picture.setPreferredSize(new Dimension(currentImage.getIconWidth(),
				currentImage.getIconHeight()));
		pictureScrollPane = new JScrollPane(picture);
		setRightComponent(pictureScrollPane);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		pictureScrollPane.setMinimumSize(minimumSize);

		// Set the initial location and size of the divider
		setDividerLocation(150);
		setDividerSize(10);

		// Provide a preferred size for the split pane
		setPreferredSize(new Dimension(1200, 1200));
	}

	// The user wants to convert the image to grayscale.
	public void setGreyCheck(boolean b) {
		greyCheck = b;
	}

	// Return if the user wants grayscale or not.
	public boolean getGreyCheck() {
		return greyCheck;
	}

	// Return the current selected image in the view pane
	public ImageIcon returnImageOnDisplay() {
		return currentImage;
	}

	// If the user selects an image from the list
	public void valueChanged(ListSelectionEvent e) {

		// The new image will not be in grayscale unless otherwise specified.
		this.setGreyCheck(false);

		// Get the image based on the index of the selected JList index
		int selected;
		selected = listOfImages.getSelectedIndex();
		currentImage = pass.get(selected).getImage();

		// Use the selected int above to find the corresponding
		// image to the index and display it in the pane.
		picture = new JLabel(currentImage);
		picture.setPreferredSize(new Dimension(currentImage.getIconWidth(),
				currentImage.getIconHeight()));
		pictureScrollPane = new JScrollPane(picture);
		setRightComponent(pictureScrollPane);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		pictureScrollPane.setMinimumSize(minimumSize);

		// Set the initial location and size of the divider
		setDividerLocation(150);
		setDividerSize(10);

		// Provide a preferred size for the split pane
		setPreferredSize(new Dimension(1200, 1200));
	}
}