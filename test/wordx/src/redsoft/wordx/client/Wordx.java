package redsoft.wordx.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Wordx implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// private final WordServiceAsync wordService =
	// GWT.create(WordService.class);

	private final ReviewServiceAsync reviewService = GWT
			.create(ReviewService.class);
	private final WordServiceAsync wordService = GWT.create(WordService.class);

	private final WordXImageBundle resources = GWT
			.create(WordXImageBundle.class);

	private HandlerManager eventBus;
	private AppController appViewer;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// ContactsServiceAsync rpcService = GWT.create(ContactsService.class);
		eventBus = new HandlerManager(null);
		Document document = Document.get();
		String innerHtml = document.getBody().getInnerHTML();
		HasWidgets root = RootPanel.get("root-container");
		appViewer = new AppController(root, reviewService, eventBus);
		appViewer.go();
	}
}
