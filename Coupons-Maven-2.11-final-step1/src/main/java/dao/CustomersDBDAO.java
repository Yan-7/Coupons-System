package dao;

import beans.Coupon;
import beans.Customer;
import db.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;

public class CustomersDBDAO implements CustomersDAO {

    static CouponsDAO couponsDBDAO;

    static {
        try {
            couponsDBDAO = new CouponsDBDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCustomerExists(String email, String password) {
        final String QUERY_IS_CUSTOMER_EXISTS = "select exists( SELECT * FROM `couponsystem`.`customers` where email=? and password=?) as res";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(QUERY_IS_CUSTOMER_EXISTS);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                int amount = resultSet.getInt(1);
                if (amount > 0) {
                    System.out.println("customer id: " + resultSet.getString(1) + " exists");
                    ConnectionPool.getInstance().restoreConnection(connection);
                    return true;
                } else {
                    System.out.println("customer does not exist in the system, need to be added");
                    ConnectionPool.getInstance().restoreConnection(connection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void addCustomer(Customer customer) {
        if (isCustomerExists(customer.getEmail(), customer.getPassword())) {
            System.out.println("customer already exists in the system, ");
            return;

        }
        String ADD_COMPANY = "INSERT INTO `couponsystem`.`customers` (`ID`, `FIRST_NAME`, `LAST_NAME`, `EMAIL`, `PASSWORD`) VALUES (?, ?, ?, ?,?)";
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(ADD_COMPANY, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, customer.getId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getLastName());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getPassword());
            pstmt.execute();
            System.out.println("customer " + customer.getFirstName() + " added");
            ConnectionPool.getInstance().restoreConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer, String firstName, String lastName, String email, String password) {
        // cannot update customer id
        final String UPDATE_Customer = "UPDATE `couponsystem`.`customers` SET  `FIRST_NAME` = ?,`LAST_NAME`=? ,`EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?)";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_Customer, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, customer.getId());
            preparedStatement.execute();
            ConnectionPool.getInstance().restoreConnection(connection);
            System.out.println("customer +  updated");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int customerID) {
        final String DELETE_CUSTOMER = "DELETE FROM `couponsystem`.`customers` WHERE (`ID` = ?)";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMER);
            statement.setInt(1, customerID);
            statement.executeUpdate();
            ConnectionPool.getInstance().restoreConnection(connection);
            System.out.println("customer deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        final String GET_ALL_CUSTOMERS = "SELECT * FROM couponsystem.customers";
        ArrayList<Customer> arrayList1 = new ArrayList();
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CUSTOMERS);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt(1));
                customer.setFirstName(resultSet.getString(2));
                customer.setLastName(resultSet.getString(3));
                customer.setEmail(resultSet.getString(4));
                customer.setPassword(resultSet.getString(5));
                arrayList1.add(customer);
            }
            ConnectionPool.getInstance().restoreConnection(connection);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("all customers: ");
        System.out.println(arrayList1);
        return arrayList1;
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        final String GET_ONE_Customer = "SELECT * FROM couponsystem.customers where id = ?";
        final String GET_CUSTOMER_COUPONS = "SELECT * FROM couponsystem.customers_vs_coupons where customer_id = ?";
        try {
            Customer customer = new Customer();
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_Customer);
            statement.setInt(1, customerID);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            ConnectionPool.getInstance().restoreConnection(connection);

            //coupons VS customers quarry:
            Connection connection1 = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement1 = connection1.prepareStatement(GET_CUSTOMER_COUPONS);
            statement1.setInt(1,customerID);
            statement1.executeQuery();
            ResultSet resultSet1 = statement1.getResultSet();
            ConnectionPool.getInstance().restoreConnection(connection1);

            if (resultSet.next()) {
                customer.setId(resultSet.getInt(1));
                customer.setFirstName(resultSet.getString(2));
                customer.setLastName(resultSet.getString(3));
                customer.setEmail(resultSet.getString(4));
                customer.setPassword(resultSet.getString(5));
                //customer coupons:
                ArrayList<Integer> couponsIDs = new ArrayList<>();
                ArrayList<Coupon> allCoupons = couponsDBDAO.getAllCoupons();
                ArrayList<Coupon> customerCoupons = new ArrayList<>();

                while (resultSet1.next()) {
                    if (customer.getId() == resultSet1.getInt("CUSTOMER_ID"));
                    couponsIDs.add(resultSet1.getInt("COUPON_ID"));
                    for (Integer id : couponsIDs) {
                        for (Coupon c : allCoupons) {
                            if (c.getId() == id) {
                                customerCoupons.add(c);
                            }
                        }

                    }
                    customer.setCoupons(customerCoupons);
                }
                return customer;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("customer not found");
        return null;
    }

}
