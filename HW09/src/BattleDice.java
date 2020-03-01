import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class BattleDice extends JComponent{
    private DieSet defenderDice;
    private DieSet attackerDice;
    private int len;
    public BattleDice(int numDefenderDice, int numAttackerDice) {
        defenderDice = new DieSet(numDefenderDice);
        attackerDice = new DieSet(numAttackerDice);
        len = (int) defenderDice.getPreferredSize().getHeight();
    }
    //Changes the size of the die sets to new values
    public void reconfigure(int nd, int na) {
        defenderDice = new DieSet(nd);
        attackerDice = new DieSet(na);
        this.repaint();
    }
    //Rolls and repaints attacker dice
    public void rollAttacker() {
        attackerDice.rollAndSort();
        repaint();
    }
    //Rolls and repaints defender dice
    public void rollDefender() {
        defenderDice.rollAndSort();
        repaint();
    }
    //Returns {defender losses, attacker losses}
    public int[] calcLosses() {
        int[] out = {0, 0};
        int n = Math.min(defenderDice.getNum(), attackerDice.getNum());
        int[] defenderRolls = defenderDice.getVals();
        int[] attackerRolls = attackerDice.getVals();
        for(int i = 0; i < n; i++) {
            if (attackerRolls[i] > defenderRolls[i]) {
                out[0]++;
            } else {
                out[1]++;
            }
        }
        return out;
    }
    public int[][] getValues() {
        return new int[][] {defenderDice.getVals(), attackerDice.getVals()};
    }
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        gc.setFont(new Font("TimesRoman", Font.BOLD, 20));
        gc.drawString("Defender Dice", 0, len + 22);
        defenderDice.paintComponent(gc);
        gc.translate(0, 2 * len);
        attackerDice.paintComponent(gc);
        gc.drawString("Attacker Dice", 0, -5);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(len * 4, len * 3 + 50);
    }
}
