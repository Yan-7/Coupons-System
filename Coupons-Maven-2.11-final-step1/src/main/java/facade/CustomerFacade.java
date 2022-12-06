package facade;

import beans.Category;
import beans.Coupon;
import beans.Customer;
import dao.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerFacade extends ClientFacade {

    private int customerID;
    CompaniesDAO companiesDBDAO = companiesDAO;
    CustomersDAO customersDBDAO = customersDAO;
    CouponsDAO couponsDBDAO = couponsDAO;


    public CustomerFacade(int customerID) throws SQLException {
        this.customerID = customerID;
    }


    public boolean login(String email, String password) throws SQLException, InterruptedException {
        Customer customer = customersDBDAO.getOneCustomer(customerID);
        if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
            System.out.println("validation confirmed");
            return true;
        }
        System.out.println("company login error");
        return false;
    }

    //cannot purchase coupon more than once.
    //cannot purchase if quantity is 0.
    // cannot purchase if expired.
    // after purchase reduce amount by 1.


    public void purchaseCoupon(Coupon coupon) {
        Customer customer = customersDBDAO.getOneCustomer(customerID);
        ArrayList<Coupon> customerCoupons = customer.getCoupons();
        if (customerCoupons != null) {
            for (Coupon c : customerCoupons) {
                if (c.equals(coupon)) {
                    System.out.println("coupon already exists");
                    return;
                }
                if (customerCoupons.size() < 1) {
                    System.out.println("no more coupons left");
                    return;
                }

                if (coupon.getEndDate().after(Date.valueOf(LocalDate.now()))) {
                    System.out.println("coupon expired");
                    return;
                }
            }
        }
            couponsDBDAO.addCouponPurchase(customerID, coupon.getId());
        if (customerCoupons != null) {
            customerCoupons.add(coupon);
        }
            customer.setCoupons(customerCoupons);
            System.out.println("coupon id " + coupon.getId() +  " purchased");
        }


        public ArrayList<Coupon> getCustomerCoupons () {
            Customer customer = customersDBDAO.getOneCustomer(customerID);
            ArrayList<Coupon> customerCoupons = customer.getCoupons();
            System.out.println(customer.getFirstName()+ " coupons:");
            return customerCoupons;
        }


        public ArrayList<Coupon> getCustomerCouponsByCategory (Category category){
            Customer customer = customersDBDAO.getOneCustomer(customerID);
            ArrayList<Coupon> coupons = customer.getCoupons();
            ArrayList<Coupon> couponsByCategory = new ArrayList<>();
            for (Coupon c : coupons) {
                if (c.getCategory().equals(category)) {
                    couponsByCategory.add(c);
                }
            }
            System.out.println(customer.getFirstName() + " coupons:");
            return couponsByCategory;
        }


        public ArrayList<Coupon> getCustomerCouponsByPrice ( double maxPrice){
            Customer customer = customersDBDAO.getOneCustomer(customerID);
            ArrayList<Coupon> coupons = customer.getCoupons();
            ArrayList<Coupon> couponsByPrice = new ArrayList<>();
            for (Coupon c : coupons) {
                if (c.getPrice() <= maxPrice) {
                    couponsByPrice.add(c);
                }
            }
            System.out.println(customer.getFirstName() + " coupons:");
            return couponsByPrice;
        }


        public Customer getCustomerDetails () {
            Customer customer = customersDBDAO.getOneCustomer(customerID);
            System.out.println(customer.getFirstName()+ " details:");
            return customer;
        }

//    ----------
    }

