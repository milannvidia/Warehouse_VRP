import java.util.Comparator;

public class SortByDistance implements Comparator<Location> {
    private final int X;
    private final int Y;

    public SortByDistance(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    @Override
    public int compare(Location pu1, Location pu2) {
        if(pu1==null) return -1;
        if(pu2==null) return 1;
        int distance1 = Math.abs(pu1.X - this.X);
        int distance2 = Math.abs(pu2.X - this.X);

        if (pu1.Y == Y) {
            if (pu2.Y == Y) {
                return Integer.signum(distance1 - distance2);
            } else {
                return 1;
            }
        } else {
            if (pu2.Y == Y) {
                return -1;
            } else {
                return Integer.signum(pu1.X - pu2.X);
            }
        }

    }

}
