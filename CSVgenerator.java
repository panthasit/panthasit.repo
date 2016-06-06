import java.awt.Dimension;
import java.text.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.*;

import org.omg.CORBA.portable.OutputStream;

public class CSVgenerator extends JFrame {
	List<DataSheet> dataList = new ArrayList<DataSheet>();
	private JTextArea textArea;
	private JTextArea textArea1;
	private JButton buttonClear;
	private JPanel panel = new JPanel();
	private JTextField fieldMessage;
	// private JButton buttonSubmit;

	private JButton generateButton;

	public CSVgenerator() {
		createView();
		setTitle("CSV-GENERATOR");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);
		setResizable(false);

	}

	public void createView() {

		getContentPane().add(panel);

		JLabel label = new JLabel("Enter Tick Size (0.000001 to 1.0)");
		panel.add(label);

		fieldMessage = new JTextField(12);
		panel.add(fieldMessage);

		/*
		 * buttonSubmit = new JButton("Submit"); panel.add(buttonSubmit);
		 * buttonSubmit.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { String message
		 * = fieldMessage.getText();
		 * 
		 * 
		 * 
		 * } });
		 */

		textArea = new JTextArea();
		textArea.setEditable(true);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		// textArea.setPreferredSize(new Dimension(350,90));

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(350, 90));
		panel.add(scrollPane);

		JLabel label2 = new JLabel("Enter Quote (eg. THB/USD)");
		panel.add(label2);

		buttonClear = new JButton("Clear");
		buttonClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				fieldMessage.setText("");
				textArea1.setText("");

			}
		});
		panel.add(buttonClear);

		generateButton = new JButton("Generate");
		generateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String COMMA_DELIMETER = ",";
				String NEW_LINE_SEPERATOR = "\n";
				String FILE_HEADER = "id,Quote,Feed Name,Exchange Symbol,Minimum Trade Size,Quoted Symbol,Tick Size,isDynamic,Active Date,Actual Decimal Places,Available Fixing Sessions,Base Symbol,Buy Spread Sides,CFIC Code,CME Channel,CMEsubChannel,Continuous Match Min Size,Contract Size,Country of Register,CUSIP,Days To Spot,Depth Of Book,Description,EBS Regular Size,Exchange Code,Expire Date Time,Fixing Date,Future Settle Date,FX Relation,ICE Deal Price Denominator,ICE Order Price Denominator,Instrument Group,Instrument Mnemonic,ISIN Code,Level 2 RIC,Liffe Generic Contract,Liffe Physical Commodity Code,Lot Size,LSE Millenium ID,LSE Order Types,Maturity Day,Maturity MY,Minimum Quote Lifecycle,Price Format,Price Scale AGS,Price Scale APS,Put Call Indicator,Quote Type,Quoted Currency,Region,RIC,Security ID,Security Type,SEDOL,Service Name,Spread,Strike Price,Supports L2,Swap Tick Size,Tick Ladder,Tradable From Date,Tradable To Date,Trade Date,Trades In Multiples Of,Trading Status,Underlying Spread Quotes,Unit of Quotation,Venue Code,WKN";
				String[] FEED_NAME = { "FXALLBANKSTREAM", "FIX.FXALL",
						"FIX.HotSpotFIX", "EBS_Ai", "EBSLive", "MOEXMcast",
						"ThomsonReutersMatching" };

				try {

					String result = textArea.getText();
					System.out.println(result);
					String[] tokens = result.split("\n");
					UUID uid = UUID
							.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

					for (String quote : tokens) {

						if (!format(quote)) {
							System.out.println(quote + " Wrong format");
							JOptionPane
									.showMessageDialog(
											null,
											" Please insert "
													+ quote
													+ "  again With Quote Format : XXX/YYY",
											"Wrong Quote Format",
											JOptionPane.ERROR_MESSAGE);

						}

					}

					if (checkTick(fieldMessage.getText())) {
						for (String quote : tokens) {
							if (format(quote)) {
								System.out.println(uid.randomUUID() + quote);
								dataList.add(new DataSheet(uid.randomUUID()
										.toString(), quote));
								textArea1.append(quote
										+ "       has been created" + "\n");

							}
						}
					}
					FileWriter fileWriter = new FileWriter(
							"C:\\Users\\U6039459\\Desktop\\TRDA_Add_CCY\\import-inputdefinitionsme.csv");
					fileWriter.append(FILE_HEADER);
					for (DataSheet d : dataList) {
						for (int i = 0; i < FEED_NAME.length; i++) {

							fileWriter.append(NEW_LINE_SEPERATOR);
							fileWriter.append(uid.randomUUID().toString());
							fileWriter.append(COMMA_DELIMETER);

							fileWriter.append(d.getCurrency());
							fileWriter.append(COMMA_DELIMETER);

							fileWriter.append(FEED_NAME[i]);
							fileWriter.append(COMMA_DELIMETER);

							fileWriter.append(d.getCurrency());
							fileWriter.append(COMMA_DELIMETER);

							fileWriter.append("0");
							fileWriter.append(COMMA_DELIMETER);

							// need to check feed name ThomsonReutersMatching
							// format XXX/YYY->XXXYYY=
							if (i == 6) {
								String newFormat = d.getCurrency();
								String x = newFormat.substring(0, 3);
								String y = newFormat.substring(4, 7);
								String currencys = x + y + "=";
								fileWriter.append(currencys);
								fileWriter.append(COMMA_DELIMETER);
							} else {
								fileWriter.append(d.getCurrency());
								fileWriter.append(COMMA_DELIMETER);
							}

							fileWriter.append(fieldMessage.getText());
							fileWriter.append(COMMA_DELIMETER);

							fileWriter.append("FALSE");

							fileWriter.flush();

						}

					}

					fileWriter.close();

				} catch (Exception g) {
					System.out.println(g.getMessage());
				}
				try {

					Runtime.getRuntime()
							.exec("cmd /c cd C:\\Users\\U6039459\\Desktop\\TRDA_Add_CCY\\trda-ret-5.4.3-HF4-89-33-java7u80\\TRDA\\Lib && java -cp \"*\" -ms64m -mx800m com.athena.common.symbology.InstrumentDefUtilities loader=C:\\Users\\U6039459\\Desktop\\TRDA_Add_CCY\\loader.xml folder=\"C:\\Users\\U6039459\\Desktop\\TRDA_Add_CCY\" import=import-inputdefinitionsme.csv");

				} catch (Exception ex) {
				}
			}

		});
		panel.add(generateButton);

		JLabel label3 = new JLabel("Result :                       ");
		panel.add(label3);

		textArea1 = new JTextArea();
		textArea1.setEditable(false);

		textArea1.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		// textArea.setPreferredSize(new Dimension(350,90));

		JScrollPane scrollPane1 = new JScrollPane(textArea1);
		scrollPane1.setPreferredSize(new Dimension(350, 90));
		panel.add(scrollPane1);

	}

	public boolean format(String str) {

		str.trim();
		if (str.matches("[A-Z][A-Z][A-Z][/][A-Z][A-Z][A-Z]")
				&& str.length() == 7) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkTick(String str) {
		boolean result = true;
		str.trim();

		try {
			double ticksize = Double.parseDouble(str);
			if (ticksize > 0.000001 && ticksize <= 1.000000 && str != "") {
				result = true;
			} else {
				JOptionPane.showMessageDialog(null,
						" Please check tick size format",
						"Wrong Tick Size Format", JOptionPane.ERROR_MESSAGE);
				result = false;
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					" Please check tick size format", "Wrong Tick Size Format",
					JOptionPane.ERROR_MESSAGE);
			result = false;
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		CSVgenerator csv = new CSVgenerator();
		SwingUtilities.invokeLater(() -> {
			csv.setVisible(true);
		});

	}
}
