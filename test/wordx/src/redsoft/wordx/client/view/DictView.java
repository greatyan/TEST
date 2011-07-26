package redsoft.wordx.client.view;

import redsoft.wordx.client.presenter.DictOracle;
import redsoft.wordx.client.presenter.DictPresenter.Display;
import redsoft.wordx.client.presenter.DictPresenter.OnAddReviewHandler;
import redsoft.wordx.client.presenter.DictPresenter.OnSearchHandler;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;

public class DictView extends Composite implements Display {

	private static DictViewUiBinder uiBinder = GWT
			.create(DictViewUiBinder.class);

	interface DictViewUiBinder extends UiBinder<Widget, DictView> {
	}

	protected SoundController soundController = new SoundController();

	@UiField
	protected Image reviewImage;

	@UiField(provided = true)
	protected SuggestBox headwordText = new SuggestBox(new DictOracle());

	@UiField
	protected HTML explainFrame;

	protected String headword;
	protected String phonetic;
	protected String simpExplain;
	protected boolean reviewed;
	protected boolean enableReview;

	public DictView() {
		initWidget(uiBinder.createAndBindUi(this));
		headwordText.getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				headwordText.getTextBox().selectAll();
			}
		});
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public String getHeadword() {
		return headwordText.getText();
	}

	public void playSound(String url) {
		Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG_MP3,
				url);
		sound.play();
	}

	@Override
	public void setOnSearch(final OnSearchHandler handler) {
		headwordText
				.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
					@Override
					public void onSelection(
							SelectionEvent<SuggestOracle.Suggestion> event) {
						SuggestOracle.Suggestion suggest = event
								.getSelectedItem();
						String headword = suggest.getReplacementString();
						String explain = suggest.getDisplayString().substring(
								headword.length() + 2);
						handler.onSearch(headword, explain);
					}

				});

	}

	@Override
	public HTML getExplain() {
		return explainFrame;
	}

	@Override
	public void setWord(String headword) {
		headwordText.setText(headword);
	}

	@Override
	public void setReviewed(boolean reviewed) {

		if (reviewImage != null) {
			if (reviewed) {
				reviewImage.setUrl("resources/star-gold32.png");
			} else {
				reviewImage.setUrl("resources/star-white32.png");
			}
		}
	}

	@Override
	public void setOnAddReview(final OnAddReviewHandler handler) {
		// try to get the onReviewImage
		reviewImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (enableReview) {
					handler.onAddReview();
				}
			}
		});
	}

	@Override
	public void enableAddReview(boolean enable) {
		enableReview = enable;
	}
}
