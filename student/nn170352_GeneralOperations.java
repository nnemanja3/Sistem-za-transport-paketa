/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author Neca
 */
public class nn170352_GeneralOperations implements GeneralOperations{
    
    private Connection conn = DB.getInstance().getConnection();

    
    //potrebno je dopuniti
    @Override
    public void eraseAll() {
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from ZahtevZaKurira");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Ponuda");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Paket");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Kurir");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Admin");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Vozilo");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Korisnik");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Opstina");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(Statement stmt = conn.createStatement();) {
            stmt.executeUpdate("delete from Grad");
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
