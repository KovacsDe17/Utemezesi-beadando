import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class Interaction implements ViewerListener {

    private static boolean loop = true;

    public void viewClosed(String id) {
        loop = false;
        System.out.println("View closed");
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
}
