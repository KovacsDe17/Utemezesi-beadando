import java.util.*;

public class Trip {
    private static int count = 0;

    private int departureTime;
    private int arriveTime;
    private int additionalTime;
    private StationPair stations;
    private final int id;

    public Trip(int departureTime, int arriveTime, int additionalTime,  StationPair stations) {
        this.departureTime = departureTime;
        this.arriveTime = arriveTime;
        this.additionalTime = additionalTime;
        this.stations = stations;
        this.id = count;
        count++;
    }

    @Override
    public String toString() {
        return "Trip_"+id;
    }

    public String toStringExtended(){
        return "Trip_" + id +
                ":{dt=" + departureTime +
                ", at=" + arriveTime +
                ", tt=" + stations +
                ", add=" + additionalTime +
                '}';
    }

    /***
     * Compatibility: If the current trip arrives at the starting station of the next one before the next one departs (including technological and equalizing times).
     * @param nextTrip The trip which comes after the current one.
     * @return True if the trips are compatible, false otherwise.
     */
    public boolean compatible(Trip nextTrip) {
        if (this.equals(nextTrip)) return false;
        return arriveTime + additionalTime <= nextTrip.departureTime &&
                stations.getEndStation().equals(nextTrip.stations.getStartStation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return arriveTime == trip.arriveTime &&
                departureTime == trip.departureTime &&
                Objects.equals(stations, trip.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arriveTime, departureTime, stations);
    }
}
