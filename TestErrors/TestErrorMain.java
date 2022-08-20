

import java.io.File;
import java.util.Arrays;

public class TestErrorMain {
        public static void main(String[] args) {
            // Creates an array in which we will store the names of files and directories
            String[] fileNames;

            // Creates a new File instance by converting the given pathname string

            String folderPath="C:\\temp\\Error Files\\" ;//replace with your own folder path

            File f = new File(folderPath);
            // Populates the array with names of files and directories
            fileNames=f.list();
            assert fileNames != null;
            Arrays.sort(fileNames);


            Engine engine=new ...;//enter your implement
            int i=0;
            // For each pathname in the pathnames array
            for (String fileName : fileNames) {
                try{
                    System.out.println((++i) +" # "+fileName+":");

                    engine.loadXMLFile(folderPath+fileName);//enter your implement
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage());
                }

            }
        }

}
