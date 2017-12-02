/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package private_clinic;

/**
 *
 * @author glsik1
 */
public class DrugChekException extends Exception{
    String str;

    public DrugChekException(String excp) {
        this.str=excp;
    }
    
}
