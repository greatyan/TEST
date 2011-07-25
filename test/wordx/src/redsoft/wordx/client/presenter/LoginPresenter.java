package redsoft.wordx.client.presenter;

import redsoft.wordx.client.ReviewServiceAsync;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter implements Presenter {

	static public interface Display {
		HasClickHandlers getLoginButton();

		HasClickHandlers getCreateUserButton();

		void setUserList(UserItem[] users);

		void setUser(UserItem user);

		UserItem getUser();

		Widget asWidget();
	}

	protected ReviewServiceAsync reviewService;
	protected HandlerManager eventBus;
	protected Display display;

	public LoginPresenter(ReviewServiceAsync reviewService,
			HandlerManager eventBus, Display display) {
		this.reviewService = reviewService;
		this.eventBus = eventBus;
		this.display = display;
	}

	private void bind() {
		display.getCreateUserButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				new AddUserDialog(reviewService,
						new AddUserDialog.AddUserCallback() {

							@Override
							public void onAddUser(long userId) {
								listUsers(userId);
							}
						}).center();
			}
		});

		display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UserItem user = display.getUser();
				if (user != null) {
					Cookies.setCookie("userId", Long.toString(user.getUserId()));
					Cookies.setCookie("userName", user.getUserName());
					History.fireCurrentHistoryState();
				}
			}
		});

		// get the userId from the session
		long userId = -1;
		String userCookie = Cookies.getCookie("userId");
		if (userCookie != null) {
			userId = Long.valueOf(userCookie);
		}
		listUsers(userId);
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(display.asWidget());
	}

	protected void listUsers(final long userId) {
		reviewService.listUsers(new AsyncCallback<UserItem[]>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(UserItem[] result) {
				display.setUserList(result);
				for (UserItem user : result) {
					if (user.getUserId() == userId) {
						display.setUser(user);
					}
				}
			}
		});

	}
}
