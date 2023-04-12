public class RouteParameter {
    private String lineID;
    private int timeFrom;
    private int timeTo;
    private int technologicalTime;
    private int equalizingTime;
    private String compatibleTrip;

    public RouteParameter(String lineID, int timeFrom, int timeTo, int technologicalTime, int equalizingTime) {
        this.lineID = lineID;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.technologicalTime = technologicalTime;
        this.equalizingTime = equalizingTime;

        switch (lineID){
            case "DA88": this.compatibleTrip = "Vá.1->Vá.2"; break;
            case "IN16": this.compatibleTrip = "Vá.2->Vá.1"; break;
            default: this.compatibleTrip = "none"; break;
        }
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getCompatibleTripType() {
        return compatibleTrip;
    }

    public void setCompatibleTrip(String compatibleTrip) {
        this.compatibleTrip = compatibleTrip;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public int getTechnologicalTime() {
        return technologicalTime;
    }

    public void setTechnologicalTime(int technologicalTime) {
        this.technologicalTime = technologicalTime;
    }

    public int getEqualizingTime() {
        return equalizingTime;
    }

    public void setEqualizingTime(int equalizingTime) {
        this.equalizingTime = equalizingTime;
    }

    public int getAdditionalTime() {
        return technologicalTime + equalizingTime;
    }

    @Override
    public String toString() {
        return "[" +
                "lineID=" + lineID +
                ", from=" + timeFrom +
                ", to=" + timeTo +
                ", tech=" + technologicalTime +
                ", eq=" + equalizingTime +
                ']';
    }
}
