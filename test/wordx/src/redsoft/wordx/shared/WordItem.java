package redsoft.wordx.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WordItem implements IsSerializable {

	protected String headword;
	protected String phonetic;
	protected String simpleTrans;
	protected String detailTrans;

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
