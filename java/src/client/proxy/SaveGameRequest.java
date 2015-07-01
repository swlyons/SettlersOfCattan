package client.proxy;

/**
 * Contains all the requisite info for saving a game to a file.
 * @author Aaron
 *
 */
public class SaveGameRequest {
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
