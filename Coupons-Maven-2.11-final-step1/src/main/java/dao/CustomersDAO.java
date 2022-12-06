package dao;

import beans.Customer;

import java.util.ArrayList;

public interface CustomersDAO {
    boolean isCustomerExists (String email, String password);
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer,String firstName,String lastName, String email,String password);
    void deleteCustomer(int customerID);
    ArrayList<Customer> getAllCustomers();
    Customer getOneCustomer(int customerID);


}
