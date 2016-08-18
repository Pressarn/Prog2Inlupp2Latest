package Marker;

import Location.Category;
import Location.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import GraphicalUI.LocationInfo;



public class Marker extends JComponent {

    private Category category;
    private Location location;
    private LocationInfo locationInfo;
    private int x;
    private int y;
    private int [] xes = {0,25,50};
    private int [] yes = {0,50,0};

    public Marker (int x, int y, Category category, LocationInfo locationInfo, Location location){
        this.category = category;
        this.location = location;
        this.locationInfo = locationInfo;
        this.x = x;
        this.y = y;

        repaint();
        setLayout(null);
        setBounds(x,y,50,50);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {

                if(SwingUtilities.isLeftMouseButton(event)){

                    if(!location.getMarked() && !location.getUnfolded()){
                        location.setMarked(true);
                    }
                    else if (location.getMarked() && !location.getUnfolded()){
                        location.setMarked(false);
                    }
                    else if (!location.getMarked() && location.getUnfolded()){

                        location.setMarked(true);
                    }
                    else if (location.getMarked() && location.getUnfolded()){

                        location.setMarked(false);
                    }
                    repaint();
                }


                else if (SwingUtilities.isRightMouseButton(event)) {

                    if (!location.getUnfolded()) {
                        location.setUnfolded(true);

                    } else if (location.getUnfolded()) {
                        location.setUnfolded(false);

                    }
                    repaint();
                }
                }

        } );}


    public void setMarked(boolean marked) {
        location.setMarked(marked);
        repaint();

    }

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        String nameAndDescription =  "" +locationInfo.getName() + "\r\n" + locationInfo.getDescription();
        Color color = Color.BLACK;
        switch (this.category) {
            case BUSS:
                color = Color.RED;
                break;
            case TÅG:
                color = Color.GREEN;
                break;
            case TUNNELBANA:
                color = Color.BLUE;
                break;
            case NONE:
                color = Color.BLACK;
                break;
        }

        if(!location.getUnfolded() && !location.getMarked()) {
            setBounds(x,y,50,50);
            g.setColor(color);
            g.fillPolygon(xes, yes, 3);
        }
        else if (location.getMarked() && !location.getUnfolded()) {
            setBounds(x,y,50,50);
            g.setColor(color);
            g.fillPolygon(xes, yes, 3);
            g.setColor(Color.RED);
            g.drawRect(0, 0, 49, 49);
            setVisible(true);
       }
        else if (location.getUnfolded() && !location.getMarked()){
            setBounds(x,y,50,50);
            drawString(g, locationInfo.getName() + "\n" + locationInfo.getDescription(), 0, 0 );


        }
        else if (location.getUnfolded() && location.getMarked()){
            setBounds(x,y,100,100);
            drawString(g, locationInfo.getName() + "\n" + locationInfo.getDescription(), 0, 0 );

            g.setColor(Color.RED);
            g.drawRect(0,0, 100, 100);
        }

    }
}