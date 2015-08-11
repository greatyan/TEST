package redsoft.wordx.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddReviewEvent extends GwtEvent<AddReviewEventHandler> {

	public static Type<AddReviewEventHandler> TYPE = new Type<AddReviewEventHandler>();

	protected String headword;
	protected String phonetic;
	protected String explain;

	public AddReviewEvent() {
	}

	public AddReviewEvent(String headword, String phonetic, String explain) {
		this.headword = headword;
		this.phonetic = phonetic;
		this.explain = explain;
	}

	@Override
	public Type<AddReviewEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddReviewEventHandler handler) {
		handler.onAddReview(this);
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

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

}
