package redsoft.wordx.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReviewItem implements IsSerializable {

	protected long reivewId;
	protected String headword;
	protected String phonetic;
	protected String translation;
	protected int level;

	public long getReivewId() {
		return reivewId;
	}

	public void setReivewId(long reivewId) {
		this.reivewId = reivewId;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

}
