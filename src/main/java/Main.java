
public class Main {
    public static void main(String[] args) {
        /*
        var excel = Excel.getInstance();
        Workbook workbook = excel.openWorkbook();

        var routeParameters = excel.getDataFrom(workbook,0);
        var busLines = excel.getDataFrom(workbook,1);

        var lines = Line.toList(busLines, Route.toList(routeParameters));

        var graph = Graph.addVerticesFromList(lines);
        Graph.addEdgesCompatible(graph);

        System.out.println("Vertex count: " + graph.vertexSet().size());
        System.out.println("Edge count: " + graph.edgeSet().size());

        Visuals.setGraph(graph);
        Visuals.MainMenu();
        */ //Old code
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

        //TODO: place this into constructor of GraphView
        System.setProperty("org.graphstream.ui", "swing");

        // --- Place here only a main menu frame! --- //

    }
}