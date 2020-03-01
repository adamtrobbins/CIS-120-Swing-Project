
public class DieRollGenerator {
    private static int[] riggedValues = null;
    private static int pos = 0;
    
    //Gets the next die roll value. If not rigged, returns a random number from 1 - 6. If, rigged
    //returns the next pre-programmed value. This is only used for debugging. 
    public static int getDieRoll() {
        if (riggedValues != null) {
            int out = riggedValues[pos];
            pos++;
            return out;
        }
        return (int) (Math.random() * 6 + 1);
    }
    //Rigs the dice values with the ones in the array. For example, if {4, 2, 3} is passed in, 
    //the next three dice rolls will be 4, then 2, then 3. Any subsequent ones will throw an error
    //likely indicating user error. Only used for debugging.
    public static void rigDice(int[] arr) {
        pos = 0;
        riggedValues = arr;
    }
}
