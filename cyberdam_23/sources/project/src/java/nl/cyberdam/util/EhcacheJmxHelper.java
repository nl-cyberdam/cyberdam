package nl.cyberdam.util;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

/**
 * Utility class that registers the ehcache mbeans with the default mbeanserver - supply
 * the cacheManager as a constructor argument. 
 */
public class EhcacheJmxHelper {
	
	public EhcacheJmxHelper(CacheManager cacheMgr) {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	    ManagementService.registerMBeans(cacheMgr, mbs, true, true, true, true);
	}

}
