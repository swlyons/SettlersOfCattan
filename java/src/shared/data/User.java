package shared.data;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private int id;
    private int gameid;

    /**
     *
     * @param username
     * @param password
     * @pre
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "User [name = " + username + ", password = *******]";
    }

    @Override
    public int hashCode() {
        return (username + password).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean condition = false;
        if (obj == null) {
            return condition;
        }
        if (getClass() != obj.getClass()) {
            return condition;
        }
        User other = (User) obj;
        if (username.equals(other.getUsername())) {
            condition = true;
        }
        return condition;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameid;
    }

    public void setGameId(int gameid) {
        this.gameid = gameid;
    }
}
