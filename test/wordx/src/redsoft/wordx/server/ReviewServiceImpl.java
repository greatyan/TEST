package redsoft.wordx.server;

import java.util.ArrayList;
import java.util.Date;

import redsoft.wordx.shared.ReviewItem;
import redsoft.wordx.shared.UserItem;
import redsoft.wordx.word.repository.Repository;
import redsoft.wordx.word.repository.Review;
import redsoft.wordx.word.repository.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReviewServiceImpl extends RemoteServiceServlet implements
		redsoft.wordx.client.ReviewService {

	@Override
	public void addReview(long userId, ReviewItem item) {
		Repository repository = Repository.getRepository();
		Review review = repository.getReview(userId, item.getHeadword());
		if (review == null) {
			review = new Review();
			review.setUserId(userId);
			review.setHeadword(item.getHeadword());
			review.setPhonetic(item.getPhonetic());
			review.setTranslation(item.getTranslation());
			review.setReviewDate(new Date());
			review.setReivewLevel(0);
			repository.saveReview(review);
		}
	}

	@Override
	public ReviewItem[] listAllReivews(long userId, int start, int count,
			boolean asc) {
		Repository repository = Repository.getRepository();
		Review[] reviews = repository.listAllReviews(userId, start, count, asc);
		ReviewItem[] items = new ReviewItem[reviews.length];
		for (int i = 0; i < items.length; i++) {
			Review review = reviews[i];
			items[i] = wrapReview(review);
		}
		return items;
	}

	@Override
	public ReviewItem[] listReviews(long userId, int count) {
		Repository repository = Repository.getRepository();

		ArrayList<ReviewItem> items = new ArrayList<ReviewItem>(count);

		ArrayList<Review> reviews = new ArrayList<Review>(count);
		for (int level = 0; level < 9; level++) {
			Review[] rvs = repository.listReview(userId, level, 0, count);
			for (Review rv : rvs) {
				items.add(wrapReview(rv));
			}
			count -= rvs.length;
			if (count <= 0) {
				break;
			}
		}

		if (count > 0) {
			Review[] rvs = repository.listUnviews(userId, 0, count);
			for (int i = 0; i < rvs.length; i++) {
				items.add(wrapReview(rvs[i]));
			}
		}
		return items.toArray(new ReviewItem[items.size()]);
	}

	@Override
	public ReviewItem[] listUnviewWords(long userId, int count) {
		Repository repository = Repository.getRepository();
		Review[] reviews = repository.listUnviews(userId, 0, count);
		ReviewItem[] items = new ReviewItem[reviews.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = wrapReview(reviews[i]);
		}
		return items;
	}

	ReviewItem wrapReview(Review review) {
		ReviewItem item = new ReviewItem();
		item.setHeadword(review.getHeadword());
		item.setPhonetic(review.getPhonetic());
		item.setTranslation(review.getTranslation());
		item.setLevel(review.getReivewLevel());
		return item;
	}

	@Override
	public ReviewItem getReview(long userId, String headword) {
		Repository repository = Repository.getRepository();
		Review review = repository.getReview(userId, headword);
		if (review != null) {
			return wrapReview(review);
		}
		return null;
	}

	@Override
	public long addUser(String userName) {
		Repository repository = Repository.getRepository();
		User user = repository.getUser(userName);
		if (user != null) {
			return user.getUserId();
		}
		user = new User();
		user.setUserName(userName);
		repository.saveUser(user);
		return user.getUserId();
	}

	@Override
	public UserItem getUser(long userId) {
		Repository repository = Repository.getRepository();
		User user = repository.getUser(userId);
		return wrap(user);
	}

	@Override
	public UserItem[] listUsers() {
		Repository repository = Repository.getRepository();
		User[] users = repository.listUsers();
		UserItem[] items = new UserItem[users.length];
		for (int i = 0; i < users.length; i++) {
			items[i] = wrap(users[i]);
		}
		return items;
	}

	protected UserItem wrap(User user) {
		UserItem item = new UserItem();
		item.setUserId(user.getUserId());
		item.setUserName(user.getUserName());
		return item;
	}

}
