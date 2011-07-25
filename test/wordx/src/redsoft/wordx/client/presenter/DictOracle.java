package redsoft.wordx.client.presenter;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class DictOracle extends SuggestOracle {

	public DictOracle() {

	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery();
		int limit = request.getLimit();
		listWord(query, limit, new JsonpCallback(request, callback));
	}

	static final String SUGGEST_PATTERN = "http://suggestqueries.google.com/complete/search?client=dict&hjson=t&ds=d&hl=zh-CN&cp=3&q=";

	void listWord(String headword, int limit, JsonpCallback callback) {
		String url = SUGGEST_PATTERN + headword;

		// RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		// builder.setCallback(callback);
		//
		// try {
		// builder.send();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }

		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		// builder.setCallbackParam("window.google.ac.hr");
		builder.requestObject(url, callback);
	}

	static private class SuggestCallback implements RequestCallback {

		Request oracleRequest;
		Callback callback;

		SuggestCallback(Request request, Callback callback) {
			this.oracleRequest = request;
			this.callback = callback;
		}

		@Override
		public void onError(com.google.gwt.http.client.Request request,
				Throwable exception) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onResponseReceived(
				com.google.gwt.http.client.Request request,
				com.google.gwt.http.client.Response response) {
			String jsonText = response.getText();
			JSONValue jsonValue = JSONParser.parseStrict(jsonText);

			ArrayList<WordSuggestion> suggestions = new ArrayList<WordSuggestion>();
			for (int i = 0; i < 10; i++) {
				suggestions.add(new WordSuggestion("ABC", "ABC DEFG"));
			}
			Response oracleResponse = new Response(suggestions);
			callback.onSuggestionsReady(oracleRequest, oracleResponse);

		}

	}

	static private class WordSuggestion implements Suggestion {

		String headword;
		String explain;

		WordSuggestion(String headword, String explain) {
			this.headword = headword;
			this.explain = explain;
		}

		@Override
		public String getDisplayString() {
			return explain;
		}

		@Override
		public String getReplacementString() {
			return headword;
		}

	}

	static private class JsonpCallback implements
			AsyncCallback<JavaScriptObject> {

		Request oracleRequest;
		Callback callback;

		JsonpCallback(Request request, Callback callback) {
			this.oracleRequest = request;
			this.callback = callback;
		}

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(JavaScriptObject result) {
			ArrayList<WordSuggestion> suggestions = new ArrayList<WordSuggestion>();
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = (JSONArray) jsonObject.get("1");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONArray jsonItem = (JSONArray) jsonArray.get(i);
				String word = ((JSONString)jsonItem.get(0)).stringValue();
				String explain = ((JSONString)jsonItem.get(1)).stringValue();
				suggestions.add(new WordSuggestion(word, word + ":\t" + explain));
			}
			Response oracleResponse = new Response(suggestions);
			callback.onSuggestionsReady(oracleRequest, oracleResponse);
		}
	}
}
