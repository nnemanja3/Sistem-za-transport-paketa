/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author Neca
 */
public class nn170352_VehicleOperations implements VehicleOperations {
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public boolean insertVehicle(String regBr, int fuelType, BigDecimal fuelConsumption) {
        int br = 0;
        
        if(!(fuelType == 0 || fuelType == 1 || fuelType == 2)) {
            return false;
        }
        
        if((fuelConsumption.compareTo(BigDecimal.ZERO) == -1) || (fuelConsumption.compareTo(BigDecimal.ZERO) == 0)){
            return false;
        }
        
        try(PreparedStatement ps = conn.prepareStatement("insert into Vozilo(RegBroj, TipGoriva, Potrosnja) values(?,?,?)");) {
            ps.setString(1, regBr);
            ps.setInt(2, fuelType);
            ps.setBigDecimal(3, fuelConsumption);
            
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int deleteVehicles(String... regBr) {
        int br = 0;
        
        for(int i = 0; i<regBr.length; i++) {
            try(PreparedStatement ps = conn.prepareStatement("delete from Vozilo where RegBroj = ?");) {
                
                ps.setString(1, regBr[i]);
                br += ps.executeUpdate();
                
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return br;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> lista = new ArrayList<>();
        
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select RegBroj from Vozilo")) {
            
            while(rs.next()) {
                lista.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public boolean changeFuelType(String regBr, int fuelType) {
        int br = 0;
       if(!(fuelType == 0 || fuelType == 1 || fuelType == 2)) {
            return false;
        }
        try(PreparedStatement ps = conn.prepareStatement("update Vozilo set TipGoriva = ? where RegBroj = ?");) {
            ps.setInt(1, fuelType);
            ps.setString(2, regBr);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean changeConsumption(String regBr, BigDecimal fuelConsumption) {
        int br = 0;
        if((fuelConsumption.compareTo(BigDecimal.ZERO) == -1) || (fuelConsumption.compareTo(BigDecimal.ZERO) == 0)){
            return false;
        }
        try(PreparedStatement ps = conn.prepareStatement("update Vozilo set Potrosnja = ? where RegBroj = ?");) {
            ps.setBigDecimal(1, fuelConsumption);
            ps.setString(2, regBr);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }
    
}
