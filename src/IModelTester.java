import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IModelTester {
	static String img_put_url = "http://www.iumol.com/imageput.php";
	static String img_imodel_url = "http://www.imodel.cc/model/23/";
	static String reg1 = "<img alt=.*?src=\"(.*?)\".*?title=\"(.*?)\" href=\"http://www.imodel.cc/u/(\\d+)\"";
	static String reg2 = "m_v_list_cover\".*?href=\"(.*?)\"";
	static String reg3 = "<img class=\"lazy\" src=\"(.*?)\"";

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				IModelTask();
			}
		}).start();

	}

	public static void IModelTask() {
		for (int i = 10; i < 11; i++) {
			String param = HttpRequest.sendGet(img_imodel_url + i, "");
			System.out.println("正在抓取" + i + "页");
			IModelTester.RegexThumAndDetailUrl(param, reg1, reg2, "imodel");
		}
	}

	public static void RegexThumAndDetailUrl(String str, String reg1,
			String reg2, String type) {

		Pattern p = Pattern.compile(reg1);

		if (!StringUtils.isBlank(str)) {
			Matcher m = p.matcher(str);
			int count = 0;
			while (m.find()) {
				String thum = m.group(1);
				String title = m.group(2);

				String param = "do=thread&uid=";
				param += m.group(3);

				String html = HttpRequest.sendGet("http://www.imodel.cc/t/",
						param);
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
				String url = m.group(1);
				String htmldetial = HttpRequest.sendGet(url, "");

				Pattern p2 = Pattern.compile(reg3);
				Matcher m2 = p2.matcher(htmldetial);
				while (m2.find()) {
					String img = m2.group(1);
					if (!StringUtils.isBlank(imagelist))
						imagelist += ",";
					imagelist += "{\"image\":\"";
					imagelist += img;
					imagelist += "\"}";
				}

			}

			if (!StringUtils.isBlank(imagelist)) {
				String param = "title=" + title + "&thum=" + thum
						+ "&imagelist=" + imagelist + "&type=" + type;
				String result = HttpRequest.sendPost(img_put_url, param);
				System.out.println("==title==" + title + "==" + result);
			} else {
				System.out.println("==title==" + title + "==匹配为空");
			}
		}

	}

}
