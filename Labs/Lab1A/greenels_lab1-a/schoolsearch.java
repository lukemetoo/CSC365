/**
 * CSC 365 Lab 1-a
 *
 * Class containing logic main method and code for searching a text file of Students
 *
 * @author Lucas Greenelsh (greenels)
 * date: 09/16/2020
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class schoolsearch {

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        String[] in;

        if(!buildList(students))
            return;

        Scanner scan = new Scanner(System.in);
        String menu =
                "\nS[tudent]: <lastname> [B[us]]\n" +
                "T[eacher]: <lastname>\n" +
                "B[us]: <number>\n" +
                "G[rade]: <number> [H[igh]|L[ow]]\n" +
                "A[verage]: <number>\n" +
                "I[nfo]\n" +
                "Q[uit]\n";

        while (true) {
            System.out.println(menu);
            String input = scan.nextLine();
            
            while (input.length() < 1)
                input = scan.nextLine();

            char choice = input.charAt(0);

            switch (choice) {
                case 'Q':
                    System.out.println("\nGoodbye");
                    return;
                case 'S':
                    in = input.split(": | ");

                    if (in.length > 2)
                    {
                        if (in[2].charAt(0) == 'B')
                            findStudentBus(in[1], students);
                        else
                            findStLast(in[1], students);
                    }
                    else if(in.length > 1)
                        findStLast(in[1], students);
                    break;

                case 'G':
                    in = input.split(": | ");
                    if (in.length > 2){
                        if(in[2].charAt(0) == 'H')
                        {
                            ArrayList<Student> cohort = getCohort(in[1], students);
                            if(cohort.size() > 0)
                                findMaxGPA(cohort);
                        }
                        else if(in[2].charAt(0) == 'L')
                        {
                            ArrayList<Student> cohort = getCohort(in[1], students);
                            if(cohort.size() > 0)
                                findMinGPA(cohort);
                        }
                    }
                    else if(in.length > 1)
                        findGrade(in[1], students);
                    break;

                case 'B':
                    in = input.split(": | ");
                    if(in.length > 1)
                        findBus(in[1], students);
                    break;

                case 'T':
                    in = input.split(": | ");
                    if(in.length > 1)
                        findTeacher(in[1], students);
                    break;

                case 'A':
                    in = input.split(": | ");
                    if(in.length > 1)
                        findAverage(in[1], students);
                    break;

                case 'I':
                    printInfo(students);
                    break;

                default:
                    System.out.println("Invalid option! Please try again");
                    break;
            }
        }
    }

    /**
     * This method constructs a new Student from each entry in students.txt and adds them to an arraylist.
     * @param students ArrayList that will serve as main data structure for storing students.
     */

    private static boolean buildList(ArrayList students) {
        String[] data;
        String line;

        try {
            Scanner fileReader = new Scanner(new File("students.txt"));
            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                data = line.split(",");
                students.add(new Student(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim()),
                        Integer.parseInt(data[3].trim()), Integer.parseInt(data[4].trim()), data[5].trim(),
                        data[6].trim(), data[7].trim()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method finds and prints the entries of students having the specified last name.
     * @param StLastName Name to seach for.
     * @param students List of students.
     */

    private static void findStLast(String StLastName, ArrayList<Student> students) {
        for (Student student : students) {
            if (student.getStLastName().equals(StLastName)) {
                System.out.println(student.getStLastName() + ", " + student.getStFirstName() + ", " +
                        student.getGrade() + ", " + student.getClassroom() + ", " +
                        student.getTLastName() + ", " + student.getTFirstName());
            }
        }
    }

    /**
     * This method finds and prints student entries having the specified last name and prints the name and
     * bus route information.
     * @param StLastName Name to search for.
     * @param students List of students.
     */

    private static void findStudentBus(String StLastName, ArrayList<Student> students) {
        for (Student student : students) {
            if (student.getStLastName().equals(StLastName)) {
                System.out.println(student.getStLastName() + ", " + student.getStFirstName() + ", " +
                        student.getBus());
            }
        }
    }

    /**
     * This method finds and prints the entries of all students that ride a certain bus route.
     * @param bus bus route of interest.
     * @param students List of students.
     */
    private static void findBus(String bus, ArrayList<Student> students) {
        int busNum = 0;
        try{
            busNum = Integer.parseInt(bus);
        }
        catch (NumberFormatException e){ return;}

        for (Student student : students)
        {
            if(student.getBus() == busNum) {
                System.out.println(student.getStLastName() + ", " + student.getStFirstName() + ", " +
                        student.getClassroom());
            }
        }
    }

    /**
     * This method prints the average GPA for all students of a specified grade level.
     * @param grade Grade level of interest.
     * @param students List of students.
     */

    private static void findAverage(String grade, ArrayList<Student> students)
    {
        int gradeLevel = 0;

        try {
            gradeLevel = Integer.parseInt(grade);
        }catch (NumberFormatException e) { return;}

        int count = 0;
        double average = 0;
        boolean validGrade = false;
        for(Student student : students)
        {
            if(gradeLevel == student.getGrade()){
                validGrade = true;
                count++;
                average += Double.parseDouble(student.getGPA());
            }
        }

        if(validGrade)
        {
            System.out.printf("Grade: %s Average GPA: %.2f\n", grade, (average/count));
        }
    }

    /**
     * This method returns a new arraylist containing only students of a specified grade level.
     * @param gradeLevel Grade level of interest.
     * @param students List of students.
     * @return New arraylist of students, all of the same grade.
     */

    private static ArrayList<Student> getCohort(String gradeLevel, ArrayList<Student> students)
    {
        int gradeNum = -1;
        try{
            gradeNum = Integer.parseInt(gradeLevel);
        }
        catch(NumberFormatException e) {}

        ArrayList<Student> cohort = new ArrayList<>();

        for(Student student : students)
        {
            if(student.getGrade() == gradeNum)
            cohort.add(student);
        }
        return cohort;
    }

    /**
     * This method finds and prints the entry of the student with the lowest GPA.
     * @param students List of students.
     */

    private static void findMinGPA(ArrayList<Student> students)
    {
        Student min = students.get(0);
        for(Student student : students)
        {
            if(Double.parseDouble(student.getGPA()) < Double.parseDouble(min.getGPA()))
            {
                min = student;
            }
        }
        System.out.println(min.getStLastName() + ", " + min.getStFirstName() + ", " + min.getGPA() + ", " +
                min.getBus() + ", " + min.getTLastName() + ", " + min.getTFirstName());
    }

    /**
     * This method finds and prints the entry of the student with the highest GPA.
     * @param students List of students.
     */

    private static void findMaxGPA(ArrayList<Student> students)
    {
        Student max = students.get(0);
        for(Student student : students)
        {
            if(Double.parseDouble(student.getGPA()) > Double.parseDouble(max.getGPA()))
            {
                max = student;
            }
        }
        System.out.println(max.getStLastName() + ", " + max.getStFirstName() + ", " + max.getGPA() + ", " +
                max.getBus() + ", " + max.getTLastName() + ", " + max.getTFirstName());
    }

    /**
     * This method finds and prints the entries of all students of a specified grade level.
     * @param gradeLevel The grade level of interest.
     * @param students List of students.
     */

    private static void findGrade(String gradeLevel, ArrayList<Student> students)
    {
        int gradeNum = -1;
        try{
            gradeNum = Integer.parseInt(gradeLevel);
        }
        catch(NumberFormatException e){ return;}

        for(Student student : students)
        {
            if(gradeNum == student.getGrade())
                System.out.println(student.getStLastName() + ", " + student.getStFirstName());
        }
    }

    /**
     * THis method finds and prints all students that have a specified teacher.
     * @param TlastName The teacher of interest last name.
     * @param students List of students.
     */

    private static void findTeacher(String TlastName, ArrayList<Student> students)
    {
        for(Student student : students)
        {
            if(student.getTLastName().equals(TlastName))
                System.out.println(student.getStLastName() + ", " + student.getStFirstName());
        }
    }

    /**
     * THis method prints the student population by grade level.
     * @param students List of students.
     */

    private static void printInfo(ArrayList<Student> students) {

        for (int i = 0; i < 7; i++)
        {
            int studentCount = 0;
            for(Student student : students)
            {
                if(student.getGrade() == i)
                    studentCount++;
            }
            System.out.println(i + " : " + studentCount);
        }
    }
}
