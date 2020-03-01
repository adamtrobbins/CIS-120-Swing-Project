import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Capital extends Territory{
    private Team trueOwner;
    public Capital(Game g, String name, Point[] points) {
        super(g, name, points);
    }
    public Capital(Game g, String name, Point[] points, Territory t) {
        super(g, name, points, t);
    }
    public Capital(Game g, String name, Point[] points, Territory t1, Territory t2) {
        super(g, name, points, t1, t2);
    }
    //Sets the hard-coded true owner, used to determine how this province is attacked (see below)
    public void setTrueOwner(Team trueOwner) {
        this.trueOwner = trueOwner;
    }
    //Determines if is currently owned by the "true" owner
    private boolean ownedByTrueOwner() {
        return getOwner() == trueOwner;
    }
    /**
     * Capital cities have a "true" owner (hard coded at the start of the game: in every game West 
     * USA is the blue team's true capital and Russia is the red team's true capital). If owned by 
     * the true owner, capitals can defend with more troops than usual. For example, when the red
     * team consequently is trying to defend Russia it gets to defend with 3 troops (if it has that
     * many) instead of the usual 2. But if the blue team conquers the province, it won't get 
     * the same bonus because it isn't the "true owner. This makes attacking capitals owned by the
     * true owner difficult. 
     */
    @Override
    public Battle generateBattle(Territory attackerSource, int attackerAmt) {
        if (ownedByTrueOwner()) {
            return new Battle(game, this, attackerSource, attackerAmt, 3, 3);
        }
        return new Battle(game, this, attackerSource, attackerAmt, 2, 3);
    }
    /**
     * Capital cities provide a base +0.30 troops, the same as a city, double that of a territory
     * However, if and only if  a capital is owned by its true owner, it grants an additional full 
     * troop. This makes keeping your true capital especially important. 
     */
    @Override
    public double troopContribution() {
        double out = 0.30;
        if (ownedByTrueOwner()) {
            out += 1.0;
        }
        return out;
    }
    //Capital cities give three times the score than for a normal city, making them 9 times more 
    //valuable to capture than a normal province.
    @Override
    public int scoreContribution() {
        return 9;
    }
    //Displays differently in order to distinguish capitals from cities and from generic territories
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);

        gc.setColor(outerColor);
        outer.draw(gc);
        
        if (highlighted) {
            gc.setColor(Color.YELLOW);
            highlight.draw(gc);
        }
        
        gc.setColor(innerColor);
        inner.draw(gc);
        
        gc.setColor(outerColor);
        gc.fillRect(center.getX() - 12, center.getY() - 20 , 30, 30);
        
        gc.setColor(new Color(200, 200, 200));
        gc.fillRect(center.getX() - 12 + 5, center.getY() - 20 + 5, 21, 21);
        
        gc.setColor(Color.BLACK);
        gc.setFont(new Font("TimesRoman", Font.BOLD, 15));
        gc.drawString("" + getTroops(), center.getX(), center.getY());
    }
}
