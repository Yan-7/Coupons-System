package dao;

import beans.Coupon;
import exceptions.CouponsException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


public interface CouponsDAO {

    void addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon) throws CouponsException;
    void deleteCoupon(int couponID);
    ArrayList<Coupon> getAllCoupons();
    Coupon getOneCoupon(int couponID );
    void addCouponPurchase(int customerID, int couponID);
    void deleteCouponPurchase(int customerID,int couponID);

    public static void main(String[] args) {

    }

}
