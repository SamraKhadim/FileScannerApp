import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class FileSearchGUI {

    static HashMap<String, String> fileIndex = new HashMap<>();
    static ArrayList<String> history = new ArrayList<>();

    public static void main(String[] args) {

        scanFolder("C:\\Users\\User\\Desktop");
        scanFolder("C:\\Users\\User\\Documents");
        scanFolder("C:\\Users\\User\\Downloads");

        JFrame frame = new JFrame("Smart File Search Engine");
        frame.setSize(750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JTextField searchField = new JTextField(25);
        JTextArea resultArea = new JTextArea(18, 55);
        resultArea.setEditable(false);

        JButton searchButton = new JButton("Search");
        JButton historyButton = new JButton("History");

        String[] options = {"All", "PDF", "Images", "Text"};
        JComboBox<String> filterBox = new JComboBox<>(options);

        frame.add(searchField);
        frame.add(filterBox);
        frame.add(searchButton);
        frame.add(historyButton);
        frame.add(new JScrollPane(resultArea));

        // 🎨 DARK UI
        frame.getContentPane().setBackground(new Color(25, 25, 25));
        searchField.setBackground(new Color(50, 50, 50));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);

        resultArea.setBackground(new Color(35, 35, 35));
        resultArea.setForeground(Color.GREEN);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        // 🔥 CORE SEARCH LOGIC (USED BY BOTH BUTTON & LIVE)
        Runnable searchLogic = () -> {

            String input = searchField.getText().toLowerCase().trim();
            String filter = (String) filterBox.getSelectedItem();

            resultArea.setText("");

            if (input.isEmpty()) return;

            history.add(input);

            boolean found = false;

            for (String fileName : fileIndex.keySet()) {

                String cleanName = fileName.toLowerCase();

                if (cleanName.contains(input)) {

                    if (filter.equals("PDF") && !fileName.endsWith(".pdf")) continue;
                    if (filter.equals("Images") &&
                            !(fileName.endsWith(".png") || fileName.endsWith(".jpg"))) continue;
                    if (filter.equals("Text") && !fileName.endsWith(".txt")) continue;

                    resultArea.append("File: " + fileName + "\n");
                    resultArea.append("Path: " + fileIndex.get(fileName) + "\n");
                    resultArea.append("-------------------------\n");

                    found = true;
                }
            }

            if (!found) {
                resultArea.setText("No results found!");
            }
        };

        // 🔍 LIVE SEARCH
        searchField.addCaretListener(e -> searchLogic.run());

        // 🔘 SEARCH + OPEN FILE
        searchButton.addActionListener(e -> {

            searchLogic.run();

            String input = searchField.getText().toLowerCase().trim();

            for (String fileName : fileIndex.keySet()) {

                if (fileName.toLowerCase().contains(input)) {
                    try {
                        File file = new File(fileIndex.get(fileName));
                        if (file.exists()) {
                            Desktop.getDesktop().open(file);
                        }
                    } catch (Exception ex) {
                        resultArea.append("Cannot open file.\n");
                    }
                    break;
                }
            }
        });

        // 📜 HISTORY BUTTON
        historyButton.addActionListener(e -> {

            resultArea.setText("SEARCH HISTORY:\n\n");

            for (String h : history) {
                resultArea.append(h + "\n");
            }
        });

        frame.setVisible(true);
    }

    static void scanFolder(String path) {

        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                fileIndex.put(file.getName(), file.getAbsolutePath());
            }
        }
    }
}