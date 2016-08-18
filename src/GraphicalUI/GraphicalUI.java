package GraphicalUI;

import javax.swing.*;
import java.awt.*;


public class GraphicalUI extends JFrame {

    private JTextField write = new JTextField("Search", 10);
    private String [] category = {"Buss", "Tåg               ", "Tunnelbana"};
    private JList <String> categories = new JList<> (category);
    private Listener listener = new Listener(this);

    public GraphicalUI() {
        super("Register");

        JMenuBar mb = new JMenuBar();
        JMenu iMen = new JMenu ("Archive");
        setJMenuBar(mb);
        mb.add(iMen);
        JMenuItem newMap = new JMenuItem ("New Map");
        iMen.add(newMap);
        newMap.addActionListener(listener.getOpenListener());
        JMenuItem loadPlaces = new JMenuItem ("Load Places");
        iMen.add(loadPlaces);
        loadPlaces.addActionListener(listener.getLoadListener());
        JMenuItem save = new JMenuItem ("Save");
        iMen.add(save);
        save.addActionListener(listener.getSaveListener());
        JMenuItem exit = new JMenuItem ("Exit");
        iMen.add(exit);
        exit.addActionListener(listener.getExitListener());

        setLayout(new BorderLayout());
        JLabel labelTop = new JLabel("Top", SwingConstants.CENTER);
        add(labelTop, BorderLayout.NORTH);



        JPanel east = new JPanel();
        east.setLayout(new BorderLayout());

        JPanel eastside = new JPanel();
        eastside.setLayout(new BoxLayout(eastside, BoxLayout.Y_AXIS));
        east.add(eastside, BorderLayout.EAST);

        JLabel kategorier = new JLabel("Categories        ");
        eastside.add(kategorier);
        kategorier.setAlignmentX(RIGHT_ALIGNMENT);


        categories.setMinimumSize(new Dimension(150,450));
        categories.setAlignmentX(RIGHT_ALIGNMENT);
        eastside.add(categories);
        categories.addListSelectionListener(listener.getCategoryListener());

        JButton hideCategoryButton = new JButton("Hide category");
        hideCategoryButton.setAlignmentX(RIGHT_ALIGNMENT);
        eastside.add(hideCategoryButton);
        hideCategoryButton.addActionListener(listener.getHideCategory());


        eastside.setMinimumSize(new Dimension (150, 450));

        add(eastside, BorderLayout.EAST);

        JPanel north = new JPanel();
        String[] choice = {"", "Named", "Described"};
        JComboBox<String> comboChoice = new JComboBox<>(choice);
        north.add(new JLabel("New:"));
        north.add(comboChoice);
        comboChoice.addActionListener(listener.getButtonPressed());

        north.add(write);

        JButton searchButton = new JButton ("Search");
        north.add(searchButton);
        searchButton.addActionListener(listener.getSearchButton());

        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(listener.getHideButton());
        north.add(hideButton);

        JButton removeButton = new JButton ("Remove");
        north.add(removeButton);
        removeButton.addActionListener(listener.getRemoveButton());

        JButton whatButton = new JButton("What is here?");
        north.add(whatButton);
        whatButton.addActionListener(listener.getWhatListener());

        add(north, BorderLayout.NORTH);
        addWindowListener(listener.getExitListener());

        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public JTextField getWrite(){return write;}

    public String getJList(){
        return categories.getSelectedValue();
    }

    public boolean getIsSelected(){
        return !categories.isSelectionEmpty();
    }

    public static void main(String[] args) {
        new GraphicalUI();
    }
}
