package redsoft.wordx.client.view;

import java.util.Arrays;

import redsoft.wordx.client.presenter.ReviewListPresenter.Display;
import redsoft.wordx.client.presenter.ReviewListPresenter.ReviewHandler;
import redsoft.wordx.shared.ReviewItem;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ReviewListView extends Composite implements Display {

	interface ReviewListViewUiBinder extends UiBinder<Widget, ReviewListView> {
	}

	private static ReviewListViewUiBinder uiBinder = GWT
			.create(ReviewListViewUiBinder.class);
	@UiField(provided = true)
	CellTable<ReviewItem> reviewList = new CellTable<ReviewItem>();

	@UiField
	Image nextButton;
	@UiField
	Image prevButton;
	@UiField
	Label headLabel;

	ReviewHandler reviewHandler;

	public ReviewListView() {

		initWidget(uiBinder.createAndBindUi(this));

		reviewList.addColumn(new TextColumn<ReviewItem>() {

			@Override
			public String getValue(ReviewItem object) {
				return object.getHeadword();
			}

		}, "HEADWORD");

		reviewList.addColumn(new TextColumn<ReviewItem>() {

			@Override
			public String getValue(ReviewItem object) {
				return object.getPhonetic();
			}

			public void render(Cell.Context context, ReviewItem object,
					SafeHtmlBuilder sb) {
				String htmlText = "<img style=\"height:16px\" src=\"/wordx/pronounce?pronounce="
						+ object.getPhonetic() + "\"/>";
				sb.appendHtmlConstant(htmlText);
			}

		}, "PHONETIC");

		reviewList.addColumn(new TextColumn<ReviewItem>() {

			@Override
			public String getValue(ReviewItem object) {
				return object.getTranslation();
			}

		}, "TRANLATION");

		final SingleSelectionModel<ReviewItem> selectionModel = new SingleSelectionModel<ReviewItem>();
		reviewList.setSelectionModel(selectionModel);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						if (reviewHandler != null) {
							ReviewItem selected = selectionModel
									.getSelectedObject();
							if (selected != null) {
								reviewHandler.onReview(selected.getHeadword());
							}
						}
					}
				});
	}

	@Override
	public HasClickHandlers getNextButton() {
		// TODO Auto-generated method stub
		return nextButton;
	}

	@Override
	public HasClickHandlers getPrevButton() {
		// TODO Auto-generated method stub
		return prevButton;
	}

	public HasClickHandlers getModeButton() {
		return headLabel;
	}

	public void setModel(String mode) {
		headLabel.setText(mode);
	}

	public void enablePrevButton(boolean enable) {
		if (enable) {
			prevButton.setUrl("resources/Left-32.png");
		} else {
			prevButton.setUrl("resources/Left-32-gray.png");
		}
	}

	public void enableNextButton(boolean enable) {
		if (enable) {
			nextButton.setUrl("resources/Right-32.png");
		} else {
			nextButton.setUrl("resources/Right-32-gray.png");
		}
	}

	@Override
	public void setReviewList(ReviewItem[] reviews) {

		reviewList.setRowCount(reviews.length);
		reviewList.setRowData(Arrays.asList(reviews));
	}

	public void setReviewHandler(ReviewHandler handler) {
		this.reviewHandler = handler;
	}
}
