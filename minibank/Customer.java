package minibank;

// (4) Implement Cloneable so clone() is allowed
public class Customer implements Cloneable {

    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    // (3) Address field
    private Address address;

    private static long customerCounter = 100;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getMobile()     { return mobile; }
    public String getCustomerId() { return customerId; }

    // (3) Address getter / setter
    public Address getAddress()              { return address; }
    public void    setAddress(Address addr)  { this.address = addr; }

    // ----------------------------------------------------------------
    // toString
    // ----------------------------------------------------------------

    @Override
    public String toString() {
        return "Customer[id=" + customerId
                + ", name=" + name
                + ", email=" + email
                + ", mobile=" + mobile
                + ", address=" + address + "]";
    }

    // ----------------------------------------------------------------
    // (4) clone – returns a copy of this Customer
    // ----------------------------------------------------------------

    @Override
    public Customer clone() {
        try {
            // super.clone() performs a shallow copy of all fields
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            // Cannot happen: this class implements Cloneable
            throw new AssertionError("Cloning failed", e);
        }
    }

    // ================================================================
    // (3) Public static nested class Address
    // ================================================================

    public static class Address {

        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line    = line;
            this.city    = city;
            this.pincode = pincode;
        }

        // Getters
        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }
}
