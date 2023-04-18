import org.apache.poi.ss.usermodel.Workbook;

public class Main {
    public static void main(String[] args) {
        var excel = Excel.getInstance();
        Workbook workbook = excel.openWorkbook();
        var routeParameters = excel.getDataFrom(workbook,0);
        var busLines = excel.getDataFrom(workbook,1);

        var trips = Line.toList(busLines, Route.toList(routeParameters));

        var graph = Graph.createFromList(trips);
        System.out.println("Vertex count: " + graph.vertexSet().size());
        System.out.println("Edge count: " + graph.edgeSet().size());

        Visuals.setGraph(graph);
        Visuals.MainMenu();
    }
}