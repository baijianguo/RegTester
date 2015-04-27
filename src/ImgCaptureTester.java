import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImgCaptureTester {
	static String img_put_url = "http://www.iumol.com/imageput.php";
	static String img_weibo_url = "http://vgirl.weibo.com/area.php";
	static String img_tuxiu_url = "http://vgirl.weibo.com/5show/aixiu.php";
	static String tuxiu_reg1 = "long_img\" src=\"(.*?)\".*?show_link_id.*?href=\".*?fid=(\\d+)\".*?>(.*?)</a>";
	static String weibo_reg1 = "<li>.*?<a href=\"/([^a].*?)\".*?src=\"(.*?)\".*?title=\"(.*?)\"";
	static String reg2 = "action-data=\"imgsrc=(.*?)\"";
	static String reg1 = "bigsrc=\"(.*?)\"";

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				WeiboTask();
			}
		}).start();

	}

	public static void TuxiuTask() {
		for (int i = 14; i > 0; i--) {
			String param = HttpRequest.sendGet(img_tuxiu_url, "page=" + i);
			System.out.println("正在抓取" + i + "页");
			ImgCaptureTester.RegexTuxiuThumAndDetailUrl(param, tuxiu_reg1,
					reg2, "tuxiu");// title_index,
									// url_index,thum_index
		}
	}

	public static void WeiboTask() {
		for (int i = 2671; i > 0; i--) {
			String param = HttpRequest.sendGet(img_weibo_url, "page=" + i);
			System.out.println("正在抓取" + i + "页");
			ImgCaptureTester.RegexThumAndDetailUrl(param, weibo_reg1, reg2,
					"weibo");// title_index,
								// url_index,thum_index
		}
	}

	public static void RegexTuxiuThumAndDetailUrl(String str, String reg1,
			String reg2, String type) {

		Pattern p = Pattern.compile(reg1);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {

				String thum = m.group(1);
				String url = m.group(2);
				String title = m.group(3);
				String html = HttpRequest.sendGet(
						"http://vgirl.weibo.com/5show/user.php", "fid=" + url);
				System.out.println(count + "=title=" + title);
				RegexDetailImageUrl(title, thum, html, reg2, type);
				count++;

			}

		}
	}

	public static void RegexThumAndDetailUrl(String str, String reg1,
			String reg2, String type) {

		Pattern p = Pattern.compile(reg1);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String url = "http://vgirl.weibo.com/";
				url += m.group(1);
				String thum = m.group(2);
				String title = m.group(3);

				String html = HttpRequest.sendGet(url, "");
				System.out.println(count + "=title=" + title);
				RegexDetailImageUrl(title, thum, html, reg2, type);
				count++;

			}

		}
	}

	public static void RegexDetailImageUrl(String title, String thum,
			String html, String reg, String type) {

		Pattern p = Pattern.compile(reg);
		String imagelist = "";
		if (!StringUtils.isBlank(html)) {
			Matcher m = p.matcher(html);

			while (m.find()) {
				String image = m.group(1);
				if (!image.contains("default_bmiddle")) {
					if (!StringUtils.isBlank(imagelist))
						imagelist += ",";
					imagelist += "{\"image\":\"";
					imagelist += image;
					imagelist += "\"}";
				}
			}
			if (!StringUtils.isBlank(imagelist)) {
				String param = "title=" + title + "&thum=" + thum
						+ "&imagelist=" + imagelist + "&type=" + type;
				String result = HttpRequest.sendPost(img_put_url, param);
				System.out.println("==title==" + title + "==" + result);
			} else {
				System.out.println("==title==" + title + "==信息为空！");
			}
		}

	}
}
