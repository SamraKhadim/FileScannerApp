import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static HashMap<String, String> fileIndex = new HashMap<>();

    public static void main(String[] args) {

        String folderPath = "C:\\Users\\User\\Desktop";

        scanFolder(folderPath);

        System.out.println("\n--- INDEX CREATED ---\n");

        for (String fileName : fileIndex.keySet()) {
            System.out.println(fileName + " -> " + fileIndex.get(fileName));
        }

        // SEARCH PART
        Scanner sc = new Scanner(System.in);

        System.out.println("\nEnter file name to search:");
        String search = sc.nextLine();

        searchFile(search);
    }

    static void scanFolder(String path) {

        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("No files found!");
            return;
        }

        for (File file : files) {

            if (file.isFile()) {
                fileIndex.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    static void searchFile(String input) {

        input = input.toLowerCase().trim();

        boolean found = false;

        System.out.println("\n" +
                " SEARCH RESULTS:");

        for (String fileName : fileIndex.keySet()) {

            String cleanName = fileName.toLowerCase();

            if (cleanName.contains(input) || input.contains(cleanName)) {

                System.out.println("-------------------------");
                System.out.println("📄 File Name: " + fileName);
                System.out.println("📁 Path: " + fileIndex.get(fileName));

                found = true;
            }
        }

        if (!found) {
            System.out.println(" No matching file found!");
        }

        System.out.println("-------------------------");
    }}