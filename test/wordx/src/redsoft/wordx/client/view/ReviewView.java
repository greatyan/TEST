package redsoft.wordx.client.view;

import redsoft.wordx.client.presenter.ReviewPresenter.Display;
import redsoft.wordx.client.presenter.ReviewPresenter.EventHandler;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ReviewView extends Composite implements Display {

	interface ReviewUiBinder extends UiBinder<Widget, ReviewView> {
	}

	private static ReviewUiBinder uiBinder = GWT.create(ReviewUiBinder.class);

	@UiField
	Image prevButton;
	@UiField
	Image nextButton;
	@UiField
	Label headword;
	@UiField
	Label phonetic;
	@UiField
	Label translation;
	@UiField
	HTML explain;

	protected ReviewItem review;

	public ReviewView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setEventHandler(final EventHandler handler) {
		nextButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				handler.onNext();
			}
		});
		prevButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				handler.onPrev();
			}
		});
		phonetic.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				handler.onSearch();
			}
		});
	}

	@Override
	public void setReview(ReviewItem review) {
		explain.setVisible(false);
		headword.setText(review.getHeadword());
		phonetic.setText(review.getPhonetic());
		translation.setText(review.getTranslation());
	}

	@Override
	public void setExplain(String html) {
		explain.setHTML(html);
	}

	public void enableNext(boolean enable) {
		if (enable) {
			nextButton.setUrl("resources/Right_32_gray.png");
		} else {
			nextButton.setUrl("resources/Right_32.png");
		}
	}

	public void enablePrev(boolean enable) {
		if (enable) {
			prevButton.setUrl("resources/Left_32_gray.png");
		} else {
			prevButton.setUrl("resources/Left_32.png");
		}
	}

}
