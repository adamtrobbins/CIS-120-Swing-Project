import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class WorldMap extends JComponent {
    private Game game;
    private int selected;
    private int width;
    private int height;
    private Territory terr0;
    private Territory terr1;
    private List<Territory> provinces;
    private Iterator<Territory> iter;
    private Team winner;
    public WorldMap(Game game, int w, int h) {
        this.game = game;
        width = w;
        height = h;
        provinces = new LinkedList<Territory>();
        iter = provinces.iterator();
        winner = null;
    }
    /* When reading files, the iterator is reset so that next() will get the first value in the list
     * and so on. The order of the territories in provinces and in the input text file are the same,
     * so as it iterates it is always exactly aligned, assigning the state to the relevant province.
     * The method itself returns true iff there are no more provinces left in provinces, telling the
     * reader to stop. 
     * 
     */
    public boolean setTerrState(String line) throws Game.FormatException {
        iter.next().setState(line);
        return !iter.hasNext();
    }
    //Resets the iterator back to the beginning
    public void resetIterator() {
        iter = provinces.iterator();
    }
    //Returns a writable string which is used to load save data for every province
    public String getData() {
        String out = "";
        for (Territory t : provinces) {
            out += t.getOwner().getShorthand() + ";";
            out += t.getTroops() + "\r\n";
        }
        return out;
    }
    //Removes all current selections
    public void deselect() {
        if (terr0 != null) {
            terr0.toggleHighlight();
        }
        if (terr1 != null) {
            terr1.toggleHighlight();
        }
        terr0 = null;
        terr1 = null;
        selected = 0;
    }
    //Selects a given territory, making it terr0 if none are selected and terr1 if one has been
    //selected
    private void select(Territory t) {
        if (terr0 == null) {
            terr0 = t;
            terr0.toggleHighlight();
        } else if (terr1 == null) {
            terr1 = t;
            terr1.toggleHighlight();
        } else {
            return;
        }
        selected++;  
    }
    //Adds a territory to provinces
    public void addTerr(Territory terr) {
        provinces.add(terr);
    }
    //Compares every territory with every other to determine which are neighboring
    public void determineAllNeighbors() {
        for (Territory t : provinces) {
            t.determineNeighbors(provinces);
        }
    }
    //Finds out which territory corresponds to a given location on the map
    private Territory findTerritory(int x, int y) {
        for (Territory t : provinces) {
            if (t.within(x, y)) {
                return t;
            }
        }
        return null;
    }
    //Does the appropriate action given a mouseclick at location x,y
    //Handles most of core game logic
    public void handleClick(int x, int y) {
        Team player = null;
        Territory t = findTerritory(x, y);
        if (t == null) {
            return;
        }
        Team owner = t.getOwner();
        
        switch (game.phase().getTeamNum()) {
            case 0 : player = game.getBlue();
                     break;
            case 1 : player = game.getRed();
                     break;
        }
        switch (game.phase()) {
            case OFFENSE_BLUE: case OFFENSE_RED :
                switch (selected) {
                    case 0 : 
                        if (owner == player && t.getTroops() > 1) {
                            select(t);
                        }
                        break;
                    case 1 :
                        if (owner == player) {
                            deselect();
                            select(t);
                        }
                        if (owner != player && t.isNeighbor(terr0) && terr0.getTroops() > 1) {
                            select(t);
                            terr0.attack(terr1);
                            deselect();
                        }
                        break;
                }
                break;
            case DRAFT_BLUE : case DRAFT_RED : 
                if (player == owner) {
                    owner.assignDraftee(t);
                }
        }
    }
    //Deselects and dehighlights all territories
    public void clearHighlights() {
        deselect();
        for (Territory t : provinces) {
            t.resetHighlight();
        }
        repaint();
    }
    //Finds territory with given name. Used for debugging. 
    public Territory getTerritory(String name) {
        for (Territory t : provinces) {
            if (t.toString().equals(name)) {
                return t;
            }
        }
        return null;
    }
    //Paints the victory message on the map
    public void announceWinner(Team t) {
        winner = t;
        repaint();
    }
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        gc.setColor(Color.GRAY);
        gc.fillRect(0, 0, width, height);
        for (Territory t : provinces) {
            t.paintComponent(gc);
        }
        
        int inset = 100;
        
        if (winner != null) {
            gc.translate(inset * 2, inset);
            gc.setColor(Color.BLACK);
            gc.fillRect(0, 0, width - inset * 4, height - inset * 2);
            
            gc.translate(100, 175);
            gc.setColor(Color.WHITE);
            gc.setFont(new Font("TimesRoman", Font.BOLD, 80));
            gc.drawString(winner + " WINS!", 0, 0);
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}
