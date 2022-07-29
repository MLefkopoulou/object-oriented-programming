/*
> NAME : Lefkopoulou Eleni Maria
> AEM  : 2557
> HW   : 3 - File Browser
> DATE : 24/04/2020
> Class: breadcrumbNode keeps info about the link which exists at breadcrumb
*/
package ce326.hw3;
import java.io.File;
import java.util.ArrayList;

public class breadcrumbNode {
    String name;
    String fullPath;
    char characteristic;
    ArrayList<File> listOfFiles;
    
    public breadcrumbNode(java.io.File dir, int sizeOfbreadcrumb,char c){
         
        listOfFiles= new ArrayList<>(); 
        fullPath = dir.getAbsolutePath();
        if(sizeOfbreadcrumb == 0){
            characteristic =c;
            name = fullPath;
         }
         else{
            characteristic = c;
            File file = new File(fullPath);
            name = file.getName();   
         }
        if(dir.canRead()){ 
            File []array;
            array = new File[(int)dir.length()];
            array = dir.listFiles();

            for(int l =0; l<array.length; l++){
                listOfFiles.add(l,array[l]);
      
            }
        }
        else{
            listOfFiles = null;
        }
    }
}
