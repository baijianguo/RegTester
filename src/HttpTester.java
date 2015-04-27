
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTester {

	static String img1 = "TB11USwGXXXXXbSXFXXBaku.pXX-238-239.png";
	static String img2 = "TB1Ui9xGXXXXXaJXFXXBaku.pXX-238-239.png";
	static String img_put_url = "http://www.iumol.com/imageput.php";
	static String taobao_url = "http://mm.taobao.com/tstar/search/tstar_model.do";
	static String taonl = "http://www.taobao.com/market/mm/tnlmmt.php";

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				for (int i = 353; i < 392; i++) {
					String param = HttpRequest.sendGet(taobao_url,
							"_input_charset=utf-8&pageSize=100&currentPage="
									+ String.valueOf(i));
					System.out.println("正在抓取第" + i + "页");
					HttpTester.RegexTaobaoIndex(param);
				}
			}
		}).start();

	}

	public static void RegexTaobaoIndex(String str) {

		// String reg =
		// "pro-item posr.*?\n.*?href=\"(.*?)\">\n.*?data-ks-lazyload=\"(.*?)\">\n.*?\n.*?\n.*?intro-content\">(.*?)</p>";
		// String reg =
		// "pro-item posr.*?href=\"(.*?)\".*?data-ks-lazyload=\"(.*?)\".*?intro-content\">(.*?)</p>";
		String reg = "avatarUrl\":\"(.*?)\".*?realName\":\"(.*?)\".*?userId\":(.*?),";
		Pattern p = Pattern.compile(reg);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String image_url = m.group(1);
				String title = m.group(2);
				String url = "userId=";
				url += m.group(3);
				// if (!image_url.contains(img1) && !image_url.contains(img2)) {
				String htmlString = HttpRequest.sendGet(
						"http://mm.taobao.com/self/aiShow.htm", url);
				System.out.println(count + "=id=" + m.group(3));
				RegexTaobaoImageUrl(title, image_url, htmlString);
				count++;
				// }
			}

		}
	}

	public static void RegexTaobaoImageUrl(String title, String image_url,
			String str) {

		String reg1 = "textarea(.*?)</textarea>";
		Pattern p = Pattern.compile(reg1);
		Matcher m = p.matcher(str);
		if (m.find())
			str = m.group(1);

		String reg = "<img src=\"(.*?)\"";
		p = Pattern.compile(reg);

		if (!StringUtils.isBlank(str)) {
			m = p.matcher(str);
			String imagelist = "";
			while (m.find()) {
				String image = m.group(1);
				if (!StringUtils.isBlank(imagelist))
					imagelist += ",";
				imagelist += "{\"image\":\"";
				imagelist += image;
				imagelist += "\"}";
			}

			try {
				title = java.net.URLEncoder.encode(title, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String param = "title=" + title + "&thum=" + image_url
					+ "&imagelist=" + imagelist + "&type=taobao";
			String result = HttpRequest.sendPost(img_put_url, param);
			System.out.println("==title==" + title + "==" + result);
		}

	}
}
