/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author Neca
 */
public class nn170352_CourierRequestOperation implements CourierRequestOperation{
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public boolean insertCourierRequest(String userName, String regBr) {
        int IdK = -1;
        int IdV = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return false;
        }
        //ako je korisnik vec kurir ne moze da trazi da bude kurir
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Kurir where IdK = ?");) {
            ps.setInt(1, IdK);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement ps = conn.prepareStatement("select IdV from Vozilo where RegBroj = ?");) {
            ps.setString(1, regBr);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdV = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdV == -1 ) {
            return false;
        }
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("insert into ZahtevZaKurira(IdK, Vozilo) values (?,?)");) {
            ps.setInt(1, IdK);
            ps.setInt(2, IdV);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
        
    }

    @Override
    public boolean deleteCourierRequest(String userName) {
        int IdK = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return false;
        }
        
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("delete from ZahtevZaKurira where IdK = ?");) {
            ps.setInt(1, IdK);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean changeVehicleInCourierRequest(String userName, String regBr) {
        int IdK = -1;
        int IdV = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return false;
        }
        try(PreparedStatement ps = conn.prepareStatement("select IdV from Vozilo where RegBroj = ?");) {
            ps.setString(1, regBr);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdV = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdV == -1 ) {
            return false;
        }
        
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("update ZahtevZaKurira set Vozilo = ? where IdK = ?");) {
            ps.setInt(1, IdV);
            ps.setInt(2, IdK);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> lista = new ArrayList<>();
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select k.KorisnickoIme from Korisnik k join ZahtevZaKurira z on k.IdK = z.IdK")) {
            while(rs.next()) {
                lista.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public boolean grantRequest(String userName) {
        int IdK = -1;
        try(PreparedStatement ps = conn.prepareStatement("select k.IdK from Korisnik k join ZahtevZaKurira z on k.IdK = z.IdK "
                    + "where k.KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                   IdK = rs.getInt(1); 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return false;
        }
        int br = 0;
        try(CallableStatement cs = conn.prepareCall("{ call SP_Odobri_Zahtev_Za_Kurira (?,?) }");) {
            cs.setInt(1, IdK);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            br = cs.getInt(2);
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
        
    }
    
}
