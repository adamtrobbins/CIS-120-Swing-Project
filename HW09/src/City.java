import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class City extends Territory {
    public City(Game g, String name, Point[] points) {
        super(g, name, points);
    }
    public City(Game g, String name, Point[] points, Territory t) {
        super(g, name, points, t);
    }
    public City(Game g, String name, Point[] points, Territory t1, Territory t2) {
        super(g, name, points, t1, t2);
    }
    //Cities are harder to attack and consequently when attacked, attackers can use only a maximum
    //of 2 troops, as opposed to the usual 3. This method ensures that battles are processed
    //differently for cities as opposed to normal Provinces to implement this unique mechanic.
    @Override
    public Battle generateBattle(Territory attackerSource, int attackerAmt) {
        return new Battle(game, this, attackerSource, attackerAmt, 2, 2);
    }
    //Cities contribute more troops to the total when drafting, double the amount for a normal
    //province.
    @Override
    public double troopContribution() {
        return 0.30;
    }
    //Cities contribute more score when conquered, triple the amount for a normal province
    @Override
    public int scoreContribution() {
        return 3;
    }
    //Displays a gray rectangle to indicate that a province is a city and distinguish it from a 
    //a generic Province or a Capital
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
        
        gc.setColor(new Color(200, 200, 200));
        gc.fillRect(center.getX() - 3, center.getY() - 12 , 15, 15);
        
        gc.setColor(Color.BLACK);
        gc.setFont(new Font("TimesRoman", Font.BOLD, 15));
        gc.drawString("" + getTroops(), center.getX(), center.getY());
    }
}
