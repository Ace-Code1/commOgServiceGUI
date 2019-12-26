package com.dji.comm.og.service;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;

import com.dji.comm.og.service.dto.Param;
import com.dji.comm.og.service.gui.FilterComboBox;
import com.dji.comm.og.service.gui.WaitDialog;
import com.dji.comm.og.service.utils.Aircrafts;
import com.dji.comm.og.service.utils.CallProcess;
import com.dji.comm.og.service.utils.CommPortLister;
import com.dji.comm.og.service.utils.CommandBuilder;
import com.dji.comm.og.service.utils.FileFinder;
import com.dji.comm.og.service.utils.OutputParser;

public class Application {

	//Logger
	private final static Logger LOGGER = Logger.getLogger(Application.class.getName());
	
	//Swing elements
	private JFrame frmCommogservicegui;
	private JTextField defaultTextField;
	private JTextField minTextField;
	private JTextField valueTextField;
	private JTextField maxTextField;
	public JTextField errorTextField;
	public JTextField pathTextField;
	public JComboBox comComboBox;
	public FilterComboBox parameterComboBox;
	public WaitDialog waitDialog;
	public JComboBox aircraftComboBox;
	
	//selectedValues
	public File selectedPath=null;
	public String selectedAircraft=null;
	public String selectedComPort=null;
	public String selectedParam=null;
	
	//List
	public List<String> availableParams=new ArrayList<String>();
		
	//Constants
	public static String EXECUTABLE_FILE="comm_og_service_tool.py";
	public static String START_AIRCRAFT="Spark";
	public static String SCRIPTING_LANGUAGE="python";
	public static String FIRMWARE_TOOLS_FOLDER="dji-firmware-tools";
	public static String PARAM_PREFIX="_0";
	public static String PARAM_FILE="params.txt";
	
	//Error messages
	public static String ERROR_NO_FIRMWARE_PATH_SET="No path to "+FIRMWARE_TOOLS_FOLDER+" set";
	public static String ERROR_NO_VALUE_SET="No value set";
	public static String ERROR_VALUE_NOT_A_NUMBER="Value is not a number";
	public static String ERROR_JUST_DIRECTORIES_ALLOWED="Only Directories are allowed";
	public static String ERROR_EXECUTABLE_NOT_FOUND = EXECUTABLE_FILE +" not found.";
	public static String ERROR_NO_COM_PORT="No COM Port set";
	public static String ERROR_NO_CONFIG_PARAM="No config parameter set";
	public static String ERROR_ON_GETTING_VALUE="Error on retrieving value";
	public static String ERROR_PARAM_FILE_NOT_FOUND="File "+PARAM_FILE+" not in "+FIRMWARE_TOOLS_FOLDER;
	
	//Success messages
	public static String SUCCESS_SETTING_VALUE="Value set";
	public static String SUCCESS_PARAMETERS_LOADED="Available parameters reloaded";
	public static String SUCCESS_PAREMTER_VALUES_LOADED="Parameter values loaded";
	public static String SUCCESS_COM_PORTS_REFRESHED="Com Ports reloaded";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frmCommogservicegui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("initialize Window");
		frmCommogservicegui = new JFrame();
		frmCommogservicegui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Application.this.actionOpenWindow();
			}
		});
		frmCommogservicegui.setTitle("CommOgServiceGUI");
		frmCommogservicegui.setBounds(100, 100, 713, 552);
		frmCommogservicegui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCommogservicegui.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 243, 698, 200);
		frmCommogservicegui.getContentPane().add(panel);
		panel.setLayout(null);
		
		valueTextField = new JTextField();
		valueTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		valueTextField.setBounds(116, 161, 108, 26);
		panel.add(valueTextField);
		valueTextField.setColumns(10);
		
		JLabel label_1 = new JLabel("");
		label_1.setBounds(32, 20, 0, 0);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("");
		label_2.setBounds(36, 20, 0, 0);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("");
		label_3.setBounds(40, 20, 0, 0);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("");
		label_4.setBounds(44, 20, 0, 0);
		panel.add(label_4);
		
		minTextField = new JTextField();
		minTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		minTextField.setEnabled(false);
		minTextField.setEditable(false);
		minTextField.setBounds(116, 7, 108, 26);
		panel.add(minTextField);
		minTextField.setColumns(10);
		
		maxTextField = new JTextField();
		maxTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		maxTextField.setEnabled(false);
		maxTextField.setEditable(false);
		maxTextField.setBounds(116, 60, 108, 26);
		panel.add(maxTextField);
		maxTextField.setColumns(10);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblValue.setBounds(15, 164, 86, 20);
		panel.add(lblValue);
		
		JLabel lblDefault = new JLabel("Default:");
		lblDefault.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDefault.setBounds(15, 115, 86, 20);
		panel.add(lblDefault);
		
		JLabel label_5 = new JLabel("");
		label_5.setBounds(48, 20, 0, 0);
		panel.add(label_5);
		
		JLabel label = new JLabel("Max:");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setBounds(15, 63, 56, 20);
		panel.add(label);
		
		JLabel lblMax = new JLabel("Min:");
		lblMax.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMax.setBounds(15, 10, 56, 20);
		panel.add(lblMax);
		
		defaultTextField = new JTextField();
		defaultTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		defaultTextField.setEnabled(false);
		defaultTextField.setEditable(false);
		defaultTextField.setBounds(116, 112, 108, 26);
		panel.add(defaultTextField);
		defaultTextField.setColumns(10);
		
		JLabel label_6 = new JLabel("");
		label_6.setBounds(52, 20, 0, 0);
		panel.add(label_6);
		
		JButton setValueButton = new JButton("Set Value");
		setValueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.actionSetValue();
			}
		});
		setValueButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		setValueButton.setBounds(239, 160, 115, 29);
		panel.add(setValueButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 459, 698, 39);
		frmCommogservicegui.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Info:");
		lblNewLabel.setBounds(24, 8, 39, 20);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(lblNewLabel);
		
		errorTextField = new JTextField();
		errorTextField.setBounds(68, 5, 512, 26);
		errorTextField.setEditable(false);
		errorTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		errorTextField.setForeground(Color.RED);
		panel_1.add(errorTextField);
		errorTextField.setColumns(40);
		
		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.actionClearErrorText();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_1.setBounds(584, 4, 99, 29);
		panel_1.add(btnNewButton_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 16, 698, 224);
		frmCommogservicegui.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton setPathButton = new JButton("Set path to dji-firmware-tools");
		setPathButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		setPathButton.setBounds(15, 5, 309, 29);
		panel_2.add(setPathButton);
		setPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.actionSelectPath();
			}
		});
		
		List<String> entries=new ArrayList<String>();
		//entries.add("value1");
		//entries.add("value2");
		//entries.add("3value");
		this.parameterComboBox = new FilterComboBox(entries);
		parameterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Application.this.actionSelectParam(e);
			}
		});
		parameterComboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		parameterComboBox.setBounds(119, 187, 444, 26);
		panel_2.add(parameterComboBox);
		
		pathTextField = new JTextField();
		pathTextField.setFont(new Font("Tahoma", Font.BOLD, 16));
		pathTextField.setEditable(false);
		pathTextField.setBounds(15, 50, 668, 26);
		panel_2.add(pathTextField);
		pathTextField.setColumns(10);
		
		LOGGER.info("load Aircrafts");
		aircraftComboBox = new JComboBox();
		aircraftComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Application.this.actionSelectAircraft(e);
			}
		});
		aircraftComboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		aircraftComboBox.setBounds(119, 142, 564, 26);
		panel_2.add(aircraftComboBox);
		List<String> ls = new ArrayList<String>(); 
		aircraftComboBox.setModel(new DefaultComboBoxModel<String>(Aircrafts.AIRCRAFTS.keySet().toArray(new String[0])));
		
		this.comComboBox = new JComboBox();
		comComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Application.this.actionSelectCom(e);
			}
		});
		this.comComboBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		this.comComboBox.setBounds(119, 92, 444, 26);
		panel_2.add(comComboBox);
		LOGGER.info("load available Com Ports");
		HashMap<String,String> serialPorts=CommPortLister.getSerialPorts();
		this.comComboBox.setModel(new DefaultComboBoxModel<String>(serialPorts.keySet().toArray(new String[0])));
		if(this.comComboBox.getSelectedItem()!=null) {
			Application.this.selectedComPort=serialPorts.get(this.comComboBox.getSelectedItem().toString());
		}
		
		JLabel lblNewLabel_1 = new JLabel("COM (USB)");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(15, 95, 89, 20);
		panel_2.add(lblNewLabel_1);
		
		JLabel lblAc = new JLabel("Model");
		lblAc.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAc.setBounds(15, 145, 89, 20);
		panel_2.add(lblAc);
		
		JLabel lblParam = new JLabel("Param");
		lblParam.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblParam.setBounds(15, 190, 89, 20);
		panel_2.add(lblParam);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.actionRefreshComPorts();
			}
		});
		btnNewButton.setBounds(578, 91, 105, 29);
		panel_2.add(btnNewButton);
		
		JButton button = new JButton("Refresh");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.actionRefreshParams(e);
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 16));
		button.setBounds(578, 186, 105, 29);
		panel_2.add(button);
	}
	
	public void actionSetValue() {
		LOGGER.info("try to set value");
		if(!this.isAllFieldsSet(true,true,true,true)) {
			LOGGER.info("actionSetValue: prevalidation failed");
			return;
		}			
		try {
			LOGGER.info("prepare and execute call");
			CommandBuilder commandBuilder=new CommandBuilder(CommandBuilder.SET, Application.this.selectedPath, Application.SCRIPTING_LANGUAGE, Application.EXECUTABLE_FILE, this.selectedComPort, Application.this.selectedAircraft, this.selectedParam+PARAM_PREFIX,this.valueTextField.getText());
			LOGGER.info(commandBuilder.getCommand());
			String output=CallProcess.executeCommand(commandBuilder.getCommand());
			LOGGER.info(output);
			setSuccessText(SUCCESS_SETTING_VALUE);			
		}catch(Throwable t) {
			setErrorText(t.getMessage());
			t.printStackTrace();
			return;
		}
	}
	
	public void actionSelectPath() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Choose dji-firmware-tools folder");
		Application.this.pathTextField.setText("");
		
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			Application.this.selectedPath = jfc.getSelectedFile();
			if(Application.this.selectedPath.isDirectory()) {
				if(FileFinder.isFilePresent(Application.this.selectedPath,EXECUTABLE_FILE)) {
					Application.this.pathTextField.setText(Application.this.selectedPath.getAbsolutePath());							
					LOGGER.info("path set");
				}
				else {
					setErrorText(ERROR_EXECUTABLE_NOT_FOUND);
					Application.this.selectedPath=null;
					LOGGER.info("no Executable found");
				}
			}
			else {
				setErrorText(ERROR_JUST_DIRECTORIES_ALLOWED);
				Application.this.selectedPath=null;
				LOGGER.info("no Directory");
			}
		}
	}
	
	private void initializeAvailableParamsFromFile(){
		LOGGER.info("load prepared parameter");
		if(!this.isAllFieldsSet(true,false,false,false)) {
			LOGGER.info("initializeAvailableParamsFromFile: prevalidation failed");
			return;
		}
		
		File paramsFile=new File(this.selectedPath+File.separator+PARAM_FILE);
		if(!paramsFile.exists()) {
			this.setErrorText(ERROR_PARAM_FILE_NOT_FOUND);
			LOGGER.info("no parameter file found");
			return;
		}
	
		List<String> params=new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.selectedPath+File.separator+PARAM_FILE));
			String line = reader.readLine();
			while (line != null) {
				params.add(line);
				line = reader.readLine();
			}
			
			Application.this.parameterComboBox.setEntries(params);
			Application.this.availableParams=params;
			setSuccessText(SUCCESS_PARAMETERS_LOADED);		
			LOGGER.info("parameters loaded");
			reader.close();
		} catch (IOException ex) {
			this.setErrorText(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private void initializeAvailableParams(ActionEvent e){
		try {
			if(!this.isAllFieldsSet(true,true,false,false)) {
				return;
			}

			
			waitDialog = new WaitDialog();
			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {

			    @Override
			    protected Void doInBackground() throws Exception {

			    	CommandBuilder commandBuilder=new CommandBuilder(CommandBuilder.LIST, Application.this.selectedPath, SCRIPTING_LANGUAGE, EXECUTABLE_FILE, Application.this.selectedComPort, Application.this.selectedAircraft);
					System.out.println(commandBuilder.getCommand());
			    	String output=CallProcess.executeCommand(commandBuilder.getCommand());
					List<Param> paramList=OutputParser.parseListOutput(output);
					Application.this.parameterComboBox.setEntries(OutputParser.getNameList(paramList));
					Application.this.availableParams=OutputParser.getNameList(paramList);
					setSuccessText(SUCCESS_PARAMETERS_LOADED);

					Application.this.waitDialog.close();
			        return null;
			    }
			};

			mySwingWorker.execute();
			waitDialog.makeWait("Please wait...", e);
			
		}catch(Throwable t) {
			setErrorText(t.getMessage());
			t.printStackTrace();
			return;
		}
	}
	
	public void actionSelectParam(ItemEvent e) {
		LOGGER.info("load selected parameter");
		boolean isTextFieldNotEmpty=!"".equals(this.parameterComboBox.getEnteredText());
		boolean isKnownParameter=this.availableParams.contains(this.parameterComboBox.getEnteredText());
		boolean isSelectedValueEqualEntered=e.getItem().toString().equals(this.parameterComboBox.getEnteredText());
				
		if(isTextFieldNotEmpty && isKnownParameter && isSelectedValueEqualEntered) {
			this.selectedParam=e.getItem().toString();
			LOGGER.info("load selected parameter execute");
			CommandBuilder commandBuilder;
			try {
				commandBuilder = new CommandBuilder(CommandBuilder.GET,this.selectedPath,Application.SCRIPTING_LANGUAGE,EXECUTABLE_FILE,this.selectedComPort,this.selectedAircraft,this.selectedParam);;
				LOGGER.info("load selected parameter execute");
				LOGGER.info("1st try");
				String output=CallProcess.executeCommand(commandBuilder.getCommand());
				if("".equals(output)) {
					commandBuilder = new CommandBuilder(CommandBuilder.GET,this.selectedPath,Application.SCRIPTING_LANGUAGE,EXECUTABLE_FILE,this.selectedComPort,this.selectedAircraft,this.selectedParam+""+this.PARAM_PREFIX);
					LOGGER.info(commandBuilder.getCommand());
					output=CallProcess.executeCommand(commandBuilder.getCommand());
				}
				if("".equals(output)) {
					setErrorText(ERROR_ON_GETTING_VALUE);
					LOGGER.info("couldn't load parameter values");
				}
				else {
					Param param=OutputParser.parseGetOutput(output);
					this.minTextField.setText(param.getMinVal());
					this.maxTextField.setText(param.getMaxVal());
					this.defaultTextField.setText(param.getDefaultVal());
					this.valueTextField.setText(param.getActVal());
					LOGGER.info("parameter values loaded");
					setSuccessText(SUCCESS_PAREMTER_VALUES_LOADED);
				}
			} catch (Exception e1) {
				setErrorText(e1.getMessage());
				e1.printStackTrace();
			}
			
		}
	}
	
	public void actionSelectAircraft(ItemEvent e) {
		this.selectedAircraft=Aircrafts.AIRCRAFTS.get(e.getItem().toString());
		LOGGER.info("choosen "+this.selectedAircraft+" = "+e.getItem().toString());
	}
	
	public void actionRefreshComPorts() {
		LOGGER.info("refresh Com Ports");	
		HashMap<String,String> commPorts=CommPortLister.getSerialPorts();
		this.comComboBox.setModel(new DefaultComboBoxModel<String>(commPorts.keySet().toArray(new String[0])));
		if(this.comComboBox.getSelectedItem()!=null) {
			this.selectedComPort=commPorts.get(this.comComboBox.getSelectedItem().toString());
			setSuccessText(SUCCESS_COM_PORTS_REFRESHED);
		}
	}
	
	public void actionSelectCom(ItemEvent e) {
		if(e.getItem()!=null) {
			HashMap<String,String> commPorts=CommPortLister.getSerialPorts();
			this.selectedComPort=commPorts.get(e.getItem().toString());
			LOGGER.info(this.selectedComPort+" selected");	
		}
	}
	
	public void actionRefreshParams(ActionEvent e) {
		LOGGER.info("reload parameters");
		initializeAvailableParamsFromFile();
	}
	
	private boolean isAllFieldsSet(boolean checkPath,boolean checkPort,boolean checkValue,boolean checkParam) {
		LOGGER.info("prevalidation called");
		boolean isAllSet=false;
		String value=this.valueTextField.getText();
		
		if(checkPath && this.selectedPath==null || "".equals(this.selectedPath)) {
			setErrorText(ERROR_NO_FIRMWARE_PATH_SET);
		}
		else if(checkPort && this.selectedComPort==null) {
			setErrorText(ERROR_NO_COM_PORT);
		}
		else if(checkValue && (value==null || "".equals(value))) {
			setErrorText(ERROR_NO_VALUE_SET);
		}
		else if(checkParam && (selectedParam==null || "".equals(selectedParam))) {
			setErrorText(ERROR_NO_CONFIG_PARAM);
		}
		else if(checkValue && (value!=null && !"".equals(value))) {
			try {
				Integer.parseInt(value); 
				isAllSet=true;
			}catch(Throwable t) {
				setErrorText(ERROR_VALUE_NOT_A_NUMBER);
			}
		}
		else {
			isAllSet=true;
		}
		
		return isAllSet;
	}
	
	private void actionClearErrorText() {
		LOGGER.info("clear message");
		this.errorTextField.setText("");
		this.errorTextField.setForeground(Color.black);
	}
	
	private void setErrorText(String text) {
		this.errorTextField.setForeground(Color.red);
		this.errorTextField.setText(text);
	}
	
	private void setSuccessText(String text) {
		this.errorTextField.setForeground(Color.green);
		this.errorTextField.setText(text);
	}
	
	public void actionOpenWindow(){
		LOGGER.info("preset personal Settings");
		//String path="D:\\"+FIRMWARE_TOOLS_FOLDER;
		//this.selectedPath=new File(path);
		//this.pathTextField.setText(this.selectedPath.getAbsolutePath());	
		this.aircraftComboBox.setSelectedItem(START_AIRCRAFT);
	}
	
}
