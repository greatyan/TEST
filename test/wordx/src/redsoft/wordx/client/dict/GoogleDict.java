package redsoft.wordx.client.dict;

import java.util.Stack;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GoogleDict {

	public static final String EN_EN = "en";
	public static final String EN_ZH = "zh";
	static final String URL_EN = "http://www.google.com/dictionary/json?callback=dict_api.callbacks.id100&sl=en&tl=en&restrict=pr%2Cde%2Csy&client=dict-chrome-ex&q=";
	static final String URL_ZH = "http://www.google.com/dictionary/json?callback=dict_api.callbacks.id100&sl=en&tl=zh&restrict=pr%2Cde%2Csy&client=dict-chrome-ex&q=";

	public static class Result {

		JSONObject jsonObject;
		String headword;
		String phonetic;
		String sound;
		String htmlText;

		Result(JSONObject jsonObject) {
			this.jsonObject = jsonObject;
			Json2Html json2html = new Json2Html(jsonObject);
			this.headword = json2html.getHeadword();
			this.htmlText = json2html.getHtmlText();
			this.phonetic = json2html.getPhonetic();
			this.sound = json2html.getSound();
		}

		public JSONObject getJSONObject() {
			return jsonObject;
		}

		public String getHtmlText() {
			return htmlText;
		}

		public String getHeadword() {
			return headword;
		}

		public String getPhonetic() {
			return phonetic;
		}

		public String getSound() {
			return sound;
		}
	}

	public static interface OnSearchHandler {
		void onSearch(Result result);
	}

	public static void search(String headword, OnSearchHandler handle) {

		search(headword, EN_ZH, handle);
	}

	public static void search(String headword, String lang,
			OnSearchHandler handle) {

		String url = EN_ZH.equals(lang) ? URL_ZH : URL_EN;
		url += headword;

		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		// builder.setCallbackParam("window.google.ac.hr");
		builder.requestObject(url, new JsonpCallback(handle));
	}

	static private class JsonpCallback implements
			AsyncCallback<JavaScriptObject> {

		OnSearchHandler callback;

		JsonpCallback(OnSearchHandler callback) {
			this.callback = callback;
		}

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(JavaScriptObject result) {
			JSONObject jsonObject = new JSONObject(result);
			Result searchResult = new Result(jsonObject);
			callback.onSearch(searchResult);
		}
	}

	static class Json2Html {

		
		Json2Html(JSONObject jsonObject) {
			StringBuilder sb = new StringBuilder();
			JSONArray primaries = getArray(jsonObject, "primaries");
			if (primaries != null) {
				outputEntries(primaries, sb);
			}
			htmlText = sb.toString();
		}

		String headword;
		String htmlText;
		String phonetic;
		String sound;

		public String getHeadword() {
			return headword;
		}

		public String getHtmlText() {
			return htmlText;
		}

		public String getPhonetic() {
			return phonetic;
		}

		public String getSound() {
			return sound;
		}

		Stack<String> parents = new Stack<String>();

		protected String getParent() {
			if (!parents.isEmpty()) {
				return parents.peek();
			}
			return null;
		}

		void outputJson(JSONValue value, StringBuilder sb, String indent) {
			JSONBoolean jb = value.isBoolean();
			if (jb != null) {
				sb.append(jb.booleanValue());
				return;
			}
			JSONNumber jn = value.isNumber();
			if (jn != null) {
				sb.append(jn.doubleValue());
				return;
			}
			JSONString js = value.isString();
			if (js != null) {
				sb.append(js.stringValue());
				return;
			}
			JSONArray ja = value.isArray();
			if (ja != null) {
				outputJson(ja, sb, indent);
			}
			JSONObject jo = value.isObject();
			if (jo != null) {
				outputJson(jo, sb, indent);
			}
		}

		JSONObject getObject(JSONValue value) {
			if (value != null) {
				return value.isObject();
			}
			return null;
		}

		JSONArray getArray(JSONValue value) {
			if (value != null) {
				return value.isArray();
			}
			return null;
		}

		String getString(JSONValue value) {
			if (value != null) {
				JSONString js = value.isString();
				if (js != null) {
					return js.stringValue();
				}
			}
			return null;

		}

		JSONObject getObject(JSONObject object, String key) {
			return getObject(object.get(key));
		}

		JSONArray getArray(JSONObject object, String key) {
			return getArray(object.get(key));
		}

		String getString(JSONObject object, String key) {
			return getString(object.get(key));
		}

		JSONArray getArray(JSONArray array, int index) {
			return getArray(array.get(index));
		}

		JSONObject getObject(JSONArray array, int index) {
			return getObject(array.get(index));

		}

		String getString(JSONArray array, int index) {
			return getString(array.get(index));
		}

		void outputTerm(JSONObject term, StringBuilder sb) {
			String type = getString(term, "type");
			if (type != null) {
				sb.append("<span class=\"dict-term-");
				sb.append(type);
				sb.append("\">");
			} else {
				sb.append("<span class=\"dict-term\">");
			}
			if ("text".equals(type)) {
				String text = getString(term, "text");
				sb.append(text);
			} else if ("phonetic".equals(type)) {
				String text = getString(term, "text");
				sb.append("<image src=\"./wordx/pronounce?pronounce=");
				sb.append(text);
				sb.append("\"/>");
				if ("headword".equals(getParent())) {
					if (phonetic == null) {
						phonetic = text;
					} else {
						phonetic += "," + text;
					}
				}
			} else if ("sound".equals(type)) {
				String text = getString(term, "text");
				if (sound == null) {
					sound = text;
				} else {
					sound += "," + text;
				}

				sb.append("<a href=\"");
				sb.append(text);
				sb.append("\">");
				sb.append("<img src=\"/resources/SpeakerOffA16.png\"/>");
				sb.append("</a>");
			} else {
				sb.append("UNKNOWN  type:" + type);
			}
			sb.append("</span>");
			JSONArray labels = getArray(term, "labels");
			if (labels != null) {
				for (int i = 0; i < labels.size(); i++) {
					sb.append(" ");
					outputLabel(getObject(labels, i), sb);
				}
			}
			JSONArray entries = getArray(term, "entries");
			if (entries != null) {
				outputEntries(entries, sb);
			}
		}

		void outputLabel(JSONObject label, StringBuilder sb) {
			String type = getString(label, "type");
			if (type != null) {
				sb.append("<span class=\"dict-label-");
				sb.append(type);
				sb.append("\">");
			} else {
				sb.append("<span class=\"dict-label\">");
			}
			String text = getString(label, "text");
			sb.append(text);
			sb.append("</span>");
		}

		void outputEntries(JSONArray entries, StringBuilder sb) {
			String prevEntryType = null;
			for (int i = 0; i < entries.size(); i++) {
				JSONObject entry = getObject(entries, i);
				String entryType = getString(entry, "type");
				if (!entryType.equals(prevEntryType)) {
					if (prevEntryType != null) {
						sb.append("</ul>");
					}
					sb.append("<ul class=\"dict-entry-");
					sb.append(entryType);
					sb.append("\">");
				}
				prevEntryType = entryType;
				outputEntry(entry, sb);
			}
			sb.append("</ul>");
		}

		void outputEntry(JSONObject entry, StringBuilder sb) {
			String type = getString(entry, "type");
			parents.push(type);
			if (type != null) {
				sb.append("<li class=\"dict-entry-");
				sb.append(type);
				sb.append("\">");
			} else {
				sb.append("<li class=\"dict-entry\">");
			}
			JSONArray terms = getArray(entry, "terms");
			if (terms != null) {
				outputTerms(terms, sb);
			}
			JSONArray entries = getArray(entry, "entries");
			if (entries != null) {
				outputEntries(entries, sb);
			}
			sb.append("</li>");
			parents.pop();
		}

		void outputTerms(JSONArray terms, StringBuilder sb) {
			for (int i = 0; i < terms.size(); i++) {
				JSONObject term = getObject(terms, i);
				outputTerm(term, sb);
				sb.append(" ");
			}
		}
	}
}
