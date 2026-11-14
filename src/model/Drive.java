package model;

public class Drive {

    private int driveId;
    private int companyId;
    private String driveDate;
    private double minCgpa;

    public Drive() {}

    public Drive(int companyId,
                 String driveDate,
                 double minCgpa) {

        this.companyId = companyId;
        this.driveDate = driveDate;
        this.minCgpa = minCgpa;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(String driveDate) {
        this.driveDate = driveDate;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }
}
