import java.util.Set;
import java.util.TreeSet;
import java.awt.*;

public class Team {
    private Game game;
    private int score;
	private Set<Territory> land;
	private String name;
	private Color primaryColor;
	private Color secondaryColor;
	private int newRecruits;
	private String shorthand;
	public Team(Game game, String name, Color primaryColor, Color secondaryColor, String c) {
	    this.game = game;
		this.name = name;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		land = new TreeSet<Territory>();
		score = 0;
		newRecruits = 0;
		shorthand = c;
	}
	//Clears all load-specific data
	public void bareTeam() {
	    land = new TreeSet<Territory>();
	    score = 0;
	    newRecruits = 0;
	}
	//Gets shorthand representation "B" for blue and "R" for red
	public String getShorthand() {
	    return shorthand;
	}
	//Calculates how many troops it can draft this turn and updates the label in game.
	public void draftTroops() {
	    double newTroops = 0.0;
	    for (Territory t : land) {
	        newTroops += t.troopContribution();
	    }
	    newRecruits = (int) newTroops;
	    game.setDraftValue(newRecruits);
	}
	//Assigns one of the new recruits to a province. If it was the last one, it ends the draft phase
	public void assignDraftee(Territory assignment) {
	    if (newRecruits > 0) {
	        assignment.addTroops(1);
	        newRecruits--;
	        game.decDraftValue(); 
	    }
	    if (newRecruits == 0) {
	        game.advancePhase();
	    }
	}
	//Adds a new territory to land
	public void addTerritory(Territory t) {
		land.add(t);
	}
	//Annexes a territory, adding it and changing score for both players. Also ends the game if the
	//opponent has no more land left. 
	public void annexTerritory(Territory t) {
	    Team prevOwner = t.getOwner();
	    int terrValue = t.scoreContribution();
	    
	    prevOwner.decScore(terrValue);
	    incScore(terrValue);
	    
	    prevOwner.cedeTerritory(t);
	    t.setOwner(this, 0);
	    land.add(t);
	    
	    if (prevOwner.isDefeated()) {
	        game.declareWinner(this);
	    }
	}
	//Loses territory, is removed from land
	public void cedeTerritory(Territory t) {
	    land.remove(t);
	}
	//The score is incremented by the given amount, and the score tracker is updated. 
	public void incScore(int x) {
	    score += x;
	    game.updateScores();
	}
	//The score is decremented by the given amount, and the score tracker is updated. 
	public void decScore(int x) {
	    score -= x;
	    game.updateScores();
	}
	public int getScore() {
	    return score;
	}
	public void setScore(int x) {
	    score = x;
	    game.updateScores();
	}
	public Color getOuterColor() {
		return primaryColor;
	}
	public Color getInnerColor() {
		return secondaryColor;
	}
	//Returns whether or not this team has no more provinces (indicating the other has won)
	public boolean isDefeated() {
	    return land.size() == 0;
	}
	@Override
	public String toString() {
	    return name;
	}
}
