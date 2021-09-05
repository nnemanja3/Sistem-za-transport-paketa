/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Neca
 */
public class nn170352_PackageOperationsPair implements PackageOperations.Pair<Integer, BigDecimal>{
    
    Integer a;
    BigDecimal b;

    public nn170352_PackageOperationsPair(Integer a, BigDecimal b) {
        this.a = a;
        this.b = b;
    }
    
    

    @Override
    public Integer getFirstParam() {
        return a;
    }

    @Override
    public BigDecimal getSecondParam() {
        return b;
    }

    
    
}
