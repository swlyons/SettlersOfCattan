package client.data;

public class HexLocation {

    private int x;
    private int y;

    public HexLocation(int xNew, int yNew) {
        x = xNew;
        y = yNew;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int xNew) {
        x = xNew;
    }

    public void setY(int yNew) {
        y = yNew;
    }

    /**
     * 
     * @param comparer the HexLoaction to compare with
     * @pre comparer is not null
     * @post returns true if the x and y values are equal with this HexLocation's. false otherwise
     */
    public boolean equivalent(HexLocation comparer) {
        if (comparer.getX() == x && comparer.getY() == y) {
            return true;
        }
        return false;
    }

}
