package entities;

public class Indicator {

    private Property property;
    private Utility utility;

    private int propertyId;
    private String utilityType;
    private int monthStartAmount;
    private int monthEndAmount;

    public Indicator(Property property, Utility utility) {
        this.property = property;
        this.utility = utility;
    }

    public Indicator(Property property, Utility utility, int propertyId, String utilityType, int monthStartAmount, int monthEndAmount) {
        this.property = property;
        this.utility = utility;
        this.propertyId = propertyId;
        this.utilityType = utilityType;
        this.monthStartAmount = monthStartAmount;
        this.monthEndAmount = monthEndAmount;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public int getPropertyId() {
        return property.getPropertyId();
    }

    public void setPropertyId(int propertyId) {
        property.setPropertyId(propertyId);
    }

    public String getUtilityType() {
        return utility.getUtilityName();
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
    }

    public int getMonthStartAmount() {
        return monthStartAmount;
    }

    public void setMonthStartAmount(int monthStartAmount) {
        this.monthStartAmount = monthStartAmount;
    }

    public int getMonthEndAmount() {
        return monthEndAmount;
    }

    public void setMonthEndAmount(int monthEndAmount) {
        this.monthEndAmount = monthEndAmount;
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "property=" + property +
                ", utility=" + utility +
                ", propertyId=" + propertyId +
                ", utilityType='" + utilityType + '\'' +
                ", monthStartAmount=" + monthStartAmount +
                ", monthEndAmount=" + monthEndAmount +
                '}';
    }
}
