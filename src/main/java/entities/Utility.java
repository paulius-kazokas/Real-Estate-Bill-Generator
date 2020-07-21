package entities;

public class Utility {

    private String utilityName;

    public Utility() {
    }

    public Utility(String utilityName) {
        this.utilityName = utilityName;
    }

    public String getUtilityName() {
        return utilityName;
    }

    @Override
    public String toString() {
        return "Utility{" +
                "utilityName='" + utilityName + '\'' +
                '}';
    }
}
