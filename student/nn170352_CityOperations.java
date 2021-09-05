/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author Neca
 */
public class nn170352_CityOperations implements CityOperations{
    
    private Connection conn = DB.getInstance().getConnection();

    @Override
    public int insertCity(String naziv, String postanskiBr) {
        
        //ovo postoji zato sto ne mogu da postoje dva grada sa istim postanskim brojem
        try(PreparedStatement ps = conn.prepareStatement("select * from Grad where PostanskiBr = ? or Naziv = ?");){
            ps.setString(1, postanskiBr);
            ps.setString(2, naziv);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query = "insert into Grad(Naziv, PostanskiBr) values(?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, naziv);
            ps.setString(2, postanskiBr);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
        
    }

    @Override
    public int deleteCity(String... name) {
        
        int br = 0;
        
        for(int i = 0; i<name.length; i++) {
            try(PreparedStatement ps = conn.prepareStatement("delete from Grad where Naziv = ?");) {
                
                ps.setString(1, name[i]);
                br += ps.executeUpdate();
                
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        return br;
    }

    @Override
    public boolean deleteCity(int i) {
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("delete from Grad where IdG = ?");) {
                
            ps.setInt(1, i);
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
    public List<Integer> getAllCities() {
        
        List<Integer> gradovi = new ArrayList<>();
        
        try(Statement stmt =  conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Grad");) {
            
            while(rs.next()) {
                gradovi.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return gradovi;
    }
    
}
