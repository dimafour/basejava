import java.io.File;
import java.io.IOException;

public class MainFile {
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) {
        try {
            File file = new File("C:\\Projects\\basejava\\src");
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
                    System.out.print(sb.append("  "));
                    System.out.println(element.getName() + ": " + sb.append("  "));
                    printFiles(element);
                } else {
                    System.out.println(sb + name);
                }
            }
        }
        sb.delete(0, sb.length());
    }
}
