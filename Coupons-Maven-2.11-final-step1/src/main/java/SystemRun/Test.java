package SystemRun;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompaniesDBDAO;
import dao.CouponsDBDAO;
import db.ConnectionPool;

import db.DatabaseManager;
import facade.AdminFacade;
import facade.CustomerFacade;
import job.CouponExpirationDailyJob;
import exceptions.CouponsException;
import facade.CompanyFacade;
import login.ClientType;
import login.LoginManager;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test {


    public static void testAll() throws SQLException, InterruptedException, CouponsException {


        // TODO: 20/11/2022  CREATE DATABASE schema - run this code only once!

//            DatabaseManager databaseManager = new DatabaseManager();
//            databaseManager.createSchema();
//            databaseManager.createTableCategories();
//            databaseManager.createTableCustomers();
//            databaseManager.createTable_companies();
//            databaseManager.createTable_coupons();
//            databaseManager.createTable_customerVsCoupons();
//            databaseManager.addVariableToCategories();
//----------------------------
//        Thread starts:
        CouponExpirationDailyJob couponExpirationDailyJob = new CouponExpirationDailyJob();
        Thread thread = new Thread(couponExpirationDailyJob);
        thread.start();

        Customer customer1 = new Customer(1, "Bob", "Sponge", "bob@", "123");
        Customer customer2 = new Customer(2, "Patrick", "SeaStar", "pat@", "123");
        Coupon coupon1 = new Coupon(1, 2, Category.Electricity, "pc chip", "processor", Date.valueOf("2021-10-24"), Date.valueOf("2024-12-26"), 100, 99, "image");
        Coupon coupon2 = new Coupon(2, 2, Category.Electricity, "server chip", "for data farms", Date.valueOf("2021-10-24"), Date.valueOf("2024-12-25"), 100, 999, "image");
        Coupon updatedCoupon = new Coupon(2, 2, Category.Electricity, "updated server chip", "updated", Date.valueOf("2222-10-24"), Date.valueOf("2222-12-25"), 666, 666, "updatedimage");
        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon1);
        coupons.add(coupon2);
        Company company1 = new Company("intel", "intel@", "123", coupons);
        Company company2 = new Company("TSMC", "tsmc@", "123", coupons);

        //only one Client type can be logged into the system in the same time!!

        //Admin facade
        //make sure the company id in the compiler & the workbench is the same when updating\deleting the Auto increment sometimes creates problems.

        AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
//        adminFacade.addCompany(company1);
//        adminFacade.addCompany(company2);
//        adminFacade.updateCompanyFacade(company1,"updatedEmail","234");
//        adminFacade.deleteCompany(1);
//        adminFacade.getAllCompanies();
//        adminFacade.getCompanyById(2);
//        adminFacade.addCustomer(customer1);
//        adminFacade.addCustomer(customer2);
//        adminFacade.updateCustomer(customer1,"updatedBob","updatedSponge","newMail","new123");
//        adminFacade.deleteCustomer(1);
//        adminFacade.getAllCustomers();
//        adminFacade.getCustomerByID(2);
//-------------------------------------------------------
        //CompanyFacade

//        CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("tsmc@", "123", ClientType.Company);
//        //if there is a problem make sure the company id matches the coupons COMPANY_ID:
//
//        companyFacade.addNewCoupon(coupon1);
//        companyFacade.addNewCoupon(coupon2);
//        System.out.println(companyFacade.getAllCompanyCoupons());
//        System.out.println(companyFacade.getAllCouponsFromCategory(Category.Electricity));
//        System.out.println(companyFacade.getAllCouponsByMaxPrice(600));
//        companyFacade.getCompanyDetails();
//        companyFacade.updateCoupon(updatedCoupon);
//        companyFacade.deleteCoupon(1);
//        companyFacade.addNewCoupon(coupon1);
//        companyFacade.addNewCoupon(coupon2);
//---------------------------------------------------------------------------

        //customer Facade:
        //make sure the coupon exists in database (after you invoked the delete coupon method)

//        CustomerFacade customerFacade = (CustomerFacade) LoginManager.getInstance().login("pat@", "123", ClientType.Customer);
//        customerFacade.purchaseCoupon(coupon2);
//        System.out.println(customerFacade.getCustomerCoupons());
//        System.out.println(customerFacade.getCustomerCouponsByCategory(Category.Electricity));
//        System.out.println(customerFacade.getCustomerCouponsByPrice(1200));
//        System.out.println(customerFacade.getCustomerDetails());


//----------------------
//        thread end:
        couponExpirationDailyJob.stop();
        ConnectionPool.getInstance().closeAllConnections();

    }
}