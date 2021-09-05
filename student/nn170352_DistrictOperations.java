/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Neca
 */
public class nn170352_DistrictOperations implements DistrictOperations {
    
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public int insertDistrict(String name, int cityId, int xCord, int yCord) {
        
        boolean moze = false;
        
        try(PreparedStatement ps = conn.prepareStatement("select IdG from Grad where IdG = ?");) {
            ps.setInt(1, cityId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    moze = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!moze) {
            return -1;
        }
       
        try(PreparedStatement ps1 = conn.prepareStatement("insert into Opstina(Naziv, X, Y, Grad) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
            
            ps1.setString(1, name);
            ps1.setInt(2, xCord);
            ps1.setInt(3, yCord);
            ps1.setInt(4, cityId);
            
            ps1.executeUpdate();
            try (ResultSet rs1 = ps1.getGeneratedKeys()) {
                if(rs1.next()) {
                    return rs1.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        
    }

    @Override
    public int deleteDistricts(String... name) {
        
        int br = 0;
        
        for(int i = 0; i<name.length; i++) {
            try(PreparedStatement ps = conn.prepareStatement("delete from Opstina where Naziv = ?");) {
                
                ps.setString(1, name[i]);
                br += ps.executeUpdate();
                
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return br;
    }

    @Override
    public boolean deleteDistrict(int IdO) {
        
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("delete from Opstina where IdO = ?");) {
                
            ps.setInt(1, IdO);
            br = ps.executeUpdate();
                
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int deleteAllDistrictsFromCity(String cityName) {
        int br = 0;
        int IdG = 0;
        //trazimo id grada
        try(PreparedStatement ps = conn.prepareStatement("select IdG from Grad where Naziv = ?");) {
            ps.setString(1, cityName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdG = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        //ako ne postoji grad onda vrati 0
        if(IdG == 0) {
            return br;
        }
        //brisemo sve opstine koje imaju taj grad
        try(PreparedStatement ps = conn.prepareStatement("delete from Opstina where Grad = ?");) {
            ps.setInt(1, IdG);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return br;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int IdG) {
        List<Integer> lista = new ArrayList<>();
        
        try(PreparedStatement ps = conn.prepareStatement("select IdO from Opstina where Grad = ?");) {
            ps.setInt(1, IdG);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    lista.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(lista.isEmpty()) {
            return null;
        }else {
            return lista;
        }
        
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> lista = new ArrayList<>();
        
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select IdO from Opstina")) {
            while(rs.next()) {
                lista.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
}
