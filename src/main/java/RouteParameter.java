public class RouteParameter {
    private final Terminals terminals;
    private final int timeFrom;
    private final int timeTo;
    private final int technologicalTime;
    private final int equalizingTime;

    public RouteParameter(Terminals terminals, int timeFrom, int timeTo, int technologicalTime, int equalizingTime) {
        this.terminals = terminals;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.technologicalTime = technologicalTime;
        this.equalizingTime = equalizingTime;
    }

    public Terminals getTerminals() {
        return terminals;
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
                "terminals=" + terminals +
                ", from=" + timeFrom +
                ", to=" + timeTo +
                ", tech=" + technologicalTime +
                ", eq=" + equalizingTime +
                ']';
    }
}
