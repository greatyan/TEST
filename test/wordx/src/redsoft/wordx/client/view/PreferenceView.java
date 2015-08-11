package redsoft.wordx.client.view;

import redsoft.wordx.client.presenter.PreferencePresenter.Display;
import redsoft.wordx.shared.UserItem;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PreferenceView extends Composite implements Display {

	TextBox userNameInput;
	Button setUserButton;

	public PreferenceView() {
		TabLayoutPanel panel = new TabLayoutPanel(1.5, Unit.EM);
		initWidget(panel);

		// User ...
		VerticalPanel userPanel = new VerticalPanel();

		userPanel.add(new Label("user name"));
		userNameInput = new TextBox();
		userPanel.add(userNameInput);
		setUserButton = new Button("set");
		userPanel.add(setUserButton);
		userPanel.setHeight("6em");

		panel.add(userPanel.asWidget(), "user");
		panel.add(new HTML("TEST2"), "user2");
		panel.add(new HTML("TEST2"), "user3");
		panel.add(new HTML("TEST2"), "user4");
		panel.add(new HTML("TEST2"), "user4");
		panel.add(new HTML("TEST2"), "user4");
	}

	public void setUserName(String userName) {
		userNameInput.setName(userName);
		userNameInput.selectAll();
	}

	public String getUserName() {
		return userNameInput.getText();
	}

	public HasClickHandlers getSetUserButton() {
		return setUserButton;
	}

	@Override
	public void setUsers(UserItem[] users) {
	}
}
