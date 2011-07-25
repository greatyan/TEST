package redsoft.wordx.word.repository;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Word {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long wordId;
	@Persistent
	protected String headword;
	@Persistent
	protected Date createDate;
	@Persistent
	protected String phonetic;
	@Persistent
	protected String simpleTrans;
	@Persistent
	protected String detailTrans;

	public long getWordId() {
		return wordId;
	}

	public void setWordId(long wordId) {
		this.wordId = wordId;
	}

	public String getHeadword() {
		return headword;
	}

	public void setHeadword(String headword) {
		this.headword = headword;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public String getSimpleTrans() {
		return simpleTrans;
	}

	public void setSimpleTrans(String simpleTrans) {
		this.simpleTrans = simpleTrans;
	}

	public String getDetailTrans() {
		return detailTrans;
	}

	public void setDetailTrans(String detailTrans) {
		this.detailTrans = detailTrans;
	}

}
