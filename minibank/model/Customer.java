package model;

public class Customer implements Cloneable {

    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    private Address address;

    private static long customerCounter = 100;

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    public Customer(String name, String email, String mobile, Address address) {
        this(name, email, mobile);
        this.address = address;
    }

    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getMobile()     { return mobile; }
    public String getCustomerId() { return customerId; }

    public Address getAddress()              { return address; }
    public void    setAddress(Address addr)  { this.address = addr; }

    @Override
    public String toString() {
        return "Customer[id=" + customerId
                + ", name=" + name
                + ", email=" + email
                + ", mobile=" + mobile
                + ", address=" + address + "]";
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning failed", e);
        }
    }

    public static class Address {

        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line    = line;
            this.city    = city;
            this.pincode = pincode;
        }

        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }
}
