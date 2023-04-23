import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is responsible for building an AMPL text IP model from a given Connection Based graph
 */
public class IPBuilder {
    private static final String SAVE_NAME = "conn_based_model\\model.txt";
    /**
     * Builds an IP model from the given Connection Based graph
     * @param graph The graph to build the model from
     * @return The IP model
     */
    public static String BuildModel(Graph<ConnectionBasedNode, DefaultEdge> graph, boolean writeToFile){
        String model = "asd";
        // --- Get graph and iterate through it ---

        // --- Add lines to the model string (remember to use '\n') ---

        if(writeToFile)
            SaveToFile(model);

        // --- Show IP model in window after button press (pass/return the same string) ---

        return model;
    }

    // --- Idea 0 --- //
    /*
    * On a Connection Based graph set a 'c' cost to the edges:
    * c_ij = 1 --> E2 (overhead lines), else c_ij = 0
    *
    * Solve for minimal cost full pairing --> get minimum number of vehicles needed
    */

    // --- Idea 1 --- //
    /*
     * On a Connection Based graph set a 'c' cost to the edges:
     * c_ij = 1 --> E2 (overhead lines), else c_ij = 0
     *
     * Solve for minimal cost pairing
     *
     * If maximum number of vehicles is set (=k):
     * Let x binary variables: x_ij = 1 if M pairing contains the (i,j) edge, 0 otherwise
     * Use 'c' cost vector
     * Vector of feasible solutions: X (of x_ij's):
     * Sum_i(x_ij)=1, i=1,2,...,n*
     * Sum_j(x_ij)=1, j=1,2,...,n*
     * Sum_[(i,j) in E2](x_ij) <= k
     * Goal: min_(i,j){cx|x in X, x_ij = {0,1}}
     */

    private static void SaveToFile(String text){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_NAME));
            writer.write(text);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
