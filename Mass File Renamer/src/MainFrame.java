import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;

import java.awt.Color;
import java.awt.Component;

public class MainFrame extends JFrame {
	
	
	char[] dict = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private JPanel contentPane;
	private int number_of_characters = 15;
	private boolean is_file_selected = false;
	private boolean is_handling_multiple_files = false;
	ArrayList<String> fileNamesAL;
	FileDialog fd;
	File[] files;
	ArrayList<String> fileExtsAL;
	String modified_output_dir;
	private boolean is_using_diff_dir_than_default = false;
	File default_path = new File("C:\\Users");
	FileWriter fileWriter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String get_file_extensions(String[] ns) {
		return ns[ns.length - 1];
		
	}
	
	/*
	public void rename_multiple_files(File[] flist) {
		String[] n;
		ArrayList<String> file_names = new ArrayList<>();
		ArrayList<String> extensions = new ArrayList<>();
		for(int i = 0; i < flist.length; i++) {
			//iterate through file names and extensions
			System.out.println(flist[i].getName());
			n = flist[i].getName().split("\\.");
			System.out.println(get_file_extensions(n));
			
			
		}
		
	}
	*/
	
	
	public String getOutputFolder() {
	    try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.startsWith("outputfolder:")) {
	                return line.replaceFirst("^outputfolder:\\s*", "").trim();
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null; // not found or error
	}
	
	
	
	
	String cleanPath(String dirtyPath) {
	    return dirtyPath
	        .replaceAll("^[^:]+:\\s*", "") // Remove label
	        .replaceAll("[\\p{C}]", "") // Remove all control characters
	        .replaceAll("[^\\p{ASCII}]", "") // Remove non-ASCII
	        .replaceAll("^\"|\"$", "") // Remove surrounding quotes
	        .trim()
	        .replace('/', '\\') // Normalize slashes
	        .replaceAll("\\\\+", "\\\\"); // Normalize multiple backslashes
	}
	
	private static void setDetailsView(JFileChooser fileChooser) {
        // Force the file chooser to use the details view
        fileChooser.setFileView(new DetailedFileView());
    }
	
	public void rename_multiple_files(File[] files,  ArrayList<String> file_names, ArrayList<String> file_exts) throws IOException {
		for(int i = 0; i < files.length; i++) {
			String rfn = generate_random_str(number_of_characters);
			//System.out.println(rfn +  "." + file_exts.get(i));
			File f3;
			if(is_using_diff_dir_than_default) {
				f3 = new File(modified_output_dir + "\\" + rfn +  "." + file_exts.get(i));
			}else {
				f3 = new File("R:\\Videos\\" + rfn +  "." + file_exts.get(i));
			}
			
			
			if (f3.exists()) {
				throw new java.io.IOException("file exists");
			}
			boolean success = files[i].renameTo(f3);
			//System.out.println(success);
		}
	}
	
	public void rename_file(File f1) throws IOException{
		String[] names = f1.getName().split("\\.");
		String extension = names[names.length - 1];
		//System.out.println(names.length);
		for(int i = 0; i < names.length; i++) {
			//System.out.println(names[i]);
		}
		
		String random_file_name = generate_random_str(number_of_characters);
		
		
		
		
		if(modified_output_dir != null) {
			//System.out.println(modified_output_dir);
			File f2 = new File(modified_output_dir + "\\" + random_file_name +  "." + extension);
			if (f2.exists()) {
				throw new java.io.IOException("file exists");
			}
				   
			
			boolean success = f1.renameTo(f2);
			//System.out.println(success);
		}else {
			File f2 = new File("R:\\Videos\\" + random_file_name +  "." + extension);
			if (f2.exists()) {
				throw new java.io.IOException("file exists");
			}
				   
			
			boolean success = f1.renameTo(f2);
			//System.out.println(success);
		}
		
		
		
	}
	
	
	public boolean hasStartFolderSaved() {
		File settings = new File("settings.txt");
    	try {
			if(settings.exists() && FileUtils.readFileToString(settings, "UTF-8").contains("startfolder:")) {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
		
	}
	
	
	
	
	public void rename_files(ArrayList<String> names, ArrayList<String> exts) {
		
	}
	
	public String generate_random_str(int noc) {
		String rnd = "";
		int rndint = 0;
		Random random = new Random();
		for(int i = 0; i < noc; i++) {
			rndint = random.nextInt(dict.length - 1);
			rnd += dict[rndint];
		}
		
		return rnd;
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public MainFrame() throws IOException {
		//File settings = new File("settings.txt");
		File settings = new File("settings.txt");
		settings.createNewFile();
		
		String content = FileUtils.readFileToString(settings, "UTF-8");
		Pattern pattern = Pattern.compile("(?m)^outputfolder:\\s*(.*)$");  // Note the capturing group (.*)
		Matcher matcher = pattern.matcher(content);

		String outputFolderPath = "";
		if (matcher.find()) {
		    outputFolderPath = matcher.group(1); // Gets only the path part (group 1)
		}
		
		if(outputFolderPath == "") {
			File def_output_folder = new File("MFN_Output");
			def_output_folder.mkdir();
			outputFolderPath = def_output_folder.getAbsolutePath();
		}
		//System.out.println("outputFolderPath = " + outputFolderPath);
		modified_output_dir = outputFolderPath;
		
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Mass File Renamer");
		lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 19));
		lblNewLabel.setBounds(131, 65, 201, 34);
		contentPane.add(lblNewLabel);
		
		JButton single_file_button = new JButton("Single file");
		single_file_button.setBounds(89, 124, 89, 23);
		contentPane.add(single_file_button);
		
		JButton multiple_files_button = new JButton("Bulk Rename");
		multiple_files_button.setBounds(232, 124, 119, 23);
		contentPane.add(multiple_files_button);
		
		JLabel file_name_label = new JLabel("");
		file_name_label.setForeground(new Color(0, 0, 255));
		file_name_label.setBounds(41, 211, 391, 42);
		contentPane.add(file_name_label);
		
		JButton random_settings_button = new JButton("Randomisation settings");
		random_settings_button.setBounds(41, 177, 193, 23);
		contentPane.add(random_settings_button);
		
		JLabel random_file_name_label =  new JLabel("");
		random_file_name_label.setBounds(41, 264, 281, 14);
		contentPane.add(random_file_name_label);
		
		JButton randomise_button = new JButton("Randomise");
		randomise_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String rstr = generate_random_str(number_of_characters);
				random_file_name_label.setText("Randomly generated filename: " + rstr);
				
			}
		});
		randomise_button.setBounds(255, 177, 119, 23);
		contentPane.add(randomise_button);
		
		JButton rename_button = new JButton("Rename");
		rename_button.setBounds(41, 289, 89, 23);
		contentPane.add(rename_button);
		
		JButton change_default_folder_button = new JButton("default start folder");
		change_default_folder_button.setBounds(277, 289, 155, 23);
		contentPane.add(change_default_folder_button);
		
		JButton infoButton = new JButton("Info");
		infoButton.setBounds(161, 289, 89, 23);
		contentPane.add(infoButton);
		
		infoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String url_open ="https://github.com/ReverseEntropy";
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		change_default_folder_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();
					setDetailsView(fc);
					File def = new File("C:\\Users");
					fc.setCurrentDirectory(def);
		            fc.setMultiSelectionEnabled(true);
		            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		         // Actually show the dialog
		            int returnVal = fc.showOpenDialog(change_default_folder_button);
		            
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File[] selectedFiles = fc.getSelectedFiles();
		                // Do something with the selected files/folders
		                String filename = fc.getSelectedFile().getAbsolutePath();
		                default_path = fc.getSelectedFile();
		                JOptionPane.showMessageDialog(null, "defau lt start folder has been changed to " + filename, "default start folder changed", JOptionPane.INFORMATION_MESSAGE);
		                
		                
		                
		                File settings = new File("settings.txt");
	                	if(settings.exists() && FileUtils.readFileToString(settings, "UTF-8").contains("startfolder:")) {
	                		try {
	                		     String content = FileUtils.readFileToString(new File("settings.txt"), "UTF-8");
	                		     //System.out.println("newly added dir: " + "startfolder: " + filename + "\n");
	                		     String escapedDir = filename.replace("\\", "\\\\");
	                		     content = content.replaceAll("(?m)^startfolder:\\s*.*$", "startfolder: " + escapedDir);
	                		     File tempFile = new File("settings.txt");
	                		     FileUtils.writeStringToFile(tempFile, content, "UTF-8");
	                		  } catch (IOException ex) {
	                		     //Simple exception handling, replace with what's necessary for your use case!
	                		     throw new RuntimeException("Generating file failed", ex);
	                		  }
	                	}else {
	                		fileWriter = new FileWriter("settings.txt", true);
			                fileWriter.write("startfolder: " + filename + "\n");
			                //System.out.println("wrote");
			                fileWriter.close();
	                	}
		                
		                
		                //System.out.println(filename);
		            }
		            
		        } catch (Exception fe) {
		            fe.printStackTrace();
		        }
		            

				
				
			}
		});
		
		
		
		
		single_file_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				is_handling_multiple_files = false;
				try {
					
					JFrame frame = new JFrame();
					frame.setVisible(false);
					frame.setSize(800, 400);
					fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
					if(hasStartFolderSaved()) {
           		     String content = FileUtils.readFileToString(new File("settings.txt"), "UTF-8");
           		  String path = content.replaceFirst("^startfolder:\\s*", "").trim();
                         //System.out.println("PATH HERE " + path);
           		    //path = new File(path).getName();  // Normalize the path
           		
           		    //System.out.println("path: " + path);
           		     
           		   String REAL4KPATH = "";
           		   
           		   for(int i = 0; i < path.length(); i++) {
           			   REAL4KPATH += path.charAt(i);
           		   }
           		   
           		/*
           		   String REAL4KPATHREMUX = REAL4KPATH.replaceAll("\\\\", "\\\\\\\\");
           		   System.out.println("REAL 4K PATH REMUX " + REAL4KPATHREMUX);
           		   
           		   */
           		   String alphapath=  "";
    
           		   
           		   
           		   String testeqpath = "C:\\Users\\yusef\\project-bo4\\shield-development";

           		     /*System.out.println("Raw path: " + path);
           		File dir = new File(path);
           		System.out.println("name of dir =" + dir.getPath()  );
           		if (!dir.exists()) {
           		    // Try alternative approaches
           		    path = path.replace("%20", " "); // Handle URL-encoded spaces
           		    dir = new File(path);
           		}
           		
           		System.out.println(path);
           		
           		//path = cleanPath(path);
           		
           		System.out.println("DIR: " + dir.getAbsolutePath());
           		System.out.println("DIR bytes: " + Arrays.toString(dir.getAbsolutePath().getBytes()));
           		System.out.println("Exists: " + dir.exists());
           		System.out.println("IsDirectory: " + dir.isDirectory());
           		System.out.println("CanRead: " + dir.canRead());
           		System.out.println("CanExecute: " + dir.canExecute());
           		
           		String teString = "K:\\APKs";
           		File t = new File(teString);
           		
           		System.out.println("test exists: " + t.exists());
           		      * 
           		      * 
           		      * 
           		      */
           		   
           		   
           		String paths = null;

           		try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
           		    String line;
           		    while ((line = reader.readLine()) != null) {
           		        if (line.startsWith("startfolder:")) {
           		            paths = line.replaceFirst("^startfolder:\\s*", "").trim();
           		            break; // we're done
           		        }
           		    }
           		} catch (IOException xe) {
           		    xe.printStackTrace();
           		}

           		if (paths != null) {
           		    //System.out.println("Extracted path: [" + paths + "]");
           		    File dir = new File(paths);
           		    if (dir.exists() && dir.isDirectory()) {
           		        fd.setDirectory(paths);
           		    } else {
           		        //System.out.println("Invalid directory: " + paths);
           		    }
           		} else {
           		    //System.out.println("startfolder not found in settings.txt");
           		}
           		   
           		   
           		   
           		   
           		   
           		   
           		   
           		   
           		   /*
           		    System.out.println("alpha path = " + alphapath);
           		    File dir = new File(path);
             		System.out.println("DIR: " + dir.getAbsolutePath());
               		System.out.println("DIR bytes: " + Arrays.toString(dir.getAbsolutePath().getBytes()));
               		System.out.println("Exists: " + dir.exists());
               		System.out.println("IsDirectory: " + dir.isDirectory());
               		System.out.println("CanRead: " + dir.canRead());
               		System.out.println("CanExecute: " + dir.canExecute());
               		
           		    
               		String fp = "C:\\Users\\yusef\\project-bo4\\shield-development";
           		     
           		     System.out.println("are they equal? " + fp.equals(path));
           		     */
           		  String contents = FileUtils.readFileToString(
           		        new File("settings.txt"), 
           		        "UTF-8"
           		    );
           		   
           		  
           		  //System.out.println(contents);
           		     
           		     alphapath.trim();
           		     fd.setDirectory(paths);
					//fd.setFile("");
					fd.setVisible(true);
					String filename = fd.getFile();
					if (filename == null) {
						//System.out.println("You cancelled the choice");
						is_file_selected = false;
					}
					  else {
						//System.out.println("You chose " + filename);
						file_name_label.setText("Chosen file: " + filename);
						is_file_selected = true;
					}
           		     
           		     
           		     
					}else {
						fd.setDirectory(default_path.getAbsolutePath());
						fd.setFile("");
						fd.setVisible(true);
						String filename = fd.getFile();
						if (filename == null) {
							//System.out.println("You cancelled the choice");
							is_file_selected = false;
						}
						  else {
							//System.out.println("You chose " + filename);
							file_name_label.setText("Chosen file: " + filename);
							is_file_selected = true;
						}
					}
					
					  
					
				}catch (Exception exc) {
					//System.out.println(exc);
				}
				
			}
		});
			
		multiple_files_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				is_handling_multiple_files = true;
				// Set the file chooser to details view
		        
				JFileChooser fileChooser = new JFileChooser();
				setDetailsView(fileChooser);
				
				   String content;
				try {
					String paths = null;

	           		try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
	           		    String line;
	           		    while ((line = reader.readLine()) != null) {
	           		        if (line.startsWith("startfolder:")) {
	           		            paths = line.replaceFirst("^startfolder:\\s*", "").trim();
	           		            break; // we're done
	           		        }
	           		    }
	           		} catch (IOException xe) {
	           		    xe.printStackTrace();
	           		}

	           		if (paths != null) {
	           		    //System.out.println("Extracted path: [" + paths + "]");
	           		    File dir = new File(paths);
	           		    if (dir.exists() && dir.isDirectory()) {
	           		        fileChooser.setCurrentDirectory(dir);
	           		    } else {
	           		        //System.out.println("Invalid directory: " + paths);
	           		    }
	           		} else {
	           		    //System.out.println("startfolder not found in settings.txt");
	           		}
				}catch (Exception xae) {
					xae.printStackTrace();
				}
				
				
				
	            fileChooser.setMultiSelectionEnabled(true);

	            int option = fileChooser.showOpenDialog(contentPane);
	            if(option == JFileChooser.APPROVE_OPTION){
	               files = fileChooser.getSelectedFiles();
	               String fileNames = "";
	               fileNamesAL = new ArrayList<>();
	               fileExtsAL = new ArrayList<>();
	               for(File file: files){
	            	  String[] names = file.getName().split("\\.");
	            	  fileNamesAL.add(names[0]);
	            	  fileExtsAL.add(names[1]);
	                  names = null;
	                  
	               }
	               file_name_label.setText("File(s) Selected: " + fileNames);
	               //System.out.println(fileNamesAL);
	               //System.out.println(fileExtsAL);
	               
	               /*
	                * try {
						rename_multiple_files(files, fileNamesAL, fileExtsAL);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                * 
	                * 
	                */
	               
	               
	               /*
	                * String random_file_name = generate_random_str(number_of_characters);
					File f3 = new File("R:\\Videos\\" + random_file_name +  "." + extension);
		
					if (f3.exists()) {
						throw new java.io.IOException("file exists");
					}
			   
		
					boolean success = f1.renameTo(f3);
					System.out.println(success);
	                * 
	                * 
	                * 
	                */
	               
	            }else{
	            	file_name_label.setText("Open command canceled");
	            }
			}
		});
		
		
		
		
		random_settings_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame myFrame = new JFrame("Settings");
				JButton save_button = new JButton("Save");
				JButton change_save_dir_button = new JButton();
				change_save_dir_button.setText("Change output directory");
				JLabel current_numbers_Label = new JLabel("Current amount: " + number_of_characters);
				JLabel nline = new JLabel("            ");
				JLabel nline_2 = new JLabel("            ");
				JLabel current_output_directory_Label = new JLabel();
				current_output_directory_Label.setText("Current Output Directory: " + modified_output_dir);
				myFrame.setSize(400, 300);
				myFrame.setVisible(true);
				myFrame.getContentPane().setLayout(new FlowLayout());
				JLabel desc = new JLabel("How many characters?");
				myFrame.getContentPane().add(desc);
				
				
				SpinnerModel values = new SpinnerNumberModel(1, 1, 100, 1);
				JSpinner testJSpinner = new JSpinner(values);
				// 1. Get the editor component of your spinner:
				Component mySpinnerEditor = testJSpinner.getEditor();
				// 2. Get the text field of your spinner's editor:
				JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
				// 3. Set a default size to the text field:
				jftf.setColumns(3);
				testJSpinner.setBounds(150, 150, 75, 75);
				myFrame.getContentPane().add(testJSpinner);
				myFrame.getContentPane().add(save_button);
				myFrame.getContentPane().add(nline);
				myFrame.getContentPane().add(current_numbers_Label);
				myFrame.getContentPane().add(nline_2);
				myFrame.getContentPane().add(current_output_directory_Label);
				
				
				myFrame.getContentPane().add(change_save_dir_button);
				
				change_save_dir_button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser f = new JFileChooser();
				        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				        //f.showSaveDialog(null);
				        
				        //System.out.println("opened change save dir button");

				        int returnVal = f.showOpenDialog(change_save_dir_button);
			            //System.out.println("returnVal = " + returnVal);
				        
			            if (returnVal == JFileChooser.APPROVE_OPTION) {
			                File[] selectedFiles = f.getSelectedFiles();
			                // Do something with the selected files/folders
			                String filename = f.getSelectedFile().getAbsolutePath();
			                default_path = f.getSelectedFile();
			                //JOptionPane.showMessageDialog(null, "default SAVE folder has been changed to " + filename, "default SAVE folder changed", JOptionPane.INFORMATION_MESSAGE);
			              
			                //System.out.println("entered the first if statement");
			                
			                File settings = new File("settings.txt");
		                	try {
								if(settings.exists() && FileUtils.readFileToString(settings, "UTF-8").contains("outputfolder:")) {
									try {
										 //System.out.println("settings does exist, case 1");
									     String content = FileUtils.readFileToString(new File("settings.txt"), "UTF-8");
									     //System.out.println("newly added dir: " + "outputfolder: " + filename + "\n");
									     String escapedDir = filename.replace("\\", "\\\\");
									     content = content.replaceAll("(?m)^outputfolder:\\s*.*$", "outputfolder: " + escapedDir);
									     modified_output_dir = content.replaceAll("(?m)^outputfolder:\\s*.*$", "outputfolder: " + escapedDir);
									     File tempFile = new File("settings.txt");
									     //System.out.println("content = " + content);
									     
									     
									     String contents = FileUtils.readFileToString(settings, "UTF-8");
									     Pattern pattern = Pattern.compile("(?m)^outputfolder:\\s*(.*)$");  // Note the capturing group (.*)
									     Matcher matcher = pattern.matcher(content);

									     String outputFolderPath = "";
									     if (matcher.find()) {
									         outputFolderPath = matcher.group(1); // Gets only the path part (group 1)
									     }
									     //System.out.println("outputFolderPath = " + outputFolderPath);
									     modified_output_dir = outputFolderPath;
									     FileUtils.writeStringToFile(tempFile, content, "UTF-8");
							             JOptionPane.showMessageDialog(null, "default SAVE folder has been changed to " + filename, "default SAVE folder changed", JOptionPane.INFORMATION_MESSAGE);

									  } catch (IOException ex) {
									     //Simple exception handling, replace with what's necessary for your use case!
									     throw new RuntimeException("Generating file failed", ex);
									  }
								}else {
									//System.out.println("settings doesnt exist, case 2");
									fileWriter = new FileWriter("settings.txt", true);
								    fileWriter.write("outputfolder: " + filename + "\n");
								    //System.out.println("wrote");
								    modified_output_dir = filename;
								    fileWriter.close();
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			                
			                
			                //System.out.println(filename);
			            }
				        
				        
				        
				        
				        
				        
				        
				        
				        
				        
				        
				        
				        
				        /*
		                try {
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	
		                	File settings = new File("settings.txt");
		                	if(settings.exists() && FileUtils.readFileToString(settings, "UTF-8").contains("outputfolder:")) {
		                		try {
		                		     String content = FileUtils.readFileToString(new File("settings.txt"), "UTF-8");
		                		     System.out.println("newly added dir: " + "outputfolder: " + modified_output_dir + "\n");
		                		     String escapedDir = modified_output_dir.replace("\\", "\\\\");
		                		     content = content.replaceAll("(?m)^outputfolder:\\s*.*$", "outputfolder: " + escapedDir);
		                		     File tempFile = new File("settings.txt");
		                		     FileUtils.writeStringToFile(tempFile, content, "UTF-8");
		                		  } catch (IOException ex) {
		                		     //Simple exception handling, replace with what's necessary for your use case!
		                		     throw new RuntimeException("Generating file failed", ex);
		                		  }
		                	}else {
		                		fileWriter = new FileWriter("settings.txt", true);
								fileWriter.write("outputfolder: " + modified_output_dir + "\n");
								System.out.println("wrote");
				                fileWriter.close();
		                	}
		                	
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		                
					*/	
					}
					
				});
				
				save_button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						number_of_characters = (int) testJSpinner.getValue();
						
					}
				});
				
			}
		});
		
		
		rename_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(modified_output_dir != null) {
					is_using_diff_dir_than_default = true;
				}
				
				if(!is_handling_multiple_files) {
					if(is_file_selected) {
						try {
							rename_file(fd.getFiles()[0]);
							//System.out.println(fd.getFiles()[0]);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(new JFrame(), "No file was chosen!", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					
					try {
						rename_multiple_files(files, fileNamesAL, fileExtsAL);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				}
				
				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}


class DetailedFileView extends javax.swing.filechooser.FileView {
    @Override
    public String getName(File f) {
        return FileSystemView.getFileSystemView().getSystemDisplayName(f);
    }
}

