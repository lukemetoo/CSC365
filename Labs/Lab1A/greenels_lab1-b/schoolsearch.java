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
import java.util.Collections;
import java.util.Scanner;

public class schoolsearch {

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Teacher> teachers = new ArrayList<>();
        String[] in;

        if (!buildStudents(students) || !buildTeachers(teachers))
            return;

        Scanner scan = new Scanner(System.in);
        String menu =
                "\nS[tudent]: <lastname> [B[us]]\n" +
                        "T[eacher]: <lastname> [G[PA]]\n" +
                        "C[lass]: <number> T[eacher]|S[tudent]\n" +
                        "B[us]: <number>\n" +
                        "G[rade]: <number> [H[igh]|L[ow]|T[eacher]]\n" +
                        "A[verage]: <number> [B[us]]\n" +
                        "I[nfo]: G[rade]|R[oom]\n" +
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
                case 'C':
                    in = input.split(": | ");

                    if (in.length > 2) {
                        if (in[2].charAt(0) == 'S')
                            findStudentsByClass(in[1], students);
                        else if (in[2].charAt(0) == 'T')
                            findTeachersByClass(in[1], teachers);
                    }
                    break;
                case 'S':
                    in = input.split(": | ");

                    if (in.length > 2) {
                        if (in[2].charAt(0) == 'B')
                            findStudentBus(in[1], students);
                        else
                            findStLast(in[1], students, teachers);
                    } else if (in.length > 1)
                        findStLast(in[1], students, teachers);
                    break;

                case 'G':
                    in = input.split(": | ");
                    if (in.length > 2) {
                        if (in[2].charAt(0) == 'H') {
                            ArrayList<Student> cohort = getCohort(in[1], students);
                            if (cohort.size() > 0)
                                findMaxGPA(cohort, teachers);
                        } else if (in[2].charAt(0) == 'L') {
                            ArrayList<Student> cohort = getCohort(in[1], students);
                            if (cohort.size() > 0)
                                findMinGPA(cohort, teachers);
                        } else if (in[2].charAt(0) == 'T') {
                            ArrayList<Integer> rooms = getRoomNumbers(in[1], students);
                            if (rooms.size() > 0)
                                findTeachersByGrade(rooms, teachers);
                        }
                    } else if (in.length > 1)
                        findGrade(in[1], students);
                    break;

                case 'B':
                    in = input.split(": | ");
                    if (in.length > 1)
                        findBus(in[1], students);
                    break;

                case 'T':
                    in = input.split(": | ");
                    if(in.length > 2){
                        if(in[2].charAt(0) == 'G'){
                            int roomNum = getTeacherRoom(in[1], teachers);
                            if(roomNum > 0){
                                findTeachersByClass(Integer.toString(roomNum), teachers);
                                printAverageGPAteacher(roomNum, students);
                            }
                        }
                    }
                    else if (in.length > 1)
                        findTeacher(in[1], students, teachers);
                    break;

                case 'A':
                    in = input.split(": | ");
                    if(in.length > 2){
                        if(in[2].charAt(0) == 'B')
                            printAverageGPAbus(in[1], students);
                    }
                    else if (in.length > 1)
                        findAverage(in[1], students);
                    break;

                case 'I':
                    in = input.split(": | ");
                    if (in.length > 1)
                        if (in[1].charAt(0) == 'G')
                            printInfo(students);
                        else if (in[1].charAt(0) == 'R') {
                            ArrayList<Integer> allRooms = getAllRooms(teachers);
                            printRoomPop(allRooms, students);
                        }

                    break;

                default:
                    System.out.println("Invalid option! Please try again");
                    break;
            }
        }
    }

    /**
     * This method constructs a new Student from each entry in list.txt and adds them to an ArrayList.
     *
     * @param students ArrayList that will serve as one of two main data structures.
     * @return A boolean value indicating file read error.
     */

    private static boolean buildStudents(ArrayList students) {
        String[] data;
        String line;

        try {
            Scanner fileReader = new Scanner(new File("list.txt"));
            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                data = line.split(",");
                students.add(new Student(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim()),
                        Integer.parseInt(data[3].trim()), Integer.parseInt(data[4].trim()), data[5].trim()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method constructs a new Teacher from each entry in teachers.txt and adds them to an ArrayList.
     *
     * @param teachers An ArrayList that will serve as one of two main data structures.
     * @return A boolean value indicating file read error.
     */

    private static boolean buildTeachers(ArrayList teachers) {
        String[] data;
        String line;

        try {
            Scanner fileReader = new Scanner(new File("teachers.txt"));
            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                data = line.split(",");
                teachers.add(new Teacher(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim())));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method finds and prints the entries of students having the specified last name.
     *
     * @param StLastName Name to search for.
     * @param students   An ArrayList of students.
     * @param teachers   An ArrayList of teachers.
     */

    private static void findStLast(String StLastName, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        int classRoom = -1;
        for (Student student : students) {
            if (student.getStLastName().equals(StLastName)) {
                classRoom = student.getClassroom();
                System.out.print(student.getStLastName() + ", " + student.getStFirstName() + ", " +
                        student.getGrade() + ", " + student.getClassroom() + ", ");
            }
        }
        for (Teacher teacher : teachers) {
            if (teacher.getClassRoom() == classRoom)
                System.out.println(teacher.getTlastName() + ", " + teacher.getTfirstName());
        }
    }

    /**
     * This method calculates and prints the average GPA of all students riding a specified bus route.
     * @param bus Bus route of interest.
     * @param students An ArrayList of students.
     */

    private static void printAverageGPAbus(String bus, ArrayList<Student> students)
    {
        int busRoute = -1;
        double average = 0;
        int count = 0;
        boolean validBus = false;
        try{
            busRoute = Integer.parseInt(bus);
        }
        catch (NumberFormatException e) {return;}
        for(Student student : students)
        {
            if(student.getBus() == busRoute){
                validBus = true;
                average += Double.parseDouble(student.getGPA());
                count++;
            }
        }
        if(validBus)
            System.out.printf("Bus route: %s Average GPA: %.2f\n", bus, (average/count));
    }

    /**
     * This method calculates and prints the average GPA of all students of a taught in a specific room number.
     * @param room room number of interest
     * @param students An ArrayList of students.
     */

    private static void printAverageGPAteacher(int room, ArrayList<Student> students)
    {
        int count = 0;
        double average = 0.0;
        boolean validRoom = false;

        for(Student student : students)
        {
            if(student.getClassroom() == room){
                validRoom = true;
                count++;
                average += Double.parseDouble(student.getGPA());
            }
        }
        if(validRoom)
            System.out.printf("Average GPA %.2f\n", (average/count));
    }

    /**
     * This method finds and returns the specified teacher's room number.
     * @param tLastName Teacher of interest last name.
     * @param teachers An ArrayList of teachers.
     * @return room number of teacher of interest.
     */

    private static int getTeacherRoom(String tLastName, ArrayList<Teacher> teachers)
    {
        int room = -1;
        for(Teacher teacher : teachers)
        {
            if(teacher.getTlastName().equals(tLastName))
                room = teacher.getClassRoom();
        }
        return room;
    }

    /**
     * This method prints the names of all students being taught in the specified room of interest.
     * @param classRoom The room number of interest.
     * @param students An ArrayList of students.
     */

    private static void findStudentsByClass(String classRoom, ArrayList<Student> students) {
        int roomNum = 0;
        try {
            roomNum = Integer.parseInt(classRoom);
        } catch (NumberFormatException e) {
            return;
        }

        for (Student student : students) {
            if (student.getClassroom() == roomNum)
                System.out.println(student.getStLastName() + ", " + student.getStFirstName() + ", " + student.getGrade() +
                        ", " + student.getClassroom() + ", " + student.getBus() + ", " + student.getGPA());
        }
    }

    /**
     * This method prints the names of all teachers teaching in a specified classroom.
     * @param classRoom The room number of interest.
     * @param teachers An ArrayList of teachers.
     */

    private static void findTeachersByClass(String classRoom, ArrayList<Teacher> teachers) {
        int roomNum = 0;
        try {
            roomNum = Integer.parseInt(classRoom);
        } catch (NumberFormatException e) {
            return;
        }

        for (Teacher teacher : teachers) {
            if (teacher.getClassRoom() == roomNum)
                System.out.println(teacher.getTlastName() + ", " + teacher.getTfirstName() + ", " + teacher.getClassRoom());
        }
    }

    /**
     * This method prints the names of the teachers that teach in the rooms of interest.
     * @param rooms An ArrayList of room numbers.
     * @param teachers An ArrayList of teachers.
     */

    private static void findTeachersByGrade(ArrayList<Integer> rooms, ArrayList<Teacher> teachers)
    {
        for(Teacher teacher : teachers) {
            for (Integer i : rooms)
                if(teacher.getClassRoom() == i) {
                    System.out.println(teacher.getTlastName() + ", " + teacher.getTfirstName() + ", " +
                            teacher.getClassRoom());
                }
        }
    }

    /**
     * This method returns an ArrayList of all room numbers the specified grade is taught in.
     * @param grade The specified grade of interest.
     * @param students The list of students.
     * @return An ArrayList containing all room numbers the specified grade is taught in.
     */

    private static ArrayList<Integer> getRoomNumbers(String grade, ArrayList<Student> students)
    {
        int gradeLevel = -1;
        try{
            gradeLevel = Integer.parseInt(grade);
        }
        catch (NumberFormatException e) {}

        ArrayList<Integer> rooms = new ArrayList<>();

        for(Student student : students)
        {
            if(student.getGrade() == gradeLevel)
                if(!rooms.contains(student.getClassroom()))
                    rooms.add(student.getClassroom());
        }
        return rooms;
    }

    /**
     * This method returns an ArrayList containing all room numbers that students are taught in.
     * @param teachers A list of teachers.
     * @return An ArrayList containing all room numbers.
     */

    private static ArrayList<Integer> getAllRooms(ArrayList<Teacher> teachers)
    {
        ArrayList<Integer> allRooms = new ArrayList<>();
        for(Teacher teacher : teachers)
        {
            if(!allRooms.contains(teacher.getClassRoom()))
                allRooms.add(teacher.getClassRoom());
        }
        return allRooms;
    }

    /**
     * This method counts the number of students in each room and then prints the totals.
     * @param rooms A list of all room numbers.
     * @param students A list of students.
     */

    private static void printRoomPop(ArrayList<Integer> rooms, ArrayList<Student> students)
    {
        int roomPop = 0;
        Collections.sort(rooms);

        for(Integer i : rooms){
            for(Student student : students){
                if(student.getClassroom() == i)
                    roomPop++;
            }
            System.out.println(i + " : " +roomPop);
            roomPop = 0;
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
     * @param students An ArrayList of students.
     * @param teachers An ArrayList of teachers
     */

    private static void findMinGPA(ArrayList<Student> students, ArrayList<Teacher> teachers)
    {
        int classRoom = -1;
        Student min = students.get(0);
        for(Student student : students)
        {
            if(Double.parseDouble(student.getGPA()) < Double.parseDouble(min.getGPA()))
            {
                min = student;
                classRoom = student.getClassroom();
            }
        }

        System.out.print(min.getStLastName() + ", " + min.getStFirstName() + ", " + min.getGPA() + ", " +
                min.getBus() + ", ");

        for(Teacher teacher : teachers){
            if(teacher.getClassRoom() == classRoom)
                System.out.println(teacher.getTlastName() + ", " + teacher.getTfirstName());
        }
    }

    /**
     * This method finds and prints the entry of the student with the highest GPA.
     * @param students An ArrayList of students.
     * @param teachers An ArrayList of teachers.
     */

    private static void findMaxGPA(ArrayList<Student> students, ArrayList<Teacher> teachers)
    {
        int classRoom = -1;
        Student max = students.get(0);
        for(Student student : students)
        {
            if(Double.parseDouble(student.getGPA()) > Double.parseDouble(max.getGPA()))
            {
                max = student;
                classRoom = student.getClassroom();
            }
        }
        System.out.print(max.getStLastName() + ", " + max.getStFirstName() + ", " + max.getGPA() + ", " +
                max.getBus() + ", ");

        for(Teacher teacher : teachers)
        {
            if(teacher.getClassRoom() == classRoom)
                System.out.println(teacher.getTlastName() + ", " + teacher.getTfirstName());
        }
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

    private static void findTeacher(String TlastName, ArrayList<Student> students, ArrayList<Teacher> teachers)
    {
        int classRoom = -1;
        for(Teacher teacher : teachers)
        {
            if(teacher.getTlastName().equals(TlastName))
                classRoom = teacher.getClassRoom();
        }

        for(Student student : students)
        {
            if(student.getClassroom() == classRoom)
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
