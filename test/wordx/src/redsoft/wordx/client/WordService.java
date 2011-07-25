package redsoft.wordx.client;

import redsoft.wordx.shared.WordItem;

import com.google.gwt.user.client.rpc.RemoteService;

public interface WordService extends RemoteService {

	WordItem[] listWord(String headword);

	void addWords(WordItem[] words);
}
