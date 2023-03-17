import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class GraphHandler {

    private static final String timestampPattern = "yyyyMMdd_HHmmss";

    public static void SaveAsImage(Graph graph){
        //Setup graph adapter for visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graph);

        setupGraphStyle(graphAdapter);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);

        //Create new file and write into it
        String filename = ".\\src\\main\\resources\\graph_exports\\graph_" + getTimestamp() + ".png";
        File imgFile = new File(filename);

        try{
            imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void Visualize(Graph graph){
        //Setup graph adapter for visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graph);

        setupGraphStyle(graphAdapter);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        Border padding = BorderFactory.createEmptyBorder(10,10,10,10);
        graphComponent.setBorder(padding);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);

        JFrame frame = new JFrame("VSP Graph Visuals");

        frame.add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static String getTimestamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampPattern);
        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.format(formatter);
    }

    private static void setupGraphStyle(JGraphXAdapter adapter){
        //Setup edge style
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");

        //Setup vertex style
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
    }
}
