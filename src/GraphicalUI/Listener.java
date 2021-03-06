package GraphicalUI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.*;
import Location.*;
import Marker.*;


public class Listener {
    private GraphicalUI write;
    private boolean visa;
    private GraphicalUI graphicalUI;
    private JFileChooser jfc = new JFileChooser(".");
    private ImagePanel imagePanel;
    private ArrayList<Location> locations = new ArrayList<>();
    private SearchButton search = new SearchButton();
    private Map<Position, Location> hashMappen = new HashMap<>();





    public Listener (GraphicalUI graphicalUI){
        this.graphicalUI=graphicalUI;
    }

    public OpenListener getOpenListener(){
        return new OpenListener();
    }
    public ExitListener getExitListener(){
        return new ExitListener();
    }
    public ActionListener getWhatListener() {
        return new WhatListener();
    }
    public ActionListener getSaveListener() {
        return new SaveListener();
    }
    public MarkerListener getMarkerListener(String value){
        return new MarkerListener(value);
    }
    public ButtonPressed getButtonPressed(){
        return new ButtonPressed();
    }
    public ActionListener getLoadListener() {
        return new LoadListener();
    }
    public CategoryListener getCategoryListener() { return new CategoryListener();}
    public HideCategory getHideCategory() {return new HideCategory(); }
    public String typ = null;
    public static Map<String, Position> lista = new HashMap<>(50);
    public SearchButton getSearchButton() {return new SearchButton();}
    public HideButton getHideButton() {
        return new HideButton();
    }

    public RemoveButton getRemoveButton() {
        return new RemoveButton();
    }

    public class LoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String line;
            try (
                    InputStream fis = new FileInputStream("places.places");
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    String[] words = line.split(",");
                    if (words.length == 5) {
                        String[] words2 = new String[6];
                        for (int i = 0; i < 5; ++i) {
                            words2[i] = words[i];
                        }
                        words2[5] = "";
                        words = words2;
                    }
                    LocationInfo locationInfo = new LocationInfo(2,2, "a", "b");
                    Location location;
                    Category category = Category.valueOf(words[1]);
                    int x = Integer.parseInt(words[2]);
                    int y = Integer.parseInt(words[3]);
                    Marker marker;
                    if (words[0].equals("Named")) {
                        marker = new NamedMarker(x, y, category, locationInfo, false);
                        location = new Named(words[4], new Position(x, y), marker, category, locationInfo);
                    } else {
                        marker = new DescribedMarker(x, y, category, locationInfo, false);
                        location = new Described(words[4], new Position(x, y), marker, category, words[5], locationInfo);
                    }
                    locations.add(location);
                    imagePanel.add(marker);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            imagePanel.validate();
            imagePanel.repaint();
        }
    }

    public class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PrintWriter writer = new PrintWriter("places.places", "UTF-8");
                for (Location location : locations) {
                    if (location instanceof Named) {
                        writer.print("Named");
                    } else {
                        writer.print("Described");
                    }
                    writer.print("," + location.category.toString());
                    writer.print("," + location.position.getX() + "," + location.position.getY());
                    writer.print("," + location.name);
                    if (location instanceof Described) {
                        Described described = (Described) location;
                        writer.print("," + described.description);
                    }
                    writer.println();
                }
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }



    public class WhatListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            imagePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mev) {
                    super.mousePressed(mev);
                    int x = mev.getX();
                    int y = mev.getY();
                    for (Location location : locations) {
                        if (Math.abs(location.position.getX() - x) <= 75 &&
                                Math.abs(location.position.getY() - y) <= 75) {
                            location.marker.setVisible(true);
                            location.marker.setMarked(true);
                        } else {
                            location.marker.setMarked(false);
                        }
                    }
                    imagePanel.setCursor(Cursor.getDefaultCursor());
                    imagePanel.removeMouseListener(this);
                }
            });
        }
    }

    class ValueListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            System.out.print("Hej");
        }
    }

    class NameListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            System.out.print("Hej");
        }
    }

    public class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            int svar = jfc.showOpenDialog(graphicalUI);
            if (svar != JFileChooser.APPROVE_OPTION)
                return;

            File fil = jfc.getSelectedFile();
            String filnamn = fil.getAbsolutePath();
            System.out.println(filnamn);
            imagePanel = new ImagePanel(filnamn);
            graphicalUI.add(imagePanel);
            JScrollPane scroll = new JScrollPane(imagePanel);
            graphicalUI.add(scroll);
            graphicalUI.pack();
            graphicalUI.validate();
            graphicalUI.repaint();

        }


    }

    public String getFilNamn(){
        return jfc.getSelectedFile().getAbsolutePath();
    }
    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            System.exit(0);
        }
    }


    public class MarkerListener extends MouseAdapter {



        private String value;
        public MarkerListener(String value) {
            this.value = value;
        }

        @Override
        public void mouseClicked(MouseEvent mev) {
            int x = mev.getX();
            int y = mev.getY();
            Position position = new Position(x, y);
            imagePanel.requestFocusInWindow();

            Category category;

            if (graphicalUI.getIsSelected()) {
                switch (graphicalUI.getJList().trim().toLowerCase()) {
                    case "bus":
                        category = Category.BUS;
                        break;
                    case "train":
                        category = Category.TRAIN;
                        break;
                    case "subway":
                        category = Category.SUBWAY;
                        break;
                    default:
                        throw new RuntimeException("Someone made a typo");
                }
            } else {
                category = Category.NONE;
            }


            imagePanel.removeMouseListener(MarkerListener.this);
            Location location;
            String description;
            String name;

            JPanel myPanel = new JPanel();
            JPanel myPanel2 = new JPanel();
            JLabel forField1 = new JLabel("Name: ");
            JLabel forField12 = new JLabel("Name: ");
            JLabel forField2 = new JLabel("Description: ");
            JTextField nameField = new JTextField(10);
            JTextField nameField12 = new JTextField(10);
            JTextField descriptionField = new JTextField(10);
            myPanel.add(forField1);
            myPanel.add(nameField);
            myPanel.add(forField2);
            myPanel.add(descriptionField);
            myPanel2.add(forField12);
            myPanel2.add(nameField12);



            Marker marker;
            LocationInfo locationInfo;
            if (value.equals("Named")) {


                JOptionPane.showMessageDialog(null, myPanel2, "New named", JOptionPane.QUESTION_MESSAGE);
                name = nameField12.getText();
                locationInfo = new LocationInfo(x + 100, y + 100, name, "");
                marker = new NamedMarker(x -25 , y -50, category, locationInfo, false);
                location = new Named(name, position, marker, category, locationInfo);
                imagePanel.add(locationInfo);
            } else {

                JOptionPane.showMessageDialog(null, myPanel, "New described", JOptionPane.QUESTION_MESSAGE);

                name = nameField.getText();
                description = descriptionField.getText();
                System.out.print(descriptionField.getText());
                locationInfo = new LocationInfo(x + 20, y + 20, name, description);
                marker = new DescribedMarker(x -25 , y -50, category, locationInfo, false);
                location = new Described(name, position, marker, category, description, locationInfo);
                imagePanel.add(locationInfo);

            }
            locations.add(location);
            hashMappen.put(position, location);

            imagePanel.add(marker);
            imagePanel.validate();

            imagePanel.repaint();

            System.out.println(hashMappen +"KING" + locationInfo);
            imagePanel.setCursor(Cursor.getDefaultCursor());


        }
    }
    public class CategoryListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent lse) {
            if (!lse.getValueIsAdjusting()) {
                System.out.println(graphicalUI.getJList());


            }
        }
    }


    public class HideCategory implements ActionListener{
        public void actionPerformed (ActionEvent ave) {
            Category category;

            if (graphicalUI.getIsSelected()) {
                switch (graphicalUI.getJList().trim().toLowerCase()) {
                    case "bus":
                        category = Category.BUS;
                        break;
                    case "train":
                        category = Category.TRAIN;
                        break;
                    case "subway":
                        category = Category.SUBWAY;
                        break;
                    default:
                        throw new RuntimeException("Someone made a typo");
                }
            } else {
                category = Category.NONE;
            }
            for (Location location : locations) {
                if (location.category == category) {
                    location.marker.setVisible(false);
                }
            }
        }
    }

    public class HideButton implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            for (Location location : locations) {
                if (!locations.isEmpty()) {
                    location.marker.setVisible(false);
                }
            }
        }
    }

    public class RemoveButton implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            if (locations.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Listan är tom");
            }
            for (Location location : locations) {



                 if (location.marker.getMarked() == true) {
                    location.marker.setVisible(false);
                    locations.remove(location);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Inget objekt valt");
                    return;
                }
            }
        }

    }
    public class SearchButton implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String word = graphicalUI.getWrite().getText();
            for (Location location : locations) {
                if (location.name.equals(word)) {
                    location.marker.setMarked(true);
                } else {
                    location.marker.setMarked(false);
                }
            }
        }

    }

    public class ButtonPressed implements ActionListener{
        public void actionPerformed (ActionEvent ave){
            String value = (String) ((JComboBox) ave.getSource()).getSelectedItem();
            imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            imagePanel.addMouseListener(getMarkerListener(value));

        }
    }
}



