import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class Interaction implements ViewerListener {
    ViewerPipe fromViewer;

    public Interaction(ViewerPipe fromViewer){
        this.fromViewer = fromViewer;

        Thread newThread = new Thread(() -> {
            while(loop) {
                fromViewer.pump();
            }
        });
        newThread.start();

    }

    private static boolean loop = true;

    public void viewClosed(String id) {
        loop = false;
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
