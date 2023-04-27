import java.util.*;
import java.util.List;

public class Trip {
    private static int count = 1;

    private final int departureTime;
    private final int arriveTime;
    private final int additionalTime;
    private final Terminals terminals;
    private final int id;

    public Trip(int departureTime, int arriveTime, int additionalTime, Terminals terminals) {
        this.departureTime = departureTime;
        this.arriveTime = arriveTime;
        this.additionalTime = additionalTime;
        this.terminals = terminals;
        this.id = count;
        count++;
    }

    private static void ResetCounter(){
        count = 1;
    }

    public int getId() {
        return id;
    }

    public Terminals getTerminals() {
        return terminals;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public int getAdditionalTime() {
        return additionalTime;
    }

    @Override
    public String toString() {
        return "L_"+id;
    }

    public String toStringExtended(){
        return "L_" + id +
                ":{dt=" + departureTime +
                ", at=" + arriveTime +
                ", tt=" + terminals +
                ", add=" + additionalTime +
                '}';
    }

    /***
     * Compatible: When the current trip arrives at the starting station of the next one before the next one departs (including technological and equalizing times).
     * @param nextTrip The trip which comes after the current one.
     * @return True if the trips are compatible, false otherwise.
     */
    public boolean compatible(Trip nextTrip) {
        if (this.equals(nextTrip)) return false;
        return arriveTime + additionalTime <= nextTrip.departureTime &&
                terminals.getEnd().equals(nextTrip.terminals.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return arriveTime == trip.arriveTime &&
                departureTime == trip.departureTime &&
                Objects.equals(terminals, trip.terminals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arriveTime, departureTime, terminals);
    }

    public static List<Trip> getListFromExcel(){
        Excel excel = Excel.getInstance();
        Map<Integer, List<String>> lineDataAsMap = excel.getDataFrom(excel.defaultWorkbook(), 1);
        List<RouteParameter> routeParameters = Route.getListFromExcel();

        //Remove unnecessary header
        lineDataAsMap.remove(0);

        //Build line list
        List<Trip> trips = new ArrayList<>();

        ResetCounter();

        for (Map.Entry<Integer, List<String>> row:
                lineDataAsMap.entrySet()) {

            int departureTime = (int) Float.parseFloat(row.getValue().get(0));
            int arriveTime = (int) Float.parseFloat(row.getValue().get(1));
            String tripType = row.getValue().get(2);

            Terminals terminals = Terminals.FindPair(tripType);
            RouteParameter routeParameter = Route.findCompatible(routeParameters, terminals, arriveTime);

            Trip trip = new Trip(
                    departureTime,
                    arriveTime,
                    routeParameter.getAdditionalTime(),
                    terminals
            );

            trips.add(trip);
        }

        return trips;
    }
}
