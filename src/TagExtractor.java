import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import static java.nio.file.StandardOpenOption.CREATE;


public class TagExtractor extends JFrame{
    ArrayList<String> stopWords = new ArrayList<>();
    HashMap<String, Integer> wordCount = new HashMap<>();

    JPanel main = new JPanel();
    JPanel controlPnl = new JPanel();
    JPanel scrollPnl = new JPanel();
    JLabel titleLbl = new JLabel("Word Frequency Scanner", JLabel.CENTER);

    JButton quitBtn = new JButton("Exit");
    JButton chooseBtn = new JButton("Choose .txt File");
    JButton saveBtn = new JButton("Save Output");

    JTextArea displayArea = new JTextArea(20, 40);
    JScrollPane scroller = new JScrollPane(displayArea);


    public TagExtractor() throws IOException {
        titleLbl.setFont(new Font("Times New Roman", Font.ITALIC, 36));
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        quitBtn.setFont(new Font("Sans Serif", Font.BOLD, 24));
        chooseBtn.setFont(new Font("Sans Serif", Font.BOLD, 24));
        saveBtn.setFont(new Font("Sans Serif", Font.BOLD, 24));
        setTitle("Word Frequency Scanner");
        setSize(1440, 810);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createGUI();
        enableButtons();
        filter();

        setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        TagExtractor frame = new TagExtractor();
        frame.setLocationRelativeTo(null);
    }

    private void createGUI()
    {
        main.setLayout(new BorderLayout());

        controlPnl.setLayout(new GridLayout(1,3 ));

        controlPnl.add(chooseBtn);
        controlPnl.add(saveBtn);
        controlPnl.add(quitBtn);

        titleLbl.setFont(new Font("Serif", Font.PLAIN, 24));

        scrollPnl.add(scroller);

        main.add(BorderLayout.NORTH, titleLbl);
        main.add(BorderLayout.CENTER, scrollPnl);
        main.add(BorderLayout.SOUTH, controlPnl);

        add(main);
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            displayArea.append(pair.getKey() + " = " + pair.getValue() + "\n");
            it.remove();
        }
    }

    public void filter () throws IOException {
        String rec = "";
        BufferedReader reader =
                new BufferedReader(new FileReader("stopWords.txt"));

        while(reader.ready()) {
            rec = reader.readLine();
            stopWords.add(rec);
        }
        reader.close();
    }

    private void enableButtons()
    {


        chooseBtn.addActionListener
                (
                        e -> {
                            JFileChooser chooser = new JFileChooser();
                            File selectedFile;

                            try {
                                File workingDirectory = new File(System.getProperty("user.dir"));

                                chooser.setCurrentDirectory(workingDirectory);

                                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                                    selectedFile = chooser.getSelectedFile();

                                    //HashMap h=new HashMap();
                                    FileInputStream fin=new FileInputStream(selectedFile);
                                    BufferedReader br=new BufferedReader(new InputStreamReader(fin));
                                    String n;
                                    while((n=br.readLine())!=null)
                                    {
                                        if(wordCount.containsKey(n))
                                        {
                                            int i=(Integer)wordCount.get(n);
                                            wordCount.put(n,(i+1));
                                        }
                                        else {
                                            wordCount.put(n, 1);
                                        }
                                        for (int i = 0; i < stopWords.size(); i++ ) {
                                            if (stopWords.contains(n.toLowerCase())) {
                                                wordCount.remove(n);
                                            }
                                        }
                                    }
                                    printMap(wordCount);

                                    System.out.println("\n\nData file read!");
                                }
                            }
                            catch (FileNotFoundException i) {
                                displayArea.append("File not found!!!");
                                i.printStackTrace();
                            }
                            catch (IOException i) {
                                i.printStackTrace();
                            }
                        }
                );

        saveBtn.addActionListener
                (
                        e -> {
                            File workingDirectory = new File(System.getProperty("user.dir"));
                            Path file = Paths.get(workingDirectory.getPath() + "\\results.txt");

                            try {
                                OutputStream out =
                                        new BufferedOutputStream(Files.newOutputStream(file, CREATE));
                                BufferedWriter writer =
                                        new BufferedWriter(new OutputStreamWriter(out));


                                String res = displayArea.getText();
                                writer.write(res);
                                writer.close();
                                JOptionPane.showMessageDialog(null, "File Written!");
                            }
                            catch (IOException a) {
                                a.printStackTrace();
                            }
                        }
                );

        quitBtn.addActionListener
                (
                        e -> {
                            System.exit(0);
                        }
                );
    }

}

