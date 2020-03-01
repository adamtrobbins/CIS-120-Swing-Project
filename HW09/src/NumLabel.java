import javax.swing.*;

@SuppressWarnings("serial")
public class NumLabel extends JLabel{
    private int num;
    private String text;
    public NumLabel(String text, int num) {
        super(text + num + " ");
        this.text = text;
        this.num = num;
    }
    //Sets numerical value and repaints
    public void setValue(int newNum) {
        num = newNum;
        super.setText(text + newNum + " ");
        repaint();
    }
    //Decrements current value, stopping at 0
    public void decValue() {
        setValue(Math.max(num - 1, 0));
    }
}
