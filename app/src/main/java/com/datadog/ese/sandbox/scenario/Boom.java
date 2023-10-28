package com.datadog.ese.sandbox.scenario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

@WebServlet("/boom")
public class Boom extends HttpServlet {

  final static org.slf4j.Logger logger = LoggerFactory.getLogger("sandbox");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    try {
      System.out.println(5 / 0);
    } catch (RuntimeException e) {
      logger.error(e.getMessage(), e);
    }
    resp.getWriter().println("<h1>Hello world!</h1>");
  }

}
