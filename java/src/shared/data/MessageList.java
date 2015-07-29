package shared.data;

import java.util.ArrayList;

public class MessageList {

    private ArrayList<MessageLine> lines;

    public MessageList() {
        lines = new ArrayList<MessageLine>();
    }

    public ArrayList<MessageLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<MessageLine> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {

        boolean hasOne = false;
        String messageLines = "{\"lines\" : [";

        for (MessageLine line : lines) {
            messageLines += line.toString() + ",";
            hasOne = true;
        }

        if (hasOne) {
            messageLines = messageLines.substring(0, messageLines.length() - 1);
        }
        
        messageLines += "]}";

        return messageLines;
    }

}
