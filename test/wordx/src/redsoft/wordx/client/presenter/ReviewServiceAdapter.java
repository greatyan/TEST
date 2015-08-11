package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReviewServiceAdapter {

	static final int ORDER_DATE_ASC = 0;
	static final int ORDER_DATE_DEC = 1;
	static final int ORDER_REVIEW = 2;

	static final int MAX_COUNT = 20;

	ReviewServiceAsync service;
	long userId;

	int mode;
	int start;
	int count;

	ReviewServiceAdapter(ReviewServiceAsync service, long userId) {
		this.service = service;
		this.userId = userId;
		this.mode = ORDER_DATE_ASC;
		this.start = 0;
		this.count = 20;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void listReviews(AsyncCallback<ReviewItem[]> callback) {

		if (mode == ORDER_DATE_DEC) {
			service.listAllReivews(userId, start, count, true, callback);
		} else if (mode == ORDER_DATE_ASC) {
			service.listAllReivews(userId, start, count, false, callback);
		} else if (mode == ORDER_REVIEW) {
			service.listReviews(userId, count, callback);
		}
	}

	public void setRequestToken(String token) {
		String[] tokens = token.split(".");
		if (tokens.length == 3) {
			mode = Integer.parseInt(tokens[0]);
			start = Integer.parseInt(tokens[1]);
			count = Integer.parseInt(tokens[2]);
		}
	}

	public String getRequestToken() {
		StringBuilder sb = new StringBuilder();
		sb.append(mode);
		sb.append(".");
		sb.append(start);
		sb.append(".");
		sb.append(count);
		return sb.toString();

	}
}
