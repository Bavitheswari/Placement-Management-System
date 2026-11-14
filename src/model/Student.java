package model;

public class Student {

    private int studentId;
    private String name;
    private String email;
    private String password;
    private String department;
    private double cgpa;
    private String phone;

    public Student() {}

    public Student(int studentId, String name, String email,
                   String password, String department,
                   double cgpa, String phone) {

        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.cgpa = cgpa;
        this.phone = phone;
    }
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}