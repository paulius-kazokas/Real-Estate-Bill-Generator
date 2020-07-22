package entities;

public class Indicator {

    private Property property;
    private Utility utility;

    private int utilityId;
    private int propertyId;

    private int monthStartAmount;
    private int monthEndAmount;

    public Indicator(Utility utility, Property property) {
        this.utility = utility;
        this.property = property;
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

    public int getUtilityId() {
        return utility.getUtilityId();
    }

    public void setUtilityId(int utilityId) {
        this.utilityId = utilityId;
    }

    public int getPropertyId() {
        return property.getPropertyId();
    }

    public void setPropertyId(int propertyId) {
        property.setPropertyId(propertyId);
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
                ", utilityId=" + utilityId +
                ", propertyId=" + propertyId +
                ", monthStartAmount=" + monthStartAmount +
                ", monthEndAmount=" + monthEndAmount +
                '}';
    }
}
