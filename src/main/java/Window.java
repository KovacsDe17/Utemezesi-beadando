import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * This class is responsible for app windows
 */
public class Window {
    private static Window instance;
    private static final String APP_NAME = "VSP Graph Visuals";
    private boolean helperHidden = false;

    public static Window getInstance() {
        if(instance == null)
            instance = new Window();

        return instance;
    }

    /*
    private static final String TIMESTAMP_PATTERN = "yyyyMMdd_HHmmss";
    private static final String SAVE_FOLDER = ".\\src\\main\\resources\\graph_exports";
    private final static JLabel label = new JLabel("Selected: None (Click on an edge or vertex to show info)");
    private static Graph<Line,DefaultEdge> _graph;

    public static void setGraph(Graph<Line, DefaultEdge> graphSet){
        _graph = graphSet;
    }

    public static boolean SaveAsImage(JGraphXAdapter<Line, DefaultEdge> graphAdapter){
        boolean isSuccessful;

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 1, Color.WHITE, true, null);

        //Create new file and write into it
        String filename = SAVE_FOLDER + "\\graph_" + getTimestamp() + ".png";
        File imgFile = new File(filename);

        try{
            boolean success = imgFile.createNewFile();
            ImageIO.write(image, "PNG", imgFile);
            isSuccessful = true;
        }catch (IOException e){
            System.err.println(e.getMessage());
            isSuccessful = false;
        }

        return isSuccessful;
    }

    public static void ConnectionBasedGraphView(){
        //Setup graph adapter for visualization
        JGraphXAdapter<Line, DefaultEdge> graphAdapter =
                new JGraphXAdapter<>(_graph);


        setupGraphEvents(graphAdapter);
        mxGraphComponent graphComponent = getConnectionBasedGraph(graphAdapter);
        graphComponent.removeMouseWheelListener(graphComponent.getMouseWheelListeners()[0]);

        addSwimlane(graphAdapter);

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
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setupGraphStyle(graphAdapter);
    }

    public static String getTimestamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.format(formatter);
    }

    private static mxGraphComponent getConnectionBasedGraph(JGraphXAdapter<Line, DefaultEdge> graphAdapter){
        mxStackLayout stackLayout = new mxStackLayout(graphAdapter,true, 20,0,0,0);
        stackLayout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        Border padding = BorderFactory.createEmptyBorder(10,10,10,10);
        graphComponent.setBorder(padding);
        graphComponent.setConnectable(false);
        graphComponent.getGraph().setAllowDanglingEdges(false);
        graphComponent.getGraph().setCellsEditable(false);

        return graphComponent;
    }

    private static void addSwimlane(JGraphXAdapter<Line, DefaultEdge> adapter){
        var graph = adapter.getView().getGraph();

        var lane1 = graph.insertVertex(graph.getDefaultParent(), null, "Lane 1", 0, 0, 1000, 100, "swimlane");
        var lane2 = graph.insertVertex(graph.getDefaultParent(), null, "Lane 2", 0, 100, 1000, 100, "swimlane");
        // use as parent...
        var v1 = graph.insertVertex(lane1, null, 'A', 0, 0, 30, 30);


    }

    private static void setupGraphStyle(JGraphXAdapter<Line, DefaultEdge> adapter){
        //Setup edge style
        Map<String, Object> edgeStyle = new HashMap<>();
        edgeStyle.put(mxConstants.STYLE_NOLABEL, "1");
        edgeStyle.put(mxConstants.STYLE_DASHED, "1");
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        adapter.getStylesheet().putCellStyle("edgeStyle", edgeStyle);

        //Setup vertex style
        Map<String, Object> vertexStyle = new HashMap<>();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#3abff1");
        vertexStyle.put(mxConstants.STYLE_STROKEWIDTH, "1.5f");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        adapter.getStylesheet().putCellStyle("vertexStyle", vertexStyle);


        //Set vertex size
        int vertexSize = 35;
        for (Object cell :
                adapter.getChildCells(adapter.getDefaultParent(), true, false)) {

            if(((mxCell) cell).isVertex()){
                mxCell vertex = (mxCell) cell;

                vertex.getGeometry().setHeight(vertexSize);
                vertex.getGeometry().setWidth(vertexSize);
                vertex.setStyle("vertexStyle");

                System.out.println(vertex.getValue().toString() + " is a vertex");
            }else if(((mxCell) cell).isEdge()){
                mxCell edge = (mxCell) cell;

                System.out.println(edge.getValue().toString() + " is an edge");
                edge.setStyle("edgeStyle");
            }

        }
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

 */ //Old code

    public void OpenMainMenu(){
        System.setProperty("sun.java2d.uiScale", "1.0"); //Must leave because of ui shifting...
        JFrame frame = new JFrame(APP_NAME);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(MenuLabel());
        panel.add(Separator(10));
        panel.add(Button("Connection Based Graph", e -> {
            OpenGraphView(
                    "Connection Based Graph",
                    GraphView.BuildConnectionBasedView(GraphModel.BuildConnectionBasedGraph())
            );
            frame.dispose();
        }));
        panel.add(Separator(10));
        panel.add(Button("Time-Space Graph", e -> {
            OpenGraphView(
                    "Time-Space Graph",
                    GraphView.BuildTimeSpacedView(GraphModel.BuildTimeSpaceGraph())
            );
            frame.dispose();
        }));
        panel.add(Separator(10));
        panel.add(Button("Build IP model", e -> {
            OpenIPModel();
            frame.dispose();
        }));        panel.add(Separator(10));
        panel.add(Button("Exit", e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }));

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,225));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void OpenGraphView(String graphType, Component graphView){
        JFrame frame = new JFrame(APP_NAME + " - " + graphType);
        JPanel mainPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        //Component graphView = ;

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(BackButton(frame));
        buttonPanel.add(Button("Toggle depots", e -> {
            GraphView.ToggleDepots();
        }));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(buttonPanel);
        mainPanel.add(graphView);

        frame.add(mainPanel);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void OpenIPModel(){
        JFrame frame = new JFrame(APP_NAME);
        JPanel mainPanel = new JPanel();
        JLabel modelText = new JLabel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(BackButton(frame));
        mainPanel.add(Button("Generate Model", e -> {
            String text = IPBuilder.BuildModel(GraphModel.BuildConnectionBasedGraph(),true);
            modelText.setText(text);
            mainPanel.updateUI();
        }));
        mainPanel.add(Separator(20));
        mainPanel.add(modelText);

        frame.add(mainPanel);
        frame.setMinimumSize(new Dimension(400,400));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static JButton BackButton(JFrame frame){
        JButton button = new JButton("Back to menu");

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            instance.OpenMainMenu();
            frame.dispose();
        });

        return button;
    }

    private static JButton Button(String label, ActionListener listener){
        JButton button = new JButton(label);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);

        return button;
    }

    private static JLabel MenuLabel(){
        JLabel jlabel = new JLabel(APP_NAME);
        jlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlabel.setVisible(true);

        return jlabel;
    }

    private static Component Separator(int height){
        return Box.createRigidArea(new Dimension(0, height));
    }

}
