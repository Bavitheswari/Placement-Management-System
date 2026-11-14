package model;

public class Company {

    private int companyId;
    private String companyName;
    private String location;
    private double companyPackage;
    private String role;

    public Company() {}

    public Company(String companyName,
                   String location,
                   double companyPackage,
                   String role) {

        this.companyName = companyName;
        this.location = location;
        this.companyPackage = companyPackage;
        this.role = role;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getCompanyPackage() {
        return companyPackage;
    }

    public void setCompanyPackage(double companyPackage) {
        this.companyPackage = companyPackage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
