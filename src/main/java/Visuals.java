import com.mxgraph.layout.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraphSelectionModel;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Visuals {

    private static final String TIMESTAMP_PATTERN = "yyyyMMdd_HHmmss";
    private static final String SAVE_FOLDER = ".\\src\\main\\resources\\graph_exports";
    private final static JLabel label = new JLabel("Selected: None (Click on an edge or vertex to show info)");
    private static Graph<Line,DefaultEdge> _graph;

    public static void setGraph(Graph<Line, DefaultEdge> graphSet){
        _graph = graphSet;
    }

    public static void SaveAsImage(){
        //Setup graph adapter for visualization
        JGraphXAdapter<Line, DefaultEdge> graphAdapter =
                new JGraphXAdapter<>(_graph);

        setupGraphStyle(graphAdapter);

        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);

        //Create new file and write into it
        String filename = SAVE_FOLDER + "\\graph_" + getTimestamp() + ".png";
        File imgFile = new File(filename);

        try{
            boolean success = imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public static void MainMenu(){
        JFrame frame = new JFrame("VSP Graph Visuals");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel jlabel = new JLabel("VSP Graph Visuals");
        jlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(jlabel);
        jlabel.setVisible(true);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton connenctionBased = new JButton("Connenction based graph");
        connenctionBased.setAlignmentX(Component.CENTER_ALIGNMENT);
        connenctionBased.addActionListener(e -> {
            ConnectionBasedGraphView();
            frame.dispose();
        });
        panel.add(connenctionBased);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton timeSpace = new JButton("Time-space graph");
        timeSpace.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeSpace.addActionListener(e -> System.out.println("Time-space pressed"));
        panel.add(timeSpace);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton buildIP = new JButton("Build IP model");
        buildIP.setAlignmentX(Component.CENTER_ALIGNMENT);
        buildIP.addActionListener(e -> System.out.println("BuildIP pressed"));
        panel.add(buildIP);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton exit = new JButton("Exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        panel.add(exit);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void ConnectionBasedGraphView(){
        //Setup graph adapter for visualization
        JGraphXAdapter<Line, DefaultEdge> graphAdapter =
                new JGraphXAdapter<>(_graph);

        setupGraphStyle(graphAdapter);
        setupGraphEvents(graphAdapter);
        mxGraphComponent graphComponent = getConnectionBasedGraph(graphAdapter);

        JFrame frame = new JFrame("VSP Graph Visuals");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        label.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(graphComponent);

        JButton toMenu = new JButton("Back to Menu");
        toMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        toMenu.addActionListener(e -> {
            MainMenu();
            frame.dispose();
        });


        panel.add(toMenu);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);


        panel.add(toMenu);
        panel.add(label);
        panel.add(scrollPane);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static String getTimestamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.format(formatter);
    }

    private static mxGraphComponent getConnectionBasedGraph(JGraphXAdapter<Line, DefaultEdge> graphAdapter){
        mxStackLayout stackLayout = new mxStackLayout(graphAdapter,false, 20,100,50,0);
        stackLayout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        Border padding = BorderFactory.createEmptyBorder(10,10,10,10);
        graphComponent.setBorder(padding);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        graphComponent.getGraph().setCellsEditable(false);



        return graphComponent;
    }

    private static void setupGraphStyle(JGraphXAdapter<Line, DefaultEdge> adapter){
        //Setup edge style
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");
        adapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_STROKECOLOR, "#d7d7d7");

        //Setup vertex style
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_LABEL);
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, "#3abff1");
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_ROUNDED, "1");
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_STROKEWIDTH, "2");
        adapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_FONTCOLOR, "#000000");
    }

    private static void setupGraphEvents(JGraphXAdapter<Line, DefaultEdge> adapter){
        adapter.getSelectionModel().addListener(mxEvent.CHANGE, (sender, evt) -> {
            mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
            mxCell cell = (mxCell) sm.getCell();

            if (cell != null && cell.isVertex()) {
                //System.out.println(cell.getValue() + " has been touched");
                Line line = (Line) cell.getValue();
                label.setText("Selected: " + line.toStringExtended());
            }else if(cell != null && cell.isEdge()){
                //System.out.println(cell.getValue() + " has been touched");

                Line source = (Line) cell.getSource().getValue();
                Line target = (Line) cell.getTarget().getValue();

                label.setText("Selected: " + source.toStringExtended() + " --> " + target.toStringExtended());
            }
            else if(cell == null){
                //System.out.println("Deselection happened");
                label.setText("Selected: None (Click on an edge or vertex to show info)");
            }
        });
    }
}
