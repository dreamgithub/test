package com.lovebaby.listeners;

import java.beans.Introspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.CachedIntrospectionResults;

public class InitSourceContextListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context=event.getServletContext();
		context.removeAttribute("path");
		 CachedIntrospectionResults.clearClassLoader(Thread.currentThread().getContextClassLoader());
		 Introspector.flushCaches();

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		ServletContext context=event.getServletContext();
		context.setAttribute("path", context.getContextPath());
	}

}
