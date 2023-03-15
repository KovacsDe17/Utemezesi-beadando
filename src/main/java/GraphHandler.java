import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

import javax.imageio.ImageIO;

public class GraphHandler {

    private static final String timestampPattern = "yyyyMMdd_HHmmss";

    public static void Visualize(Graph graph){
        //Setup graph adapter for visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);

        //Create new file and write into it
        String filename = ".\\src\\main\\resources\\graph_" + getTimestamp() + ".png";
        File imgFile = new File(filename);

        try{
            imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static String getTimestamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampPattern);
        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.format(formatter);
    }
}
