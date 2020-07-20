package entities;

public class Property {

    private int propertyId;
    private User user;
    private String ownderPersonalCode;
    private String type;
    private String address;
    private String numberOfRooms;

    public Property(User user) {
        this.user = user;
    }

    public Property(int propertyId, User user, String ownderPersonalCode, String type, String address, String numberOfRooms) {
        this.propertyId = propertyId;
        this.user = user;
        this.ownderPersonalCode = ownderPersonalCode;
        this.type = type;
        this.address = address;
        this.numberOfRooms = numberOfRooms;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    @Override
    public String toString() {
        return "Property{" +
                "ownderPersonalCode='" + ownderPersonalCode + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", numberOfRooms='" + numberOfRooms + '\'' +
                '}';
    }
}
