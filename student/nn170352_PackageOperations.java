/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Neca
 */
public class nn170352_PackageOperations implements PackageOperations {
    
    private final Connection conn = DB.getInstance().getConnection();

    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        int IdO = -1;
        int IdK = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdO from Opstina where IdO = ?");) {
            ps.setInt(1, districtFrom);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdO = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdO == -1) {
            return -1;
        }
        IdO = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdO from Opstina where IdO = ?");) {
            ps.setInt(1, districtTo);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdO = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdO == -1) {
            return -1;
        }
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return -1;
        }
        if(!(packageType == 0 || packageType == 1 || packageType == 2)) {
            return -1;
        }
        
        if((weight.compareTo(BigDecimal.ZERO) == -1) || (weight.compareTo(BigDecimal.ZERO) == 0)){
            return -1;
        }
        
        int br = 0;
        int vracenId = -1;
        try(PreparedStatement ps = conn.prepareStatement("insert into Paket(Korisnik, OpstinaPreuzimanja, OpstinaDostavljanja, "
                    + "TipPaketa, TezinaPaketa) values(?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, IdK);
            ps.setInt(2, districtFrom);
            ps.setInt(3, districtTo);
            ps.setInt(4, packageType);
            ps.setBigDecimal(5, weight);
            br = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    vracenId = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return -1;
        }else {
            return vracenId;
        }
    }

    //status kurira mora da bude 0 - ne vozi
    //napraviti da procenat moze biti i negativan 
    @Override
    public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {
        int IdK = -1;
        int IdP = -1;
        BigDecimal procenat;
        if(pricePercentage == null) {
            procenat = new BigDecimal(Math.random()*10);
        }else {
            procenat = pricePercentage;
        }
        //kurir mora da bude i u statusu ne vozi
        try(PreparedStatement ps = conn.prepareStatement("select k.IdK from Korisnik k join Kurir ku on k.IdK = ku.IdK "
                + "where k.KorisnickoIme = ? and ku.Status = 0");) {
            ps.setString(1, couriersUserName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return -1;
        }
        try(PreparedStatement ps = conn.prepareStatement("select IdP from Paket where IdP = ?");) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdP = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdP == -1) {
            return -1;
        }
        int br = 0;
        int vracenId = -1;
        try(PreparedStatement ps = conn.prepareStatement("insert into Ponuda(Kurir, Paket, ProcenatCeneIsporuke) values(?,?,?)"
                , PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, IdK);
            ps.setInt(2, IdP);
            ps.setBigDecimal(3, procenat);
            br = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    vracenId = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return -1;
        }else {
            return vracenId;
        }
    }

    @Override
    public boolean acceptAnOffer(int offerId) {
        Timestamp vremePrihvatanja = new Timestamp(new java.util.Date().getTime());
//        System.out.println(vremePrihvatanja);

        int idK = -1;
        int idP = -1;
        BigDecimal procenat = BigDecimal.ZERO;
        
        int tipPaketa = -1;
        int opstina1 = -1;
        int opstina2 = -1;
        BigDecimal tezinaPaketa = BigDecimal.ZERO;
        
        int x1 = -1;
        int x2 = -1;
        int y1 = -1;
        int y2 = -1;
        
        double euklidska_distanca = -1;
        
        try(PreparedStatement ps = conn.prepareStatement("select Kurir, Paket, ProcenatCeneIsporuke from Ponuda where IdPonuda = ?");) {
            ps.setInt(1, offerId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    idK = rs.getInt(1);
                    idP = rs.getInt(2);
                    procenat = rs.getBigDecimal(3);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idK == -1 || idP == -1) {
            return false;
        }
        
        
        try(PreparedStatement ps = conn.prepareStatement("select TipPaketa, OpstinaPreuzimanja, OpstinaDostavljanja, TezinaPaketa from Paket where IdP = ?");) {
            ps.setInt(1, idP);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                  tipPaketa = rs.getInt(1);
                  opstina1 = rs.getInt(2);
                  opstina2 = rs.getInt(3);
                  tezinaPaketa = rs.getBigDecimal(4);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tipPaketa == -1 || opstina1 == -1 || opstina2 == -1 ) {
            return false;
        }
       
        
        try(PreparedStatement ps = conn.prepareStatement("select X, Y from Opstina where IdO = ?");) {
            ps.setInt(1, opstina1);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    x1 = rs.getInt(1);
                    y1 = rs.getInt(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(x1 == -1 || y1 == -1) {
            return false;
        }
        
        
        try(PreparedStatement ps = conn.prepareStatement("select X, Y from Opstina where IdO = ?");) {
            ps.setInt(1, opstina2);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    x2 = rs.getInt(1);
                    y2 = rs.getInt(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(x2 == -1 || y2 == -1) {
            return false;
        }
        
        
        euklidska_distanca = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
        
        
        BigDecimal cena = BigDecimal.ZERO;
        if(tipPaketa == 0) {
            //tip paketa pismo
            cena = new BigDecimal(10 * euklidska_distanca);
        }else {
            if(tipPaketa == 1) {
                //tip paketa standardno
//                (25 + (1 * tezinaPaketa) * 100) * euklidksa_distanca
                cena = tezinaPaketa.multiply(new BigDecimal(100)).add(new BigDecimal(25));
                cena = cena.multiply(new BigDecimal(euklidska_distanca));
            }else {
                //tip paketa lomljivo
//                (75 + (2 * tezinaPaketa) * 300) * euklidska_distanca
                cena = tezinaPaketa.multiply(new BigDecimal(2)).multiply(new BigDecimal(300));
                cena = cena.add(new BigDecimal(75)).multiply(new BigDecimal(euklidska_distanca));
            }
        }
        
        //pronadjeno u javnom testu cena je za toliko procenata veca od ceneJedneIsporuke
        cena = cena.multiply(procenat.add(new BigDecimal(100)));
        cena = cena.divide(new BigDecimal(100));
        
        //ako imam neku gresku prebaciti okidac na update paket
        //treba obrisati ponudu, automatski se poziva trigger koji brise ostale ponude za paket obrisane ponude
//        int provera = 0;
//        try(PreparedStatement ps = conn.prepareStatement("delete from Ponuda where IdPonuda = ?");){
//            ps.setInt(1, offerId);
//            provera = ps.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        if(provera == 0) {
//            return false;
//        }
        
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("update Paket "
                    + "set Kurir = ?, StatusIsporuke = 1, Cena = ?, VremePrihvatanja = ? where IdP = ?");) {
            ps.setInt(1, idK);
            ps.setBigDecimal(2, cena);
            ps.setTimestamp(3, vremePrihvatanja);
            ps.setInt(4, idP);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
        
    }

    @Override
    public List<Integer> getAllOffers() {
        List<Integer> lista = new ArrayList<>();
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select IdPonuda from Ponuda")) {
            while(rs.next()) {
                lista.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        List<Pair<Integer, BigDecimal>> lista = new ArrayList<>();
        
        try(PreparedStatement ps = conn.prepareStatement("select Kurir, ProcenatCeneIsporuke from Ponuda where Paket = ?");) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    lista.add(new nn170352_PackageOperationsPair(rs.getInt(1), rs.getBigDecimal(2)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public boolean deletePackage(int packageId) {
        
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("delete from Paket where IdP = ?");) {
            ps.setInt(1, packageId);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("update Paket set TezinaPaketa = ? where IdP = ?");) {
            ps.setBigDecimal(1, newWeight);
            ps.setInt(2, packageId);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean changeType(int packageId, int newType) {
        if(!(newType == 0 || newType == 1 || newType == 2)) {
            return false;
        }
        int br = 0;
        try(PreparedStatement ps = conn.prepareStatement("update Paket set TipPaketa = ? where IdP = ?");) {
            ps.setInt(1, newType);
            ps.setInt(2, packageId);
            br = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(br == 0) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Integer getDeliveryStatus(int packageId) {
        Integer status = -1;
        try(PreparedStatement ps = conn.prepareStatement("select StatusIsporuke from Paket where IdP = ?");) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    status = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status == -1) {
            return null;
        }else {
            return status;
        }
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        
        BigDecimal broj = BigDecimal.ZERO;
        try(PreparedStatement ps = conn.prepareStatement("select Cena from Paket where IdP = ?");) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    broj = rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(broj == BigDecimal.ZERO){
            return null;
        }else {
            return broj;
        }
        
    }

    @Override
    public java.sql.Date getAcceptanceTime(int packageId) {
        
        java.sql.Date vreme = null;
        try(PreparedStatement ps = conn.prepareStatement("select VremePrihvatanja from Paket where IdP = ?");) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    vreme = rs.getDate(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vreme;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> lista = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement("select IdP from Paket where TipPaketa = ?");) {
            ps.setInt(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    lista.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public List<Integer> getAllPackages() {
        List<Integer> lista = new ArrayList<>();
        try(Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select IdP from Paket")) {
            while(rs.next()) {
                lista.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public List<Integer> getDrive(String userName) {
        List<Integer> lista = new ArrayList<>();
        int IdK = -1;
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) { 
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    IdK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(IdK == -1) {
            return null;
        }
        
        try(PreparedStatement ps = conn.prepareStatement("select p.IdP from Paket p join Kurir k on p.Kurir = k.IdK "
                + "where p.StatusIsporuke = 2 and k.IdK = ?");) {
            ps.setInt(1, IdK);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                lista.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(lista.isEmpty() ){
            return null;
        }else {
            return lista;
        }
    }

    @Override
    public int driveNextPackage(String courierUserName) {
        
        boolean poslednjiPaket = false;
        
        int status = -2;
        int idK = -2;
        int idV = -2;
        int brojIsporucenihPaketa = -2;
        BigDecimal profitKurira = BigDecimal.ZERO;
        //dohvatamo prvo IdK za kurira sa korisnickim imenom
        try(PreparedStatement ps = conn.prepareStatement("select IdK from Korisnik where KorisnickoIme = ?");) {
            ps.setString(1, courierUserName);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    idK = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(idK == -2) {
            return -2;
        }
        //dohvatamo status kurira, da li vozi ili ne
        try(PreparedStatement ps = conn.prepareStatement("select Status, Vozilo, BrojIsporucenihPaketa, OstvarenProfit from Kurir where IdK = ?");) {
            ps.setInt(1, idK);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    status = rs.getInt(1);
                    idV = rs.getInt(2);
                    brojIsporucenihPaketa = rs.getInt(3);
                    profitKurira = rs.getBigDecimal(4);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status == -2 || idV == -2) {
            return -2;
        }
        
        int idP = -2;
        int opstina1 = -2;
        int opstina2 = -2;
        int x1 = -2;
        int x2 = -2;
        int y1 = -2;
        int y2 = -2;
        double euklidska_distanca;
        BigDecimal cena = BigDecimal.ZERO;
        
        int opstina3 = -2;
        int x3 = -2;
        int y3 = -2;
        int idPaketa2 = -2;
        
        //ako je status 0 - ne vozi onda menjamo status u 1-vozi i sve pakete koji su u statusu 1-zahtev prihvacen
        //menjamo u status 2-pokupljen za tog kurira
        if (status == 0) {
//            try(PreparedStatement ps = conn.prepareStatement("update Paket set StatusIsporuke = 2 where StatusIsporuke = 1 and Kurir = ?");) {
//                ps.setInt(1, idK);
//                int br = ps.executeUpdate();
//                if(br == 0) return -1; //ne postoji ni jedan paket koji bi kurir trebao da vozi
//            } catch (SQLException ex) {
//                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
//            }

            //dohvatamo paket po FCFS redosledu
            try (PreparedStatement ps = conn.prepareStatement("select IdP, OpstinaPreuzimanja, OpstinaDostavljanja, Cena from Paket "
                    + "where Kurir = ? and StatusIsporuke = 1 order by VremePrihvatanja ");) {
                ps.setInt(1, idK);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idP = rs.getInt(1);
                        opstina1 = rs.getInt(2);
                        opstina2 = rs.getInt(3);
                        cena = rs.getBigDecimal(4);
                    } else {
                        return -1;
                    }
                    //dohvatim naredni da vidim gde je mesto preuzimanja da bih izracunao i to u profit
                    if (rs.next()) {
                        idPaketa2 = rs.getInt(1);
                        opstina3 = rs.getInt(2);
                    } else {
                        poslednjiPaket = true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (opstina1 == -2 || opstina2 == -2 || idP == -2 || cena == BigDecimal.ZERO) {
                return -2;
            }

            try (PreparedStatement ps = conn.prepareStatement("update Kurir set Status = 1 where IdK = ?");) {
                ps.setInt(1, idK);
                int br = ps.executeUpdate();
                if (br == 0) {
                    return -2;
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try (PreparedStatement ps = conn.prepareStatement("select IdP, OpstinaPreuzimanja, OpstinaDostavljanja, Cena from Paket "
                    + "where Kurir = ? and StatusIsporuke = 2 ");) {
                ps.setInt(1, idK);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idP = rs.getInt(1);
                        opstina1 = rs.getInt(2);
                        opstina2 = rs.getInt(3);
                        cena = rs.getBigDecimal(4);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (opstina1 == -2 || opstina2 == -2 || idP == -2 || cena == BigDecimal.ZERO) {
                return -2;
            }

            try (PreparedStatement ps = conn.prepareStatement("select IdP, OpstinaPreuzimanja from Paket "
                    + "where Kurir = ? and StatusIsporuke = 1 order by VremePrihvatanja ");) {
                ps.setInt(1, idK);
                try (ResultSet rs = ps.executeQuery()) {
                    //dohvatim naredni da vidim gde je mesto preuzimanja da bih izracunao i to u profit
                    if (rs.next()) {
                        idPaketa2 = rs.getInt(1);
                        opstina3 = rs.getInt(2);
                    } else {
                        poslednjiPaket = true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        
        
        
        //dohvatanje x, y koordinata za opstine da bi se izracunalo euklidska_distanca
        try(PreparedStatement ps = conn.prepareStatement("select X, Y from Opstina where IdO = ?");) {
            ps.setInt(1, opstina1);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    x1 = rs.getInt(1);
                    y1 = rs.getInt(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(x1 == -2 || y1 == -2) {
            return -2;
        }
        
        
        try(PreparedStatement ps = conn.prepareStatement("select X, Y from Opstina where IdO = ?");) {
            ps.setInt(1, opstina2);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    x2 = rs.getInt(1);
                    y2 = rs.getInt(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(x2 == -2 || y2 == -2) {
            return -2;
        }
        if(!poslednjiPaket){
            try(PreparedStatement ps = conn.prepareStatement("select X, Y from Opstina where IdO = ?");) {
                ps.setInt(1, opstina3);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) {
                        x3 = rs.getInt(1);
                        y3 = rs.getInt(2);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(x3 == -2 || y3 == -2) {
                return -2;
            }
        }
        
        
        
        euklidska_distanca = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
        
        int tipGoriva = -2;
        BigDecimal potrosnja = BigDecimal.ZERO;
        //dohvatanje vozila
        try(PreparedStatement ps = conn.prepareStatement("select TipGoriva, Potrosnja from Vozilo where IdV = ?");) {
            ps.setInt(1, idV);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    tipGoriva = rs.getInt(1);
                    potrosnja = rs.getBigDecimal(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(tipGoriva == -2 || BigDecimal.ZERO == potrosnja) {
            return -2;
        }
        
        BigDecimal profit;
        BigDecimal broj;
        if(tipGoriva == 0){
            //plin
            //profit = cena - euklidska_distanca * potrosnja * cena_po_litru
            broj = potrosnja.multiply(new BigDecimal(15));
            profit = cena.subtract(broj.multiply(new BigDecimal(euklidska_distanca)));
        }else {
            if(tipGoriva == 1) {
                //dizel
                broj = potrosnja.multiply(new BigDecimal(32));
                profit = cena.subtract(broj.multiply(new BigDecimal(euklidska_distanca)));
            }else {
                //benzin
                broj = potrosnja.multiply(new BigDecimal(36));
                profit = cena.subtract(broj.multiply(new BigDecimal(euklidska_distanca)));
            }
        }
        
        
        //oduzimamo od profita onoliko koliko se trosi od mesta gde se isporucuje do narednog mesta preuzimanja
        //naredno mesto nam je opstina 3 za koje prvo dohvatamo koordinate
        if(!poslednjiPaket) {
            euklidska_distanca = Math.sqrt(Math.pow(x2-x3, 2) + Math.pow(y2-y3, 2));
            profit = profit.subtract(broj.multiply(new BigDecimal(euklidska_distanca)));
        }
        
        
        profitKurira = profitKurira.add(profit);
        //update paketa da je isporucen
        try(PreparedStatement ps = conn.prepareStatement("update Paket set StatusIsporuke = 3 where IdP = ?");) {
            ps.setInt(1, idP);
            int br = ps.executeUpdate();
            if(br == 0) return -2;
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        //update kurira
        try(PreparedStatement ps = conn.prepareStatement("update Kurir set OstvarenProfit = ?, BrojIsporucenihPaketa = ?, Status = ? where IdK = ?");) {
            ps.setBigDecimal(1, profitKurira);
            ps.setInt(2, brojIsporucenihPaketa + 1);
            if(poslednjiPaket) {
                ps.setInt(3, 0);
            }else {
                ps.setInt(3, 1);
            }
            ps.setInt(4, idK);
            int br = ps.executeUpdate();
            if(br == 0) return -2;
        } catch (SQLException ex) {
            Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!poslednjiPaket) {
            //update paketa koji se sledeci isporucuje da je pokupljen
            try (PreparedStatement ps = conn.prepareStatement("update Paket set StatusIsporuke = 2 where IdP = ?");) {
                ps.setInt(1, idPaketa2);
                int br = ps.executeUpdate();
                if (br == 0) {
                    return -2;
                }
            } catch (SQLException ex) {
                Logger.getLogger(nn170352_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return idP;
    }
    
}
