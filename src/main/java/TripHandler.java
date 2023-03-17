import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TripHandler {

    public static List<Trip> dataToTrips(Map<Integer, List<String>> data){
        List<Trip> trips = new ArrayList<>();

        data.remove(0);
        for (Map.Entry row:
                data.entrySet()) {

            int departureTime = (int) Float.parseFloat(((List<String>) row.getValue()).get(0));
            int arriveTime = (int) Float.parseFloat(((List<String>) row.getValue()).get(1));
            String tripType = ((List<String>) row.getValue()).get(2);

            Trip trip = new Trip(
                departureTime,
                arriveTime,
                tripType
            );

            trips.add(trip);
        }

        return trips;
    }

    public static Graph<Trip, DefaultEdge> tripsToGraph(List<Trip> trips){
        Graph<Trip, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        int index = 0;
        for (Trip trip : trips) {
            graph.addVertex(trip);


            if(index >= 10)
                break;

            index++;

        }

        for (Trip trip_i : graph.vertexSet()) {
            for (Trip trip_j : graph.vertexSet()) {
                if(trip_i.compatible(trip_j))
                    graph.addEdge(trip_i,trip_j);
            }
        }

        return graph;
    }

    public static Graph<Trip, DefaultEdge> testGraph(){
        Graph<Trip, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        Trip trip0 = new Trip(1,4,"type1");
        Trip trip1 = new Trip(1,2,"type1");
        Trip trip2 = new Trip(3,5,"type1");
        Trip trip3 = new Trip(2,4,"type1");
        Trip trip4 = new Trip(1,3,"type1");
        Trip trip5 = new Trip(1,2,"type2");
        Trip trip6 = new Trip(1,3,"type2");
        Trip trip7 = new Trip(2,3,"type2");
        Trip trip8 = new Trip(4,5,"type2");
        Trip trip9 = new Trip(3,5,"type2");

        graph.addVertex(trip0);
        graph.addVertex(trip1);
        graph.addVertex(trip2);
        graph.addVertex(trip3);
        graph.addVertex(trip4);
        graph.addVertex(trip5);
        graph.addVertex(trip6);
        graph.addVertex(trip7);
        graph.addVertex(trip8);
        graph.addVertex(trip9);

        graph.addEdge(trip0,trip1);
        graph.addEdge(trip2,trip3);
        graph.addEdge(trip0,trip4);
        graph.addEdge(trip1,trip3);
        graph.addEdge(trip0,trip1);
        graph.addEdge(trip5,trip6);
        graph.addEdge(trip6,trip8);
        graph.addEdge(trip6,trip9);
        graph.addEdge(trip7,trip8);

        return graph;
    }
}
