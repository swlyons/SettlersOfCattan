package client.proxy;

/**
 * Holds a username and password for logins and registration
 * @author Aaron
 *
 */
public class CredentialRequest {
	private String username;
	private String password;
	
	public CredentialRequest(String user, String pass) {
		setUsername(user);
		setPassword(pass);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
