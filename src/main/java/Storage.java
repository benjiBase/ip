import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Storage {
    private static Path filePath = null;
    private static String line;
    public Storage(String filePath) throws DukeException {
        Storage.filePath = Paths.get(filePath);
        createFile();
    }

    private void createDirectory() throws  DukeException {
        Path directory = filePath.getParent();
        if (Files.notExists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("new directory created: " + directory);
            } catch (IOException e) {
                throw new DukeException("Error creating directory");
            }
        }
    }

    public void createFile() throws DukeException {
        if(Files.notExists(filePath)){
            createDirectory();
            try {
                Files.createFile(filePath);
                System.out.println("new file created: " + filePath);
            } catch (IOException e) {
                throw new DukeException("Error creating file");
            }
        }
    }

    public static void readFile() throws DukeException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new DukeException("Error reading file");
        }
    }

    public static void writeFile(ArrayList<Task> tasks) throws DukeException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.toString()))) {
            for (Task task : tasks) {
                bw.write(task.toFileString());
                bw.newLine(); // Ensure each task is on a new line
            }
        } catch (IOException e) {
            throw new DukeException("Error writing to file");
        }
    }

    public ArrayList<Task> loadFile() throws DukeException {
        ArrayList<Task> tasks = new ArrayList<>();
        boolean isDone;
        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            while ((line = br.readLine()) != null) {
                isDone = line.charAt(4) == '1';
                tasks.add(stringToTask(line));
                tasks.get(tasks.size() - 1).isDone = isDone;
            };
        } catch (IOException e) {
            throw new DukeException("Error reading file");
        }
        return tasks;
    }

    private Task stringToTask(String line) {
        String[] words = line.split(" \\| ");
        if(words[0].equals("T")){
            return new Todo(words[2]);
        } else if(words[0].equals("D")){
            return new Deadline(words[2], words[3]);
        } else if(words[0].equals("E")){
            return new Event(words[2], words[3], words[4]);
        } else {
            return null;
        }
    }
}
