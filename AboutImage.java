package cs;

// Import required packages
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextPane;

/**
 * This is the about page. It pops up when the user clicks the 'About'
 * button on the main ImageStartup page. It displays a stock image to the user
 *  to demonstrate the convert to grayscale option as well as providing 
 *  some background information on the program and how it works.
 */

public class AboutImage extends JFrame {

	// Create JPanel
	private JPanel contentPane;

	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AboutImage frame = new AboutImage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AboutImage() {

		// Create the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 484);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Create the stock photo. It is already saved inside the package.
		// Add the icon to the pane.
		ImageIcon stock = new ImageIcon("stockphoto.jpg");

		JLabel iconLabel = new JLabel();
		iconLabel.setBounds(35, 61, 350, 200);
		iconLabel.setIcon(stock);
		contentPane.add(iconLabel);

		// Button for the user to switch back to the main screen
		JButton btnMain = new JButton("Main");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Switch frames
				dispose();
				ImageStartup i = new ImageStartup();
				i.main(null);
			}
		});
		btnMain.setBounds(300, 15, 115, 30);
		contentPane.add(btnMain);

		// Convert the stock image to back and white to demonstrate to the user
		JButton btnPurify = new JButton("Purify");
		btnPurify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Get the Image from the ImageIcon, then to a BufferedImage
				BufferedImage bi = getBufferedImage(stock.getImage());

				// Prepare to grayscale the image
				BufferedImage newImg = new BufferedImage(
						bi.getWidth(), 
						bi.getHeight(), 
						BufferedImage.TYPE_INT_RGB);

				// Grayscale the image
				ColorConvertOp grayOp = new ColorConvertOp(
						ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
				BufferedImage converted = grayOp.filter(bi, newImg);

				// Display the converted image in the contentPane
				ImageIcon done = new ImageIcon(converted);
				iconLabel.setIcon(done);
			}
		});
		btnPurify.setBounds(15, 15, 115, 30);
		contentPane.add(btnPurify);

		// Add a message explaining the basic mechanics of the program to the contentPane
		String message = "Welcome! This is a very simple image storing application we have made"
				+ " using the WindowBuilder utility for Java Eclipse for our Data Structures final project. " 
				+ "A user creates an account, uploads an image to an online SQL database and "
				+ "can later convert it to grayscale, download it or simply enjoy all their pictures"
				+ "stored together as they please. All the images are displayed as a JList. "
				+ "We hope you enjoy using this small program as much as we enjoyed making it!";
		JLabel lblNewLabel = new JLabel("<html>"+message+"</html>");

		lblNewLabel.setBounds(35, 280, 360, 135);
		contentPane.add(lblNewLabel);

	}

	// Convert an image to a buffered image
	public static BufferedImage getBufferedImage(Image img)
	{	
		// Create the new buffered image
		BufferedImage bimage = new BufferedImage(img.getWidth(null), 
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}