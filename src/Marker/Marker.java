package Marker;


import GraphicalUI.Listener;
import GraphicalUI.GraphicalUI;
import Location.Category;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import GraphicalUI.LocationInfo;


public class Marker extends JComponent {
    private GraphicalUI graphicalUI;
    boolean marked = false;
    boolean folded = false;
    private Category category;

    private int [] xes = {0,25,50};
    private int [] yes = {0,50,0};

    public Marker (int x, int y, Category category, LocationInfo locationInfo, boolean marked){
        this.category = category;
        this.marked = marked;



        LocationInfo lol = new LocationInfo(x, y, "Herro", "Merby");
        this.add(lol);


        setLayout(null);
        setBounds(x,y,50,50);
        setPreferredSize(new Dimension(50,50));
        setMaximumSize(new Dimension(50,50));
        setMinimumSize(new Dimension(50,50));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setMarked(!marked);
            }
        });
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
        repaint();
    }
    public boolean getMarked(){

        return marked;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Color color = Color.BLACK;
        switch (this.category) {
            case BUS:
                color = Color.BLUE;
                break;
            case TRAIN:
                color = Color.RED;
                break;
            case SUBWAY:
                color = Color.GREEN;
                break;
            case NONE:
                color = Color.BLACK;
                break;
        }
        g.setColor(color);
        g.fillPolygon(xes, yes, 3);
        if (marked) {
            g.setColor(Color.RED);
            g.drawRect(0, 0, 49, 49);

        if (folded){
   //         g.drawString("LOL");

        }

        }
    }
}