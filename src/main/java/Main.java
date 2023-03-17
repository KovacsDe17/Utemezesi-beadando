import org.apache.poi.ss.usermodel.Workbook;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class Main {
    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        String v1 = "Vertex 01";
        String v2 = "Vertex 02";
        String v3 = "Vertex 03";
        String v4 = "Vertex 04";

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(v1,v2);
        graph.addEdge(v1,v4);
        graph.addEdge(v2,v3);
        graph.addEdge(v3,v4);

        System.out.println(graph.toString());

        //GraphHandler.Visualize(graph);

        String fileLocation = ".\\src\\main\\resources\\input\\Input_VSP.xlsx";
        Workbook workbook = ExcelHandler.openWorkbook(fileLocation);
        var data = ExcelHandler.getDataFrom(workbook,1);

        System.out.println(data.toString());
    }
}
