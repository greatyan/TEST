package redsoft.wordx.client;

import redsoft.wordx.client.event.AddReviewEvent;
import redsoft.wordx.client.event.AddReviewEventHandler;
import redsoft.wordx.client.event.AddUserEvent;
import redsoft.wordx.client.event.AddUserEventHandler;
import redsoft.wordx.client.event.DictEvent;
import redsoft.wordx.client.event.DictEventHandler;
import redsoft.wordx.client.presenter.DictPresenter;
import redsoft.wordx.client.presenter.LoginPresenter;
import redsoft.wordx.client.presenter.PreferencePresenter;
import redsoft.wordx.client.presenter.Presenter;
import redsoft.wordx.client.presenter.ReviewListPresenter;
import redsoft.wordx.client.presenter.ReviewPresenter;
import redsoft.wordx.client.view.DictView;
import redsoft.wordx.client.view.LoginView;
import redsoft.wordx.client.view.PreferenceView;
import redsoft.wordx.client.view.ReviewListView;
import redsoft.wordx.client.view.ReviewView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class AppController implements ValueChangeHandler<String> {

	long userId;
	ReviewServiceAsync reviewService;
	WordServiceAsync wordService;
	HandlerManager eventBus;
	HasWidgets container;
	Image menuIcon;

	AppController(HasWidgets root, ReviewServiceAsync reviewService, HandlerManager eventBus) {

		this.container = root;
		this.eventBus = eventBus;
		this.reviewService = reviewService;
		// this.container = container;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);
		eventBus.addHandler(DictEvent.TYPE, new DictEventHandler() {
			public void onDict(DictEvent event) {
			}
		});
		eventBus.addHandler(AddUserEvent.TYPE, new AddUserEventHandler() {
			public void onAddUser(AddUserEvent event) {

			}
		});

		eventBus.addHandler(AddReviewEvent.TYPE, new AddReviewEventHandler() {
			public void onAddReview(AddReviewEvent event) {
			}
		});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub

		if (Cookies.getCookie("userId") == null) {
			Presenter presenter = new LoginPresenter(reviewService, eventBus,
					new LoginView());
			presenter.go(container);
			return;
		}

		installMenu();

		String token = event.getValue();
		if (token != null) {
			Presenter presenter = null;
			int dotPos = token.indexOf('.');
			String command = token;
			String argument = null;
			if (dotPos != -1) {
				command = token.substring(0, dotPos);
				argument = token.substring(dotPos + 1);
			}

			if (command.equals("dict")) {
				presenter = new DictPresenter(userId, reviewService, eventBus,
						new DictView());
			}
			if (command.equals("preference")) {
				presenter = new PreferencePresenter(reviewService, eventBus,
						new PreferenceView());
			}
			if (command.equals("review-list")) {
				presenter = new ReviewListPresenter(userId, reviewService,
						eventBus, new ReviewListView(), argument);
			}
			if (command.equals("review")) {
				presenter = new ReviewPresenter(reviewService, userId,
						eventBus, new ReviewView(), argument);
			}
			if (presenter != null && container != null) {
				presenter.go(container);
			}
		}
	}

	public void go() {
		if ("".equals(History.getToken())) {
			History.newItem("dict");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	protected void installMenu() {
		if (menuIcon == null) {
			// Create a PopUpPanel with a button to close it
			menuIcon = new Image();
			menuIcon.setUrl("/resources/preferences-32.png");
			menuIcon.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					PopupPanel panel = new PopupPanel(true);
					MenuBar menuBar = new MenuBar(true);
					menuBar.addItem("<b>D</b>ictionary", true, new MenuCommand(
							panel, "view", "dict"));
					menuBar.addItem("<b>R</b>eview", true, new MenuCommand(
							panel, "view", "review-list"));
					menuBar.addItem("<b>P</b>reference", true, new MenuCommand(
							panel, "view", "preference"));
					menuBar.addItem("<b>B</b>ackup", true, new MenuCommand(
							panel, "backup", null));
					panel.add(menuBar);
					panel.showRelativeTo(menuIcon);
				}
			});
			RootPanel.get("menu-container").add(menuIcon);
		}
	}

	private static class MenuCommand implements Command {

		PopupPanel panel;
		String name;
		String view;

		MenuCommand(PopupPanel panel, String name, String viewname) {
			this.panel = panel;
			this.name = name;
			this.view = viewname;
		}

		@Override
		public void execute() {
			if ("view".equals(name)) {
				panel.hide();
				History.newItem(view);
			} else if ("backup".equals(name)) {
				Window.Location.assign("/wordx/backup");
			}
		}
	}
}