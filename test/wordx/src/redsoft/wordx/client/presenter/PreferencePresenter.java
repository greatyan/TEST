package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class PreferencePresenter implements Presenter {

	static public interface Display {

		HasClickHandlers getSetUserButton();

		void setUserName(String userName);

		void setUsers(UserItem[] users);

		String getUserName();

		Widget asWidget();
	}

	protected ReviewServiceAsync reviewService;
	protected HandlerManager eventBus;
	protected Display display;

	public PreferencePresenter(ReviewServiceAsync reviewService,
			HandlerManager eventBus, Display display) {
		this.reviewService = reviewService;
		this.eventBus = eventBus;
		this.display = display;
	}

	private void bind() {
		display.getSetUserButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});

		reviewService.listUsers(new AsyncCallback<UserItem[]>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(UserItem[] result) {
				display.setUsers(result);
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
		// get the login user name and set iti to the user name
		display.setUserName("TEST");
	}
}
