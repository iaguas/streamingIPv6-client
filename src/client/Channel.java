package client;

/**
 *
 * @author inigo
 */
public class Channel {
    private final int numChannel;
    private final String title;
    
    public Channel(int num, String title){
        numChannel = num;
        this.title = title;
    }
    
    public int getNumChannel(){
        return numChannel;
    }
    
    public String getTitle(){
        return title;
    }
}
