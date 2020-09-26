/**
 * CSC 365 Lab 1-a
 *
 * Class containing Student data fields and associated methods.
 *
 * @author Lucas Greenelsh (greenels)
 * date: 09/16/2020
 */

public class Student {

    private String StLastName;
    private String StFirstName;
    private String GPA;
    private int Grade;
    private int Classroom;
    private int Bus;

    /**
     * Constructor initializes all data fields.
     *
     * @param StLastName Student's last name.
     * @param StFirstName Student's first name.
     * @param grade Student's grade level.
     * @param classroom Student's classroom.
     * @param bus Student's bus route.
     * @param gpa Student's grade point average.
     */

    public Student(String StLastName, String StFirstName, int grade, int classroom, int bus, String gpa)
    {
        this.StLastName = StLastName;
        this.StFirstName = StFirstName;
        this.Grade = grade;
        this.Classroom = classroom;
        this.Bus = bus;
        this.GPA = gpa;
    }

    public String getStLastName() { return StLastName; }
    public String getStFirstName() { return StFirstName;}
    public String getGPA() { return GPA;}
    public int getGrade() { return Grade;}
    public int getClassroom() { return Classroom;}
    public int getBus() {return Bus;}
}
