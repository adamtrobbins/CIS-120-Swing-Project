
public class Battle {
    private Game game;
    private BattleDice dice;
	private Territory battleground;
	private Territory source;
	private int attackers;
	private int defenders;
	public Battle(Game g, Territory battleground, Territory source, int attackers, int defenderMax,
	        int attackerMax) {
	    game = g;
		this.battleground = battleground;
		this.attackers = attackers;
		this.defenders = battleground.getTroops();
		this.source = source;
		
		game.reconfigureDice(Math.min(defenderMax, defenders), Math.min(attackerMax, attackers));
		dice = game.getDice();
	}
	public Battle(Game g, Territory battleground, Territory source, int attackers) {
	    this(g, battleground, source, attackers, 2, 3);
	}
	//Runs the course of the battle: calculates casualties, determine who wins, and changes terr
	//owner if necesssary
	public void process() {
		if (engage()) {
		    battleground.clearTroops();
		    source.getOwner().annexTerritory(battleground);
			battleground.addTroops(attackers);
		} else {
		    source.addTroops(attackers);

		}
		source.repaint();
		battleground.repaint();
	}
	//Rolls both sets of dice
	private void roll() {
	    dice.rollDefender();
	    dice.rollAttacker();
	}
	//Returns whether or not attacker wins
	private boolean engage() {
	    roll();
	    int[] losses = dice.calcLosses();
        battleground.removeTroops(losses[0]);
	    defenders -= losses[0];
	    attackers -= losses[1];
	    return defenders == 0;
	}
}
