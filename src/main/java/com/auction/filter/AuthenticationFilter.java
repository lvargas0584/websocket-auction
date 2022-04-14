package com.auction.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//@WebFilter
public class AuthenticationFilter implements Filter {
   private ServletContext context;

   public void init(FilterConfig fConfig) throws ServletException {
      this.context = fConfig.getServletContext();
      this.context.log("AuthenticationFilter initialized");
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      HttpServletRequest req = (HttpServletRequest)request;
      HttpServletResponse res = (HttpServletResponse)response;
      String uri = req.getRequestURI();
      this.context.log("Requested Resource::" + uri);
      HttpSession session = req.getSession(false);
      System.out.println(req.getHeader("Origin"));
      if (req.getHeader("Origin") != null) {
         if (session == null && !req.getHeader("Origin").contains("websocketking.com")) {
            this.context.log("Unauthorized access request");
            res.setStatus(401);
         } else {
            this.context.log("AuthenticationFilter Success");
            chain.doFilter(request, response);
         }
      } else {
         this.context.log("Unauthorized access request");
      }

   }

   public void destroy() {
   }
}
