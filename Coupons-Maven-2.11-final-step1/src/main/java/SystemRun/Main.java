package SystemRun;

import SystemRun.Test;
import exceptions.CouponsException;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException, CouponsException {

        Test.testAll();

    }
}
