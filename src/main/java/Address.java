public class Address {
    private String country;
    private String city;
    private String street;
    private int house_number;

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return house_number;
    }
    public void setHouse_number(int house_number) {
        this.house_number = house_number;
    }

    @Override
    public String toString(){
        return getCountry() + " " + getCity() + " "+getStreet()+" "+getHouseNumber();
    }
}
