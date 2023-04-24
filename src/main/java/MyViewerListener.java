import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.ViewerListener;

public class MyViewerListener implements ViewerListener {
    protected static boolean loop = true;
    protected Graph graph;

    private String originalNodeClasses = "";
    private String originalEdgeClasses = "";

    public MyViewerListener(Graph graph){
        this.graph = graph;
        loop = true;
    };

    @Override
    public void viewClosed(String s) {
        loop = false;
    }

    @Override
    public void buttonPushed(String s) {
        Node node = graph.getNode(s);

        originalNodeClasses = (String) node.getAttribute("ui.class");
        node.setAttribute("highlighted", true);
        node.setAttribute("ui.class", originalNodeClasses + ", highlighted");


        for(Object edgeObject : node.edges().toArray()){
            Edge edge = (Edge) edgeObject;

            originalEdgeClasses = (String) edge.getAttribute("ui.class");
            edge.setAttribute("ui.class", originalEdgeClasses + ", highlighted");
        }

        for(Object otherNodeObject : node.neighborNodes().toArray()){
            Node otherNode = (Node) otherNodeObject;

            originalNodeClasses = (String) otherNode.getAttribute("ui.class");
            otherNode.setAttribute("highlighted", true);
            otherNode.setAttribute("ui.class", originalNodeClasses + ", highlighted");
        }


    }

    @Override
    public void buttonReleased(String s) {
    }

    @Override
    public void mouseOver(String s) {
    }

    @Override
    public void mouseLeft(String s) {
        Node node = graph.getNode(s);

        //Return if it's not a node or it's not highlighted
        if(node == null || node.hasAttribute("highlighted") && !((boolean) node.getAttribute("highlighted")))
            return;

        node.setAttribute("ui.class", originalNodeClasses);
        node.setAttribute("highlighted", false);

        for(Object edgeObject : node.edges().toArray()){
            Edge edge = (Edge) edgeObject;

            edge.setAttribute("ui.class", originalEdgeClasses);
        }

        for(Object otherNodeObject : node.neighborNodes().toArray()){
            Node otherNode = (Node) otherNodeObject;
            if(!(otherNode == null || otherNode.hasAttribute("highlighted") && !((boolean) otherNode.getAttribute("highlighted")))){
                otherNode.setAttribute("highlighted", false);
                otherNode.setAttribute("ui.class", originalNodeClasses);
            }
        }
    }
}
