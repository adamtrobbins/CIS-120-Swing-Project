public enum GamePhase {
    DRAFT_BLUE(0), OFFENSE_BLUE(0), DRAFT_RED(1), OFFENSE_RED(1);
    private int team;
    private GamePhase(int x) {
        team = x;
    }
    //Gets value corresponding to which team it is
    public int getTeamNum() {
        return team;
    }
    //Retrieves next sequential phase
    public GamePhase advance() {
        switch(this){
            case DRAFT_BLUE: return OFFENSE_BLUE;
            case OFFENSE_BLUE: return DRAFT_RED;
            case DRAFT_RED: return OFFENSE_RED;
            case OFFENSE_RED: return DRAFT_BLUE;
            default : return null;
        }
    }
    @Override
    public String toString() {
        switch(this){
            case DRAFT_BLUE: return "Team Blue: Draft";
            case OFFENSE_BLUE: return "Team Blue: Offense";
            case DRAFT_RED: return "Team Red: Draft";
            case OFFENSE_RED: return "Team Red: Offense";
        }
        return "BAD PHASE";
    }
}
