import java.util.List;

public class People {
    private String name_suffix;
    private String first_name;
    private String last_name;
    private List<String> drugs;
    private String email;
    private String gender;
    private Address address;
    private List<Parents> parents;

    public People(String name_suffix, String first_name, String last_name, List<String> drugs, String email, String gender, Address address, List<Parents> parents) {
        this.name_suffix = name_suffix;
        this.first_name = first_name;
        this.last_name = last_name;
        this.drugs = drugs;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.parents = parents;
    }


    public String getName_suffix() {
        return name_suffix;
    }

    public void setName_suffix(String name_suffix) {
        this.name_suffix = name_suffix;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<String> drugs) {
        this.drugs = drugs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Parents> getParents() {
        return parents;
    }

    public void setParents(List<Parents> parents) {
        this.parents = parents;
    }

}
