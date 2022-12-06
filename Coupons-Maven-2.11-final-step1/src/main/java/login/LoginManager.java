package login;

import beans.Company;
import beans.Customer;
import dao.*;
import facade.AdminFacade;
import facade.ClientFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginManager {

    private static LoginManager instance = new LoginManager();

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) throws SQLException, InterruptedException {
        if (clientType == ClientType.Administrator) {
            if (email == "admin@admin.com" && password == "admin") {
                AdminFacade adminFacade = new AdminFacade();
                System.out.println("System is running, Admin logged in");
                return adminFacade;
            }
        }
        if (clientType == ClientType.Company) {
            CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();
            ArrayList<Company> companies = companiesDBDAO.getAllCompanies();
            for (Company c: companies) {
                if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                    int companyID = c.getId();
                    String companyName = c.getName();
                    CompanyFacade companyFacade = new CompanyFacade(companyID);
                    System.out.println("client company " + companyName + " logged in");
                    return companyFacade;
                }
            }
        }
        if (clientType.equals(ClientType.Customer)) {
            CustomersDBDAO customersDBDAO = new CustomersDBDAO();
            ArrayList<Customer> customers = customersDBDAO.getAllCustomers();
            for (Customer c:customers) {
                if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                    Customer customer1 = c;
                    int customerID = c.getId();
                    CustomerFacade customerFacade = new CustomerFacade(customerID);
                    System.out.println("customer is logged into the system");
                    System.out.println();
                    return customerFacade;
                }

            }
        }
        System.out.println("login error, cannot connect");
        System.out.println("please check that user details and password are correct");
        return null;
    }


}

