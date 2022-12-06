package facade;

import beans.Category;
import beans.Company;
import beans.Coupon;
import dao.*;
import exceptions.CouponsException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyFacade extends ClientFacade {

    private int companyID; // the id of the company that made login


    CompaniesDAO companiesDBDAO = companiesDAO;
    CustomersDAO customersDBDAO = customersDAO;
    CouponsDAO couponsDBDAO = couponsDAO;



    public int getCompanyID() {
        return companyID;
    }


    public CompanyFacade(int companyID) throws SQLException {
        this.companyID = companyID;
    }

    public boolean login(String email, String password) throws SQLException, InterruptedException {
        Company company = companiesDBDAO.getOneCompany(companyID);
        if (company.getEmail().equals(email) && company.getPassword().equals(password)) {
            System.out.println("validation confirmed");
            return true;
        }
        System.out.println("company login error");
        return false;
    }


    public void addNewCoupon(Coupon coupon) {
        try {
            Company company = companiesDBDAO.getOneCompany(companyID);
            System.out.println(company);
            ArrayList<Coupon> companyCoupons = company.getCoupons();

            if (companyCoupons != null) {
                for (Coupon c : companyCoupons) {
                    if (c.getTitle().equals(coupon.getTitle())) {
                        System.out.println("same title is not allowed, action failed");
                        return;
                    }
                    if (c.getId() == coupon.getId()) {
                        System.out.println("coupon ID already exists, cannot add new coupon");
                        return;
                    }

                }
            }
            couponsDBDAO.addCoupon(coupon);
        } catch (SQLException |
                InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void updateCoupon(Coupon coupon) throws CouponsException {
        couponsDBDAO.updateCoupon(coupon);

    }
//v
    public void deleteCoupon(int couponID) {
        couponsDBDAO.deleteCoupon(couponID);
    }
//v
    public ArrayList<Coupon> getAllCompanyCoupons() {
        ArrayList<Coupon> allCoupons = new ArrayList<>();
        ArrayList<Coupon> companyCoupons = new ArrayList<>();
        allCoupons = couponsDBDAO.getAllCoupons();
        for (Coupon c : allCoupons) {
            if (c.getCompanyId() == companyID) {
                companyCoupons.add(c);
            }
        }
//        System.out.println(allCoupons);
        System.out.println("company id: " + companyID);
        return companyCoupons;
    }
//v
    public ArrayList<Coupon> getAllCouponsFromCategory(Category category) {
        ArrayList<Coupon> coupons1 = getAllCompanyCoupons();
        ArrayList<Coupon> coupons2 = new ArrayList<>();
        for (Coupon c : coupons1) {
            if (c.getCategory().equals(category)) {
                coupons2.add(c);
            }
        }
        return coupons2;
    }
//v
    public ArrayList<Coupon> getAllCouponsByMaxPrice(double maxPrice) {
        ArrayList<Coupon> coupons1 = getAllCompanyCoupons();
        ArrayList<Coupon> coupons2 = new ArrayList<>();
        for (Coupon c : coupons1) {
            if (c.getPrice() <= maxPrice) {
                coupons2.add(c);
            }
        }
        return coupons2;
    }
//v
    public Company getCompanyDetails() throws SQLException, InterruptedException {
        Company company = companiesDBDAO.getOneCompany(companyID);
        ArrayList<Coupon> allCoupons = couponsDBDAO.getAllCoupons();
        ArrayList<Coupon> companyCoupons = new ArrayList<>();
        if (allCoupons != null) {
            for (Coupon c:allCoupons) {
                if (c.getCompanyId() == companyID) {
                    companyCoupons.add(c);
                }
            }
            company.setCoupons(companyCoupons);
        }

        System.out.println(company);
        return company;
    }
}
