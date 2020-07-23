package entities;

public class Property {

    private int propertyId;
    private User user;
    private String ownderPersonalCode;
    private String type;
    private String address;

    public Property(User user) {
        this.user = user;
    }

    public Property(int propertyId, User user, String ownderPersonalCode, String type, String address) {
        this.propertyId = propertyId;
        this.user = user;
        this.ownderPersonalCode = ownderPersonalCode;
        this.type = type;
        this.address = address;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getOwnderPersonalCode() {
        return user.getPersonalCode();
    }

    public void setOwnderPersonalCode(String ownderPersonalCode) {
        this.ownderPersonalCode = ownderPersonalCode;
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
                ", ownderPersonalCode='" + ownderPersonalCode + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
