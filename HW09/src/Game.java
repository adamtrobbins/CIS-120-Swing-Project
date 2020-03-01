import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game implements Runnable{
    private String dataFile;
    private WorldMap map;
    private Team blue;
    private Team red;
    private GamePhase phase;
    private BattleDice gameDice;
    private NumLabel draftLabel;
    private NumLabel scorecardBlue;
    private NumLabel scorecardRed;
    private JLabel phaseLabel;
    private JFrame frame;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game("templateData.txt"));
    }
    public Game(String dataFile) {
        this.dataFile = dataFile;
        
        blue = new Team(this, "Blue", Color.BLUE, Color.CYAN, "B");
        red = new Team(this, "Red", Color.RED, Color.PINK, "R");
        
        gameDice = new BattleDice(2, 3);
        map = new WorldMap(this, 1000, 500);
        draftLabel = new NumLabel("New Troops: ", 0);
        phaseLabel = new JLabel("");
        frame = new JFrame("Game");
        
        scorecardBlue = new NumLabel("Team Blue Score: ", blue.getScore());
        scorecardRed = new NumLabel("Team Red Score: ", red.getScore());
    }
    public Game() {
        this("templateData.txt");
    }
    //Updates the draft label to a given value
    public void setDraftValue(int v) {
        draftLabel.setValue(v);
    }
    //Decrements the value on the draft label
    public void decDraftValue() {
        draftLabel.decValue();
    }
    public void reconfigureDice(int a, int b) {
        gameDice.reconfigure(a, b);
    }
    public void updateScores() { 
        scorecardBlue.setValue(blue.getScore());
        scorecardRed.setValue(red.getScore());
    }
    public void updatePhaseLabel() {
        phaseLabel.setText(phase.toString());
        phaseLabel.repaint();
    }
    public Team getRed() {
        return red;
    }
    public Team getBlue() {
        return blue;
    }
    public WorldMap getMap() {
        return map;
    }
    //Shows winner graphic on the map
    public void declareWinner(Team t) {
        map.announceWinner(t);
    }
    public BattleDice getDice() {
        return gameDice;
    }
    //Moves to next phase, doing appropriate actions
    public void advancePhase() {
        phase = phase.advance();
        map.clearHighlights();
        updatePhaseLabel();
        switch(phase) {
            case DRAFT_BLUE : blue.draftTroops();
                              break;
            case DRAFT_RED : red.draftTroops();
                             break;
            default : return;
        }
    }
    //Changes the turn and does the appropriate actions
    public void endTurn() {
        switch (phase) {
            case OFFENSE_BLUE : case OFFENSE_RED : 
                advancePhase(); 
                break;
            case DRAFT_BLUE : case DRAFT_RED : 
                advancePhase();
                advancePhase();
                break;
        }
        updatePhaseLabel();
    }
    //Returns what phase it is
    public GamePhase phase() {
        return phase;
    }
    //Helper method for writeSave() which uses current phase and scores
    private String firstLine() {
        String out = "";
        switch (phase) {
            case DRAFT_BLUE: case OFFENSE_BLUE: out += "B;";
                                                break;
            case DRAFT_RED: case OFFENSE_RED: out += "R;";
                                              break;
        }
        out += blue.getScore() + ";" + red.getScore() + "\r\n";
        return out;
    }
    //Writes save to the save file
    public void writeSave() {
        writeSave("saveFile.txt");
    }
    //Writes a save to the given file with all of the game data correctly formatted. The data in 
    //that file can then be used to load a save at a later time. 
    private void writeSave(String fileName) {
        endTurn();
        try {
            Writer w = new FileWriter("files/" + fileName);
            BufferedWriter bw = new BufferedWriter(w);
            
            bw.write(firstLine());
            bw.write(map.getData());
            
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    //Lays out all of the territories, adds them to the map, and initializes which territories are
    //adjacent to each other
    public void createMap() {
        //Point Defines
        //South America
        Point sa = new Point(200, 450);
        Point sb = new Point(200, 350);
        Point sc = new Point(250, 375);
        Point sd = new Point(175, 275);
        Point se = new Point(200, 250);
        Point sf = new Point(225, 325);
        Point sg = new Point(225, 275);
        Point sh = new Point(275, 300);
        
        //North America
        Point na = new Point(125, 175);
        Point nb = new Point(175, 175);
        Point nc = new Point(275, 200);
        Point nd = new Point(250, 125);
        Point ne = new Point(275, 75);
        //Point nf = new Point(125, 175);
        Point ng = new Point(175, 125);
        Point nh = new Point(100, 125);
        Point ni = new Point(125, 75);
        Point nj = new Point(50, 50);
        Point nk = new Point(100, 50);
        Point nl = new Point(225, 50);
        Point nm = new Point(200, 75);
        //Point nn = new Point(100, 50);
        Point no = new Point(235, 50);
        Point np = new Point(310, 25);
        Point nq = new Point(310, 85);
        
        //Africa
        Point fa = new Point(325, 300);
        Point fb = new Point(350, 225);
        Point fc = new Point(425, 225);
        Point fd = new Point(475, 250);
        Point fe = new Point(425, 275);
        Point ff = new Point(400, 325);
        Point fg = new Point(415, 375);
        Point fh = new Point(435, 425);
        Point fi = new Point(475, 400);
        Point fj = new Point(475, 350);
        Point fk = new Point(500, 300);
        Point fl = new Point(500, 345);
        Point fm = new Point(550, 325);
        Point fn = new Point(500, 400);
        
        //Europe
        Point ea = new Point(325, 175);
        Point eb = new Point(375, 125);
        Point ec = new Point(425, 100);
        Point ed = new Point(400, 75);
        Point ee = new Point(450, 50);
        Point ef = new Point(550, 50);
        Point eg = new Point(475, 170);
        Point eh = new Point(425, 175);
        Point ei = new Point(400, 175);
        Point ej = new Point(325, 125);
        Point ek = new Point(365, 115);
        Point el = new Point(325, 75);
        Point em = new Point(385, 75);
        Point en = new Point(385, 50);
        Point eo = new Point(335, 50);
        Point ep = new Point(335, 75);
        Point eq = new Point(525, 125);
        
        //Asia
        Point aa = new Point(550, 250);
        Point ab = new Point(600, 325);
        Point ac = new Point(650, 250);
        Point ad = new Point(750, 325);
        Point ae = new Point(750, 250);
        Point af = new Point(800, 200);
        Point ag = new Point(800, 100);
        Point ah = new Point(900, 50);
        Point ai = new Point(775, 25);
        Point aj = new Point(650, 125);
        //Point ak = new Point(365, 115);
        Point al = new Point(600, 175);
        Point am = new Point(850, 100);
        Point an = new Point(850, 150);
        Point ao = new Point(825, 185);
        Point ap = new Point(825, 125);
        
        //Oceania
        Point oa = new Point(650, 350);
        Point ob = new Point(700, 320);
        Point oc = new Point(725, 350);
        Point od = new Point(700, 375);
        Point oe = new Point(745, 345);
        Point of = new Point(795, 345);
        Point og = new Point(820, 375);
        Point oh = new Point(770, 375);
        Point oi = new Point(700, 425);
        Point oj = new Point(700, 475);
        Point ok = new Point(750, 465);
        Point ol = new Point(800, 475);
        Point om = new Point(800, 425);
        Point on = new Point(750, 400);
        
        Territory argentina = new Province(this,"Argentina", new Point[] {sa, sb, sc});
        Territory peru = new Province(this, "Peru", new Point[] {sb, sd, sf, sc});
        Territory colombia = new Province(this, "Colombia", new Point[] {sd, se, sg, sf});
        Territory brazil = new City(this, "Brazil", new Point[] {sc, sf, sg, sh});
        
        map.addTerr(argentina);
        map.addTerr(peru);
        map.addTerr(colombia);
        map.addTerr(brazil);
        
        Territory greenland = new Province(this, "Greenland", new Point[] {no, nq, np});
        Territory mexico = new Province(this, "Mexico", new Point[] {se, na, nb},
                colombia);
        Capital westUsa = new Capital(this, "West USA", new Point[] {na, nb, ng, nh});
        westUsa.setTrueOwner(blue);
        Territory eastUsa = new City(this, "East USA", new Point[] {nb, ng, nd, nc});
        Territory quebec = new Province(this, "Quebec", new Point[] {ng, nm, ne, nd}, 
                greenland);
        Territory westCanada = new City(this, "West Canada", new Point[] {nh, ni, nm, ng});
        Territory alaska = new Province(this, "Alaska", new Point[] {nj, nk, ni, nh});
        Territory nunavut = new Province(this, "Nunavut", new Point[] {ni, nk, nl, nm},
                greenland);
        
        
        map.addTerr(mexico);
        map.addTerr(westUsa);
        map.addTerr(eastUsa);
        map.addTerr(quebec);
        map.addTerr(westCanada);
        map.addTerr(alaska);
        map.addTerr(nunavut);
        map.addTerr(greenland);
        
        Territory madagascar = new Province(this, "Madagascar", new Point[] {fl, fm, fn});
        Territory westAfrica = new Province(this, "West Africa", new Point[] {fa, fb, fc, fe, ff}, 
                brazil);
        Territory egypt = new Province(this, "Egypt", new Point[] {fc, fe, fd});
        Territory eastAfrica = new Province(this, "East Africa", new Point[] {ff, fe, fd, fk, fj},
                madagascar);
        Territory congo = new Province(this, "Congo", new Point[] {ff, fg, fj});
        Territory southAfrica = new City(this, "South Africa", new Point[] {fg, fj, fi, fh},
                madagascar);
        
        map.addTerr(westAfrica);
        map.addTerr(egypt);
        map.addTerr(eastAfrica);
        map.addTerr(congo);
        map.addTerr(southAfrica);
        map.addTerr(madagascar);
        
        Territory britain = new City(this, "Britain", new Point[] {ej, el, ek});
        Territory iceland = new Province(this, "Iceland", new Point[] {ep, eo, en, em},
                greenland, britain);
        Territory westEurope = new City(this, "West Europe", new Point[] {fb, ea, eb, ei},
                britain, westAfrica);
        Territory northEurope = new City(this, "North Europe", new Point[] {eb, ec, eh, ei},
                britain);
        Territory southEurope = new City(this, "South Europe", new Point[] {ei, fc, eg, eh},
                westAfrica, egypt);
        Territory scandinavia = new Province(this, "Scandinavia", new Point[] {ec, ed, ee}, 
                iceland);
        Capital russia = new Capital(this, "Russia", new Point[] {ee, ef, eq, eg, eh, ec});
        russia.setTrueOwner(red);
        
        map.addTerr(westEurope);
        map.addTerr(northEurope);
        map.addTerr(southEurope);
        map.addTerr(scandinavia);
        map.addTerr(russia);
        map.addTerr(britain);
        map.addTerr(iceland);
        
        Territory middleEast = new Province(this, "Middle East", new Point[] {eg, fd, fk, aa},
                egypt);
        Territory india = new City(this, "India", new Point[] {aa, ab, ac, al});
        Territory siam = new Province(this, "Siam", new Point[] {ac, ae, ad});
        Territory china = new City(this, "China", new Point[] {al, aj, af, ae, ac});
        Territory manchuria = new City(this, "Manchuria", new Point[] {aj, ag, af});
        Territory kamchatka = new Province(this, "Kamchatka", new Point[] {aj, ai, ah, ag});
        Territory siberia = new Province(this, "Siberia", new Point[] {ef, al, aj, ai});
        Territory ural = new Province(this, "Ural", new Point[] {ef, eq, al});
        Territory centralAsia = new Province(this, "Central Asia", new Point[] {eq, aa, al});
        Territory afghanistan = new Province(this, "Afghanistan", new Point[] {eg, aa, eq});
        Territory japan = new City(this, "Japan", new Point[] {ap, ao, an, am},
                kamchatka, manchuria);
        
        map.addTerr(middleEast);
        map.addTerr(india);
        map.addTerr(siam);
        map.addTerr(china);
        map.addTerr(manchuria);
        map.addTerr(kamchatka);
        map.addTerr(siberia);
        map.addTerr(ural);
        map.addTerr(centralAsia);
        map.addTerr(afghanistan);
        map.addTerr(japan);
        
        Territory indonesia = new Province(this, "Indonesia", new Point[] {oa, ob, oc, od},
                siam);
        Territory newGuinea = new Province(this, "New Guinea", new Point[] {oe, of, og, oh},
                indonesia);
        Territory westAustralia = new Province(this, "West Australia", new Point[] {oi, oj, ok, on},
                indonesia);
        Territory eastAustralia = new City(this, "East Australia", new Point[] {on, om, ol, ok},
                newGuinea);
        
        map.addTerr(indonesia);
        map.addTerr(newGuinea);
        map.addTerr(westAustralia);
        map.addTerr(eastAustralia);
        
        map.determineAllNeighbors();
    }
    @SuppressWarnings("serial")
    public static class FormatException extends Exception {
        public FormatException(String msg) {
            super(msg);
        }
    }
    public void loadSave() {
        loadSave("saveFile.txt");
    }
    //Loads the game data represented in the given text file
    public void loadSave(String saveFile) {
        map.resetIterator();
        map.deselect();
        blue.bareTeam();
        red.bareTeam();
        
        Reader r = null;
        BufferedReader br = null;
        
        try {
            r = new FileReader("files/" + saveFile);
            br = new BufferedReader(r);
            
            String line = "";
            boolean first = true;
            boolean finished = false; 
            
            while ((line = br.readLine()) != null) {
                if (first) {
                    String[] data = line.split(";");
                    if (data.length != 3) {
                        throw new FormatException("Invalid First Line: " + line);
                    }
                    switch (data[0]) {
                        case "B" : phase = GamePhase.DRAFT_BLUE;
                                   break;
                        case "R" : phase = GamePhase.DRAFT_RED; 
                                   break;
                        default : throw new FormatException("Must be B or R: " + line);
                    }
                    blue.setScore(Integer.parseInt(data[1]));
                    red.setScore(Integer.parseInt(data[2]));
                    first = false;
                } else if (map.setTerrState(line)) {
                        finished = true;
                        break;
                }
            }
            
            br.close();
            r.close();
            
            switch (phase) {
                case DRAFT_BLUE : blue.draftTroops();
                                  break;
                case DRAFT_RED : red.draftTroops();
                                 break;
                default : 
            } 
            
            if (!finished) {
                throw new FormatException("Too few entries");
            }
        } catch (FormatException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Improper File Formatting", 
                    JOptionPane.ERROR_MESSAGE);
            map.resetIterator();
        } catch (NumberFormatException e) {
            String message = "NumberFormatException: Fix and try again";
            JOptionPane.showMessageDialog(frame, message, "Improper File Formatting", 
                    JOptionPane.ERROR_MESSAGE);
            map.resetIterator();
        } catch (IOException e) {
            String message = "IOException: " + e.getMessage();
            JOptionPane.showMessageDialog(frame, message, "Improper File Formatting", 
                    JOptionPane.ERROR_MESSAGE);
            map.resetIterator();
        } finally {
            try {
                br.close();
                r.close();
            } catch (IOException e) {
            } catch (NullPointerException e) {}
        }
        map.repaint();
    }
    private String instructions() {
        String instructions = 
                "INSTRUCTIONS:\n" +
                "Welcome to Risk! The map is divided into two teams, blue and red, which take turns.\n"+
                "Each turn has a draft phase, where you get more troops to put on the map, and an \n" +
                "attack phase, where you can use those troops to attack the enemy.\n\n" +
                "DRAFTING: Look on the left hand bar to see how many more drafted troops you can \n" + 
                "place. This number is calculated from the territories, cities and capitals you own\n" +
                "with cities and capitals especially giving extra troops. To place down troops, \n" +
                "simply click on any owned territory. Position them carefully! When you finish, \n" +
                "the phase will automatically advance to attacking. \n\n" +
                "ATTACKING: To attack an enemy territory, click on an owned territory which, \n" +
                "if valid, will highlight. Then select a nearby enemy territory to start a \n"+
                "Battle. Remember, you must always keep one troop behind to guard every territory \n" +
                "so you need at least 2 troops in a territory to attack!\n\n" +
                "BATTLES: When a battle is started, both sides roll dice. The defender rolls two \n" +
                "dice or the number of troops defending, whichever is smaller. The attacker rolls \n" +
                "three dice or the number of troops attacking, whichever is smaller. The dice are \n" +
                "then sorted and compared. The highest of both are compared, then the second \n" +
                "highest and so on. If the defender's roll is higher or equal to the attacker's, \n" +
                "the attacker loses 1 troop. Otherwise, the defender loses 1. For example, if \n" + 
                "the defender rolls 6, 2 and the attacker rolls 5, 4, 4, both sides would lose \n" + 
                "one, because 6 beats 5 but 4 beats 2. If all the defenders are eliminated, the \n" +
                "attacker wins, takes the territory, and gets points while the other team will \n" +
                "lose points. When you've finished attacking, click to end your turn.\n\n" +
                "CITIES: Cities (with the grey boxes) work differently from normal territories\n" +
                "Because of their urban nature, they are natural chokepoints and are harder to \n" +
                "attack. You may only attack cities with two troops at a time, making them\n" +
                "harder to take, but you get more points and drafted troops for doing so.\n\n" +
                "CAPITALS: Capitals (with grey and colored boxes) operate differently from both\n" +
                "normal territories and cities. Red team's true capital is Russia, and the blue\n" +
                "team's true capital is West USA. When a team defends their true capital, they \n" +
                "are able to defend with three instead of the usual 2 troops. For this reason\n" +
                "capitals are very difficult to take but yield many more points and troops than\n" +
                "either cities or normal provinces.\n\n" +
                "SAVING & LOADING: To save your game at the end of your turn, click \n" +
                "Save & End Turn. This writes it to a save file. At any point, you may switch from \n" +
                "your current game to the last saved game by clicking Load Save";
        return instructions;
    }
    //Actually runs the game, simply specifies that the game should be visible
    @Override
    public void run() {
        run(true);
    }
    //Runs the game, but only displays anything iff isVisible is true
    public void run(boolean isVisible) {
        createMap();
        loadSave(dataFile);
        
        map.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                map.handleClick(e.getX(), e.getY());
                map.repaint();
            }
        });
        
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(new BorderLayout());
        
        phaseLabel = new JLabel(phase.toString());
        
        JButton endTurn = new JButton("End Turn");
        endTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurn();
            }
        });
        
        JButton saveGame = new JButton("Save & End Turn");
        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeSave();
            }
        });
        
        JButton loadSave = new JButton("Load Save");
        loadSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSave("saveFile.txt");
                updatePhaseLabel();
            }
        });
        
        panel.add(map, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        panel.add(rightPanel, BorderLayout.LINE_END);
        
        rightPanel.add(gameDice);
        
        JPanel leftPanel = new JPanel(); 
        leftPanel.setLayout(new GridLayout(7, 1));
        
        leftPanel.add(scorecardBlue);
        leftPanel.add(scorecardRed);
        leftPanel.add(phaseLabel);
        leftPanel.add(draftLabel);  
        leftPanel.add(endTurn);
        leftPanel.add(saveGame);
        leftPanel.add(loadSave);
        
        panel.add(leftPanel, BorderLayout.LINE_START);
       
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(isVisible); 

        if (isVisible) {
            JOptionPane.showMessageDialog(frame, instructions());
        }
    }
    //Identical to run except the frame is not set to visible, so the game does not open for JUnit 
    //tests
    public void runInv() {
        run(false);
    }
}
