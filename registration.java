package cs;

// Import required packages
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.sql.*;
import java.util.ArrayList;

/**
 * This is the registration page. It appears when the Register button
 * on the ImageStartup page is pressed. It makes a new account for the
 * user and displays a welcome popup box only if:
 * 1. the username is not already taken (case-sensitive)
 * 2. the passwords match
 * 3. the username/password is not a blank space
 */

public class registration extends JFrame {

	// Create GUI components
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnCreate;
	private JPasswordField enterPW;
	private JPasswordField enterPWagain;

	// Launch the application
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registration frame = new registration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public registration() {

		// Create the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// If the Main button it pressed it disposes the current
		// frame and sends the viewer back to the ImageStartup main page
		JButton btnMain = new JButton("Main");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				ImageStartup i = new ImageStartup();
				contentPane.setVisible(false);
				dispose();
				i.main(null);
			}
		});
		btnMain.setBounds(300, 15, 115, 30);
		contentPane.add(btnMain);

		// Make the username label
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(40, 80, 105, 20);
		contentPane.add(usernameLabel);

		// Make the password label
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(40, 120, 105, 20);
		contentPane.add(passwordLabel);

		// Make the repeat password label
		JLabel lblRepeatPassword = new JLabel("Repeat password:");
		lblRepeatPassword.setBounds(40, 155, 125, 20);
		contentPane.add(lblRepeatPassword);

		// Create username text field
		textField = new JTextField();
		textField.setBounds(200, 75, 145, 25);
		contentPane.add(textField);
		textField.setColumns(10);

		// Create password text field
		enterPW = new JPasswordField();
		enterPW.setBounds(200, 115, 145, 25);
		contentPane.add(enterPW);

		// Create repeat password text field
		enterPWagain = new JPasswordField();
		enterPWagain.setBounds(200, 150, 145, 25);
		contentPane.add(enterPWagain);

		// Create a new account
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// If the two password fields don't match or one of them is a
				// blank space, display an password error message dialog box.
				if((!(enterPW.getText().equals(enterPWagain.getText()))) || enterPW.getText().equals("")
						|| enterPWagain.getText().equals("")) {
					WelcomePopUp.infoBox("Passwords do not match!", "Error");

				}

				// If the username exists, display a username error message
				// dialog box. Case-sensitive is enforced for SQL
				else if (checkIfExists(textField.getText().toLowerCase())) {
					WelcomePopUp.infoBox("User already exists!", "Error");

				}

				// Else the username and password fit the requirements and can 
				// be used to make a new account.
				else {
					if(!textField.getText().isEmpty()||!(enterPW.getText().isEmpty())) {
					registerUser(textField.getText(),enterPWagain.getText());
					}
				}
			}
		});
		btnCreate.setBounds(138, 199, 115, 29);
		contentPane.add(btnCreate);
	}

	// Check if a username already exists or not
	public boolean checkIfExists(String username) {

		// All the existing usernames from the database will
		// be put in this ArrayList
		ArrayList<String> users = new ArrayList<String>();

		try {

			// Establish connection
			Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
			myConn.setAutoCommit(false);

			// Make a prepared statement to extract
			// all existing usernames from the database.
			Statement stm;
			stm = myConn.createStatement();
			String sql = "Select username From users";

			
			// Execute the query
			ResultSet rst;   
			rst = stm.executeQuery(sql);
			myConn.commit();

			// Add all the usernames to an ArrayList
			while (rst.next()) {
				String user = rst.getString("username");
				users.add(user);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// If the ArrayList contains the desired username to be created,
		// return true (to stop the insertion of a duplicate username).
		if(users.contains(username)) {
			return true;
		}
		else return false;

	}

	// Make a new account for the user in the online SQL database.
	public void registerUser(String username, String password) {
		try {

	
			// Establish connection, set autocommit to false 
			// to prevent unwanted console messages.
			Connection myConn = DriverManager.getConnection("jdbc:mysql://sql9.freesqldatabase.com:3306/sql9198225","sql9198225","Jd42BXqWMu");
			myConn.setAutoCommit(false);

			// Make a prepared statement with the username and password, insert it
			// into the online database.
			PreparedStatement update = myConn.prepareStatement
					("INSERT INTO users SET username = ?, password = ?");

			update.setString(1, username);
			update.setString(2, password);

			update.executeUpdate();
			myConn.commit();

			// Display a welcome message for the new user, dispose this
			// frame and send the user back to the ImageStartup main screen.
			WelcomePopUp.infoBox("Welcome " + username + "!", "Success");
			ImageStartup i = new ImageStartup();
			contentPane.setVisible(false);
			dispose();
			i.main(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Welcome message for the new user.
	public static class WelcomePopUp
	{
		public static void infoBox(String infoMessage, String titleBar)
		{
			JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
		}
	}
}