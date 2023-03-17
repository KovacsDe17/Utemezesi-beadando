import com.mxgraph.layout.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraphSelectionModel;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GraphVisuals {

    private static final String TIMESTAMP_PATTERN = "yyyyMMdd_HHmmss";
    private static final String SAVE_FOLDER = ".\\src\\main\\resources\\graph_exports";
    private static JLabel label = new JLabel("Selected: -");

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
        String filename = SAVE_FOLDER + "\\graph_" + getTimestamp() + ".png";
        File imgFile = new File(filename);

        try{
            imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);

        }catch (IOException e){
            System.err.println(e);
        }
    }

    public static void OpenInJFrame(Graph graph){
        //Setup graph adapter for visualization
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graph);

        setupGraphStyle(graphAdapter);
        setupGraphEvents(graphAdapter);

        mxCircleLayout circleLayout = new mxCircleLayout(graphAdapter);
        mxIGraphLayout otherLayout = new mxStackLayout(graphAdapter);
        circleLayout.setRadius(1);
        ((mxIGraphLayout) circleLayout).execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        Border padding = BorderFactory.createEmptyBorder(10,10,10,10);
        graphComponent.setBorder(padding);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        label.setVisible(true);

        JFrame frame = new JFrame("VSP Graph Visuals");

        panel.add(label);
        panel.add(graphComponent);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static String getTimestamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.format(formatter);
    }

    private static void setupGraphStyle(JGraphXAdapter adapter){
        //Setup edge style
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_STROKECOLOR, "#d7d7d7");

        //Setup vertex style
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
    }

    private static void setupGraphEvents(JGraphXAdapter adapter){
        adapter.getSelectionModel().addListener(mxEvent.CHANGE, (sender, evt) -> {
            mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
            mxCell cell = (mxCell) sm.getCell();

            //TODO: Set proper info (at_i, dt_i, at_j, dt_j, etc.) using Trip class
            if (cell != null && cell.isVertex()) {
                //System.out.println(cell.getValue() + " has been touched");
                label.setText("Selected: " + cell.getValue());
            }else if(cell != null && cell.isEdge()){
                //System.out.println(cell.getValue() + " has been touched");
                label.setText("Selected: " + cell.getValue());
            }
            else if(cell == null){
                System.out.println("Deselection happened");
                label.setText("Selected: -");
            }
        });
    }
}