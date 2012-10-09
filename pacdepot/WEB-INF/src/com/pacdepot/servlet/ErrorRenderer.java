package com.pacdepot.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Refer to "Java and XSLT" 1/e, O'Reilly, p.297
 * 
 */
public class ErrorRenderer extends Renderer {
	private String message;
	private Throwable throwable;

	public ErrorRenderer(Throwable throwable) {
		this(throwable, throwable.getMessage());
	}

	public ErrorRenderer(String message) {
		this(null, message);
	}

	public ErrorRenderer(Throwable throwable, String message) {
		this.throwable = throwable;
		this.message = message;
	}

	public void render(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		pw.println("<html>");
		pw.println("<body>");
		pw.println("<p>");
		pw.println(this.message);
		pw.println("</p>");

		if (this.throwable != null) {
			pw.println("<pre>");
			this.throwable.printStackTrace(pw);
			pw.println("</pre>");
		}

		pw.println("</body>");
		pw.println("</html>");
	}

}
