package redsoft.wordx.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DictEvent extends GwtEvent<DictEventHandler> {

	public static Type<DictEventHandler> TYPE = new Type<DictEventHandler>();

	protected String headword;

	public DictEvent(String headword) {
		this.headword = headword;
	}

	@Override
	public Type<DictEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DictEventHandler handler) {
		handler.onDict(this);
	}

	public String getHeadword() {
		return headword;
	}
}
