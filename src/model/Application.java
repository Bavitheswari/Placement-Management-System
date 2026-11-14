package model;

public class Application {

    private int studentId;
    private int driveId;
    private String status;

    public Application() {}

    public Application(int studentId,
                       int driveId) {

        this.studentId = studentId;
        this.driveId = driveId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}