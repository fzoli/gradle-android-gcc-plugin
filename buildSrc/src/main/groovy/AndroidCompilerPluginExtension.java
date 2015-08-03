import java.io.File;

public class AndroidCompilerPluginExtension {
    
    public String ndkRoot = "";

    public String getNdkRootPath() {
        if (ndkRoot == null || ndkRoot.trim().isEmpty()) {
            return "";
        }
        File rootDir = new File(ndkRoot.trim());
        if (!rootDir.isDirectory()) {
            return "";
        }
        return rootDir.getAbsolutePath();
    }

}
