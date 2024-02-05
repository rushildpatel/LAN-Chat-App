/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Keval Sanghvi
 */
public class Hash {
    // This method is static and returns Hashed value of the string passed as parameter to it
    public static String getHashValue(String input) {
        String result = null;
	try {
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    digest.reset();
	    digest.update(input.getBytes("utf8"));
	    result = String.format("%064x", new BigInteger(1, digest.digest()));
	} catch(UnsupportedEncodingException e) {
	    System.out.println("Error in Hashing! " + e);
	} catch(NoSuchAlgorithmException e) {
            System.out.println("Error in Hashing! " + e);
        } catch(Exception e) {
            System.out.println("Error in Hashing! " + e);
        }
        return result;
    }
}
