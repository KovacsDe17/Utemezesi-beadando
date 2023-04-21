import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * This class is responsible for building an IP model from a given graph
 */
public class IPBuilder {

    /**
     * Builds an IP model from the given graph
     * @param graph The graph to build the model from
     * @return The IP model
     */
    public static Object BuildModel(Graph<Line, DefaultEdge> graph){
        return null;
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
     * Goal: min_(i,j){cx|x in X, x_ij in {0,1}}
     */
}
