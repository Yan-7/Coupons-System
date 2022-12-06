package dao;

import beans.Company;
import beans.Coupon;
import db.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;

public class CompaniesDBDAO implements CompaniesDAO {


    public CompaniesDBDAO() throws SQLException {
    }


    @Override
    public boolean isCompanyExists(String email, String pass) throws SQLException, InterruptedException {
        final String QUERY_IS_COMPANY_EXIST = "select exists( SELECT * FROM couponsystem.companies where email=? and password=?) as res";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(QUERY_IS_COMPANY_EXIST);
            statement.setString(1, email);
            statement.setString(2, pass);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            return resultSet.next(); //returns boolean

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().restoreConnection(conn);
        }
        System.out.println("cannot find company");
        return false;
    }

    @Override
    public void addCompany(Company company) throws SQLException, InterruptedException {
        String ADD_COMPANY = "INSERT INTO `couponsystem`.`companies` ( `NAME`, `EMAIL`, `PASSWORD`) VALUES ( ?, ?, ?)";
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(ADD_COMPANY, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getEmail());
            pstmt.setString(3, company.getPassword());
            pstmt.execute();
            ConnectionPool.getInstance().restoreConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void updateCompany(Company company, String email, String password) throws SQLException, InterruptedException {
        //not allowed changing id or name
        final String UPDATE_COMPANY = "UPDATE `couponsystem`.`companies` SET  `EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?)";
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement ptsmt = conn.prepareStatement(UPDATE_COMPANY, PreparedStatement.RETURN_GENERATED_KEYS);
            ptsmt.setString(1, email);
            ptsmt.setString(2, password);
            ptsmt.setInt(3, company.getId());
            ptsmt.execute();
            ConnectionPool.getInstance().restoreConnection(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(int companyID) throws SQLException, InterruptedException {
        //delete the coupons
        Company company = getOneCompany(companyID);
        ArrayList<Coupon> nullCoupons = new ArrayList<>();
        company.setCoupons(nullCoupons);
        final String DELETE_COMPANY = "DELETE FROM `couponsystem`.`companies` WHERE (`ID` = ?)";
        final String DELETE_COUPON_BY_COMPANY_ID = "DELETE FROM `couponsystem`.`coupons` WHERE (`COMPANY_ID` = ?)";

        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement2 = conn.prepareStatement(DELETE_COUPON_BY_COMPANY_ID);
            statement2.setInt(1, companyID);
            statement2.execute();
            ConnectionPool.getInstance().restoreConnection(conn);

            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COMPANY);
            statement.setInt(1, companyID);
            statement.execute();
            ConnectionPool.getInstance().restoreConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("company " + company.getName()+ " deleted");
    }

    @Override
    public ArrayList<Company> getAllCompanies() throws SQLException, InterruptedException {
        final String GET_ALL_COMPANIES = "SELECT * FROM couponsystem.companies";
        ArrayList<Company> allCompanies = new ArrayList<>();
        CouponsDAO couponsDAO = new CouponsDBDAO();
        try {

            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ALL_COMPANIES);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            ArrayList<Coupon> allCoupons = couponsDAO.getAllCoupons();

            while (resultSet.next()) {
                Company currentCompany = new Company();
                currentCompany.setId(resultSet.getInt(1));
                currentCompany.setName(resultSet.getString(2));
                currentCompany.setEmail(resultSet.getString(3));
                currentCompany.setPassword(resultSet.getString(4));

                //add coupons from coupons list to specific company:
                ArrayList<Coupon> companyCoupons = new ArrayList<>();
                for (Coupon c : allCoupons) {
                    if (c.getCompanyId() == currentCompany.getId()) {
                        companyCoupons.add(c);

                    }
                }
                currentCompany.setCoupons(companyCoupons);
                allCompanies.add(currentCompany);
            }
            ConnectionPool.getInstance().restoreConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCompanies;
    }

    @Override
    public Company getOneCompany(int companyID) throws SQLException, InterruptedException {
        final String GET_ONE_COMPANY = "SELECT * FROM couponsystem.companies where id = ?";
        try {
            Company company = new Company();
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(GET_ONE_COMPANY);
            statement.setInt(1, companyID);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                company.setId(companyID);
                company.setName(resultSet.getString(2));
                company.setEmail(resultSet.getString(3));
                company.setPassword(resultSet.getString(4));
                ConnectionPool.getInstance().restoreConnection(conn);
                return company;

            }
//             currentCompany.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("no such company");
        return null;

    }
}
