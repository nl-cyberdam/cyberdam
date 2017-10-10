package nl.cyberdam.web.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {
	private final static int DEF_expirationTime = 36000;
	private FilterConfig filterConfig;
	private int expirationTime;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		try {
			expirationTime = Integer.parseInt(this.filterConfig
					.getInitParameter("expirationTime"));
		} catch (NumberFormatException e) {
			expirationTime = DEF_expirationTime;
		}
		if (expirationTime < 0) {
			expirationTime = 0;
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (response != null) {
			if (expirationTime == 0) {
				httpResponse.setDateHeader("expires", 0);
				httpResponse
						.setHeader(
								"cache-control",
								"public, max-age=0, no-cache, no-store, must-revalidate, proxy-revalidate, private");
			} else {
				Calendar cal = new GregorianCalendar();
				cal.add(Calendar.SECOND, expirationTime);
				httpResponse.setHeader("Cache-Control", "public, max-age="
						+ expirationTime + ", must-revalidate");
				httpResponse.setHeader("Expires", htmlExpiresDateFormat()
						.format(cal.getTime()));

			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		this.filterConfig = null;
	}

	private DateFormat htmlExpiresDateFormat() {
		DateFormat httpDateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return httpDateFormat;
	}

}
