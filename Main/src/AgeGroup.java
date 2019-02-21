public enum AgeGroup {
    GROUP1("<25"),
    GROUP2("25-34"),
    GROUP3("35-44"),
    GROUP4("45-54"),
    GROUP5(">54");
    private final String name;
    private AgeGroup(String s){
        this.name = s;
    }

    public String getName() {
        return name;
    }
}
