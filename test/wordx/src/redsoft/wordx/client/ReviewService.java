package redsoft.wordx.client;

import redsoft.wordx.shared.ReviewItem;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("reviewService")
public interface ReviewService extends RemoteService {

	void addReview(long userId, ReviewItem review);

	ReviewItem[] listUnviewWords(long userId, int count);

	ReviewItem[] listAllReivews(long userId, int start, int count, boolean asc);

	ReviewItem[] listReviews(long userId, int count);

	ReviewItem getReview(long userId, String headword);

	long addUser(String userName);

	UserItem getUser(long userId);

	UserItem[] listUsers();

}
