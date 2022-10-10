

public enum Player {

    XPlayer, OPlayer;

    public Player next(){
        if (this == XPlayer)
            return OPlayer;
        else
            return XPlayer;
    }
}
