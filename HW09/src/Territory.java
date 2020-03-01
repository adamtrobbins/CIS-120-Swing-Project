import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public abstract class Territory extends JComponent implements Comparable<Territory> {
    private static final int PROV_LEN = 200;
    protected Game game;
	private Set<Territory> neighbors;
	private int troops;
	private Team owner;
	private String name;
	
    protected boolean highlighted;
    protected Pgon highlight;
    protected Pgon outer;
    protected Pgon inner;
    protected Point center;
    protected Color outerColor;
    protected Color innerColor;
	public Territory(Game game) {
	    this.game = game;
		neighbors = new TreeSet<Territory>();
		troops = 2;
	}
	public Territory(Game g, String name) {
	    this(g);
	    this.name = name;
	}
	public Territory(Game g, String name, Point[] points) {
	    this(g, name);
	    
        outer = new Pgon(points);
        inner = outer.shiftIn(5);
        highlight = outer.shiftIn(2);
        highlighted = false;
        center = outer.center();
	}
	public Territory(Game g, String name, Point[] points, Territory t) {
	    this(g, name, points);
	    addNeighbor(t);
	}
    public Territory(Game g, String name, Point[] points, Territory t1, Territory t2) {
        this(g, name, points);
        addNeighbor(t1);
        addNeighbor(t2);
    }
    //Reads a line from an input file to determine its owner and the number of troops
    public void setState(String line) throws Game.FormatException {
        String[] data = line.split(";");
        
        if (data.length != 2) {
            throw new Game.FormatException("Line must be two values separated by a semicolon: " 
        + line);
        }
        
        int troops = Integer.parseInt(data[1]);
        
        switch(data[0]) {
            case "B" : setOwner(game.getBlue(), troops);
                       break;
            case "R" : setOwner(game.getRed(), troops);
                       break;
            default: throw new Game.FormatException("Bad first character: " + data[0]);
        }
    }
    //Returns the number of troops this province contributes to the total draftable number of troops
    //These values are all summed to calculate draft amounts. 
    public abstract double troopContribution();
    //Returns the score gained when a territory is conquered, or lost when lost
    public abstract int scoreContribution();
    //Toggles the highlight, switching it from its current state to the other state
    public void toggleHighlight() {
        highlighted = !highlighted;
    }
    //Turns off the highlight
    public void resetHighlight() {
        if (highlighted) {
            toggleHighlight();
        }
    }
    //Uses the list of all territories, passed in as a parameter, to determine to which they are
    //adjacent (purely geometrically)
	public void determineNeighbors(List<Territory> terrs) {
	    for(Territory t : terrs) {
	        if (this != t && borders(t)) {
	            addNeighbor(t);
	        }
	    }
	}
	//Causes the troops in this province to attack the target province, starting a battle
	public void attack(Territory target) {
	    if (!isNeighbor(target) || target.getOwner() == owner) {
	        throw new IllegalArgumentException();
	    }
	    
	    target.generateBattle(this, removeTroops(troops - 1)).process();
	    revalidate();
	    repaint();
	}
	//Returns a battle to be used in the attack method. This depends on the province being attacked
	//and allows for different functionality for Provinces, Cities, and Capitals
	public abstract Battle generateBattle(Territory attackerSource, int attackerAmt);
	public Team getOwner() {
		return owner;
	}
	public int getTroops() {
	    return troops;
	}
	//Adds troops to the province
	public int addTroops(int reinforcements) {
	    if (reinforcements < 0) {
	        throw new IllegalArgumentException();
	    }
	    troops += reinforcements;
	    return reinforcements;
	}
	//Removes troops from the province
	public int removeTroops(int departures) {
	    troops = Math.max(0, troops - departures);
	    return departures;
	}
	//Resets troop values
	public void clearTroops() {
	    troops = 0;
	}
	//Sets the owner and with it the colors
	public void setOwner(Team p, int troops) {
		owner = p;
	    owner.addTerritory(this);
	    outerColor = owner.getOuterColor();
	    innerColor = owner.getInnerColor();
	    this.troops = troops;
	}
	//Adds a province to indicate that they are neighbors
	private void addNeighbor(Territory t) {
	    neighbors.add(t);
	    if (!t.isNeighbor(this)) {
	        t.addNeighbor(this);
	    }
	}
	//Returns true iff the provinces are neighbors
	public boolean isNeighbor(Territory t) {
		return neighbors.contains(t);
	}
	//Helper method to allow doubles to be used to simplify math
	private int rel(double x) {
		return (int) (x * PROV_LEN);
	}
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
    }
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(rel(1), rel(1));
	}
	//Returns true iff the polygon represented borders with the polygon of the other territory
    public boolean borders(Territory t) {
        return outer.sharesEdge(t.getOuter());
    }
    public Pgon getOuter() {
        return outer;
    }
    //Returns true iff the given point is within the area defined by the territory
    public boolean within(int xm, int ym) {
        return outer.isWithin(new Point(xm, ym));
    }
    //Uses the names of the two provinces to compare with each other (for TreeSets)
	public int compareTo(Territory t) {
	    return t.toString().compareTo(toString());
	}
	@Override
	public String toString() {
	    return name;
	}
}
