package entities;

public class Property {

    private int propertyId;
    private lUser user;
    private String ownerPersonalCode;
    private String type;
    private String address;

    public Property() {}

    public Property(lUser user) {
        this.user = user;
        this.ownerPersonalCode = user.getPersonalCode();
    }

    public Property(int propertyId, lUser user, String ownerPersonalCode, String type, String address) {
        this.propertyId = propertyId;
        this.user = user;
        this.ownerPersonalCode = ownerPersonalCode;
        this.type = type;
        this.address = address;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getOwnerPersonalCode() {
        return user.getPersonalCode();
    }

    public void setOwnerPersonalCode(String ownerPersonalCode) {
        this.ownerPersonalCode = ownerPersonalCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Property{" +
                "propertyId=" + propertyId +
                ", ownderPersonalCode='" + ownerPersonalCode + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
