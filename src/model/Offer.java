package model;

import java.sql.Date;

public class Offer {
    private int offerId;
    private int studentId;
    private int companyId;
    private String role;
    private double ctc;
    private Date offerDate;
    private String status;

    public Offer() {
    }

    public Offer(int offerId, int studentId, int companyId, String role, double ctc, Date offerDate, String status) {
        this.offerId = offerId;
        this.studentId = studentId;
        this.companyId = companyId;
        this.role = role;
        this.ctc = ctc;
        this.offerDate = offerDate;
        this.status = status;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getCtc() {
        return ctc;
    }

    public void setCtc(double ctc) {
        this.ctc = ctc;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
