import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

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


        //Setup graph adapter for visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);

        //Create new file and write into it
        File imgFile = new File(".\\src\\main\\resources\\graph.png");
        try{
            imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);
        }catch (IOException e){
            System.err.println(e);
        }
    }
}
