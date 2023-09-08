import java.io.File;
import java.util.ArrayList;

public class MainFile {
    static StringBuilder sb = new StringBuilder();

    public static ArrayList<String> allFilesList = new ArrayList<>();

    public static void main(String[] args) {
        printFiles(new File("C:\\Projects\\basejava\\src"));
//        System.out.println(allFilesList);
    }

    public static void printFiles(File file) {
        String[] list = file.list();
        if (list != null) {
            for (String name : list) {
                File element = new File(file.getAbsolutePath(), name);
                if (element.isDirectory()) {
                    System.out.print(sb.append("  "));
                    System.out.println(element + ": " + sb.append("  "));
                    printFiles(element);
                } else {
                    System.out.println(sb + name);
                }
            }
        }
        sb.delete(0, sb.length());
    }
}
