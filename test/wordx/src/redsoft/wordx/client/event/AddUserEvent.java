package redsoft.wordx.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddUserEvent extends GwtEvent<AddUserEventHandler> {

	public static Type<AddUserEventHandler> TYPE = new Type<AddUserEventHandler>();

	protected String userName;

	public AddUserEvent() {
	}

	public AddUserEvent(String userName) {
		this.userName = userName;
	}

	@Override
	public Type<AddUserEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddUserEventHandler handler) {
		handler.onAddUser(this);
	}

	public String getUserName() {
		return userName;
	}
}
