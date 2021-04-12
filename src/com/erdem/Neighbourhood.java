package com.erdem;

public class Neighbourhood {

    public Pair Coordinates;
    public int EffectedCount;
    public boolean IsTop = false;

    public static class Pair{
        public int X;
        public int Y;
        
        public Pair(int x, int y){
            X = x;
            Y = y;
        } 
    }

    public Neighbourhood(){}

    @Override
    public String toString() {
        return String.format(Coordinates.X + "-" + Coordinates.Y +
         "-count" + EffectedCount + "- isTop -" + IsTop);
    }
}
