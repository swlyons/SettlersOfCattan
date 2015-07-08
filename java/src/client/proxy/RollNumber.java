package client.proxy;

/**
 * represents a reqeust to force a particular roll.
 *
 * @author Aaron
 *
 */
public class RollNumber extends Command {

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
