import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for the graph logic, such as manipulating nodes and edges
 */
public class GraphModel {

    /**
     * Loads data from the Excel input file and builds a Connection Based VSP Graph based on it
     * @return A graph based on the Excel input file
     */
    public static org.jgrapht.Graph<ConnectionBasedNode, DefaultEdge> BuildConnectionBasedGraph(){
        //Load line list
        List<Trip> trips = Trip.getListFromExcel();

        Graph<ConnectionBasedNode, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        //Add depot nodes (D1 and D2 represent the same depot, hence the set id's)
        ConnectionBasedNode D1 = new ConnectionBasedNode("D", "D1");
        ConnectionBasedNode D2 = new ConnectionBasedNode("D'", "D2");

        graph.addVertex(D1);
        graph.addVertex(D2);

        for(Trip trip : trips){
            //Add G nodes
            ConnectionBasedNode nodeG1 = new ConnectionBasedNode(trip.getId() + "","G1");
            ConnectionBasedNode nodeG2 = new ConnectionBasedNode(trip.getId() + "'","G2");

            graph.addVertex(nodeG1);
            graph.addVertex(nodeG2);

            //Set main edges (depot->G1, G1->G2, G2->depot)
            graph.addEdge(D1,nodeG1);
            graph.addEdge(nodeG1,nodeG2);
            graph.addEdge(nodeG2,D2);

            for(Trip nextTrip : trips){
                if(trip.compatible(nextTrip)){
                    ConnectionBasedNode nextG1 = findConnBasedNodeWithID(graph, nextTrip.getId() + "");
                    if (nextG1 != null)
                        graph.addEdge(nodeG2, nextG1);
                }
            }
        }

        //Return graph
        return graph;
    }


    /**
     * Loads data from the Excel input file and builds a Connection Based VSP Graph based on it
     * @return A graph based on the Excel input file
     */
    public static org.jgrapht.Graph<TimeSpaceNode, DefaultEdge> BuildTimeSpaceGraph(){
        //Load line list
        List<Trip> trips = Trip.getListFromExcel();

        Graph<TimeSpaceNode, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        //Parse list to graph...

        // -- Rules of making the graph -- //
        /*
         * Nodes:
         * 2 per line --> when arrive to terminal, when depart from terminal
         * Nodes can be merged if arrive/depart time and terminal are the same
         *
         * Edges:
         *
         */

        //Add depot nodes (D1 and D2 are the same, hence the set ids)
        TimeSpaceNode D1 = new TimeSpaceNode(
                "D",
                "Garázs",
                TimeSpaceNode.NodeType.DEPART,
                240
        ); //(=04:00)

        TimeSpaceNode D2 = new TimeSpaceNode(
                "D'",
                "Garázs",
                TimeSpaceNode.NodeType.ARRIVE,
                1439
        ); //(=23:59)

        graph.addVertex(D1);
        graph.addVertex(D2);

        for(Trip trip : trips) {
            //Add G nodes
            TimeSpaceNode nodeG1 = new TimeSpaceNode(
                    trip.getId() + "",
                    trip.getTerminals().getStart(),
                    TimeSpaceNode.NodeType.DEPART,
                    trip.getDepartureTime()
            );

            TimeSpaceNode nodeG2 = new TimeSpaceNode(
                    trip.getId() + "'",
                    trip.getTerminals().getEnd(),
                    TimeSpaceNode.NodeType.ARRIVE,
                    trip.getArriveTime()
            );

            graph.addVertex(nodeG1);
            graph.addVertex(nodeG2);


            //Set main edges (depot->G1, G1->G2, G2->depot)
            graph.addEdge(D1, nodeG1);
            graph.addEdge(nodeG1, nodeG2);
            graph.addEdge(nodeG2, D2);

            // --- Add waiting edges ---
            // For each node, find the next (in time) node which could make a waiting edge
            for (TimeSpaceNode node : graph.vertexSet()) {
                //
                for (TimeSpaceNode nextNode : graph.vertexSet().stream().sorted(Comparator.comparingInt(TimeSpaceNode::getTime)).collect(Collectors.toList())) {
                    //If two nodes are in the same terminal, the first one is arriving before and the next one is departing after, add a "waiting edge"
                    if (node.getTerminal().equals(nextNode.getTerminal()) &&
                            (node.getType() == TimeSpaceNode.NodeType.ARRIVE && nextNode.getType() == TimeSpaceNode.NodeType.DEPART) &&
                            node.getTime() < nextNode.getTime()) {
                        graph.addEdge(node, nextNode);
                        break; //Get only the first edge for each of these
                    }
                }
            }
        }

        //Add overhead edges
        Map<TimeSpaceNode, TimeSpaceNode> lastFirstMatches = getLastFirstMatches(graph);
        for(TimeSpaceNode node : lastFirstMatches.keySet()) {
            graph.addEdge(node, lastFirstMatches.get(node));
        }

        //Return graph
        return graph;
    }

    /**
     * Gets the last-first pairs of nodes for the overhead edges
     * @param graph The graph from which to get the last-first pairs
     * @return The last-first pairs of nodes
     */
    private static Map<TimeSpaceNode, TimeSpaceNode> getLastFirstMatches(Graph<TimeSpaceNode, DefaultEdge> graph){
        Map<TimeSpaceNode, TimeSpaceNode> nodePair = new HashMap<>();

        //Sorted node set by time
        List<TimeSpaceNode> sortedNodes = graph.vertexSet().stream().sorted(Comparator.comparingInt(TimeSpaceNode::getTime)).collect(Collectors.toList());

        //First matches: For every node_i, save the first nodes (after node_i's time) from each of the other terminals' departing nodes
        //[node_i - [terminal_name - first_possible_node]]
        Map<TimeSpaceNode, Map<String, TimeSpaceNode>> firstMatches = new HashMap<>();

        //Save how many distinct terminals we have --> later we'll know when to quit loop
        int terminalCount = 0;

        //Phase 1 - First matches
        for (TimeSpaceNode node : sortedNodes){
            //Skip if it's not arrive type node
            if(node.getType() != TimeSpaceNode.NodeType.ARRIVE)
                continue;

            for(TimeSpaceNode nextNode : sortedNodes){
                //Skip if it's at the same terminal or the time is before node's or the nextNode is not departing
                if(nextNode.getTerminal().equals(node.getTerminal()) ||
                        nextNode.getTime() < node.getTime() ||
                        nextNode.getType() != TimeSpaceNode.NodeType.DEPART)
                    continue;

                //Create pair "slots" if not set previously
                firstMatches.computeIfAbsent(node, k -> new HashMap<>());

                //Only set new values if not set previously
                firstMatches.get(node).putIfAbsent(nextNode.getTerminal(), nextNode);

                //Quit this loop when we have all the [Terminal - Node] pairs for this node
                if(firstMatches.get(node).size() == terminalCount)
                    break;
            }
        }

        //Phase 2 - Merging first matches
        for (TimeSpaceNode node : firstMatches.keySet()){
            TimeSpaceNode lastNextNode = null;
            for(TimeSpaceNode nextNode : firstMatches.get(node).values()){
                //Initialize with first element if not set yet
                if(lastNextNode == null)
                    lastNextNode = nextNode;

                //Check time and set the latest
                if(lastNextNode.getTime()<nextNode.getTime())
                    lastNextNode = nextNode;
            }

            nodePair.put(node, lastNextNode);
        }

        return nodePair;
    }

    private static ConnectionBasedNode findConnBasedNodeWithID(Graph<ConnectionBasedNode, DefaultEdge> graph, String id){
        for(ConnectionBasedNode node : graph.vertexSet()){
            if(node.getId().equals(id)){
                return node;
            }
        }

        return null;
    }
}
