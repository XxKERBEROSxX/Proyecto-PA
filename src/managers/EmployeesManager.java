/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import models.Employee;
import utils.CountElements;
import utils.TextUtils;

/**
 *  it Controls the "Employees" objects in the ArrayList
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class EmployeesManager {


    public static EmployeesManager sInstance;
    private final ArrayList<Employee> employeesList;

    /**
     * it declares employeesList like a ArrayList
     */
    public EmployeesManager() {
        employeesList = new ArrayList<>();
    }
    
    /**
     * it Creates an instance to work with this class
     * @return
     */
    public static EmployeesManager getInstance() {
        if (sInstance == null) {
            sInstance = new EmployeesManager();
        }
        return sInstance;
    }
    
    /**
     * it adds a new "Employee" object to the list
     * @param username String username
     * @param name Strign name
     * @param password String password
     * @param accountType int accountType. 1 = Normal Account, 2 = Administrator Account
     * @return true if employee was added successfully 
     */
    public boolean addEmployee(String username, String name, String password, int accountType) {
        String stringPassword = String.valueOf(password);
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(stringPassword)) {
            if(getEmployeeIndexByUserName(username) != -1){
                JOptionPane.showMessageDialog(null, "Ya existe el empleado");
            } else {
                employeesList.add(new Employee(username, name, password, accountType));
                System.out.println("Total employees: "+ CountElements.count(employeesList));
                return true;
            }
        }
        return false;
    }
    
    /**
     * gets object employee 
     * @param employeeIndex index of employee in the arrayList
     * @return employee
     */
    public Employee getEmployee(int employeeIndex) {
        if (employeeIndex != -1 && employeeIndex < employeesList.size()) {
            return employeesList.get(employeeIndex);
        }
        return null;
    }
    
    /**
     * it Searches an "Employee" object
     * @param username string username of the employee NOT their name
     * @return the exact username or the one that contains the username
     */
    public int getEmployeeIndexByUserName(String username) {
        int equalsSelection = -1;
        int containsSelection = -1;
        if (!TextUtils.isEmpty(username)) {
            for (Employee employee : employeesList) {
                if (username.equals(employee.getName())) {
                    equalsSelection = employeesList.indexOf(employee);
                    break;
                } else {
                    if (username.contains(employee.getUsername())) {
                        containsSelection = employeesList.indexOf(employee);
                    }
                }
            }
        }
        return equalsSelection == -1 && containsSelection == -1 ? -1 : equalsSelection != -1 ? equalsSelection : containsSelection;
    }
    
    /**
     *  It removes an "Employee" objetc in the list
     * @param employeeIndex index of the employee in the arraylist
     * @return true if employee deleted successfully 
     */
    public boolean deleteEmployee(int employeeIndex) {
        if (employeeIndex == -1 || employeeIndex >= employeesList.size()) {
            return false;
        } else {
            employeesList.remove(employeeIndex);
            return true;
        }
    }
    
    /**
     * it Updates "Employee" object parameters 
     * @param employeeIndex
     * @param username
     * @param name
     * @param password
     * @param accountType
     * @return true if employee updated successfully
     */
    public boolean updateEmployee(int employeeIndex, String username, String name, String password, int accountType) {
        String stringPassword = String.valueOf(password);
        if (employeeIndex != -1 && employeeIndex < employeesList.size() && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(stringPassword)) {
            employeesList.get(employeeIndex).setUsername(username);
            employeesList.get(employeeIndex).setName(name);
            employeesList.get(employeeIndex).setPassword(password);
            
            return true;
        }
        return false;
    }
    /**
     * The list that gets the list of employees
     * @return list of employees
     */
    public ArrayList<Employee> getEmp(){
        return employeesList;
    }
            

}