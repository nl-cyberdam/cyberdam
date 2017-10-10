package nl.cyberdam.tools;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.encoding.PasswordEncoder;

/**
 *
 */
public class PasswordHelper {

    
    /** Creates a new instance of PasswordEncoder */
    public PasswordHelper() {
    }
    
    public static void main(String[] args) {
        String password;
        PasswordEncoder p = new Md5PasswordEncoder();
        if (args.length != 1) {
            printUsage();
        } else {
            password = args[0];
            System.out.println(p.encodePassword(password, null));
        }
    }
    
    public static void printUsage() {
        System.out.println("Usage: PasswordHelper <password>");
    }
    
}
