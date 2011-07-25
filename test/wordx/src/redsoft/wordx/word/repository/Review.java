package redsoft.wordx.word.repository;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Review {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long reviewId;
	@Persistent
	private long userId;
	@Persistent
	private String headword;
	@Persistent
	private String phonetic;
	@Persistent
	private String translation;
	@Persistent
	private int reivewLevel;
	@Persistent
	private Date reviewDate;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getHeadword() {
		return headword;
	}

	public void setHeadword(String headword) {
		this.headword = headword;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public int getReivewLevel() {
		return reivewLevel;
	}

	public void setReivewLevel(int reivewLevel) {
		this.reivewLevel = reivewLevel;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public Long getReviewId() {
		return reviewId;
	}
}
