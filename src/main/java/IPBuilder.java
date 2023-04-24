import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for building an AMPL text IP model from a given Connection Based graph
 */
public class IPBuilder {
    /**
     * Builds an IP model from the given Connection Based graph
     * @param graph The graph to build the model from
     * @return The IP model
     */
    public static String BuildModel(Graph<ConnectionBasedNode, DefaultEdge> graph){
        String modelText = "<html>---<br>---<br>---<br>This is a test text for the model<br>---<br>---<br>---</html>";

        // --- Initialize model
        Loader.loadNativeLibraries();
        MPSolver model = MPSolver.createSolver("GLOP");

        //Set variable vector "X" and constant vector "C"
        Map<DefaultEdge, MPVariable> X = new HashMap<>();
        Map<DefaultEdge, Integer> C = new HashMap<>();
        for(DefaultEdge edge : graph.edgeSet()){
            ConnectionBasedNode source = graph.getEdgeSource(edge);
            ConnectionBasedNode target = graph.getEdgeTarget(edge);

            String varName = "x_(" + source.getId() + "_to_" + target.getId() + ")";
            X.put(edge, model.makeIntVar(0,1,varName));

            //Set cost of new vehicle high (they always start at depot), else it's 1
            if(source.getId().equals("D"))
                C.put(edge, 999999);
            else
                C.put(edge, 1);
        }

        // --- Set constraint: Execute every line exactly once ---
        MPConstraint allLinesExactlyOnce = model.makeConstraint(1,1,"allLinesExactlyOnce");
        for(DefaultEdge edge : graph.edgeSet()){
            ConnectionBasedNode source = graph.getEdgeSource(edge);
            ConnectionBasedNode target = graph.getEdgeTarget(edge);

            //If it's a line edge <-- not a depot edge and the nodes id's are identical besides the "'"
            if((source.getId()+"'").equals(target.getId())){
                allLinesExactlyOnce.setCoefficient(X.get(edge),1);
            }
        }

        // --- Set constraint: Every vehicle starts in a depot and finishes in one ---
        // --- This equals: For every node it enters, will leave it too, except the depot nodes ---
        MPConstraint startAndFinishInDepot = model.makeConstraint(0,0,"startAndFinishInDepot");

        //For each node, check their edges
        for(ConnectionBasedNode node : graph.vertexSet()){
            for(DefaultEdge edgeOfNode : graph.edgesOf(node)){
                //Check if this edge of the node is an exiting one
                boolean isExiting = graph.getEdgeSource(edgeOfNode).getId().equals(node.getId());
                //If it is an exiting edge, set coefficient to one, otherwise to -1
                int coeff = isExiting ? 1 : -1;
                //Add this edge to the constraints
                startAndFinishInDepot.setCoefficient(X.get(edgeOfNode),coeff);
            }
        }

        //Set Objective: Minimize Sum of X_e*C_e
        MPObjective objective = model.objective();
        for(DefaultEdge edge : graph.edgeSet()){
            objective.setCoefficient(X.get(edge),C.get(edge));
        }
        objective.setMinimization();

        modelText = model.exportModelAsLpFormat();

        // --- Show IP model in window after button press (pass/return the same string) ---

        return modelText;

    }

    public static void SaveToFile(String modelText, String savePath){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));
            writer.write(modelText);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
