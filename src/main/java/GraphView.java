import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for graph visualization
 */
public class GraphView {
    private static final String STYLESHEET_URL = Main.class.getClassLoader().getResource("stylesheet.css").toExternalForm(); //"url(file://./src/main/resources/stylesheet.css)";
    private static final String CONNECTION_BASED_ID = "ConnectionBased";
    private static final String TIME_SPACE_ID = "TimeSpace";

    private static org.graphstream.graph.Graph currentGraph;
    private static boolean depotshidden = false;

    /*
        MultiGraph graph = new MultiGraph("Bazinga!");

        // Populate the graph
        Node a = graph.addNode("A" );
        Node b = graph.addNode("B" );
        Node c = graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        //Setting position of nodes
        a.setAttribute("xy", 1,1);
        b.setAttribute("xy", 2,1);
        c.setAttribute("xy", 1.5,2);

        //Attaching sprite to node
        SpriteManager sman = new SpriteManager(graph);
        Sprite s = sman.addSprite("S1");
        s.attachToNode("A");
        s.setPosition(1,0,0);

        //Setting styles from stylesheet file
        String stylesheet = "url(file://./src/main/resources/stylesheet.css)";
        graph.setAttribute("ui.stylesheet", stylesheet);

        //Embedded viewer

        Viewer swingViewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        swingViewer.disableAutoLayout();
        View swingView = swingViewer.addDefaultView(false);   // false indicates "no JFrame".
        JFrame frame = new JFrame("Embedded viewer");
        frame.add((Component) swingView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        //Controlling the view, the View API
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.disableAutoLayout();
        View view = viewer.getDefaultView();
        view.getCamera().setViewCenter(0, 0, 0);
        view.getCamera().setViewPercent(0.5);
        view.getCamera().resetView();

        //Retrieving mouse clicks
        viewer.getDefaultView().enableMouseOptions();
        //viewer.getDefaultView().setMouseManager(new MyMouseManager());

        //Open Clicks
        new Clicks();

        */ //Test code for GraphStream

    /**
     * Builds a Connection Based Graphview from the given graph
     * @param graphModel The graph which the view is based on
     * @return The Connection Based Graphview
     */
    public static Component BuildConnectionBasedView(Graph<ConnectionBasedNode, DefaultEdge> graphModel){
        System.setProperty("org.graphstream.ui", "swing");

        SingleGraph graphView = new SingleGraph(CONNECTION_BASED_ID);
        graphView.setAttribute("stylesheet", "url("+ STYLESHEET_URL +")");
        graphView.setAttribute("ui.antialias");


        int[] y = {0,0,0,0};
        int x = 0;
        //For each node in model --> add a node into the view and set attributes, edges
        for (ConnectionBasedNode node : graphModel.vertexSet()){
            Node viewNode = graphView.addNode(node.getId());

            viewNode.setAttribute("ui.label", node.getId());
            viewNode.setAttribute("type", node.getType());
            viewNode.setAttribute("layout.frozen");

            switch (viewNode.getAttribute("type").toString()){
                case "D1": x = 0; break;
                case "G1": x = 1; break;
                case "G2": x = 2; break;
                case "D2": x = 3; break;
            }
            y[x]++;
            viewNode.setAttribute("xy", x*10, y[x]*-1);
        }

        //Center Depot nodes
        Node d1 = graphView.getNode("D");
        Node d2 = graphView.getNode("D'");
        double center = graphView.getNodeCount() / 4.0; //We want the half, but we doubled the nodes --> 2×2
        center += ((center % 2 == 0) ? 0.5 : 0); //If the number of nodes are even, add +0.5
        d1.setAttribute("y", -center);
        d2.setAttribute("y", -center);

        for (DefaultEdge edge : graphModel.edgeSet()) {
            ConnectionBasedNode sourceModel = graphModel.getEdgeSource(edge);
            ConnectionBasedNode targetModel = graphModel.getEdgeTarget(edge);

            Node sourceView = graphView.getNode(sourceModel.getId());
            Node targetView = graphView.getNode(targetModel.getId());

            Edge edgeView = graphView.addEdge(sourceModel.getId() + "->" + targetModel.getId(), sourceView, targetView, true);

            String type = "";

            //If edge comes from or goes to depot
            if(sourceView.getId().contains("D") || targetView.getId().contains("D")){
                type = "depot";
            }

            //If edge: [N]-->[N']
            if(targetView.getId().equals(sourceView.getId()+"'")){
                type = "busline";
            }

            if(type.equals("")){
                type = "overhead";
            }

            edgeView.setAttribute("ui.class", type);
        }

        SwingViewer swingViewer = new SwingViewer(graphView, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        View view = swingViewer.addDefaultView(false);
        view.getCamera().setViewPercent(0.02);
        view.getCamera().setViewCenter(1.5,2.5,0);
        view.getCamera().resetView();
        view.enableMouseOptions();

        /*
        Thread thread = new Thread(() -> {
            ViewerPipe fromViewer = swingViewer.newViewerPipe();
            fromViewer.addViewerListener(new MyViewerListener(graphView));
            fromViewer.addSink(graphView);

            while(MyViewerListener.loop) {
                fromViewer.pump();
            }
        });
        thread.start();
         */

        JPanel graphPanel = new JPanel(); //To eliminate mouse shifting
        graphPanel.setLayout(new GridLayout(0, 1));
        graphPanel.add((Component) view);
        currentGraph = graphView;

        System.out.println("Connection Based graph info --- Vertices: " + graphModel.vertexSet().size() + ", Edges: " + graphModel.edgeSet().size());

        return graphPanel;
    }

    /**
     * Builds a Time-Space Graphview from the given graph
     * @param graphModel The graph which the view is based on
     * @return The Time-Space Graphview
     */
    public static Component BuildTimeSpacedView(Graph<TimeSpaceNode, DefaultEdge> graphModel){
        System.setProperty("org.graphstream.ui", "swing");

        SingleGraph graphView = new SingleGraph(TIME_SPACE_ID);
        graphView.setAttribute("stylesheet", "url("+ STYLESHEET_URL +")");
        graphView.setAttribute("ui.antialias");

        //Adding sprites to Terminals
        SpriteManager spriteManager = new SpriteManager(graphView);

        Sprite terminal1 = spriteManager.addSprite("terminal1");
        terminal1.setAttribute("ui.class", "terminalLabel");
        terminal1.setAttribute("ui.label", "Terminal 1");

        Sprite terminal2 = spriteManager.addSprite("terminal2");
        terminal2.setAttribute("ui.class", "terminalLabel");
        terminal2.setAttribute("ui.label", "Terminal 2");

        Sprite depot = spriteManager.addSprite("depot");
        depot.setAttribute("ui.class", "terminalLabel");
        depot.setAttribute("ui.label", "Depot");



        int x;
        int y;
        double yScale = 50;
        //For each node in model --> add a node into the view and set attributes, edges
        for (TimeSpaceNode modelNode : graphModel.vertexSet()){
            Node viewNode = graphView.addNode(modelNode.getId());

            viewNode.setAttribute("ui.label", modelNode.getId());
            viewNode.setAttribute("type", modelNode.getType());
            viewNode.setAttribute("layout.frozen");

            x = modelNode.getTime();

            switch (modelNode.getTerminal()){
                case "Vá.1": y = 1; break;
                case "Vá.2": y = 2; break;
                default: y = 0; //Default is Depot node
            }

            viewNode.setAttribute("xy", x, y*yScale);

            System.out.println("Terminal of node [" + modelNode.getId() + "] is " + modelNode.getTerminal());
        }

        depot.setPosition(200, 0*yScale,0);
        terminal1.setPosition(200, 1*yScale,0);
        terminal2.setPosition(200, 2*yScale,0);

        //Set edges
        for (DefaultEdge modelEdge : graphModel.edgeSet()) {
            TimeSpaceNode modelSourceNode = graphModel.getEdgeSource(modelEdge);
            TimeSpaceNode modelTargetNode = graphModel.getEdgeTarget(modelEdge);

            Node sourceView = graphView.getNode(modelSourceNode.getId());
            Node targetView = graphView.getNode(modelTargetNode.getId());

            Edge edgeView = graphView.addEdge(modelSourceNode.getId() + "->" + modelTargetNode.getId(), sourceView, targetView, true);

            String type = "";

            //If edge comes from or goes to depot
            if(modelSourceNode.getTerminal().equals("Garázs") || modelTargetNode.getTerminal().equals("Garázs")){
                type = "depot";
            }

            //If edge "stays" in the same terminal and goes from an arriving (w/ ') node to a departing (w/o ') one
            if(modelSourceNode.getTerminal().equals(modelTargetNode.getTerminal()) &&
                    (sourceView.getId().contains("'") && !targetView.getId().contains("'"))){
                type = "waiting";
            }

            //If edge: [N]-->[N']
            if(targetView.getId().equals(sourceView.getId()+"'")){
                type = "busline";
            }

            if(type.equals("")){
                type = "overhead";
            }

            edgeView.setAttribute("ui.class", type);
        }

        SwingViewer swingViewer = new SwingViewer(graphView, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        View view = swingViewer.addDefaultView(false);
        view.getCamera().setViewPercent(0.02);
        view.getCamera().setViewCenter(1.5,2.5,0);
        view.getCamera().resetView();

        JPanel graphPanel = new JPanel(); //To eliminate mouse shifting
        graphPanel.setLayout(new GridLayout(0, 1));
        graphPanel.add((Component) view);
        currentGraph = graphView;

/*
        Thread thread = new Thread(() -> {
            ViewerPipe fromViewer = swingViewer.newViewerPipe();
            fromViewer.addViewerListener(new MyViewerListener(graphView));
            fromViewer.addSink(graphView);

            while(MyViewerListener.loop) {
                fromViewer.pump();
            }
        });
        thread.start();

 */

        System.out.println("Time-Space Graph info --- Vertices: " + graphModel.vertexSet().size() + ", Edges: " + graphModel.edgeSet().size());

        return graphPanel;
    }

    public static void ResetToggleDepots(){
        depotshidden = false;
    }

    public static void ToggleDepots(){

        Node d1 = currentGraph.getNode("D");
        Node d2 = currentGraph.getNode("D'");

        depotshidden = !depotshidden;

        if(depotshidden){
            d1.setAttribute("ui.hide");
            d2.setAttribute("ui.hide");

            for(Object edgeObject : d1.leavingEdges().toArray()){
                Edge edge = (Edge) edgeObject;

                edge.setAttribute("ui.hide");
            }

            for(Object edgeObject : d2.enteringEdges().toArray()){
                Edge edge = (Edge) edgeObject;

                edge.setAttribute("ui.hide");
            }

        } else {
            d1.removeAttribute("ui.hide");
            d2.removeAttribute("ui.hide");

            for(Object edgeObject : d1.leavingEdges().toArray()){
                Edge edge = (Edge) edgeObject;

                edge.removeAttribute("ui.hide");
            }

            for(Object edgeObject : d2.enteringEdges().toArray()){
                Edge edge = (Edge) edgeObject;

                edge.removeAttribute("ui.hide");
            }
        }
    }
}
