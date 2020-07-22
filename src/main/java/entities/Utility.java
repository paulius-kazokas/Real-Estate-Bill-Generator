package entities;

public class Utility {

    private int utilityId;
    private String utilityName;

    public Utility() {
    }

    public Utility(int utilityId) {
        this.utilityId = utilityId;
    }

    public int getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(int utilityId) {
        this.utilityId = utilityId;
    }

    public String getUtilityName() {
        return utilityName;
    }

    public void setUtilityName(String utilityName) {
        this.utilityName = utilityName;
    }

    public Utility(String utilityName) {
        this.utilityName = utilityName;
    }

    @Override
    public String toString() {
        return "Utility{" +
                "utilityId=" + utilityId +
                ", utilityName='" + utilityName + '\'' +
                '}';
    }
}
