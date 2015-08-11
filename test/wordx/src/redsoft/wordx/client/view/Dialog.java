package redsoft.wordx.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Dialog {

	static public void failed(Throwable ex) {
		final DialogBox dialogbox = new DialogBox(false);
		VerticalPanel panel = new VerticalPanel();
		dialogbox.setText("FAILED");
		HTML message = new HTML("<pre>" + getStackTrace(ex) + "</pre>");
		Button button = new Button("Close");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogbox.hide();
			}
		});
		panel.add(message);
		panel.add(button);
		dialogbox.add(panel);
		dialogbox.center();
	}

	static String getStackTrace(Throwable ex) {
		return ex.toString();
	}

}
