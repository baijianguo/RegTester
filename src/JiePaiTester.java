import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiePaiTester {
	static String img_put_url = "http://www.iumol.com/imageput.php";
	static String img_jiepai_url = "http://www.jiepaihui.com/forum.php";
	static String jiepai_reg1 = "<li style=\".*?<a href=\"(.*?)\".*?title=\"(.*?)\".*?<img src=\"(.*?)\"";
	static String reg1 = "src=\"http://pic.www-siwaniu-com.iego.net/qq32593992/(.*?).jpg";

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				JiePaiTask();
			}
		}).start();

	}

	public static void JiePaiTask() {
		for (int i = 1; i < 9; i++) {
			String param = HttpRequest.sendGet(img_jiepai_url,
					"mod=forumdisplay&fid=67&sortid=5&page=" + i);
			System.out.println("正在抓取" + i + "页");
			JiePaiTester.RegexJiePaiThumAndDetailUrl(param, jiepai_reg1, reg1,
					"jiepai");
		}
	}

	public static void JiePaiNvHaiWuTask() {
		for (int i = 1; i < 10; i++) {
			String url = "http://www.nvhaiwu.com/tu/jp/69_%1$s.html";
			url = String.format(url, i);
			String param = HttpRequest.sendGet(url, "");
			System.out.println("正在抓取" + i + "页");
			JiePaiTester.RegexNvHaiWuThumAndDetailUrl(param, "jiepai");
		}
	}

	public static void RegexJiePaiThumAndDetailUrl(String str, String reg1,
			String reg2, String type) {

		Pattern p = Pattern.compile(reg1);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String url = m.group(1);
				String title = m.group(2);
				String thum = m.group(3);
				try {
					title = java.net.URLEncoder.encode(title, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

				if (!StringUtils.isBlank(imagelist))
					imagelist += ",";
				imagelist += "{\"image\":\"http://pic.www-siwaniu-com.iego.net/qq32593992/";
				imagelist += image;
				imagelist += ".jpg\"}";
			}

			if (!StringUtils.isBlank(imagelist)) {
				String param = "title=" + title + "&thum=" + thum
						+ "&imagelist=" + imagelist + "&type=" + type;
				String result = HttpRequest.sendPost(img_put_url, param);
				System.out.println("==title==" + title + "==" + result);
			} else {
				System.out.println("==title==" + title + "==配陪为空");
			}
		}

	}

	public static void RegexNvHaiWuThumAndDetailUrl(String str, String type) {
		String reg = "<div class=\"pic\">.*?href=\"(.*?).html\".*?src=\"(.*?)\".*?alt=\"(.*?)\"";
		Pattern p = Pattern.compile(reg);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String url = m.group(1);
				String thum = m.group(2);
				String title = m.group(3);
				try {
					title = java.net.URLEncoder.encode(title, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(count + "=title=" + title);
				RegexDetailNvHaiWuImageUrl(title, thum, url, type);
				count++;

			}

		}
	}

	public static void RegexDetailNvHaiWuImageUrl(String title, String thum,
			String src_url, String type) {
		String reg = "src=\"/uploads/allimg/(.*?)\"";
		String imagelist = "";
		for (int i = 1; i <= 10; i++) {
			String url = src_url;
			if (i == 1)
				url += ".html";
			else
				url = url + "_" + String.valueOf(i) + ".html";

			String html = HttpRequest.sendGet(url, "");
			Pattern p = Pattern.compile(reg);

			if (!StringUtils.isBlank(html)) {
				Matcher m = p.matcher(html);

				while (m.find()) {
					String image = m.group(1);

					if (!StringUtils.isBlank(imagelist))
						imagelist += ",";
					imagelist += "{\"image\":\"http://www.nvhaiwu.com//uploads/allimg/";
					imagelist += image;
					imagelist += "\"}";
				}
			}

		}
		if (!StringUtils.isBlank(imagelist)) {
			String param = "title=" + title + "&thum=" + thum + "&imagelist="
					+ imagelist + "&type=" + type;
			String result = HttpRequest.sendPost(img_put_url, param);
			System.out.println("==title==" + title + "==" + result);
		} else {
			System.out.println("==title==" + title + "==配陪为空");
		}
	}
}
