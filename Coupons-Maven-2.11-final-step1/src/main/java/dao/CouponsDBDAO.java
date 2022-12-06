package dao;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import db.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import  exceptions.CouponsException;

public class CouponsDBDAO implements CouponsDAO {

    public CouponsDBDAO() throws SQLException {
    }
//v
    @Override
    public void addCoupon(Coupon coupon) {
        final String ADD_COUPON = "INSERT INTO `couponsystem`.`coupons` (`ID`, `COMPANY_ID`, `CATEGORY_ID`, `TITLE`, `DESCRIPTION`, `START_DATE`, `END_DATE`, `AMOUNT`, `PRICE`, `IMAGE`) VALUES (?, ?, ?, ?, ?, ?,?, ?, ?,?)";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_COUPON, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, coupon.getId());
            statement.setInt(2, coupon.getCompanyId());

            Category category1 = coupon.getCategory();
            int categoryIndex = category1.ordinal();
            statement.setInt(3, categoryIndex);

            statement.setString(4, coupon.getTitle());
            statement.setString(5, coupon.getDescription());

            statement.setDate(6, (Date) coupon.getStartDate());
            statement.setDate(7, (Date) coupon.getEndDate());
            statement.setInt(8, coupon.getAmount());
            statement.setInt(9, (int) coupon.getPrice());
            statement.setString(10, coupon.getImage());
            statement.executeUpdate();

            System.out.println("Coupon " + coupon.getId() + " added");
            ConnectionPool.getInstance().restoreConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void updateCoupon(Coupon coupon) throws CouponsException {

        final String UPDATE_COUPON = "UPDATE `couponsystem`.`coupons` SET `CATEGORY_ID` = ?, `TITLE` = ?, `DESCRIPTION` = ?, `START_DATE` = ?, `END_DATE` = ?, `AMOUNT` = ?, `PRICE` = ? WHERE (`ID` = ?)";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_COUPON, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, coupon.getCategory().ordinal());
            statement.setString(2, coupon.getTitle());
            statement.setString(3, coupon.getDescription());
            statement.setDate(4, (Date) coupon.getStartDate());
            statement.setDate(5, (Date) coupon.getEndDate());
            statement.setInt(6, coupon.getAmount());
            statement.setDouble(7,  coupon.getPrice());
            statement.setInt(8, coupon.getId());
            statement.executeUpdate();
            System.out.println("new coupon data: ");
            System.out.println(coupon);

            ConnectionPool.getInstance().restoreConnection(connection);
            System.out.println("Coupon updated");
        } catch (SQLException e) {
            throw new CouponsException("updateCoupon failed", e);
        }

    }

    @Override
    public void deleteCoupon(int couponID) {
        final String DELETE_COUPON = "DELETE FROM `couponsystem`.`coupons` WHERE (`ID` = ?)";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON);
            statement.setInt(1, couponID);
            statement.execute();
            ConnectionPool.getInstance().restoreConnection(connection);
            System.out.println("coupon " + couponID + " deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() {
        final String GET_ALL_COUPONS = "SELECT * FROM couponsystem.coupons";

        ArrayList<Coupon> list = null;
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_COUPONS);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            list = new ArrayList<>();
            while (resultSet.next()) {

                Coupon currentCoupon = new Coupon();

                currentCoupon.setId(resultSet.getInt(1));
                currentCoupon.setCompanyId(resultSet.getInt(2));
                int couponIndex = resultSet.getInt(3);
                currentCoupon.setCategory(Category.values()[couponIndex]);
                currentCoupon.setTitle(resultSet.getString(4));
                currentCoupon.setDescription(resultSet.getString(5));
                currentCoupon.setStartDate(resultSet.getDate(6));
                currentCoupon.setEndDate(resultSet.getDate(7));
                currentCoupon.setAmount(resultSet.getInt(8));
                currentCoupon.setPrice(resultSet.getInt(9));
                //coupons list
                list.add(currentCoupon);
            }
            ConnectionPool.getInstance().restoreConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        final String GET_ONE_COUPON = "SELECT * FROM couponsystem.coupons where id = ?";
        try {
            Coupon currentCoupon = new Coupon();
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ONE_COUPON);
            statement.setInt(1, couponID);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                currentCoupon.setId(resultSet.getInt(1));
                currentCoupon.setCompanyId(resultSet.getInt(2));
//                currentCoupon.setCategory(resultSet.));
                currentCoupon.setTitle(resultSet.getString(4));
                currentCoupon.setDescription(resultSet.getString(5));
                currentCoupon.setStartDate(resultSet.getDate(6));
                currentCoupon.setStartDate(resultSet.getDate(7));
                currentCoupon.setAmount(resultSet.getInt(8));
                currentCoupon.setPrice(resultSet.getInt(9));

                return currentCoupon;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        System.out.println("coupon not found");
        return null;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) {
        final String ADD_COUPON_PURCHASE = "INSERT INTO couponsystem.customers_vs_coupons VALUES (?,?);";


        try {
            //FIND COUPON
            Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_COUPON_PURCHASE);

            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            statement.execute();
            ConnectionPool.getInstance().restoreConnection(connection);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {
        final String DELETE_COUPON_PURCHASE = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`CUSTOMER_ID` = ?) and (`COUPON_ID` = ?)";

        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_COUPON_PURCHASE);

            statement.setInt(1, customerID);
            statement.setInt(2, couponID);
            statement.execute();
            System.out.println("coupon deleted");
            ConnectionPool.getInstance().restoreConnection(connection);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }
}
