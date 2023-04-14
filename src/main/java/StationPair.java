import java.util.Objects;

public class StationPair {
    private String startStation;
    private String endStation;

    public StationPair(String startStation, String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    @Override
    public String toString() {
        return "[" +
                "s='" + startStation + '\'' +
                ", e='" + endStation + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPair that = (StationPair) o;
        return Objects.equals(startStation, that.startStation) &&
                Objects.equals(endStation, that.endStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStation, endStation);
    }
}
