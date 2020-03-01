import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Province extends Territory{
    public Province(Game g, String name, Point[] points) {
        super(g, name, points);
    }
    public Province(Game g, String name, Point[] points, Territory t) {
        super(g, name, points, t);
    }
    public Province(Game g, String name, Point[] points, Territory t1, Territory t2) {
        super(g, name, points, t1, t2);
    }
    //Battles on provinces involve the standard 2-dice defender max and 3 dice-attacker max. This
    //ensures that battles are handled in this way for provinces. This is different for cities and
    //for capitals
    @Override
    public Battle generateBattle(Territory attackerSource, int attackerAmt) {
        return new Battle(game, this, attackerSource, attackerAmt, 2, 3);
    }
    //Provinces provide the fewest troops, at 0.15, meaning that many normal provinces are needed
    //to be able to draft a single whole troop
    @Override
    public double troopContribution() {
        return 0.15;
    }
    //Provinces provide the base amount of score for losing or capturing, 1
    @Override
    public int scoreContribution() {
        return 1;
    }
    //Paints the province with the number of troops in the center. Paints itself differently if 
    //highlighted so the user knows if they have picked it
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
        
        gc.setColor(Color.BLACK);
        gc.setFont(new Font("TimesRoman", Font.BOLD, 15));
        gc.drawString("" + getTroops(), center.getX(), center.getY());
    }
}
