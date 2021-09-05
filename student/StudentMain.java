/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.*;
import student.*;

/**
 *
 * @author Neca
 */
public class StudentMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CityOperations cityOperations = new nn170352_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new nn170352_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new nn170352_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new nn170352_CourierRequestOperation();
        GeneralOperations generalOperations = new nn170352_GeneralOperations();
        UserOperations userOperations = new nn170352_UserOperations();
        VehicleOperations vehicleOperations = new nn170352_VehicleOperations();
        PackageOperations packageOperations = new nn170352_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    

        
    }
    
}
