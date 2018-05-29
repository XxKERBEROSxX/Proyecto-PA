package managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Student;
import utils.TextUtils;

/**
 * it controls the students objects in the ArrayList
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class StudentsManager {
    
    private static ArrayList<Student> studentList;
    public static StudentsManager sInstance;

     /**
     * it declares studentList like a ArrayList
     */
    public StudentsManager() {
        studentList= new ArrayList<>();
    }
    /**
     * it creates an instance to work with this class
     * @return instance sInstance
     */
    public static StudentsManager getInstance() {
        if (sInstance == null) {
            sInstance = new StudentsManager();
        }
        return sInstance;
    }
    /**
     * it adds a new "student" object to the list
     * @param id string id of the student
     * @param name string name of the student
     * @param address string address of the student
     * @param phoneNumber string phone number of the student
     * @return boolean true if student was added successfully 
     */
    public boolean addStudent(String id, String name, String address, String phoneNumber) {
        if (!TextUtils.isEmpty(id)) {
            if (getStudentIndexById(id) != -1) {
                JOptionPane.showMessageDialog(null, "Error, ya existe el estudiante");
            } else {
                studentList.add(new Student(id, name, address, phoneNumber));
                return true;
            }

        }
        return false;
    }
    
    /**
     * gets student 
     * @param studentIndex index of the student in the arraylist
     * @return Student student
     */
    public Student getStudent(int studentIndex) {
        if (studentIndex != -1 && studentIndex < studentList.size()) {
            return studentList.get(studentIndex);
        }
        return null;
    }
    /**
     * it Searches for a "Student" object using the id parameter
     * @param id string id of the student
     * @return int student index in the arraylist
     */
    public int getStudentIndexById(String id) {
        int studentIndex = -1; 
        if (!TextUtils.isEmpty(id)) {
            for (Student student : studentList) {
                if (id.equals(student.getId())) {
                    studentIndex = studentList.indexOf(student);
                } 
            }
        } 
        return studentIndex;
    }
    /**
     * It removes a "Student" object of the list
     * @param studentIndex index of the student in the arraylist
     * @return boolean true if it was deleted successfully
     */
    public boolean deleteStudent(int studentIndex) {
        if (studentIndex == -1) {
            return false;
        } else {
            studentList.remove(studentIndex);
            return true;
        }
    }
    /**
     * it Updates "Student" object parameters 
     * @param studentIndex int index of the student in the arraylist
     * @param id string student id
     * @param name string student name
     * @param address string student address
     * @param phoneNumber string student phone number
     * @return boolean true if update was successfull 
     */
    public boolean updateStudent(int studentIndex, String id, String name, String address, String phoneNumber) {
        if (studentIndex != -1 && studentIndex < studentList.size() && !TextUtils.isEmpty(id)) {
            studentList.get(studentIndex).setId(id);
            studentList.get(studentIndex).setName(name);
            studentList.get(studentIndex).setAddress(address);
            studentList.get(studentIndex).setPhone_number(phoneNumber);
            return true;
        }
        return false;
    }
    /**
     * it Gets the students list
     * @return arraylist studentList
     */
    public ArrayList<Student> getStudents() {
        return studentList;
    }
      /**
     * it Deletes all the "studens" objects in the list
     */
    public void clearAll() {
        studentList.clear();
    }
}