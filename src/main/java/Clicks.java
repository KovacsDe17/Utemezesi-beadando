import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.util.InteractiveElement;
import org.graphstream.ui.view.util.MouseManager;

import javax.swing.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

public class Clicks implements ViewerListener {
    protected boolean loop = true;

    public Clicks() {
        System.setProperty("sun.java2d.uiScale", "1.0"); //Must leave because of ui shifting...

        // We do as usual to display a graph. This
        // connect the graph outputs to the viewer.
        // The viewer is a sink of the graph.
        SingleGraph graph = new SingleGraph("Clicks");

        // Populate the graph
        Node a = graph.addNode("A" );
        Node b = graph.addNode("B" );
        Node c = graph.addNode("C" );
        Edge ab = graph.addEdge("AB", "A", "B", true);
        Edge bc = graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);

        //Setting position of nodes
        a.setAttribute("xy", 1,1);
        b.setAttribute("xy", 2,1);
        c.setAttribute("xy", 1.5,2);

        for (Node n : graph){
            n.setAttribute("ui.label", n.getId());
        }

        ab.setAttribute("ui.class", "rezsi");
        ab.removeAttribute("ui.class");
        bc.setAttribute("ui.class", "rezsi");

        //Setting styles from stylesheet file
        String stylesheet = "url(file://./src/main/resources/stylesheet.css)";
        graph.setAttribute("ui.stylesheet", stylesheet);

        //Getting node position
        double[] pos = nodePosition(a);
        System.out.println("Position of " + a + " is " + Arrays.toString(pos));

        //View display
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(true);
        //view.getCamera().setViewCenter(0, 0, 0);
        viewer.disableAutoLayout();
        viewer.getDefaultView().enableMouseOptions();
        view.getCamera().setViewPercent(2);
        view.getCamera().resetView();
        System.out.println("Camera pos: " + view.getCamera().getViewCenter());

        //Adding sprite
        SpriteManager sman = new SpriteManager(graph);
        Sprite s1 = sman.addSprite("S1");
        double[] aPos = nodePosition(a);
        s1.setPosition(aPos[0], aPos[1], aPos[2]);

        //JFrame frame = new JFrame("Jihha");
        //frame.add(view);

        // The default action when closing the view is to quit
        // the program.
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        // We connect back the viewer to the graph,
        // the graph becomes a sink for the viewer.
        // We also install us as a viewer listener to
        // intercept the graphic events.
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);

        // Then we need a loop to do our work and to wait for events.
        // In this loop we will need to call the
        // pump() method before each use of the graph to copy back events
        // that have already occurred in the viewer thread inside
        // our thread.

        while(loop) {
            fromViewer.pump(); // or fromViewer.blockingPump(); in the nightly builds

            // here your simulation code.

            // You do not necessarily need to use a loop, this is only an example.
            // as long as you call pump() before using the graph. pump() is non
            // blocking.  If you only use the loop to look at event, use blockingPump()
            // to avoid 100% CPU usage. The blockingPump() method is only available from
            // the nightly builds.
        }
    }

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        System.out.println("Button pushed on node " + id);
    }

    public void buttonReleased(String id) {
        System.out.println("Button released on node " + id);
    }

    public void mouseOver(String id) {
        System.out.println(id + " is hovered over");
    }

    public void mouseLeft(String id) {
        System.out.println(id + " has left ");
    }

    public static void main(String[] args) {
        new Clicks();
    }
}
