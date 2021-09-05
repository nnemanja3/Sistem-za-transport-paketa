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
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author Neca
 */
public class nn170352_CourierOperations implements CourierOperations{
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public boolean insertCourier(String userName, String regBr) {
        
        int IdK = -1;
        
        
        
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(IdK == -1) {
            return false;
        }
        
        int IdV = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdV from Vozilo where RegBroj = ?");) {
            ps.setString(1, regBr);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdV = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(IdV == -1) {
            return false;
        }
        int br = 0;
        br = 0;
        try(PreparedStatement ps = conn.prepareStatement("insert into Kurir(IdK, Vozilo) values(?,?)");) {
            ps.setInt(1, IdK);
            ps.setInt(2, IdV);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean deleteCourier(String userName) {
        
        int IdK = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return false;
        }
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("delete from Kurir where IdK = ?");) {
            ps.setInt(1, IdK);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    
    @Override
    public List<String> getCouriersWithStatus(int status) {
        List<String> lista = new ArrayList<>();
        
        try(PreparedStatement ps = conn.prepareStatement("select k.KorisnickoIme from Korisnik k join Kurir ku on k.IdK = ku.IdK"
                + " where ku.Status = ?");) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    
    //proveriti da li je dobro sortirano
    @Override
    public List<String> getAllCouriers() {
        List<String> lista = new ArrayList<>();
        
         try(Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("select k.KorisnickoIme "
                         + "from Korisnik k join Kurir ku on k.IdK = ku.IdK order by ku.OstvarenProfit");) {
                while(rs.next()) {
                    lista.add(rs.getString(1));
                }

         } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveris) {
        
        BigDecimal br = BigDecimal.ZERO;
        try(PreparedStatement ps = conn.prepareStatement("select AVG(OstvarenProfit) from Kurir where BrojIsporucenihPaketa >= ?");) {
            ps.setInt(1, numberOfDeliveris);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    br = rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(br == null) {
            br = BigDecimal.ZERO;
        }
        
        return br;
    }
    
}
