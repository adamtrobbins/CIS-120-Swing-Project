import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Die extends JComponent{
	private int value;
	private int length;
	public Die(int length) {
		value = 0;
		this.length = length;
	}
	public int getRoll() {
	    return value;
	}
	//Rolls and repaints dice
	public void roll() {
		value = DieRollGenerator.getDieRoll();
		repaint();
	}
	@Override
	public void paintComponent(Graphics gc) {
		super.paintComponent(gc);
		gc.setColor(Color.BLACK);
		gc.drawRect(0, 0, length, length);
		
		gc.setColor(Color.WHITE);
		gc.fillRect(1, 1, length - 1, length - 1);
		
		gc.setColor(Color.BLACK);
		ArrayList<double[]> dotCoords = new ArrayList<double[]>();
		switch(value) {
			case 1: dotCoords.add(new double[] {0.5, 0.5});
					break;
			case 2: dotCoords.add(new double[] {0.25, 0.75});
					dotCoords.add(new double[] {0.75, 0.25});
					break;
			case 3: dotCoords.add(new double[] {0.25, 0.75});
					dotCoords.add(new double[] {0.5, 0.5});
					dotCoords.add(new double[] {0.75, 0.25});
					break;
			case 4: dotCoords.add(new double[] {0.25, 0.25});
					dotCoords.add(new double[] {0.25, 0.75});
					dotCoords.add(new double[] {0.75, 0.25});
					dotCoords.add(new double[] {0.75, 0.75});
					break;
			case 5: dotCoords.add(new double[] {0.25, 0.25});
					dotCoords.add(new double[] {0.25, 0.75});
					dotCoords.add(new double[] {0.5, 0.5});
					dotCoords.add(new double[] {0.75, 0.25});
					dotCoords.add(new double[] {0.75, 0.75});
					break; 
			case 6: dotCoords.add(new double[] {0.25, 0.25});
					dotCoords.add(new double[] {0.25, 0.5});
					dotCoords.add(new double[] {0.25, 0.75});
					dotCoords.add(new double[] {0.75, 0.25});
					dotCoords.add(new double[] {0.75, 0.5});
					dotCoords.add(new double[] {0.75, 0.75});
					break; 			
		}
		drawCircles(gc, dotCoords);
	}
	private void drawCircle(Graphics gc, int x, int y) {
		int r = length / 10;
		gc.fillOval(x - r, y - r, 2 * r, 2 * r);
	}
	//Helper method to draw circles on the dice
	private void drawCircles(Graphics gc, ArrayList<double[]> coords) {
		for(double[] p : coords) {
			drawCircle(gc, (int) (p[0] * length), (int) (p[1] * length));
		}
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(length, length);
	}
}
