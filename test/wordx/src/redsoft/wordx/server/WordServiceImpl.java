package redsoft.wordx.server;

import java.util.Date;

import redsoft.wordx.client.WordService;
import redsoft.wordx.shared.WordItem;
import redsoft.wordx.word.repository.Repository;
import redsoft.wordx.word.repository.Word;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class WordServiceImpl extends RemoteServiceServlet implements
		WordService {

	@Override
	public void addWords(WordItem[] items) {

		Repository repository = Repository.getRepository();
		for (WordItem item : items) {
			Word[] words = repository.getWord(item.getHeadword());
			if (words != null && words.length > 0) {
				// update the word[0]
				Word word = words[0];
				String phonetic = item.getPhonetic();
				if (phonetic != null) {
					word.setPhonetic(phonetic);
				}
				String simpTrans = item.getSimpleTrans();
				if (simpTrans != null) {
					word.setSimpleTrans(simpTrans);
				}
				String detailTrans = item.getDetailTrans();
				if (detailTrans != null) {
					word.setDetailTrans(detailTrans);
				}
				repository.saveWord(word);
			} else {
				Word word = new Word();
				word.setCreateDate(new Date());
				word.setHeadword(item.getHeadword());
				word.setPhonetic(item.getPhonetic());
				word.setDetailTrans(item.getDetailTrans());
				word.setSimpleTrans(item.getSimpleTrans());
				repository.saveWord(word);
			}
		}

	}

	@Override
	public WordItem[] listWord(String headword) {
		Repository repository = Repository.getRepository();
		Word[] words = repository.getWord(headword);
		WordItem[] items = new WordItem[words.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = wrap(words[i]);
		}
		return items;
	}

	public Word getWord(long wordId) {
		Repository repository = Repository.getRepository();
		return repository.getWord(wordId);
	}

	protected WordItem wrap(Word word) {
		WordItem item = new WordItem();
		item.setHeadword(word.getHeadword());
		item.setPhonetic(word.getPhonetic());
		item.setSimpleTrans(word.getSimpleTrans());
		item.setDetailTrans(word.getDetailTrans());
		return item;
	}
}
