package cs;

// Import required packages
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * This is the logged in frame. This is where the user
 * can upload, delete, save or choose to convert an image
 * grayscale. Any action the user takes is closely interwined
 * with the ImageJListDisplay class as that is the actual one
 * which displays the images.
 */

public class loggedIn extends JFrame implements ListSelectionListener{

	// Make the content pane, tool bar
	static JPanel contentPane;
	static JToolBar toolBar = new JToolBar();

	// Declare an ImageUser object to later be assigned
	// the passed user credentials
	public static ImageUser x = new ImageUser("___","___");

	// Create appropriate ArrayLists for storing the images and related information
	static ArrayList<storeImages> markedImages = new ArrayList<storeImages>();
	static ArrayList< BufferedImage > listOfImages = new ArrayList< BufferedImage >();
	static ArrayList< String > imageName = new ArrayList< String >();
	static ArrayList< ImageIcon > iconArray = new ArrayList< ImageIcon >();

	// Get the user login info from the main page, assign them
	// to an ImageUser object in this class
	public void passUser(ImageUser u) {
		x.changeUsername(u.getUsername());
		x.changePassword(u.getPassword());

	}

	public loggedIn(ImageUser e) {
		// Create the frame.
		x = e;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 550, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// Clear all the ArrayLists. Must be done so data
		// for another user does not appear if one user logs out and
		// another user logs in.
		markedImages.clear();
		listOfImages.clear();
		toolBar.removeAll();

		// Clear the other ArrayLists, populate the pane with
		// already uploaded images if any using the populate() method
		// if there are no uploaded images it will simply display
		// a blank panel
		try {
			imageName.clear();
			iconArray.clear();
			populate();
		}catch(java.lang.IndexOutOfBoundsException E){

		}

		// Add the upload button to upload images.
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// File chooser to pick the image. For simplicity the
				// desired images are only jpg and jpeg
				JFileChooser fc = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JPG Images", "jpg","jpeg");
				fc.setFileFilter(filter);

				// Get the file chosen by the user
				int returnVal = fc.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						ImageIcon getPic = new ImageIcon(ImageIO.read(file));


						try {
							// Get the file name from an input dialog box
							String name = JOptionPane.showInputDialog("What is the name of the file?");

							// Users are not allowed to upload multiple images with the same name
							if(imageName.contains(name.toLowerCase())) {
								JOptionPane.showMessageDialog(null, "An image with the same name already exists.", "Error!", JOptionPane.INFORMATION_MESSAGE);

							}

							else {

								// If the upload is successful, this frame will be disposed
								// and a new copy of the same fame will be created and displayed
								// with the uploaded image available to view and modify
								toolBar.removeAll();
								uploadPicture(getPic,name.toLowerCase());
								contentPane.setVisible(false);
								dispose();
								loggedIn e = new loggedIn(x);
								e.setVisible(true);
							}

						}catch(Exception e) {
							// If the upload fales load a new copy of this frame.
							loggedIn frame = new loggedIn(x);
							frame.setVisible(true);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnUpload.setBounds(178, 199, 115, 29);

		// Add the Upload button to the toolbar.
		toolBar.add(btnUpload);
		contentPane.add(toolBar,BorderLayout.NORTH);

		// Create and add the 'Main' button to go back to the main screen if desired.
		JButton btnMain = new JButton("Main");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Clear the toolbar, dispose this frame and display the main page
				toolBar.removeAll();
				markedImages.clear();
				contentPane.setVisible(false);
				dispose();
				ImageStartup.main(null);

			}
		});
		btnMain.setBounds(298, 16, 115, 29);
		toolBar.add(btnMain);
	}

	// Method to convert an image to a buffered image.
	public BufferedImage getBufferedImage(Image img)
	{	
		// Conversion process
		BufferedImage bimage = new BufferedImage(img.getWidth(null), 
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	// Convert an ImageIcon to a byte[]array. Done to upload as a
	// BLOB into the database.
	public  byte[] toByte(ImageIcon c) {
		try {

			// Conversion process
			BufferedImage bi = getBufferedImage(c.getImage());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			baos.flush();
			byte[] byteArray= baos.toByteArray();
			baos.close();

			// Return the byte[] array
			return byteArray;

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	// The process of uploading the actual image into the databse is done here
	public  void uploadPicture(ImageIcon pic, String name) {
		try {

			// Establish connection
			Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
			myConn.setAutoCommit(false);

			// Prepare the statement to insert
			String INSERT_PICTURE = "INSERT INTO images SET imageID = ?, username = ?, image = ?";

			// Get the byte[] array and create the prepared statement
			byte[] upload = toByte(pic);
			PreparedStatement ps = null;

			// Convert the byte[] array to a BLOB
			Blob blob = new javax.sql.rowset.serial.SerialBlob(upload);

			// Update the prepared statement and execute it into the database.
			ps = myConn.prepareStatement(INSERT_PICTURE);
			ps.setString(1, name);
			ps.setString(2, x.getUsername());
			ps.setBlob(3, blob);

			ps.executeUpdate();
			myConn.commit();

			// Make a new storeImages object with the uploaded image,
			// add it to the list of existing images and repopulate
			// the screen with the new image to display it in the pane.
			storeImages uploadedPic = new storeImages(name,pic);
			markedImages.add(uploadedPic);
			populate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// The images will be obtained from the database and added into lists here
	public  void populate() {

		// Clear the list to erase any previous users' info
		markedImages.clear();

		// Get the current user's username
		String n = x.getUsername();

		// ArrayList of blobs to store all images obtained from the database
		ArrayList< Blob > b = new ArrayList< Blob >();

		// ArrayList of information about images received
		ArrayList<storeImages> recievedImages = new ArrayList<storeImages>();

		try {
			// Establish connection
			Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
			myConn.setAutoCommit(false);

			// Get all the images belonging to a particular username
			String sql = "SELECT * FROM images WHERE username=?";
			PreparedStatement statement = myConn.prepareStatement(sql);
			statement.setString(1, n);

			ResultSet result = statement.executeQuery();

			// Put all the images and information obtained into the ArrayLists 
			// for further use in the program.
			while (result.next()) {

				try {
					String getImageName = result.getString("imageID");

					java.sql.Blob blob = result.getBlob("image");  
					b.add(blob);
					InputStream in = blob.getBinaryStream();  
					BufferedImage image = ImageIO.read(in);
					listOfImages.add(image);
					ImageIcon h = new ImageIcon(image);
					iconArray.add(h);

					imageName.add(getImageName);

					storeImages x = new storeImages(getImageName,h);
					recievedImages.add(x);
					markedImages.add(x);


				} catch (Exception e) {
					// If it fails display a fresh copy of this frame 
					e.printStackTrace();
					loggedIn frame = new loggedIn(x);
					frame.setVisible(true);
				}
			}
			myConn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			// Display a fresh copy in case of failure
			loggedIn frame = new loggedIn(x);
			frame.setVisible(true);
		}

		// The images will be passed into the ImageJListDisplay here 
		// to be shown to the user.
		displayAll(markedImages);

	}

	// The images will be displayed here. Functions such as Save, Delete,
	// convert to grayscale (Purify) will be accessible here.
	public  void displayAll(ArrayList<storeImages> markedImages2) {


		ImageJListDisplay i = new ImageJListDisplay(markedImages2);
		contentPane.add("Center", i);

		// Create and display the svae button
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Return the current selected image from ImageJListDisplay
				ImageIcon yourImage = i.returnImageOnDisplay();
				Image image = yourImage.getImage();

				// Convert it to a buffered image to be saved to file
				BufferedImage buffered = (BufferedImage) image;

				// Use JFileChooser to choose the directory. The extension
				// cannot be added in without directly modifying the JFileChooser
				// class built in inside java so the user is instructed to enter
				// their desired extension while saving.
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter f = new FileNameExtensionFilter(
						"NOTE: Add '.desired image extension' after the name!","jpg");
				fileChooser.setFileFilter(f);

				// Directory has been chosen
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					// Save to file, if it has been grayscaled save it
					// in its grayscale form
					try {
						if(i.getGreyCheck()) {
							ColorConvertOp grayOp = new ColorConvertOp(
									ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
							BufferedImage newImg = new BufferedImage(
									buffered.getWidth(), 
									buffered.getHeight(), 
									BufferedImage.TYPE_INT_RGB);

							BufferedImage converted = grayOp.filter(buffered, newImg);
							ImageIO.write(converted, "jpg", file);
							i.setGreyCheck(false);
						}

						//else save it in its normal colors
						else ImageIO.write(buffered, "jpg", file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}    	
			}
		});
		btnSave.setBounds(300, 15, 115, 30);
		toolBar.add(btnSave);

		// Create and add the Purify button
		JButton btnGray = new JButton("Purify");
		btnGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// If the user wants to convert the image to grayscale,
				// convert it and send it back to the ImageJListDisplay
				// to be displayed
				i.setGreyCheck(true);
				int ghg = i.getIndex();

				storeImages y = markedImages.get(ghg);
				ImageIcon gt = y.getImage();

				BufferedImage bi = getBufferedImage(gt.getImage());

				BufferedImage newImg = new BufferedImage(
						bi.getWidth(), 
						bi.getHeight(), 
						BufferedImage.TYPE_INT_RGB);

				ColorConvertOp grayOp = new ColorConvertOp(
						ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

				BufferedImage converted = grayOp.filter(bi, newImg);
				ImageIcon done = new ImageIcon(converted);

				JLabel thumb = new JLabel();
				thumb.setIcon(done);

				i.purify(done);
			}
		});
		btnGray.setBounds(300, 15, 115, 30);
		toolBar.add(btnGray);


		// Create and add the Delete button
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Get the current selected image
				int ghg = i.getIndex();

				storeImages y = markedImages.get(ghg);

				// Delete the image from the database, show the user a confirmation
				// dialog box to inform the successful deletion
				try {

					Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
					myConn.setAutoCommit(false);

					String update =  ("DELETE FROM images WHERE imageID = ? AND username = ?");

					PreparedStatement ps = null;
					ps = myConn.prepareStatement(update);

					ps.setString(1, y.getName());
					ps.setString(2, x.getUsername());
					ps.executeUpdate();
					myConn.commit();

					JOptionPane.showMessageDialog(null, "Your image will be deleted.", "Done", JOptionPane.INFORMATION_MESSAGE);

					markedImages2.clear();
					dispose();
					loggedIn e = new loggedIn(x);
					e.setVisible(true);


				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnDelete.setBounds(300, 15, 115, 30);
		toolBar.add(btnDelete);
	}

	//	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
