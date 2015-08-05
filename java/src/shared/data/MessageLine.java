package shared.data;

import java.io.Serializable;

public class MessageLine implements Serializable {

    private String message;
    private String source;

    public MessageLine() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "{" + "\"source\" : \"" + source + "\", \"message\" : \"" + message + "\"}";
    }

}
