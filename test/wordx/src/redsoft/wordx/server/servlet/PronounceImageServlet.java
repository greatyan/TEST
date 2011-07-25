package redsoft.wordx.server.servlet;

import gmail.greatyan.phonetic.appengine.SymbolImageService;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PronounceImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SymbolImageService service;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			service = SymbolImageService.getService();
		} catch (IOException ex) {
			throw new ServletException("can't initalize the image service", ex);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pronounce = req.getParameter("pronounce");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("image/png");
		OutputStream out = resp.getOutputStream();
		byte[] bytes = service.getImageData(pronounce);
		out.write(bytes);
		out.flush();
	}

}
