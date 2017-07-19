
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FtpClient {

	private JFrame frame;
	private JTextField textField;
	static Socket socket;
	String str;
	static InputStream in ;
	static OutputStream out;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FtpClient window = new FtpClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		socket = new Socket("127.0.0.1", 4000);
		
		
		
	}

	/**
	 * Create the application.
	 */
	public FtpClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(UIManager.getColor("Button.highlight"));
		frame.setBackground(UIManager.getColor("Button.highlight"));
		frame.setBounds(100, 100, 629, 307);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Select file : ");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		lblNewLabel.setBounds(12, 12, 120, 33);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(btnBrowse);
				  str = fileChooser.getSelectedFile().getAbsolutePath();
				  textField.setText(str);
			}
		});
		btnBrowse.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		btnBrowse.setForeground(Color.ORANGE);
		btnBrowse.setBackground(Color.DARK_GRAY);
		btnBrowse.setBounds(510, 12, 105, 33);
		frame.getContentPane().add(btnBrowse);
		
		textField = new JTextField();
		textField.setFont(new Font("Caladea", Font.PLAIN, 20));
		textField.setBounds(127, 12, 376, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				File file = new File(str);
				
				byte [] b = new byte[16*1024];
				
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				try {
					out = socket.getOutputStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					int count ;
					while ((count  = in.read(b))>0) {
						out.write(b, 0, count);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSend.setFont(new Font("Dialog", Font.BOLD, 20));
		btnSend.setForeground(Color.DARK_GRAY);
		btnSend.setBackground(Color.ORANGE);
		btnSend.setBounds(475, 211, 117, 48);
		frame.getContentPane().add(btnSend);
	}
}