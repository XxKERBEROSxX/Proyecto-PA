package models;

import java.io.Serializable;

/**
 * contains the student's parameters that are saved later in the list
 *
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class Student implements Serializable{

    private String name;
    private String id;
    private String address;
    private String phone_number;
    
    /**
     * 
     * @param id string id of the student 
     * @param name string full name of the student (last name and name)
     * @param address string address of the student
     * @param phone_number string phone number of the student (any format) 
     */

    public Student(String id, String name, String address, String phone_number) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    /**
     * it gets the parameter name
     * @return string name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * it assigns the parameter name
     *
     * @param name string name of the student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * it gets the parameter id
     *
     * @return string id of the student
     */
    public String getId() {
        return id;
    }

    /**
     * it assigns the parameter id
     *
     * @param id id of the student
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * it gets the parameter address
     *
     * @return string address of the student
     */
    public String getAddress() {
        return address;
    }

    /**
     * it assigns the parameter address
     * @param address string address of the student
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * it gets the parameter phone_number
     *
     * @return string phone number of the student
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * it assigns the parameter phone_number
     *
     * @param phone_number string phone number of the student
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


}


