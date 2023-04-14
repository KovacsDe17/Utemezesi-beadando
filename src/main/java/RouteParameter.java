public class RouteParameter {
    private StationPair stationPair;
    private int timeFrom;
    private int timeTo;
    private int technologicalTime;
    private int equalizingTime;

    public RouteParameter(StationPair stationPair, int timeFrom, int timeTo, int technologicalTime, int equalizingTime) {
        this.stationPair = stationPair;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.technologicalTime = technologicalTime;
        this.equalizingTime = equalizingTime;
    }

    public StationPair getStationPair() {
        return stationPair;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public int getAdditionalTime() {
        return technologicalTime + equalizingTime;
    }

    @Override
    public String toString() {
        return "[" +
                "stationPair=" + stationPair +
                ", from=" + timeFrom +
                ", to=" + timeTo +
                ", tech=" + technologicalTime +
                ", eq=" + equalizingTime +
                ']';
    }
}
