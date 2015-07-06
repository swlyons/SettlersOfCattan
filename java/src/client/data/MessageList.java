package client.data;

import java.util.ArrayList;

public class MessageList {
	
	private ArrayList<MessageLine> lines;
	
	public MessageList(){
		
	}
	
	public ArrayList<MessageLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<MessageLine> lines) {
		this.lines = lines;
	}

    @Override
    public String toString() {
        
        String messageLines = "{lines : [";
        
        for(MessageLine line : lines){
            messageLines += line.toString() + ",";
        }
        
        messageLines += "]}";
        
        return messageLines;
    }
        
        
}
