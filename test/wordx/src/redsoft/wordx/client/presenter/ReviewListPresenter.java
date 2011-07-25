package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.client.event.ReviewEvent;
import redsoft.wordx.client.view.Dialog;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ReviewListPresenter implements Presenter {

	static public interface ReviewHandler {
		void onReview(String headword);
	}

	static public interface Display {
		HasClickHandlers getPrevButton();

		HasClickHandlers getNextButton();

		HasClickHandlers getModeButton();

		void setReviewHandler(ReviewHandler handler);

		// HasClickHandlers getReviewList();

		void setReviewList(ReviewItem[] reviews);

		void setModel(String label);

		void enableNextButton(boolean enable);

		void enablePrevButton(boolean enable);

		Widget asWidget();
	}

	enum REVIEW_ACTION {
		NEXT, PREV, INIT
	}

	static final int PAGE_SIZE = 20;
	protected long userId;

	protected int mode;
	protected int start;
	protected int count;

	protected ReviewItem[] reviews;
	protected ReviewServiceAdapter service;
	protected HandlerManager eventBus;
	protected Display display;

	public ReviewListPresenter(long userId, ReviewServiceAsync service,
			HandlerManager eventBus, Display display, String request) {
		this.userId = userId;
		this.eventBus = eventBus;
		this.service = new ReviewServiceAdapter(service, userId);
		if (request != null) {
			this.service.setRequestToken(request);
		}
		this.display = display;
		this.mode = this.service.getMode();
		this.start = this.service.getStart();
		this.count = this.service.getCount();
	}

	private void bind() {
		// display.getViewButton().addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// // change view mode
		// // eventBus.fireEvent(new AddReviewEvent());
		// }
		// });

		display.getModeButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (mode == ReviewServiceAdapter.ORDER_DATE_ASC) {
					mode = ReviewServiceAdapter.ORDER_DATE_DEC;
					display.setModel("时间顺序");

				} else if (mode == ReviewServiceAdapter.ORDER_DATE_DEC) {
					mode = ReviewServiceAdapter.ORDER_REVIEW;
					display.setModel("记忆顺序");

				} else if (mode == ReviewServiceAdapter.ORDER_REVIEW) {
					mode = ReviewServiceAdapter.ORDER_DATE_ASC;
					display.setModel("时间倒序");
				}
				start = 0;
				count = 0;
				listReviews(REVIEW_ACTION.INIT);
			}
		});

		display.getPrevButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (mode != ReviewServiceAdapter.ORDER_REVIEW && start > 0) {
					listReviews(REVIEW_ACTION.PREV);
				}
			}
		});

		display.getNextButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (mode != ReviewServiceAdapter.ORDER_REVIEW
						&& count == PAGE_SIZE) {
					listReviews(REVIEW_ACTION.NEXT);
				}
			}
		});

		display.setReviewHandler(new ReviewHandler() {
			public void onReview(String headword) {
				String indexToken = service.getRequestToken();
				History.newItem("review." + indexToken);
				eventBus.fireEvent(new ReviewEvent(reviews, headword));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		listReviews(REVIEW_ACTION.INIT);
	}

	void listReviews(REVIEW_ACTION action) {
		int startNumber = start;
		if (action == REVIEW_ACTION.NEXT) {
			startNumber += PAGE_SIZE;
		} else if (action == REVIEW_ACTION.PREV) {
			startNumber -= PAGE_SIZE;
		}
		OnReviewCallback callback = new OnReviewCallback(action);
		service.setMode(mode);
		service.setStart(startNumber);
		service.setCount(PAGE_SIZE);
		service.listReviews(callback);
	}

	void setupUIStatus() {
		if (mode == ReviewServiceAdapter.ORDER_REVIEW) {
			display.enableNextButton(false);
			display.enablePrevButton(false);
		} else {
			if (start == 0) {
				display.enablePrevButton(false);
			} else {
				display.enablePrevButton(true);
			}
			if (count < PAGE_SIZE) {
				display.enableNextButton(false);
			} else {
				display.enableNextButton(true);
			}
		}
	}

	private class OnReviewCallback implements AsyncCallback<ReviewItem[]> {

		REVIEW_ACTION action;

		OnReviewCallback(REVIEW_ACTION action) {
			this.action = action;
		}

		@Override
		public void onFailure(Throwable caught) {
			Dialog.failed(caught);
		}

		@Override
		public void onSuccess(ReviewItem[] result) {
			display.setReviewList(result);
			if (action == REVIEW_ACTION.NEXT) {
				start += PAGE_SIZE;
			} else if (action == REVIEW_ACTION.PREV) {
				start -= PAGE_SIZE;
			}
			count = result.length;
			setupUIStatus();
		}
	}

}
