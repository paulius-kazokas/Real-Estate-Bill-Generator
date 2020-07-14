package entities;

public class Property {

    private String ownderPersonalCode;
    private String type;
    private String address;
    private String numberOfRooms;

    public Property(String ownderPersonalCode, String type, String address, String numberOfRooms) {
        this.ownderPersonalCode = ownderPersonalCode;
        this.type = type;
        this.address = address;
        this.numberOfRooms = numberOfRooms;
    }

    public String getOwnderPersonalCode() {
        return ownderPersonalCode;
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
