package ro.pub.cs.systems.eim.practicaltest02;

public class Information {
    public Information(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Information{" +
                ", definition='" + definition + '\'' +
                '}';
    }


    private String definition;
}
