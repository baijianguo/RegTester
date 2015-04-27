import javax.swing.UIManager;

/**
 * 测试 Java 正则表达式
 */
public class RegTester {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new RegTesterFrame().setVisible(true);
	}
}
