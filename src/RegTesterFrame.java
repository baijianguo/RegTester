import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class RegTesterFrame extends JFrame {

	private JTextField textbox = new JTextField(); // ���������ı����ı���

	private JTextArea textarea = new JTextArea(); // ����������ʽ���ı���

	private RegTesterFrame thisFrame = this;

	private DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
			Color.green);

	// ���췽��
	RegTesterFrame() throws HeadlessException {
		super("���� Java ������ʽ");
		setSize(500, 300);
		setLocation(300, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setupComponents();
	}

	// ��ʼ���ؼ�
	private void setupComponents() {
		setupBorder();

		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		this.add(topPanel, BorderLayout.NORTH);
		setupTopPanel(topPanel);

		JPanel centerPanel = new JPanel(new BorderLayout());
		this.add(centerPanel, BorderLayout.CENTER);
		setupCenterPanel(centerPanel);
	}

	// �����������ϱ߿�
	private void setupBorder() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setContentPane(panel);
	}

	// �ı������
	private void setupCenterPanel(JPanel panel) {
		textarea.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel.add(new JScrollPane(textarea));
	}

	// �Ϸ����
	private void setupTopPanel(JPanel panel) {
		textbox.setFont(new Font(Font.DIALOG_INPUT, 0, 12));
		panel.add(textbox, BorderLayout.CENTER);

		JButton button = new JButton("����");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkRegularExpression();
			}
		});
		panel.add(button, BorderLayout.EAST);
		panel.add(new JLabel("������ʽ��"), BorderLayout.WEST);
	}

	// ���������ʽ
	private void checkRegularExpression() {
		textarea.getHighlighter().removeAllHighlights();

		String reg = textbox.getText();
		String text = textarea.getText();

		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(text);
		if (m.find()) {
			thisFrame.setTitle("�ı����ϱ��ʽ��");
			highlightMatches(m);
		} else {
			thisFrame.setTitle("�ı������ϱ��ʽ��");
		}
	}

	// ������������ʽ�Ĳ��ָ�����ʾ
	private void highlightMatches(Matcher m) {
		highlight(m);
		int start = m.end();
		while (m.find(start)) {
			highlight(m);
			start = m.end();
		}
	}

	private void highlight(Matcher m) {
		try {
			textarea.getHighlighter().addHighlight(m.start(), m.end(),
					highlightPainter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}