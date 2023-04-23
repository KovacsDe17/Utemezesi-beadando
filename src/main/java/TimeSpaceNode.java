import org.w3c.dom.Node;

public class TimeSpaceNode {
    private final String id;
    private final String terminal;
    private final NodeType type; //arrive or depart
    private final int time;

    public enum NodeType {ARRIVE,DEPART}

    public TimeSpaceNode(String id, String terminal, NodeType type, int time) {
        this.id = id;
        this.terminal = terminal;
        this.type = type;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getTerminal() {
        return terminal;
    }

    public NodeType getType() {
        return type;
    }

    public int getTime() {
        return time;
    }
}
