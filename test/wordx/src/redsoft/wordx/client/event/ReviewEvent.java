package redsoft.wordx.client.event;

import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.event.shared.GwtEvent;

public class ReviewEvent extends GwtEvent<ReviewEventHandler> {

	public static Type<ReviewEventHandler> TYPE = new Type<ReviewEventHandler>();

	protected ReviewItem[] reviews;
	protected String headword;

	public ReviewEvent(ReviewItem[] reviews, String headword) {
		this.reviews = reviews;
		this.headword = headword;
	}

	@Override
	public Type<ReviewEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ReviewEventHandler handler) {
		handler.onReview(this);
	}

	public ReviewItem[] getReviews() {
		return reviews;
	}

	public String getHeadword() {
		return headword;
	}
}
