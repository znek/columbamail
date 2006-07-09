package IMAPsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PasswordField {

	   /**
	    *@param prompt The prompt to display to the user
	    *@return The password as entered by the user
	    */
	   public static String readPassword (String prompt) {
	      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	      String password = "";

	      try {
	         password = in.readLine();
	      } catch (IOException ioe) {
	        ioe.printStackTrace();
	      }
	      // return the password entered by the user
	      return password;
	   }
	}