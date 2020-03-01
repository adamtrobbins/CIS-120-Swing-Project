import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class MyTests {
    private Game g = new Game();
    private WorldMap m = g.getMap();

    //Setup Tests
    @Test //Adding territories to map works
    public void testMapAddterr() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(12, 6);
        Point p3 = new Point(3, 87);
        Territory t = new Province(g, "Borneo", new Point[] {p1, p2, p3});
        m.addTerr(t);
        assertEquals(t, m.getTerritory("Borneo"));
    }
    @Test //Correctly determines adjacency
    public void testTerrsAdjacent() {
        Point sa = new Point(200, 450);
        Point sb = new Point(200, 350);
        Point sc = new Point(250, 375);
        Point se = new Point(200, 250);
        
        Territory t1 = new Province(g, "Iran", new Point[] {sb, sa, sc});
        Territory t2 = new Province(g, "Venezuela", new Point[] {sc, sb, se});
        
        assertTrue(t2.borders(t1));
        assertTrue(t1.borders(t2));
        
        m.addTerr(t1);
        m.addTerr(t2);
        m.determineAllNeighbors();

        assertTrue(t1.isNeighbor(t2));
        assertTrue(t2.isNeighbor(t1));
    }
    @Test //Correctly determines not adjacent for only one common point
    public void testTerrsNotAdjacentOneCommonPoint() {
        Point sa = new Point(200, 450);
        Point sb = new Point(200, 350);
        Point sc = new Point(250, 375);
        Point sd = new Point(175, 275);
        Point se = new Point(200, 250);
        
        Territory t1 = new Province(g, "Iran", new Point[] {sb, sa, sc});
        Territory t2 = new Province(g, "Venezuela", new Point[] {sc, sd, se});
        
        assertFalse(t2.borders(t1));
        assertFalse(t1.borders(t2));
        
        m.addTerr(t1);
        m.addTerr(t2);
        m.determineAllNeighbors();

        assertFalse(t1.isNeighbor(t2));
        assertFalse(t2.isNeighbor(t1));
    }
    @Test //Correctly determines provinces with no shared point not adjacent
    public void testTerrsNotAdjacentNoCommonPoint() {
        Point sa = new Point(200, 450);
        Point sb = new Point(200, 350);
        Point sc = new Point(250, 375);
        Point sd = new Point(175, 275);
        Point se = new Point(200, 250);
        Point x = new Point(500, 350);
        
        Territory t1 = new Province(g, "Iran", new Point[] {sb, sa, sc});
        Territory t2 = new Province(g, "Venezuela", new Point[] {sd, se, x});
        
        assertFalse(t2.borders(t1));
        assertFalse(t1.borders(t2));
        
        m.addTerr(t1);
        m.addTerr(t2);
        m.determineAllNeighbors();

        assertFalse(t1.isNeighbor(t2));
        assertFalse(t2.isNeighbor(t1));
    }
    @Test //Correctly gets province owner from save
    public void correctOwnerBlue() {
        g.createMap();
        g.loadSave("templateData.txt");
        assertEquals(g.getBlue(), m.getTerritory("Mexico").getOwner());
    }
    @Test //Correctly gets province owner from save
    public void correctOwnerRed() {
        g.createMap();
        g.loadSave("templateData.txt");
        assertEquals(g.getRed(), m.getTerritory("Russia").getOwner());
    }
    @Test //Correctly gets initial scores from save
    public void correctInitialScores() {
        g.createMap();
        g.loadSave("testData1.txt");
        assertEquals(15, g.getBlue().getScore());
        assertEquals(7, g.getRed().getScore());
    }
    //Gameplay Testing
    //Drafting Testing
    @Test //Drafting works as normal across two provinces
    public void draftClickingWorksTwoProvinces() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within West Africa
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        m.handleClick(350, 250);
        assertEquals(3, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within Brazil
        m.handleClick(250, 300);
        assertEquals(3, wa.getTroops());
        assertEquals(2, br.getTroops());
    }
    @Test //Nothing happens if no province is clicked
    public void draftClickingSelectsNoProvince() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within West Africa
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        m.handleClick(350, 250);
        assertEquals(3, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within ocean (invalid)
        m.handleClick(125, 300);
        assertEquals(3, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within Brazil
        m.handleClick(250, 300);
        assertEquals(3, wa.getTroops());
        assertEquals(2, br.getTroops());
    }
    @Test //Selecting nothing does not deplete draftable troops
    public void draftSelectingNothingDoesNotDrainTroops() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        for (int i = 0; i < 100; i++) {
            //Within ocean (invalid)
            m.handleClick(125, 300);
        }
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within West Africa
        
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops()); 
    }
    @Test //Selecting enemy province when drafting does nothing
    public void draftClickingSelectsEnemyProvince() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within West Africa
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within Egypt (enemy province)
        m.handleClick(450, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops());
        
        //Within Brazil
        m.handleClick(250, 300);
        assertEquals(2, wa.getTroops());
        assertEquals(2, br.getTroops());
    }
    @Test //Trying to draft on an enemy province does nothing
    public void draftSelectingEnemyTerrDoesNotDrainTroops() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");
        Territory eg = m.getTerritory("Egypt");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        assertEquals(1, eg.getTroops());
        
        for (int i = 0; i < 100; i++) {
            //Within Egypt (enemy province)
            m.handleClick(442, 250);
        }
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, br.getTroops());
        assertEquals(1, eg.getTroops());
        
        //Within West Africa
        
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, br.getTroops());
        assertEquals(1, eg.getTroops());
    }
    @Test //Drafting all available troops advances turn automatically
    public void draftingLastTroopAdvancesTurn() {
        g.runInv();
        g.loadSave("testData2.txt");
        
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        
        //Clicks in Egypt, valid
        m.handleClick(442, 250);
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        
        m.handleClick(442, 250);
        assertEquals(GamePhase.OFFENSE_RED, g.phase());
    }
    @Test //The amount of troops drafted is correct based on territory/city/capital amounts
    public void correctDraftAmount() {
        g.createMap();
        g.loadSave("testData1.txt");
        
        assertEquals(GamePhase.DRAFT_BLUE, g.phase());
        
        //Clicks in West Africa
        for (int i = 0; i < 9; i++) {
            m.handleClick(350, 250);
        }
        
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
    }
    //Attacking Tests
    //    Attacking to a valid province
    @Test
    public void attacksProvinceAndLoses() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 9; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {4, 2, 2, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(8, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(15, g.getBlue().getScore());
        assertEquals(7, g.getRed().getScore());   
    }
    @Test //Checks that ties are resolved in favor of defender
    public void attacksProvinceAndLosesWithTie() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {3, 3, 1, 2});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(8, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(15, g.getBlue().getScore());
        assertEquals(7, g.getRed().getScore());   
    }
    @Test //Checks that conquering provinces works. Also checks that draft rule works for capitals
    //only when true owner (draftable troops is 8).
    public void attacksProvinceAndWins() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {3, 3, 5, 2});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(1, wa.getTroops());
        assertEquals(8, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getBlue(), eg.getOwner());
        assertEquals(16, g.getBlue().getScore());
        assertEquals(6, g.getRed().getScore());   
    }
    @Test //Checks that both sides can lose troops simultaneously
    public void attacksProvinceBothSidesLoseTroops() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 4, 4, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());   
    }
    @Test //Checks that dice are sorted and then compared (and not just compared in original order)
    public void attacksProvinceBothSidesLoseTroopsDiceSorted() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 9; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 1, 4, 4});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());   
    }
    @Test //Selecting nothing does nothing
    public void attackNothingDoesNothing() {
        g.runInv();
        g.loadSave("testData1.txt");

        Territory wa = m.getTerritory("West Africa");

        //Clicks in West Africa, valid
        for (int i = 0; i < 9; i++) {
            m.handleClick(350, 250);
        }

        assertEquals(9, wa.getTroops());

        //Clicks in West Africa, valid 
        m.handleClick(350, 250);

        //Clicks in ocean, invalid
        m.handleClick(10, 250);

        assertEquals(9, wa.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
    }
    @Test //Checks that trying to attack a territory which is not adjacent is ignored
    public void attackEnemyProvinceNotAdjacentDoesNothing() {
        g.runInv();
        g.loadSave("testData1.txt");

        Territory br = m.getTerritory("Brazil");
        Territory eg = m.getTerritory("Egypt");

        //Clicks in Brazil, valid
        for (int i = 0; i < 9; i++) {
            m.handleClick(250, 300);
        }

        assertEquals(9, br.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());

        //Clicks in Brazil, valid 
        m.handleClick(250, 300);

        //Clicks in Egypt, invalid
        m.handleClick(442, 250);

        assertEquals(9, br.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
    }
    @Test //Trying to attack a friendly adjacent province is ignored
    public void attackFriendlyAdjProvinceDoesNothing() {
        g.runInv();
        g.loadSave("testData1.txt");

        Territory wa = m.getTerritory("West Africa");
        Territory br = m.getTerritory("Brazil");

        //Clicks in Brazil, valid
        for (int i = 0; i < 9; i++) {
            m.handleClick(250, 300);
        }

        assertEquals(9, br.getTroops());
        assertEquals(1, wa.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());

        //Clicks in Brazil, valid 
        m.handleClick(250, 300);

        //Clicks in West Africa, invalid
        m.handleClick(350, 250);

        assertEquals(9, br.getTroops());
        assertEquals(1, wa.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
    }
    @Test //Trying to attack a friendly non-adjacent province is ignored
    public void attackFriendlyNonAdjProvinceDoesNothing() {
        g.runInv();
        g.loadSave("testData1.txt");

        Territory ea = m.getTerritory("East Australia");
        Territory br = m.getTerritory("Brazil");

        //Clicks in Brazil, valid
        for (int i = 0; i < 8; i++) {
            m.handleClick(250, 300);
        }

        assertEquals(9, br.getTroops());
        assertEquals(1, ea.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());

        //Clicks in Brazil, valid 
        m.handleClick(250, 300);

        //Clicks in East Australia, invalid
        m.handleClick(775, 450);

        assertEquals(9, br.getTroops());
        assertEquals(1, ea.getTroops());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
    }
    @Test //Clicking nothing is ignored
    public void userClicksNothingThenValidProvince() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 4, 4, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in ocean
        m.handleClick(0, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());   
    }
    @Test //Tests that user can change his or her mind and change the source attack province
    public void userSwitchesAttackSourceProvince() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory br = m.getTerritory("Brazil");
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        assertEquals(2, br.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 4, 4, 1});
        
        //Clicks in ocean
        m.handleClick(250, 300);
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(2, br.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());   
    }
    //    Attacking to invalid provinces, then choosing a valid one
    @Test //Trying to attack nothing is ignored
    public void userSwitchesAttackTargetFromNothingToValid() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        Territory ng = m.getTerritory("New Guinea");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        assertEquals(2, ng.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 4, 4, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in ocean, invalid
        m.handleClick(0, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(2, ng.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());         
    }
    @Test //Trying to attack an invalid enemy province is ignored
    public void userClicksInvalidEnemyProvinceThenGoodProvince() {
        g.runInv();
        g.loadSave("testData3.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        Territory ng = m.getTerritory("New Guinea");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(10, wa.getTroops());
        assertEquals(2, eg.getTroops());
        assertEquals(2, ng.getTroops());
        
        DieRollGenerator.rigDice(new int[] {2, 6, 4, 4, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in New Guinea, enemy province but not adjacent so invalid
        m.handleClick(795, 350);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(9, wa.getTroops());
        assertEquals(1, eg.getTroops());
        assertEquals(2, ng.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), eg.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());   
    }
    @Test //Checks that special City mechanics are working
    public void userAttacksCityCanOnlyUseTwoDice() {
        g.runInv();
        g.loadSave("testData4.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory se = m.getTerritory("South Europe");
        
        for (int i = 0; i < 9; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {3, 1, 1, 6});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in South Europe, valid
        m.handleClick(425, 220);
        
        assertEquals(8, wa.getTroops());
        assertEquals(1, se.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getRed(), se.getOwner());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());          
    }
    @Test //Checks if the bonus given to teams defending their true capitals works
    public void playerAttacksCapitalDefenseBonusActivates() {
        g.runInv();
        g.loadSave("testData4.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory se = m.getTerritory("South Europe");
        Territory ru = m.getTerritory("Russia");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {1, 6, 6});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in South Europe, valid. Wins battle
        m.handleClick(425, 220);
        
        assertEquals(8, se.getTroops());
        assertEquals(g.getBlue(), se.getOwner());
        
        //If Russia only got two dice, 2 and 4, they would not have inflicted any casualties (with
        //the 6). If the attackers take 1 casualty, this shows that the bonus works
        DieRollGenerator.rigDice(new int[] {2, 4, 6, 5, 5, 5});
        
        //Clicks in South Europe, valid
        m.handleClick(425, 220);
        //Clicks in Russia, valid. Starts battle
        m.handleClick(430, 100);
        
        assertEquals(7, se.getTroops());
        assertEquals(1, ru.getTroops());
        assertEquals(g.getBlue(), se.getOwner());
        assertEquals(g.getRed(), ru.getOwner());               
    }
    @Test //Makes sure that capital bonus only activates if owner is true owner
    public void playerAttacksCapitalNotTrueOwnerNoBonus() {
        g.runInv();
        g.loadSave("testData4.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory se = m.getTerritory("South Europe");
        Territory ru = m.getTerritory("Russia");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {1, 6, 6});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in South Europe, valid. Wins battle
        m.handleClick(425, 220);
        
        assertEquals(8, se.getTroops());
        assertEquals(g.getBlue(), se.getOwner());
        
        //Because the red team should only get two dice, they should inflict no losses on attackers
        //6 and 5 beats 2 and 4. 
        DieRollGenerator.rigDice(new int[] {2, 4, 6, 5, 5, 5});
        ((Capital) ru).setTrueOwner(null);
        
        //Clicks in South Europe, valid
        m.handleClick(425, 220);
        //Clicks in Russia, valid. Starts battle
        m.handleClick(430, 100);
        
        assertEquals(8, se.getTroops());
        assertEquals(1, ru.getTroops());
        assertEquals(g.getBlue(), se.getOwner());
        assertEquals(g.getRed(), ru.getOwner());  
    }
    @Test //Checks correct score from capital capture
    public void playerTakesEnemyCapitalScoreBonus() {
        g.runInv();
        g.loadSave("testData4.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory se = m.getTerritory("South Europe");
        Territory ru = m.getTerritory("Russia");
        
        for (int i = 0; i < 8; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {1, 6, 6});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in South Europe, valid. Wins battle
        m.handleClick(425, 220);
        
        assertEquals(8, se.getTroops());
        assertEquals(g.getBlue(), se.getOwner());
        assertEquals(3, g.getBlue().getScore());
        assertEquals(-3, g.getRed().getScore());
        
        DieRollGenerator.rigDice(new int[] {2, 4, 6, 5, 5, 5});
        
        //Clicks in South Europe, valid
        m.handleClick(425, 220);
        //Clicks in Russia, valid. Starts battle
        m.handleClick(430, 100);
        
        DieRollGenerator.rigDice(new int[] {3, 4, 6, 5});
        
        assertEquals(7, se.getTroops());
        assertEquals(1, ru.getTroops());
        
        //Clicks in South Europe, valid
        m.handleClick(425, 220);
        //Clicks in Russia, valid. Starts battle
        m.handleClick(430, 100);
        
        
        assertEquals(g.getBlue(), se.getOwner());
        assertEquals(g.getBlue(), ru.getOwner());  
        assertEquals(1, se.getTroops());
        assertEquals(6, ru.getTroops());
        //Capital should be worth 9 points (added to 3 and -3 earlier)
        assertEquals(12, g.getBlue().getScore());
        assertEquals(-12, g.getRed().getScore());
    }
    //Ending Turn
    @Test //Checks that end turn works after attacking
    public void endsTurnWhenOffense() {
        g.runInv();
        g.loadSave("templateData.txt");
        
        //Clicks in Brazil, valid
        for (int i = 0; i < 9; i++) {
            m.handleClick(250, 300);
        }
        
        //Loses
        DieRollGenerator.rigDice(new int[] {1, 1, 1, 1});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid, attacks it
        m.handleClick(442, 240);
        
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase()); 
        g.endTurn();
        assertEquals(GamePhase.DRAFT_RED, g.phase());
    }
    @Test //Checks that end turn works correctly when drafting
    public void endsTurnWhenDraft() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        assertEquals(GamePhase.DRAFT_BLUE, g.phase()); 
        g.endTurn();
        assertEquals(GamePhase.DRAFT_RED, g.phase());
    }
    @Test //Checks that the basic cycling works
    public void endsTurnSeveralTimes() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        //Clicks in Brazil, valid
        for (int i = 0; i < 9; i++) {
            m.handleClick(250, 300);
        }
        
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase()); 
        g.endTurn();
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        g.endTurn();
        assertEquals(GamePhase.DRAFT_BLUE, g.phase());
        g.endTurn();
        assertEquals(GamePhase.DRAFT_RED, g.phase());
    }
    @Test //Check that endTurn works properly with drafting
    public void endTurnDraftsWhenAppropriate() {
        g.runInv();
        g.loadSave("testData2.txt");
        
        Territory br = m.getTerritory("Brazil");
        
        //Clicks in Egypt, valid
        for (int i = 0; i < 2; i++) {
            m.handleClick(442, 240);
        }
        assertEquals(GamePhase.OFFENSE_RED, g.phase()); 
        g.endTurn();
        assertEquals(GamePhase.DRAFT_BLUE, g.phase());
        
        //Clicks in Brazil, valid
        assertEquals(1, br.getTroops());
        m.handleClick(250, 300);
        assertEquals(2, br.getTroops());
    }
    //File Reading Testing
    @Test //Tests that file is correctly read
    public void testFileCorrectlyProcessed() {
        g.runInv();
        g.loadSave("testData2.txt");
        
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        assertEquals(7, g.getBlue().getScore());
        assertEquals(12, g.getRed().getScore());
        assertEquals(g.getRed(), m.getTerritory("East Africa").getOwner());
        assertEquals(g.getBlue(), m.getTerritory("China").getOwner());
        assertEquals(5, m.getTerritory("Middle East").getTroops());
        assertEquals(8, m.getTerritory("Russia").getTroops());
    }
    @Test //Test that only the most recent load is used
    public void testFileCorrectlyProcessedMultipleTimes() {
        g.runInv();
        g.loadSave("testData1.txt");
        g.loadSave("testData2.txt");
        g.loadSave("templateData.txt");
        
        assertEquals(GamePhase.DRAFT_BLUE, g.phase());
        assertEquals(0, g.getBlue().getScore());
        assertEquals(0, g.getRed().getScore());
        assertEquals(g.getRed(), m.getTerritory("China").getOwner());
        
        g.loadSave("testData2.txt");
        
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        assertEquals(7, g.getBlue().getScore());
        assertEquals(12, g.getRed().getScore());
        assertEquals(g.getRed(), m.getTerritory("East Africa").getOwner());
        assertEquals(g.getBlue(), m.getTerritory("China").getOwner());
        assertEquals(5, m.getTerritory("Middle East").getTroops());
        assertEquals(8, m.getTerritory("Russia").getTroops());
    }
    //File Writing Testing
    @Test //Tests that end turn and save works
    public void draftingEndingTurnAndSaving() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        assertEquals(1, wa.getTroops());
        assertEquals(1, eg.getTroops());
        
        //Within West Africa
        m.handleClick(350, 250);
        assertEquals(2, wa.getTroops());
        assertEquals(1, eg.getTroops());
        
        assertEquals(GamePhase.DRAFT_BLUE, g.phase());
        
        g.writeSave();
        
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        
        g.loadSave();
        
        assertEquals(2, wa.getTroops());
        assertEquals(GamePhase.DRAFT_RED, g.phase());
        assertEquals(1, eg.getTroops());
    }
    @Test //Ensures map changes actually carry over through saving and loading
    public void conqueringTerritorySavingAndReloading() {
        g.runInv();
        g.loadSave("testData1.txt");
        
        Territory wa = m.getTerritory("West Africa");
        Territory eg = m.getTerritory("Egypt");
        
        for (int i = 0; i < 9; i++) {
            //Clicks in West Africa, valid
            m.handleClick(350, 250);
        }
        
        assertEquals(15, g.getBlue().getScore());
        assertEquals(7, g.getRed().getScore());
        assertEquals(GamePhase.OFFENSE_BLUE, g.phase());
        assertEquals(9, wa.getTroops());
        
        DieRollGenerator.rigDice(new int[] {3, 3, 5, 2});
        
        //Clicks in West Africa, valid
        m.handleClick(350, 250);
        //Clicks in Egypt, valid
        m.handleClick(442, 240);
        
        assertEquals(1, wa.getTroops());
        assertEquals(8, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getBlue(), eg.getOwner());
        assertEquals(16, g.getBlue().getScore());
        assertEquals(6, g.getRed().getScore()); 
        
        g.writeSave();
        g.loadSave("testData1.txt");
        g.loadSave();
        
        assertEquals(1, wa.getTroops());
        assertEquals(8, eg.getTroops());
        assertEquals(g.getBlue(), wa.getOwner());
        assertEquals(g.getBlue(), eg.getOwner());
        assertEquals(16, g.getBlue().getScore());
        assertEquals(6, g.getRed().getScore()); 
    }
}
