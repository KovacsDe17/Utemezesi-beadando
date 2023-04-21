import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
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
    private static final String STYLESHEET = "url(file://./src/main/resources/stylesheet.css)";
    private static final String CONNECTION_BASED_ID = "ConnectionBased";
    private static final String TIME_SPACE_ID = "TimeSpace";

    private static org.graphstream.graph.Graph currentGraph;
    private static boolean depotshidden = false;
    private static boolean loop = true;


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
        graphView.setAttribute("stylesheet", STYLESHEET);
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

            graphView.addEdge(sourceModel.getId() + "->" + targetModel.getId(), sourceView, targetView, true);
        }

        SwingViewer swingViewer = new SwingViewer(graphView, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = swingViewer.addDefaultView(false);
        view.getCamera().setViewPercent(0.02);
        view.getCamera().setViewCenter(1.5,2.5,0);
        view.getCamera().resetView();

        JPanel graphPanel = new JPanel(); //To eliminate mouse shifting
        graphPanel.setLayout(new GridLayout(0, 1));
        graphPanel.add((Component) view);
        currentGraph = graphView;

        //Adding controls sprite
        SpriteManager spriteManager = new SpriteManager(graphView);
        Sprite s = spriteManager.addSprite("info");
        s.setAttribute("ui.hide");

        /*
        swingViewer.getDefaultView().enableMouseOptions();

        ViewerPipe fromViewer = swingViewer.newViewerPipe();
        fromViewer.addViewerListener(new Interaction(fromViewer));
        fromViewer.addSink(graphView);
         */

        return graphPanel;
    }

    /**
     * Builds a Time-Space Graphview from the given graph
     * @param graph The graph which the view is based on
     * @return The Time-Space Graphview
     */
    public SwingViewer BuildTimeSpaceView(Graph<Line, DefaultEdge> graph){
        return null;
    }

    private SingleGraph testGraph(){
        SingleGraph graphView = new SingleGraph("TestGraph");
        graphView.setAttribute("stylesheet", STYLESHEET);

        //Populating the graph
        Node a = graphView.addNode("A" );
        Node b = graphView.addNode("B" );
        Node c = graphView.addNode("C" );
        graphView.addEdge("AB", "A", "B");
        graphView.addEdge("BC", "B", "C");
        graphView.addEdge("CA", "C", "A");

        //Setting position of nodes
        a.setAttribute("xy", 1,1);
        b.setAttribute("xy", 2,1);
        c.setAttribute("xy", 1.5,2);

        return graphView;
    }

    public static void ToggleDepots(){
        if(!currentGraph.getId().equals(CONNECTION_BASED_ID))
            return;

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

    private void SelectNode(){

    }

}
