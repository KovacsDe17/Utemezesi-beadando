public class Main {
    public static void main(String[] args) {
        var routeParameters = ExcelHandler.getDataFrom(ExcelHandler.openWorkbook(),0);


        var busLines = ExcelHandler.getDataFrom(ExcelHandler.openWorkbook(),1);
        System.out.println("Size: " + busLines.size());
        var trips = TripHandler.getTrips(busLines, RouteHandler.getRouteParameters(routeParameters));


        var graph = TripHandler.tripsToGraph(trips);
        System.out.println("Edge count: " + graph.edgeSet().size());
        GraphVisuals.OpenInJFrame(graph);


        //TripHandler.dataToParameters(parameters);
    }
}