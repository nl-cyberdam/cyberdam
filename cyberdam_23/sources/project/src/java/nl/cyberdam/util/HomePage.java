package nl.cyberdam.util;

import javax.servlet.http.HttpServletRequest;

public class HomePage {
    
    public static String getURL(HttpServletRequest request) {
        String returnValue = "";
        String requestURL = request.getRequestURL().toString();
        String servletPath = request.getServletPath();
        if (requestURL != null && servletPath != null) {
            int poz = requestURL.indexOf(servletPath);
            returnValue = requestURL.substring(0, poz);
        }
        return returnValue;
    }
}
