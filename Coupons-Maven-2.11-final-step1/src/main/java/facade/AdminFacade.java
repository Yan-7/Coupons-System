package facade;

import beans.Company;
import beans.Customer;
import dao.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminFacade extends ClientFacade {

    private final static String email = "admin@admin.com";
    private final static String password = "admin";

    CompaniesDAO companiesDBDAO = companiesDAO;
    CustomersDAO customersDBDAO = customersDAO;
    CouponsDAO couponsDBDAO = couponsDAO;

    public AdminFacade() throws SQLException {
    }

    public boolean login(String password, String email) {
        if (email == "admin@admin.com" && password == "admin") {
            return true;
        }
        return false;
    }
//v
    public void addCompany(Company company) throws SQLException, InterruptedException {
        String email = company.getEmail();
        String password = company.getPassword();
        ArrayList<Company> allCompanies = companiesDBDAO.getAllCompanies();
        //checks if mail exists
        for (Company c:allCompanies) {
            if (company.getEmail().equals(c.getEmail())) {
                System.out.println("company mail already exists, cannot add");
                return;
            }
        }
        //checks if name exists
        for (Company c:allCompanies) {
            if (company.getName().equals(c.getName())) {
                System.out.println("company name already exists, cannot add");
                return;
            }
        }
        companiesDBDAO.addCompany(company);
        System.out.println("company " + company.getName() + " added" );
    }
//v
    public void updateCompanyFacade(Company company,String email2,String password2) throws SQLException, InterruptedException {
        boolean checkIfExists = companiesDBDAO.isCompanyExists(company.getEmail(),company.getPassword());
        if (checkIfExists == true) {
         companiesDBDAO.updateCompany(company,email2,password2);
            System.out.println("company "+ company.getName() +" updated");
            return;
        }
        System.out.println("company was not found");

    }
//v
    public void deleteCompany(int companyID) throws SQLException, InterruptedException {
        companiesDBDAO.deleteCompany(companyID);
    }
//v
    // TODO: 24/10/2022 make nice print
    public ArrayList<Company> getAllCompanies() throws SQLException, InterruptedException {
        ArrayList<Company> companies = companiesDBDAO.getAllCompanies();
        System.out.println("All companies:");
        System.out.println(companies);
        return companies;
    }

//v
    public Company getCompanyById(int companyID) throws SQLException, InterruptedException {
        System.out.println("company details: ");
        System.out.println(companiesDBDAO.getOneCompany(companyID));
        return companiesDBDAO.getOneCompany(companyID);
    }
//v
    public void addCustomer(Customer customer) {
        customersDBDAO.addCustomer(customer);
    }
//v
    public void updateCustomer(Customer customer, String firstName, String lastName, String email, String password) {
        customersDBDAO.updateCustomer(customer,firstName,lastName,email,password);
    }
//v
    public void deleteCustomer(int customerID) {
        customersDBDAO.deleteCustomer(customerID);
    }
//v
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = customersDBDAO.getAllCustomers();
        return customers;
    }
//v
    public Customer getCustomerByID(int customerID) {
        Customer customer = customersDBDAO.getOneCustomer(customerID);
        System.out.println("getting customer:");
        System.out.println(customer);
        return customer;
    }
}
