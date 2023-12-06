import java.util.Comparator;

public class SortByDistance implements Comparator<PickUp> {
    private int X;
    private int Y;

    public SortByDistance(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    @Override
    public int compare(PickUp pu1, PickUp pu2) {
        int distance1 = Math.abs(pu1.X - this.X);
        int disance2 = Math.abs(pu2.X - this.X);

        if (pu1.Y == Y) {
            if (pu2.Y == Y) {
                return Integer.signum(distance1 - disance2);
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
