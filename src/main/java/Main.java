public class Main {
    public static void main(String[] args) {
        var data = ExcelHandler.getDataFrom(ExcelHandler.openWorkbook(),1);
        System.out.println("Size: " + data.size());

        var graph = TripHandler.tripsToGraph(TripHandler.dataToTrips(data));
        System.out.println("Edge count: " + graph.edgeSet().size());
        GraphVisuals.OpenInJFrame(graph);
    }
}
