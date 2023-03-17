import java.util.Objects;

public class Trip {
    private static int count = 0;

    private int arriveTime;
    private int departureTime;
    private String tripType;
    private int id;

    public Trip(int departureTime, int arriveTime,  String tripType) {
        this.departureTime = departureTime;
        this.arriveTime = arriveTime;
        this.tripType = tripType;
        this.id = count;
        count++;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    @Override
    public String toString() {
        return "Trip_"+id;
    }

    public String toStringExtended(){
        return "Trip{" +
                "dt=" + departureTime +
                ", at=" + arriveTime +
                ", type='" + tripType + '\'' +
                '}';

    }

    public boolean compatible(Trip trip) {
        if (this == trip) return false;
        return arriveTime <= trip.departureTime &&
                Objects.equals(tripType, trip.tripType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return arriveTime == trip.arriveTime &&
                departureTime == trip.departureTime &&
                Objects.equals(tripType, trip.tripType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arriveTime, departureTime, tripType);
    }
}
