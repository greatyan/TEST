package redsoft.wordx.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redsoft.wordx.word.repository.Repository;
import redsoft.wordx.word.repository.Review;

public class BackupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/xml");
		resp.setHeader("Content-disposition",
				"attachment; filename=reviews.xml");
		Repository repository = Repository.getRepository();
		Review[] reviews = repository.listAllReviews();
		OutputStream out = resp.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,
				"utf-8"));
		writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.println("<reviews>");
		for (Review review : reviews) {
			outputReview(writer, review);
		}
		writer.println("</reviews>");
		writer.close();
	}

	protected void outputReview(PrintWriter writer, Review review)
			throws IOException {

		writer.println("<review>");
		writer.print("<id>");
		writer.print(review.getReviewId());
		writer.println("</id>");
		writer.print("<user>");
		writer.print(review.getUserId());
		writer.println("</user>");
		writer.print("<headword>");
		outputText(writer, review.getHeadword());
		writer.print("</headword>");
		writer.print("<phonetic>");
		outputText(writer, review.getPhonetic());
		writer.print("</phonetic>");
		writer.print("<translation>");
		outputText(writer, review.getTranslation());
		writer.println("</translation>");
		writer.print("<level>");
		writer.print(review.getReivewLevel());
		writer.println("</level>");
		writer.print("<date>");
		outputDate(writer, review.getReviewDate());
		writer.println("</date>");
		writer.println("</review>");
	}

	static final SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");

	protected void outputText(Writer writer, String text) throws IOException {
		if (text != null) {
			for (int i = 0; i < text.length(); i++) {
				char ch = text.charAt(i);
				switch (ch) {
				case '&':
					writer.append("&amp;");
					break;
				case '<':
					writer.append("&lt;");
					break;
				case '>':
					writer.append("&gt;");
					break;
				default:
					writer.append(ch);
				}
			}
		}
	}

	protected void outputDate(Writer writer, Date date) throws IOException {
		if (date != null) {
			writer.write(ISO_8601_FORMAT.format(date));
		}
	}

}
