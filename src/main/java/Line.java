import java.util.*;

public class Line {
    private static int count = 0;

    private final int departureTime;
    private final int arriveTime;
    private final int additionalTime;
    private final Terminals terminals;
    private final int id;

    public Line(int departureTime, int arriveTime, int additionalTime, Terminals terminals) {
        this.departureTime = departureTime;
        this.arriveTime = arriveTime;
        this.additionalTime = additionalTime;
        this.terminals = terminals;
        this.id = count;
        count++;
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
     * Compatibility: If the current trip arrives at the starting station of the next one before the next one departs (including technological and equalizing times).
     * @param nextLine The trip which comes after the current one.
     * @return True if the trips are compatible, false otherwise.
     */
    public boolean compatible(Line nextLine) {
        if (this.equals(nextLine)) return false;
        return arriveTime + additionalTime <= nextLine.departureTime &&
                terminals.getEnd().equals(nextLine.terminals.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return arriveTime == line.arriveTime &&
                departureTime == line.departureTime &&
                Objects.equals(terminals, line.terminals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arriveTime, departureTime, terminals);
    }

    public static List<Line> toList(Map<Integer, List<String>> busLinesData, List<RouteParameter> routeParameters){
        List<Line> lines = new ArrayList<>();

        //Remove header
        busLinesData.remove(0);
        for (Map.Entry<Integer, List<String>> row:
                busLinesData.entrySet()) {

            int departureTime = (int) Float.parseFloat(row.getValue().get(0));
            int arriveTime = (int) Float.parseFloat(row.getValue().get(1));
            String tripType = row.getValue().get(2);

            Terminals terminals = Terminals.FindPair(tripType);
            RouteParameter routeParameter = Route.findCompatible(routeParameters, terminals, arriveTime);

            Line line = new Line(
                    departureTime,
                    arriveTime,
                    routeParameter.getAdditionalTime(),
                    terminals
            );

            lines.add(line);
        }

        return lines;
    }
}
