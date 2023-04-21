import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;

/**
 * This class is responsible for the graph logic, such as manipulating nodes and edges
 */
public class GraphModel {
    private static Graph<ConnectionBasedNode, DefaultEdge> connectionBasedGraph;

    /**
     * Loads data from the Excel input file and builds a Connection Based VSP Graph based on it
     * @return A graph based on the Excel input file
     */
    public static org.jgrapht.Graph<ConnectionBasedNode, DefaultEdge> BuildConnectionBasedGraph(){
        //Load line list
        List<Line> lines = Line.getListFromExcel();

        Graph<ConnectionBasedNode, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        //Parse list to graph...

        // -- Rules of making the graph -- //
        /*
         * Nodes:
         * Add +2 depot nodes
         * Add all G1 and G2 from lines list
         * Set edges described below
         *
         * Edges:
         * From DepotStart --> All G1
         * From All G2 --> DepotEnd
         * All G1-G2 pair
         * All G2-G1 pair where they are compatible
         */

        //Add depot nodes (D1 and D2 are the same, hence the set id's)
        ConnectionBasedNode D1 = new ConnectionBasedNode("D", "D1");
        ConnectionBasedNode D2 = new ConnectionBasedNode("D'", "D2");

        graph.addVertex(D1);
        graph.addVertex(D2);

        for(Line line : lines){
            //Add G nodes
            ConnectionBasedNode nodeG1 = new ConnectionBasedNode(line.getId() + "","G1");
            ConnectionBasedNode nodeG2 = new ConnectionBasedNode(line.getId() + "'","G2");

            graph.addVertex(nodeG1);
            graph.addVertex(nodeG2);

            //Set main edges (depot->G1, G1->G2, G2->depot)
            graph.addEdge(D1,nodeG1);
            graph.addEdge(nodeG2,D2);
            graph.addEdge(nodeG1,nodeG2);

            for(Line nextLine : lines){
                if(line.compatible(nextLine)){
                    ConnectionBasedNode nextG1 = findNodeWithID(graph, nextLine.getId() + "");
                    if (nextG1 != null)
                        graph.addEdge(nodeG2, nextG1);
                }
            }
        }

        //Return graph
        return graph;
    }

    private static ConnectionBasedNode findNodeWithID(Graph<ConnectionBasedNode, DefaultEdge> graph, String id){
        for(ConnectionBasedNode node : graph.vertexSet()){
            if(node.getId().equals(id)){
                return node;
            }
        }

        return null;
    }

    /*
    public static org.jgrapht.Graph<Line, DefaultEdge> addVerticesFromList(List<Line> lines){
        org.jgrapht.Graph<Line, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        int index = 0;
        for (Line line : lines) {
            //Debug - Select specific parts of the whole list
            if(!((index >= 10 && index <= 80) || index >= 90)) {
                graph.addVertex(line);
            }

            index++;
        }

        return graph;
    }

    public static void addEdgesCompatible(org.jgrapht.Graph<Line, DefaultEdge> graph){
        //Set edge between them if they are compatible
        for (Line line_i : graph.vertexSet()) {
            for (Line line_j : graph.vertexSet()) {
                if(line_i.compatible(line_j))
                    graph.addEdge(line_i, line_j);
            }
        }

    }

    public static org.jgrapht.Graph<Line, DefaultEdge> createTest(){
        org.jgrapht.Graph<Line, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        Terminals type1 = new Terminals("A","B");
        Terminals type2 = new Terminals("B","A");

        Line line0 = new Line(1,4,0,type1);
        Line line1 = new Line(1,2,0,type1);
        Line line2 = new Line(3,5,0,type1);
        Line line3 = new Line(2,4,0,type1);
        Line line4 = new Line(1,3,0,type1);
        Line line5 = new Line(1,2,0,type2);
        Line line6 = new Line(1,3,0,type2);
        Line line7 = new Line(2,3,0,type2);
        Line line8 = new Line(4,5,0,type2);
        Line line9 = new Line(3,5,0,type2);

        graph.addVertex(line0);
        graph.addVertex(line1);
        graph.addVertex(line2);
        graph.addVertex(line3);
        graph.addVertex(line4);
        graph.addVertex(line5);
        graph.addVertex(line6);
        graph.addVertex(line7);
        graph.addVertex(line8);
        graph.addVertex(line9);

        graph.addEdge(line0, line1);
        graph.addEdge(line2, line3);
        graph.addEdge(line0, line4);
        graph.addEdge(line1, line3);
        graph.addEdge(line0, line1);
        graph.addEdge(line5, line6);
        graph.addEdge(line6, line8);
        graph.addEdge(line6, line9);
        graph.addEdge(line7, line8);

        return graph;
    }

     */ //Old code
}
