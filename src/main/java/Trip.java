import java.util.Objects;

public class Trip {
    private int arriveTime;
    private int departureTime;
    private String tripType;

    public Trip(int arriveTime, int departureTime, String tripType) {
        this.arriveTime = arriveTime;
        this.departureTime = departureTime;
        this.tripType = tripType;
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
        return "Trip{" +
                "at = " + arriveTime +
                ", dt = " + departureTime +
                ", type = '" + tripType + '\'' +
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
