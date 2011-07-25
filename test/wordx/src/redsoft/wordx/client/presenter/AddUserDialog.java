package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddUserDialog extends DialogBox {

	protected ReviewServiceAsync reviewService;
	protected AddUserCallback callback;

	private static AddUserDialogUiBinder uiBinder = GWT
			.create(AddUserDialogUiBinder.class);

	@UiField
	TextBox userText;
	@UiField
	Button createButton;
	@UiField
	Button cancelButton;

	interface AddUserDialogUiBinder extends UiBinder<Widget, AddUserDialog> {
	}

	static public interface AddUserCallback {
		void onAddUser(long userId);
	}

	public AddUserDialog(ReviewServiceAsync reviewService,
			AddUserCallback callback) {
		super(false);
		this.reviewService = reviewService;
		this.callback = callback;
		setWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		createButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// add a user ...
				String userName = userText.getText();
				if (userName != null && userName.length() > 0) {
					reviewService.addUser(userName, new AsyncCallback<Long>() {
						@Override
						public void onFailure(Throwable caught) {
							AddUserDialog.this.hide();
						}

						@Override
						public void onSuccess(Long user) {
							AddUserDialog.this.hide();
							if (callback != null) {
								callback.onAddUser(user);
							}
						}
					});
				}
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AddUserDialog.this.hide();
			}
		});
	}
}
