package cs;

// Import required packages
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JPasswordField;

/**
 * This is the startup page. It is the first page that appears
 * when the program runs. It displays the login panel for the
 * user to enter the program and it gives them the option to 
 * create a new account or view an 'About' page which has
 * some meta details about the program
 * 
 * NOTE: The online SQL database used is a bit slow and also
 * requires regular affirmation to keep the database from being
 * erased due to inactivity.
 */

public class ImageStartup {

	// Create GUI components
	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;

	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageStartup window = new ImageStartup();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application
	public ImageStartup() {
		initialize();
	}

	private void initialize() {

		// Create the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 547, 434);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Purified Images!");
		
		// Add the username label
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(87, 60, 120, 20);
		frame.getContentPane().add(usernameLabel);

		// Add the password label
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(87, 110, 95, 20);
		frame.getContentPane().add(passwordLabel);

		// Add the username field
		usernameField = new JTextField();
		usernameField.setBounds(220, 60, 145, 25);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);

		// Add the password field
		passwordField = new JPasswordField();
		passwordField.setBounds(220, 110, 145, 25);
		frame.getContentPane().add(passwordField);

		// Create the login button
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// If the username and password can't be validated, display an error 
				// info box.
				if(!(login(usernameField.getText(),passwordField.getText()))) {
					Info.infoBox("Invalid username/password!", "Error");
				}
				// If username/password field is blank.
				else if(usernameField.getText().equals("")||passwordField.getText().equals("")){
					
				}
				else {
					// Make a new ImageUser object with the validated username and password,
					// pass it into a new loggedIn frame to work with. Dispose this frame
					// and load the loggedIn frame
					ImageUser current = new ImageUser(usernameField.getText(),passwordField.getText());
					frame.dispose();
					loggedIn e = new loggedIn(current);
					e.setVisible(true);
				}
			}


		});
		btnNewButton.setSize(115, 40);
		btnNewButton.setPreferredSize(new Dimension(100,50));
		btnNewButton.setLocation(175, 187);
		frame.getContentPane().add(btnNewButton);

		// Add the register button
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Dispose this frame, load the registration page
				frame.dispose();
				registration r = new registration();
				r.setVisible(true);

			}
		});
		btnRegister.setBounds(175, 270, 115, 40);
		frame.getContentPane().add(btnRegister);

		// Add the About button
		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Dispose this frame, display the About frame.
				frame.dispose();
				AboutImage a = new AboutImage();
				a.main(null);
			}
		});
		btnAbout.setBounds(395, 15, 115, 30);
		frame.getContentPane().add(btnAbout);
		
		


	}

	// Login validation method. Checks if the username and password
	// match up in an existing account in the SQL online database.
	public boolean login(String username, String password) {

		// The users ArrayList will store all the accounts stored in the database
		// The check ImageUser object will store this current username and password
		// entered by the user to validate.
		ArrayList<ImageUser> users = new ArrayList<ImageUser>();
		ImageUser check = new ImageUser(username,password);

		try {
			// Establish connection, set autocommit to false
			// to prevent unnecessary console dialog messages
			Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
			myConn.setAutoCommit(false);

			// Create a prepared statement, read the data from the databse
			Statement stm;
			stm = myConn.createStatement();
			String sql = "Select * From users";
			ResultSet rst;
			rst = stm.executeQuery(sql);
			myConn.commit();

			// Use a while loop to add all the existing users inside the ArrayList
			while (rst.next()) {

				ImageUser u = new ImageUser(rst.getString("username"),rst.getString("password"));
				users.add(u);

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		
		// If the username and password match, the login
		// has been validated. Else the information either
		// does not exist or is invalid/incorrect.
		for(int x = 0; x < users.size(); x++) {
			if(users.get(x).equals(check)){
				return true;
			}
		}
		return false;
	}

	// Info box method to display pop up messages.
	public static class Info
	{
		public static void infoBox(String infoMessage, String titleBar)
		{
			JOptionPane.showMessageDialog(null, infoMessage,titleBar, JOptionPane.INFORMATION_MESSAGE);
		}
	}
}