package redsoft.wordx.client.view;

import redsoft.wordx.client.presenter.LoginPresenter.Display;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements Display {

	private static LoginViewUiBinder uiBinder = GWT
			.create(LoginViewUiBinder.class);
	@UiField
	Button loginButton;
	@UiField
	ListBox userList;
	@UiField
	Button newButton;

	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}

	protected UserItem selectUser;
	protected UserItem[] allUsers;

	public LoginView() {

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getCreateUserButton() {
		return newButton;
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return loginButton;
	}

	@Override
	public void setUser(UserItem user) {
		selectUser = user;
		if (allUsers != null) {
			for (int i = 0; i < allUsers.length; i++) {
				if (allUsers[i].getUserId() == selectUser.getUserId()) {
					userList.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public UserItem getUser() {
		int index = userList.getSelectedIndex();
		if (index != -1 && allUsers != null) {
			return allUsers[index];
		}
		return null;
	}

	@Override
	public void setUserList(UserItem[] users) {
		this.allUsers = users;
		userList.clear();
		for (UserItem user : users) {
			userList.addItem(user.getUserName(), Long
					.toString(user.getUserId()));
		}
		if (selectUser != null) {
			for (int i = 0; i < allUsers.length; i++) {
				if (allUsers[i].getUserId() == selectUser.getUserId()) {
					userList.setSelectedIndex(i);
					break;
				}
			}
		}
	}
}
