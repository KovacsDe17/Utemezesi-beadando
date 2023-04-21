/**
 * Node of the Connection Based Graph
 */
public class ConnectionBasedNode {
    private final String id;
    private final String type;

    public ConnectionBasedNode(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
