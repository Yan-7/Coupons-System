package facade;

import dao.*;

import java.sql.SQLException;

public abstract class ClientFacade {

    public CompaniesDAO companiesDAO = new CompaniesDBDAO();

    public CustomersDAO customersDAO = new CustomersDBDAO();

    public CouponsDAO couponsDAO = new CouponsDBDAO();

    protected ClientFacade() throws SQLException {
    }

    public boolean login(String email, String password) throws SQLException, InterruptedException {
        return true;
    }


}
