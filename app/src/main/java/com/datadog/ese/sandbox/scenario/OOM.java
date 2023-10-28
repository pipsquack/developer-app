package com.datadog.ese.sandbox.scenario;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.LoggerFactory;

@WebServlet("/oom")
public class OOM extends HttpServlet {

  final static org.slf4j.Logger logger = LoggerFactory.getLogger("sandbox");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    new Thread(() -> forceOOM()).start();
    resp.getWriter().println("<h1>Hello world!</h1>");
  }

  private void forceOOM() {
    Map<Object, Object> m = new HashMap<Object, Object>();
    logger.info("Exhausting memory");
    while (true) {
      m.put(RandomStringUtils.randomAlphabetic(16), RandomStringUtils.randomAlphabetic(64 * 1024));
    }
  }

}
