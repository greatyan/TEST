package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.client.dict.GoogleDict;
import redsoft.wordx.client.dict.GoogleDict.Result;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class DictPresenter implements Presenter {

	static public interface OnSearchHandler {
		public void onSearch(String headword, String simpTrans);
	}

	static public interface OnAddReviewHandler {
		public void onAddReview();
	}

	static public interface Display {

		void setOnSearch(OnSearchHandler handler);

		void setOnAddReview(OnAddReviewHandler handler);

		void enableAddReview(boolean enable);

		void setWord(String headword);

		HTML getExplain();

		String getHeadword();

		Widget asWidget();

		void setReviewed(boolean reviewd);

		void playSound(String url);
	}

	protected long userId;
	protected ReviewServiceAsync service;
	protected HandlerManager eventBus;

	protected Display display;

	protected String headword;
	protected String phonetic;
	protected String simpTrans;
	protected boolean isReviewed;

	public DictPresenter(long userId, ReviewServiceAsync service,
			HandlerManager eventBus, Display display) {
		this.userId = userId;
		this.service = service;
		this.eventBus = eventBus;
		this.display = display;
	}

	private void bind() {

		display.setOnSearch(new OnSearchHandler() {
			public void onSearch(String headword, String simpTrans) {
				display.enableAddReview(false);
				isReviewed = false;
				DictPresenter.this.headword = headword;
				DictPresenter.this.simpTrans = simpTrans;
				GoogleDict.search(headword, new GoogleDict.OnSearchHandler() {
					@Override
					public void onSearch(Result result) {
						String htmlText = result.getHtmlText();
						if (htmlText != null && htmlText.length() > 0) {
							display.getExplain().setHTML(htmlText);
							// add the click to all the images
							DictPresenter.this.phonetic = result.getPhonetic();
							display.enableAddReview(true);

							// DOM.sinkEvents(display.getExplain().getElement(),
							// Event.CLICK );
							// display.getExplain().addClickHandler(new
							// ClickHandler() {
							// public void onClick(ClickEvent event) {
							// Object src = event.getSource();
							// display.playSound(anchorCtrl
							// .getHref());
							// }
							// });

							NodeList<Element> sounds = display.getExplain()
									.getElement().getElementsByTagName("img");
							for (int i = 0; i < sounds.getLength(); i++) {
								Element element = sounds.getItem(i);
								final ImageElement image = ImageElement
										.as(element);
								DOM.sinkEvents(
										(com.google.gwt.user.client.Element) element,
										Event.ONCLICK);
								DOM.setEventListener(
										(com.google.gwt.user.client.Element) element,
										new EventListener() {
											@Override
											public void onBrowserEvent(
													Event event) {
												if (event.getTypeInt() == Event.ONCLICK) {
													display.playSound(image
															.getTitle());
												}
											}

										});
							}
						} else {
							display.getExplain().setHTML("unknow word");
							display.enableAddReview(false);
						}
					}
				});
				service.getReview(userId, headword,
						new AsyncCallback<ReviewItem>() {
							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(ReviewItem result) {
								if (result != null) {
									isReviewed = true;
									display.setReviewed(true);
								} else {
									isReviewed = false;
									display.setReviewed(false);
								}
							}
						});
			}
		});

		display.setOnAddReview(new OnAddReviewHandler() {
			public void onAddReview() {
				if (isReviewed) {
					return;
				}
				ReviewItem item = new ReviewItem();
				item.setHeadword(headword);
				item.setPhonetic(phonetic);
				item.setTranslation(simpTrans);
				service.addReview(userId, item, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
						isReviewed = true;
						display.setReviewed(true);
					}
				});
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

}
