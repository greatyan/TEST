package redsoft.wordx.word.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class Repository {

	static Repository _instance = new Repository();

	static public Repository getRepository() {
		return _instance;
	}

	PersistenceManager pm;

	private Repository() {
		pm = JDOHelper.getPersistenceManagerFactory("transactions-optional")
				.getPersistenceManager();
	}

	public Word getWord(long wordId) {
		return pm.getObjectById(Word.class, wordId);
	}

	public Word[] getWord(String word) {
		Query query = pm.newQuery(Word.class);
		query.setFilter("headword == wordParam");
		query.declareParameters("String wordParam");
		try {
			List<Word> results = (List<Word>) query.execute(word);
			if (!results.isEmpty()) {
				return results.toArray(new Word[results.size()]);
			} else {
				return null;
			}
		} finally {
			query.closeAll();
		}
	}

	public void saveWord(Word word) {
		save(new Word[] { word });
	}

	public void saveWords(Word[] words) {
		save(words);
	}

	/**
	 * get the word in time order
	 * 
	 * @param date
	 * @param before
	 * @param count
	 * @return
	 */
	public Word[] listWord(Date date, boolean before, int count) {
		Query query = pm.newQuery(Word.class);
		if (before) {
			query.setFilter("createDate < dateParam");
			query.setOrdering("createDate desc");
		} else {
			query.setFilter("createDate > dateParam");
			query.setOrdering("createDate asc");
		}
		query.declareParameters("java.util.Date dateParam");
		if (count > 0) {
			query.setRange(0, count);
		}
		try {
			List<Word> results = (List<Word>) query.execute(date);
			if (!results.isEmpty()) {
				return results.toArray(new Word[results.size()]);
			} else {
				return new Word[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	/**
	 * list word in alphabet order
	 * 
	 * @param headword
	 * @param before
	 * @param count
	 * @return
	 */
	public Word[] listWord(String headword, boolean before, int count) {
		Query query = pm.newQuery(Word.class);
		if (before) {
			query.setFilter("headword < wordParam");
			query.setOrdering("headword desc");
		} else {
			query.setFilter("headword > wordParam");
			query.setOrdering("headword asc");
		}
		query.declareParameters("String wordParam");
		if (count > 0) {
			query.setRange(0, count);
		}
		try {
			List<Word> results = (List<Word>) query.execute(headword);
			if (!results.isEmpty()) {
				return results.toArray(new Word[results.size()]);
			} else {
				return new Word[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	public User[] listUsers() {
		Query query = pm.newQuery(User.class);
		try {
			List<User> results = (List<User>) query.execute();
			if (!results.isEmpty()) {
				return results.toArray(new User[results.size()]);
			} else {
				return new User[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	public User getUser(String userName) {
		Query query = pm.newQuery(User.class);
		query.setFilter("userName == userParam");
		query.declareParameters("String userParam");
		try {
			List<User> results = (List<User>) query.execute(userName);
			if (!results.isEmpty()) {
				return results.get(0);
			} else {
				return null;
			}
		} finally {
			query.closeAll();
		}
	}

	public User getUser(long userId) {
		return pm.getObjectById(User.class, userId);
	}

	public void saveUser(User user) {
		save(new User[] { user });
	}

	public void saveUser(User[] users) {
		save(users);
	}

	public void saveReview(Review review) {
		save(new Review[] { review });
	}

	public void saveReviews(Review[] reviews) {
		save(reviews);
	}

	protected void save(Object[] objects) {
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistentAll(objects);
			tx.commit();
		} catch (Exception x) {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public Review getReview(long userId, String headword) {
		Query query = pm.newQuery(Review.class);
		query.setFilter("userId == userParam && headword == headwordParam");
		query.declareParameters("Long userParam, String headwordParam");

		try {
			List<Review> results = (List<Review>) query.execute(userId,
					headword);
			if (!results.isEmpty()) {
				return results.get(0);
			} else {
				return null;
			}
		} finally {
			query.closeAll();
		}
	}

	public Review[] listUnviews(long userId, int start, int count) {

		Query unviewQuery = pm.newQuery(Review.class);
		unviewQuery.setFilter("userId != userParam");
		unviewQuery.declareParameters("long userParam");
		try {
			List<Review> results = (List<Review>) unviewQuery.execute(userId);
			if (!results.isEmpty()) {
				ArrayList<Review> unviews = new ArrayList<Review>(count);
				int index = 0;
				for (Review unview : results) {
					if (getReview(userId, unview.getHeadword()) == null) {
						if (index >= start) {
							unviews.add(unview);
							if (unviews.size() >= count) {
								return unviews.toArray(new Review[unviews
										.size()]);
							}
						}
						index++;
					}
				}
				return unviews.toArray(new Review[unviews.size()]);
			}
			return new Review[] {};
		} finally {
			unviewQuery.closeAll();
		}
	}

	public Review[] listAllReviews() {
		return listAllReviews(0, 0);
	}

	public Review[] listAllReviews(int start, int count) {
		Query query = pm.newQuery(Review.class);
		if (count <= 0) {
			query.setRange(start, Long.MAX_VALUE);
		} else {
			query.setRange(start, start + count);
		}

		try {
			List<Review> results = (List<Review>) query.execute();
			if (!results.isEmpty()) {
				return results.toArray(new Review[results.size()]);
			} else {
				return new Review[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	public Review[] listAllReviews(long userId, int start, int count, boolean asc) {
		Query query = pm.newQuery(Review.class);
		query.setFilter("userId == userParam");
		query.declareParameters("int userParam");
		if (asc) {
			query.setOrdering("reviewDate asc");
		} else {
			query.setOrdering("reviewDate desc");
		}

		if (count <= 0) {
			query.setRange(start, Long.MAX_VALUE);
		} else {
			query.setRange(start, start + count);
		}

		try {
			List<Review> results = (List<Review>) query.execute(userId);
			if (!results.isEmpty()) {
				return results.toArray(new Review[results.size()]);
			} else {
				return new Review[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	/**
	 * list review in time order
	 * 
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public Review[] listReviewBefore(long userId, Date date) {
		return listReviewBefore(userId, date, 0, -1);
	}

	public Review[] listReviewBefore(long userId, Date date, int start,
			int count) {
		return listReview(userId, date, true, start, count);
	}

	public Review[] listReviewAfter(long userId, Date date) {
		return listReviewAfter(userId, date, 0, -1);
	}

	public Review[] listReviewAfter(long userId, Date date, int start, int count) {
		return listReview(userId, date, false, start, count);
	}

	public Review[] listReview(long userId, Date date, boolean before,
			int start, int count) {
		Query query = pm.newQuery(Review.class);
		if (before) {
			query.setFilter("userId == userParam && reviewDate <= dateParam");
			query.setOrdering("reviewDate desc");
		} else {
			query.setFilter("userId == userParam && reviewDate >= dateParam");
			query.setOrdering("reviewDate asc");
		}
		query.declareParameters("int userParam");
		query.declareParameters("java.util.Date dateParam");

		if (count > 0) {
			query.setRange(start, start + count);
		} else {
			query.setRange(start, Long.MAX_VALUE);
		}

		try {
			List<Review> results = (List<Review>) query.execute(userId, date);
			if (!results.isEmpty()) {
				return results.toArray(new Review[results.size()]);
			} else {
				return new Review[] {};
			}
		} finally {
			query.closeAll();
		}
	}

	public Review[] listReview(long userId, int level) {
		return listReview(userId, level, 0, -1);
	}

	/**
	 * list review in level order
	 * 
	 * @param userId
	 * @param level
	 * @return
	 */
	public Review[] listReview(long userId, int level, int start, int count) {
		Query query = pm.newQuery(Review.class);
		query.setFilter("userId == userParam && reivewLevel == levelParam");
		query.setOrdering("reviewDate desc");
		query.declareParameters("long userParam, int levelParam");
		if (count <= 0) {
			query.setRange(start, Long.MAX_VALUE);
		} else {
			query.setRange(start, start + count);
		}

		try {
			List<Review> results = (List<Review>) query.execute(userId, level);
			if (!results.isEmpty()) {
				return results.toArray(new Review[results.size()]);
			} else {
				return new Review[] {};
			}
		} finally {
			query.closeAll();
		}
	}
}
