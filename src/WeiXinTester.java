import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiXinTester {

	static String img1 = "TB11USwGXXXXXbSXFXXBaku.pXX-238-239.png";
	static String img2 = "TB1Ui9xGXXXXXaJXFXXBaku.pXX-238-239.png";
	static String weixin_put_url = "http://www.iumol.com/addweixinnews.php";
	static String weixin_no_url = "http://www.iumol.com/getjson.php";
	static String taonl = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=oIWsFtzz8vKujVM3opV5b4291sZQ&page=1";

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				for (int i = 1; i < 3; i++) {
					String param = HttpRequest.sendGet(weixin_no_url, "page="
							+ String.valueOf(i));
					WeiXinTester.RegexNoIndex(param);
				}
			}
		}).start();

	}

	public static void RegexNoIndex(String str) {

		String reg = "name\":\"(.*?)\".*?openid\":\"(.*?)\".*?type\":\"(.*?)\"";
		Pattern p = Pattern.compile(reg);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String name = m.group(1);
				String openid = m.group(2);
				String type = m.group(3);
				String htmlString = HttpRequest.sendGet(
						"http://weixin.sogou.com/gzhjs",
						"cb=sogou.weixin.gzhcb&page=1&openid=" + openid);
				System.out.println(count + "=name=" + name);

				RegexWeiXinNews(openid, type, htmlString);
				count++;

			}

		}
	}

	@SuppressWarnings("deprecation")
	public static void RegexWeiXinNews(String openid, String type, String str) {

		String reg1 = "<display> <docid>(.*?)<\\\\/docid>.*?<title><!\\[CDATA\\[(.*?)\\]\\]><\\\\/title>.*?<url><!\\[CDATA\\[(.*?)\\]\\]><\\\\/url>.*?<imglink><!\\[CDATA\\[(.*?)\\]\\]><\\\\/imglink>.*?<content><!\\[CDATA\\[(.*?)\\]\\]><\\\\/content>.*?<date><!\\[CDATA\\[(.*?)\\]\\]><\\\\/date>";
		Pattern p = Pattern.compile(reg1);
		Matcher m = p.matcher(str);
		while (m.find()) {
			String docid = m.group(1);
			String title = m.group(2);
			String url = m.group(3);
			url = java.net.URLEncoder.encode(url);
			String img = m.group(4);
			String content = m.group(5);
			String date = m.group(6);

			String param = "docid=" + docid + "&url=" + url + "&title=" + title
					+ "&desc=" + content + "&image=" + img + "&openid="
					+ openid + "&date=" + date + "&type=" + type;

			String result = HttpRequest.sendPost(weixin_put_url, param);
			System.out.println("·µ»Ø½á¹û===" + result);

		}

	}
}
