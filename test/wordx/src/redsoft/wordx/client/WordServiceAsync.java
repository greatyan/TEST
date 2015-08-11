package redsoft.wordx.client;

import redsoft.wordx.shared.WordItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WordServiceAsync {

	void addWords(WordItem[] words, AsyncCallback<Void> callback);

	void listWord(String headword, AsyncCallback<WordItem[]> callback);

}
