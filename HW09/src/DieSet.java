import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DieSet extends JComponent {
    private final static int DIE_LENGTH = 75;
    private int n;
    private Die[] dice;
    public DieSet(int n) {
        this.n = n;
        if (n < 1 || n > 3) {
            throw new IllegalArgumentException("Bad n is " + n);
        }
        dice = new Die[n];
        for (int i = 0; i < n; i++) {
            dice[i] = new Die(DIE_LENGTH);
        }
    }
    //Rolls all of the dice and sorts them from greatest to least
    public void rollAndSort() {
        for (Die d : dice) {
            d.roll();
        }
        for (int i = 0; i < n; i++) {
            int maxVal = dice[i].getRoll();
            int index = i;
            for (int j = i + 1; j < n; j++) {
                int x = dice[j].getRoll();
                if(x > maxVal) {
                    maxVal = x;
                    index = j;
                }
            }
            Die temp = dice[i];
            dice[i] = dice[index];
            dice[index] = temp;
        }
    }
    public int[] getVals() {
        int[] out = new int[n];
        for (int i = 0; i < n; i++) {
            out[i] = dice[i].getRoll();
        }
        return out;
    }
    public int getNum() {
        return n;
    }
    @Override
    public void paintComponent(Graphics gc) {
        super.paintComponent(gc);
        for (Die d : dice) {
            d.paintComponent(gc);
            gc.translate(DIE_LENGTH, 0);
        }
        gc.translate(-1 * n * DIE_LENGTH, 0);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DIE_LENGTH * n, DIE_LENGTH);
    }
}
