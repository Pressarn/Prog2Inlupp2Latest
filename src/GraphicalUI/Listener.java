    package GraphicalUI;

    import javax.swing.*;
    import javax.swing.event.ListSelectionListener;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.*;
    import java.nio.charset.Charset;
    import java.util.*;
    import java.util.List;
    import javax.swing.event.*;
    import Location.*;
    import Marker.*;



    public class Listener {


        private GraphicalUI graphicalUI;
        private JFileChooser jfc = new JFileChooser(".");
        private ImagePanel imagePanel;
        private boolean changed = false;
        private ArrayList<Location> locationsForLoadSave = new ArrayList<>();
        private Map<Position, Location> locationsByPosition = new HashMap<>();
        private Map<String, ArrayList<Location>> locationByCategory = new HashMap<>();
        private Map<String, ArrayList<Location>> locationByName = new HashMap<>();
        private ArrayList<Location> markedLocations = new ArrayList<>();

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

        public SearchButton getSearchButton() {return new SearchButton();}

        public HideButton getHideButton() {
            return new HideButton();
        }

        public RemoveButton getRemoveButton() {
            return new RemoveButton();
        }

        private void clear(){
            for (Location location : locationsForLoadSave) {
                location.getMarker().setVisible(false);
            }
            locationsForLoadSave.clear();
            locationsByPosition.clear();
            locationByCategory.clear();
            locationByName.clear();
        }

        private class LoadListener implements ActionListener {


            @Override
            public void actionPerformed(ActionEvent e) {

                if (changed) {
                    int loadQuestion = JOptionPane.showConfirmDialog(graphicalUI, "You have not saved, load anyway?", "Continue", JOptionPane.OK_CANCEL_OPTION);

                    if (loadQuestion != JOptionPane.OK_OPTION) {
                        return;
                    }
                }
                        clear();
                        String line;

                        int answer = jfc.showOpenDialog(graphicalUI);
                        if (answer != JFileChooser.APPROVE_OPTION)
                            return;
                        File file = jfc.getSelectedFile();
                        try (
                                InputStream fis = new FileInputStream(file);
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

                                Location location;
                                LocationInfo locationInfo;
                                Category category = Category.valueOf(words[1].toUpperCase());
                                int x = Integer.parseInt(words[2]);
                                int y = Integer.parseInt(words[3]);
                                Marker marker;
                                if (words[0].equalsIgnoreCase("Named")) {
                                    locationInfo = new LocationInfo(x - 50, y - 50, words[4], "");
                                    Position p = new Position (x, y);
                                    location = new Named(words[4], p,  category, locationInfo, false, false);
                                    addCategorysToList(category.name().toLowerCase(), location);
                                    addNameToList(words[4], location);
                                    locationsByPosition.put(p, location);

                                } else {
                                    locationInfo = new LocationInfo(x - 50, y - 50, words[4], words[5]);
                                    Position p = new Position (x, y);
                                    location = new Described(words[4], p, category, words[5], locationInfo, false, false);
                                    addCategorysToList(category.name().toLowerCase(), location);
                                    addNameToList(words[4], location);
                                    locationsByPosition.put(p, location);
                                }
                                locationsForLoadSave.add(location);
                                imagePanel.add(location.getMarker());
                                changed = true;

                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        imagePanel.validate();
                        imagePanel.repaint();


            }
        }

        private class SaveListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {

                int answer = jfc.showSaveDialog(graphicalUI);
                if (answer != JFileChooser.APPROVE_OPTION)
                    return;
                File file = jfc.getSelectedFile();

                try {
                    PrintWriter writer = new PrintWriter(file, "UTF-8");
                    for (Location location : locationsForLoadSave) {
                        if (location instanceof Named) {
                            writer.print("Named");
                        } else {
                            writer.print("Described");
                        }
                        writer.print("," + location.getCategory().toString());
                        writer.print("," + location.getPosition().getX() + "," + location.getPosition().getY());
                        writer.print("," + location.getName());
                        if (location instanceof Described) {
                            Described described = (Described) location;
                            writer.print("," + described.getDescription());
                        }
                        writer.println();
                    }
                    writer.close();
                    changed = false;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        private class WhatListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                imagePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mev) {
                        super.mousePressed(mev);
                        int x = mev.getX();
                        int y = mev.getY();
                        for (Location location : locationsForLoadSave) {
                            if (Math.abs(location.getPosition().getX() - x) <= 75 &&
                                    Math.abs(location.getPosition().getY() - y) <= 75) {
                                location.getMarker().setVisible(true);
                                location.setMarked(true);
                                location.setUnfolded(false);
                            } else {
                                location.setMarked(false);
                            }
                        }
                        imagePanel.setCursor(Cursor.getDefaultCursor());
                        imagePanel.removeMouseListener(this);
                    }
                });
            }
        }

        private class OpenListener implements ActionListener {
            public void actionPerformed(ActionEvent ave) {


                if (changed) {
                    int loadQuestion = JOptionPane.showConfirmDialog(graphicalUI, "You have not saved, open new map anyway?",
                            "Open map", JOptionPane.OK_CANCEL_OPTION);

                    if (loadQuestion != JOptionPane.OK_OPTION) {
                        return;
                    }
                }

                clear();
                int answer = jfc.showOpenDialog(graphicalUI);
                if (answer != JFileChooser.APPROVE_OPTION)
                    return;

                File file = jfc.getSelectedFile();
                String filename = file.getAbsolutePath();
                imagePanel = new ImagePanel(filename);
                graphicalUI.add(imagePanel);
                JScrollPane scroll = new JScrollPane(imagePanel);
                graphicalUI.add(scroll);
                graphicalUI.pack();
                graphicalUI.validate();
                graphicalUI.repaint();
                clear();
                changed = false;

            }


        }

        private class ExitListener extends WindowAdapter implements ActionListener {
            public void close() {

                if (changed) {
                    int answer = JOptionPane.showConfirmDialog(
                            graphicalUI,
                            "You have not saved, exit anyway?",
                            "Exit",
                            JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else {
                        return;
                    }
                }
                else{
                    System.exit(0);
                }
            }

            public void actionPerformed(ActionEvent ave){
                close();
            }
            public void windowClosing(WindowEvent wev){
                close();

            }
        }

        private void addNameToList(String name, Location location){
            if (!locationByName.containsKey(name)){
                locationByName.put(name, new ArrayList<>());
                locationByName.get(name).add(location);
            }
            else{
                locationByName.get(name).add(location);
            }
        }

        private void addCategorysToList(String name, Location location){
            if (!locationByCategory.containsKey(name)){
                locationByCategory.put(name, new ArrayList<>());
                locationByCategory.get(name).add(location);
            }
            else{
                locationByCategory.get(name).add(location);
            }
        }

        private class MarkerListener extends MouseAdapter {

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
                        case "buss":
                            category = Category.BUSS;
                            break;
                        case "tåg":
                            category = Category.TÅG;
                            break;
                        case "tunnelbana":
                            category = Category.TUNNELBANA;
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

                JPanel describedPanel = new JPanel();
                JPanel namedPanel = new JPanel();
                JLabel forField1 = new JLabel("Name: ");
                JLabel forField12 = new JLabel("Name: ");
                JLabel forField2 = new JLabel("Description: ");
                JTextField nameField = new JTextField(10);
                JTextField nameField12 = new JTextField(10);
                JTextField descriptionField = new JTextField(10);
                describedPanel.add(forField1);
                describedPanel.add(nameField);
                describedPanel.add(forField2);
                describedPanel.add(descriptionField);
                namedPanel.add(forField12);
                namedPanel.add(nameField12);



                Marker marker;
                LocationInfo locationInfo;
                if (value.equals("Named")) {


                    JOptionPane.showMessageDialog(null, namedPanel, "New named", JOptionPane.QUESTION_MESSAGE);
                    name = nameField12.getText();
                    locationInfo = new LocationInfo(x -50 , y - 50, name, "");
                    location = new Named(name, position, category, locationInfo, false, false);
                    addCategorysToList(category.name().toLowerCase(), location);
                    addNameToList(name, location);
                    locationsByPosition.put(position, location);

                } else {

                    JOptionPane.showMessageDialog(null, describedPanel, "New described", JOptionPane.QUESTION_MESSAGE);
                    name = nameField.getText();
                    description = descriptionField.getText();
                    locationInfo = new LocationInfo(x - 50, y - 50, name, description);
                    location = new Described(name, position, category, description, locationInfo, false, false);
                    addCategorysToList(category.name().toLowerCase(), location);
                    addNameToList(name, location);
                    locationsByPosition.put(position, location);

                }

                changed = true;
                locationsForLoadSave.add(location);
                imagePanel.add(location.getMarker());
                imagePanel.validate();
                imagePanel.repaint();
                imagePanel.setCursor(Cursor.getDefaultCursor());


            }
        }

        private class CategoryListener implements ListSelectionListener{
            public void valueChanged(ListSelectionEvent lse) {

                try{
                    if (!lse.getValueIsAdjusting()) {
                        Category category;

                        if (graphicalUI.getIsSelected()) {
                            switch (graphicalUI.getJList().trim().toLowerCase()) {
                                case "buss":
                                    category = Category.BUSS;
                                    break;
                                case "tåg":
                                    category = Category.TÅG;
                                    break;
                                case "tunnelbana":
                                    category = Category.TUNNELBANA;
                                    break;
                                default:
                                    throw new RuntimeException("Someone made a typo");
                            }
                        } else {
                            category = Category.NONE;
                        }

                        List<Location> locList = locationByCategory.get(category.name().toLowerCase());

                        for (Location loc : locList) {
                            loc.getMarker().setVisible(true);
                            loc.setMarked(false);
                        }

                    }

                }catch(NullPointerException e){

                }
            }
        }

        private class HideCategory implements ActionListener{
            public void actionPerformed (ActionEvent ave) {
                Category category;

                if (graphicalUI.getIsSelected()) {
                    switch (graphicalUI.getJList().trim().toLowerCase()) {
                        case "buss":
                            category = Category.BUSS;
                            break;
                        case "tåg":
                            category = Category.TÅG;
                            break;
                        case "tunnelbana":
                            category = Category.TUNNELBANA;
                            break;
                        default:
                            throw new RuntimeException("Someone made a typo");
                    }
                } else {
                    category = Category.NONE;
                }

                List<Location> locList = locationByCategory.get(category.name().toLowerCase());

                for (Location loc : locList){
                    loc.getMarker().setVisible(false);
                    loc.setMarked(false);
                    loc.setUnfolded(false);
                }
            }
        }

        private class HideButton implements ActionListener {
            public void actionPerformed(ActionEvent ave) {

                if (!locationsForLoadSave.isEmpty()) {


                    for (Location location : locationsForLoadSave) {

                        if (location.getMarked()) {

                            location.getMarker().setVisible(false);

                        }
                    }
                }
                }
        }

        private class RemoveButton implements ActionListener {
            public void actionPerformed(ActionEvent ave) {

                if (locationsForLoadSave.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No objects to remove.");
                    return;
                }

                for (Location location : locationsForLoadSave){
                    if (location.getMarked()){
                        markedLocations.add(location);
                    }
                }

                Iterator<Location> iter = locationsForLoadSave.iterator();

                while (iter.hasNext()) {
                    Location loc = iter.next();

                    if (loc.getMarked()) {
                        iter.remove();
                    }
                }

                for (Location loc : markedLocations) {
                    loc.getMarker().setVisible(false);
                }

                Iterator<Location> locList = locationsByPosition.values().iterator();

                while (locList.hasNext()) {
                    if (markedLocations.contains(locList.next())) {
                        locList.remove();

                    }
                }

                for (Map.Entry<String, ArrayList<Location>> me : locationByCategory.entrySet()) {
                    List<Location> locationList = me.getValue();
                    Iterator<Location> locationIterator = locationList.iterator();
                    while (locationIterator.hasNext()) {
                        if (markedLocations.contains(locationIterator.next())) {
                            locationIterator.remove();
                        }
                    }
                }

                for (Map.Entry<String, ArrayList<Location>> me : locationByName.entrySet()) {
                    List<Location> locationList = me.getValue();
                    Iterator<Location> locationIterator = locationList.iterator();
                    while (locationIterator.hasNext()) {
                        if (markedLocations.contains(locationIterator.next())) {
                            locationIterator.remove();
                        }
                    }
                }
                changed = true;
            }

        }

        private class SearchButton implements ActionListener {
            public void actionPerformed(ActionEvent ave) {
                String word = graphicalUI.getWrite().getText();
                List<Location> locList = locationByName.get(word);

                try {
                    for (Location loc : locList) {
                        loc.getMarker().setMarked(true);
                        loc.getMarker().setVisible(true);

                    }
                } catch (NullPointerException e) {

                }
            }
        }

        private class ButtonPressed implements ActionListener{
            public void actionPerformed (ActionEvent ave){
                String value = (String) ((JComboBox) ave.getSource()).getSelectedItem();
                imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                imagePanel.addMouseListener(getMarkerListener(value));

            }
        }
    }



