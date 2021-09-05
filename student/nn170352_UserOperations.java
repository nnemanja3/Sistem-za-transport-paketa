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
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author Neca
 */
public class nn170352_UserOperations implements UserOperations {
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        
        int br = 0;
        //provera da li je prvo slovo imena i prezimena veliko
        char prvoSlovo = firstName.charAt(0);
        if(Character.isLowerCase(prvoSlovo)) {
            return false;
        }
        prvoSlovo = lastName.charAt(0);
        if(Character.isLowerCase(prvoSlovo)) {
            return false;
        }
        //da li postoji barem jedna cifra
        if(!(password.matches(".*\\d.*"))){
            return false;
        }
        //da li postoji barem jedno slovo
        if(!(password.matches(".*[a-zA-Z].*"))){
            return false;
        }
        //da li je duze od 8 znakova
        if(password.length() < 8){
            return false;
        }
        
        try(PreparedStatement ps = conn.prepareStatement("insert into Korisnik(Ime, Prezime, KorisnickoIme, Sifra) "
                + "values(?,?,?,?)");) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, userName);
            ps.setString(4, password);
            
            br = ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int declareAdmin(String userName) {
        
        int IdK = -1;
        
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(IdK == -1) {
            return 2;
        }
        
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Admin where IdK = ?");) {
            ps.setInt(1, IdK);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return 1;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(PreparedStatement ps = conn.prepareStatement("insert into Admin(IdK) values(?)");) {
            ps.setInt(1, IdK);
            ps.executeUpdate();
            
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 2;
    }

    //proveriti jos da li radi
    @Override
    public Integer getSentPackages(String... userName) {
        
        int br = 0;
        boolean postoji = false;
        
        for(int i=0; i<userName.length; i++) {
            try(PreparedStatement ps = conn.prepareStatement("select BrojPoslatihPaketa from Korisnik where KorisnickoIme = ?");) {
                ps.setString(1, userName[i]);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    postoji = true;
                    br += rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(postoji) {
            return br;
        }else {
            return null;
        }
    }

    @Override
    public int deleteUsers(String... userName) {
        int br = 0;
        
        for(int i=0; i<userName.length; i++) {
            try(PreparedStatement ps = conn.prepareStatement("delete from Korisnik where KorisnickoIme = ?");) {
                
                ps.setString(1, userName[i]);
                br += ps.executeUpdate();
                
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return br;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> lista = new ArrayList<>();
        
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select KorisnickoIme from Korisnik")) {
            
            while(rs.next()) {
                lista.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
}
