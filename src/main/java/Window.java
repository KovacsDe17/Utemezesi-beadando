import org.jfree.chart.resources.JFreeChartResources;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * This class is responsible for app windows
 */
public class Window {
    private static Window instance;
    private static final String APP_NAME = "VSP Graph Visuals";
    private boolean helperHidden = false;

    public static Window getInstance() {
        if(instance == null)
            instance = new Window();

        return instance;
    }

    public void OpenMainMenu(){
        System.setProperty("sun.java2d.uiScale", "1.0"); //Must leave this here because of ui shifting...
        JFrame frame = new JFrame(APP_NAME);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(MenuLabel());
        panel.add(Separator(10));
        panel.add(Button("Connection Based Graph", e -> {
            GraphView.ResetToggleDepots();
            OpenGraphView(
                    "Connection Based Graph",
                    GraphView.BuildConnectionBasedView(GraphModel.BuildConnectionBasedGraph())
            );
            frame.dispose();
        }));
        panel.add(Separator(10));
        panel.add(Button("Time-Space Graph", e -> {
            GraphView.ResetToggleDepots();
            OpenGraphView(
                    "Time-Space Graph",
                    GraphView.BuildTimeSpacedView(GraphModel.BuildTimeSpaceGraph())
            );
            frame.dispose();
        }));
        panel.add(Separator(10));
        panel.add(Button("Build IP model", e -> {
            OpenIPModel();
            frame.dispose();
        }));        panel.add(Separator(10));
        panel.add(Button("Exit", e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }));

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,225));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void OpenGraphView(String graphType, Component graphView){
        JFrame frame = new JFrame(APP_NAME + " - " + graphType);
        JPanel mainPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(BackButton(frame));
        buttonPanel.add(Button("Toggle depots", e -> {
            GraphView.ToggleDepots();
        }));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(buttonPanel);
        mainPanel.add(graphView);

        frame.add(mainPanel);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void OpenIPModel(){
        JFrame frame = new JFrame(APP_NAME);
        JPanel mainPanel = new JPanel();
        JTextArea modelText = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(modelText);
        JButton saveButton = Button("Save into file...", e -> {
            SaveFile(modelText.getText());
        });

        modelText.setEditable(false);

        scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Connection Based Graph - Minimize vehicles required"));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(BackButton(frame));
        mainPanel.add(Button("Generate Model", e -> {
            String text = IPBuilder.BuildModel(GraphModel.BuildConnectionBasedGraph());
            modelText.setText(text);
            saveButton.setEnabled(true);
            mainPanel.updateUI();
        }));
        mainPanel.add(saveButton);
        saveButton.setEnabled(false);
        mainPanel.add(Separator(20));
        mainPanel.add(scrollPane);

        frame.add(mainPanel);
        frame.setMinimumSize(new Dimension(800,600));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JFileChooser SaveFile(String modelText){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save model...");
        fileChooser.setSelectedFile(new File("model.txt"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            IPBuilder.SaveToFile(modelText, fileToSave.getAbsolutePath());
            System.out.println("File saved: " + fileToSave.getAbsolutePath());
        }

        return fileChooser;
    }

    private static JButton BackButton(JFrame frame){
        JButton button = new JButton("Back to menu");

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            instance.OpenMainMenu();
            frame.dispose();
        });

        return button;
    }

    private static JButton Button(String label, ActionListener listener){
        JButton button = new JButton(label);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);

        return button;
    }

    private static JLabel MenuLabel(){
        JLabel jlabel = new JLabel(APP_NAME);
        jlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlabel.setVisible(true);

        return jlabel;
    }

    private static Component Separator(int height){
        return Box.createRigidArea(new Dimension(0, height));
    }

}
