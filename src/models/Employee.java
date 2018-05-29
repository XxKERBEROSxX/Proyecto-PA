/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

/**
 * it contains the employee's parameters that are saved later in the list
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class Employee implements Serializable{
    private String username; 
    private String name; 
    private String password; 
    private int accountType; 
    
    int ADMIN = 1; 
    int NORMAL = 2; 
    
     /**
     * 
     * @param username String of the username
     * @param name String of the user's name
     * @param password String of password for current user
     * @param accountType int of the account Type. 1 = Administrator, 2 = Normal user
     */
    
    public Employee(String username, String name, String password, int accountType) {
        this.name = name;
        this.username = username; 
        this.password = password; 
        this.accountType = accountType; 
    }
     /**
     * it gets the parameter username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * it assigns the parameter username 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * it gets the parameter name
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * it assigns the parameter name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * it gets the parameter password
     * @return
     */
    public String getPassword() {
        return password;
    }
    /**
     * it assigns the parameter password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * it gets the parameter accountType
     * @return
     */
    public int getAccountType() {
        return accountType;
    }
    /**
     * it assigns the parameter accountType
     * @param accountType
     */
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
    
    
}
