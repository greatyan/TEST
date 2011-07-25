package redsoft.wordx.client;

import redsoft.wordx.shared.ReviewItem;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReviewServiceAsync {

	void listAllReivews(long userId, int start, int count, boolean asc,
			AsyncCallback<ReviewItem[]> callback);

	void listReviews(long userId, int count,
			AsyncCallback<ReviewItem[]> callback);

	void listUnviewWords(long userId, int count,
			AsyncCallback<ReviewItem[]> callback);

	void getReview(long userId, String headword,
			AsyncCallback<ReviewItem> callback);

	void addUser(String userName, AsyncCallback<Long> callback);

	void getUser(long userId, AsyncCallback<UserItem> callback);

	void listUsers(AsyncCallback<UserItem[]> callback);

	void addReview(long userId, ReviewItem review, AsyncCallback<Void> callback);

}
