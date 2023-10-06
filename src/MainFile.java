import java.io.File;
import java.io.IOException;

public class MainFile {
    static String space = "    ";
    static int counter;

    public static void main(String[] args) {
        try {
            File file = new File("./src");
            printFiles(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printFiles(File file) throws IOException {
        String[] list = file.list();
        if (list != null) {
            for (String name : list) {
                File element = new File(file.getCanonicalPath(), name);
                if (element.isDirectory()) {
                    System.out.println(space.repeat(counter++) + element.getName() + ": ");
                    printFiles(element);
                } else {
                    System.out.println(space.repeat(counter) + name);
                }
            }
        }
        counter--;
    }
}
