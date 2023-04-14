public class Main {
    public static void main(String[] args) {
        var routeParameters = Excel.getDataFrom(Excel.openWorkbook(),0);
        var busLines = Excel.getDataFrom(Excel.openWorkbook(),1);

        System.out.println("Size: " + busLines.size());
        var trips = TripHandler.toList(busLines, RouteHandler.toList(routeParameters));

        var graph = TripHandler.createGraph(trips);
        System.out.println("Edge count: " + graph.edgeSet().size());

        Visuals.OpenInJFrame(graph);
    }
}