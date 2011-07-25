package redsoft.wordx.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserItem implements IsSerializable {

	private long userId;
	private String userName;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
