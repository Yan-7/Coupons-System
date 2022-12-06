package job;

import beans.Coupon;
import dao.CouponsDAO;
import dao.CouponsDBDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

//works once daily and deletes expired coupons and purchase history.
// TODO: 20/10/2022 job starts as system goes up and finish when i closes
public class CouponExpirationDailyJob implements Runnable {


    CouponsDAO couponsDBDAO = new CouponsDBDAO();
    private boolean quit;
    boolean loopSwitch = true;

    public CouponExpirationDailyJob() throws SQLException {
    }
//v
    @Override
    public void run() {

        while (loopSwitch == true) {
            Object sleeper = new Object();
            synchronized (sleeper) {
                System.out.println("Thread started running");
                CouponsDAO couponsDAO = null;
                try {
                    couponsDAO = new CouponsDBDAO();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ArrayList<Coupon> couponsList = couponsDAO.getAllCoupons();

                for (Coupon c : couponsList) {
//                    System.out.println(c.getEndDate());
                    if (c.getEndDate().before(Date.valueOf(LocalDate.now()))) {
                        couponsDAO.deleteCoupon(c.getId());
                        System.out.println(c.getTitle()+ " deleted");
                    }
                }
                try {
                    System.out.println("Thread sleeping");
                    sleeper.wait(1000 * 60 * 60 * 24);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        loopSwitch = false;

    }
}
