import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.List;

public class Graph {

    public static org.jgrapht.Graph<Line, DefaultEdge> createFromList(List<Line> lines){
        org.jgrapht.Graph<Line, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        int index = 0;
        for (Line line : lines) {
            //Debug - Select specific parts of the whole list
            if(!((index >= 10 && index <= 80) || index >= 90)) {
                graph.addVertex(line);
            }

            index++;
        }

        //Set edge between them if they are compatible
        for (Line line_i : graph.vertexSet()) {
            for (Line line_j : graph.vertexSet()) {
                if(line_i.compatible(line_j))
                    graph.addEdge(line_i, line_j);
            }
        }

        return graph;
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
}
