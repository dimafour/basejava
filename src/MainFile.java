import java.io.File;
import java.util.ArrayList;

public class MainFile {

    public static ArrayList<String> allFilesList = new ArrayList<>();

    public static void main(String[] args) {
        addFiles(new File("C:\\Projects\\basejava\\src"));
        System.out.println(allFilesList);
    }

    public static void addFiles(File file) {
        String[] list = file.list();
        if (list != null) {
            for (String name : list) {
                File element = new File(file.getAbsolutePath(), name);
                if (element.isDirectory()) {
                    addFiles(element);
                } else {
                    allFilesList.add(name);
                }
            }
        }
    }
}
