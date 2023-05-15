package sym;


import java.util.ArrayList;

public class Frame {

    boolean isFinished;
    int channel;
    int hops;
    ArrayList<Pair> Path = new ArrayList<>();

    Frame(int ch)
    {
        this.isFinished = false;
        this.hops = 0;
        this.channel = ch;
    }

}
