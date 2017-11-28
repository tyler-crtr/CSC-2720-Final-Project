package cs;

/**
 *This class will be used to store a username and password
 *of one user to pass between multiple frames
 *for the purpose of this program
 */

public class ImageUser {
	
	// Create variables
	public String username,password;
	
	// Define the constructor
	public ImageUser(String u,String p) {
		this.username = u;
		this.password = p;
	}
	
	// Change password
	public void changeUsername(String u) {
		this.username = u;
	}
	
	// Change username
	public void changePassword(String p) {
		this.password = p;
	}
	
	// Return username
	public String getUsername() {
		return username;
	}
	
	// Return password
	public String getPassword() {
		return password;
	}
	
	// Equals method
	public boolean equals(Object o) {
		ImageUser i = (ImageUser) o;
		return this.getUsername().equals(i.getUsername()) && this.getPassword().equals(i.getPassword());
	}
	
	// toString method
	public String toString() {
		return "Username: " + this.username + " Password: " + this.password + "/n";
	}
}