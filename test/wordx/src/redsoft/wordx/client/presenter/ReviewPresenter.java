package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.client.dict.GoogleDict;
import redsoft.wordx.client.dict.GoogleDict.Result;
import redsoft.wordx.client.view.Dialog;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ReviewPresenter implements Presenter {

	static public interface EventHandler {
		void onSearch();

		void onNext();

		void onPrev();

		void onLevel(int value);
	}

	static public interface Display {

		void setEventHandler(EventHandler handler);

		void setExplain(String html);

		void setReview(ReviewItem review);

		void enableNext(boolean enable);

		void enablePrev(boolean enable);

		Widget asWidget();
	}

	protected ReviewServiceAdapter service;
	// protected long userId;
	// protected ReviewServiceAsync service;
	protected HandlerManager eventBus;
	protected Display display;

	protected int reviewMode;
	protected int reviewStart;
	protected int reviewCount;

	protected ReviewItem[] reviews;
	protected int[] reviewLevels;
	protected int reviewIndex;

	public ReviewPresenter(ReviewServiceAsync service, long userId,
			HandlerManager eventBus, Display display, String request) {
		this.eventBus = eventBus;
		this.display = display;
		this.service = new ReviewServiceAdapter(service, userId);
		if (request != null) {
			this.service.setRequestToken(request);
		}
	}

	private void bind() {
		display.setEventHandler(new EventHandler() {
			public void onSearch() {
				String headword = reviews[reviewIndex].getHeadword();
				GoogleDict.search(headword, new GoogleDict.OnSearchHandler() {
					@Override
					public void onSearch(Result result) {
						String htmlText = result.getHtmlText();
						if (htmlText != null && htmlText.length() > 0) {
							display.setExplain(htmlText);
						} else {
							display.setExplain("unknow word");
						}
					}
				});
			}

			public void onPrev() {
				if (reviewIndex > 0) {
					reviewIndex--;
					display.setReview(reviews[reviewIndex]);
				}
				display.enablePrev(reviewIndex >= 0);
			}

			public void onNext() {
				if (reviewIndex < reviews.length - 1) {
					reviewIndex++;
					display.setReview(reviews[reviewIndex]);
				}
				display.enableNext(reviewIndex < reviews.length - 1);
			}

			public void onLevel(int value) {
				reviewLevels[reviewIndex] = value;
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		listReviews();
	}

	void listReviews() {
		OnReviewCallback callback = new OnReviewCallback();
		service.listReviews(callback);
	}

	private class OnReviewCallback implements AsyncCallback<ReviewItem[]> {

		OnReviewCallback() {
		}

		@Override
		public void onFailure(Throwable caught) {
			Dialog.failed(caught);
		}

		@Override
		public void onSuccess(ReviewItem[] result) {
			reviews = result;
			reviewLevels = new int[reviews.length];
			if (reviewIndex > reviews.length) {
				reviewIndex = reviews.length - 1;
			}
		}
	}
}
