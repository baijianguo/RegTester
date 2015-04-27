import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiXinNub {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] openid = { "oIWsFt7GFmAzMbK27p-C6kHvYpgM",
				"oIWsFt8VEQu1CwXD3CLJOjLDF62s", "oIWsFt5ZnVjqRWH8VD4ZdxS35fBo",
				"oIWsFt7DhDlauT4fvd_HR2ZmtMBU" };
		for (int i = 0; i < openid.length; i++) {
			String html = HttpRequest.sendGet("http://weixin.sogou.com/gzh",
					"openid=" + openid[i]);
			RegexNoIndex(html, openid[i], "2");
		}

	}

	public static void RegexNoIndex(String str, String openid, String type) {

		String reg = "<h3 id=\"weixinname\">(.*?)</h3>.*?<span>微信号：(.*?)</span>.*?<span class=\"sp-txt\">(.*?)</span>.*?<img style=\"width:16px;height:16px\" src=\"(.*?)\"";
		Pattern p = Pattern.compile(reg);
		String postUrl = "http://www.iumol.com/addweixinno.php";
		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			while (m.find()) {
				String name = m.group(1);
				String no = m.group(2);
				String desc = m.group(3);
				String headimg = m.group(4);

				String param = "name=" + name + "&no=" + no + "&desc=" + desc
						+ "&headimg=" + headimg + "&openid=" + openid
						+ "&type=" + type;
				System.out.println(param);
				String result = HttpRequest.sendPost(postUrl, param);
				System.out.println(result);
			}

		}
	}
}
