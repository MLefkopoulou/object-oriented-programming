/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : Histogram
*/
package ce326.hw2;
import java.io.*;

public class Histogram {
    float []Array=new float[256];
    int []equalizationArray=new int[256];
    int pixels;

     public Histogram(YUVImage img) {
        pixels=img.width*img.height;
        for(int i=0;i<img.height;i++){
            for(int j=0;j<img.width;j++){

                Array[img.arrayImage[i][j].getY()]++;

            }
        }
    }
    @Override
    public String toString(){
        String str="";
        int hashtag;
        int dollar;
        int starburst;
        int temp;
        for(int i=0;i<256;i++){
            hashtag=(int) (Array[i]/1000);
            temp=(int) (Array[i]-(hashtag*1000));
            dollar=temp/100;
            temp=temp-(100*dollar);
            starburst=temp;
            str=str+i;
            for(int j=0;i<hashtag;j++){
                str=str+"#";
            }
            for(int j=0;i<dollar;j++){
                str=str+"$";
            }
            for(int j=0;i<starburst;j++){
                str=str+"*";
            }

        }
        return(str);
    }
    public void toFile(File file){
        FileWriter writer=null;
        File archive;
        String str;
        str=toString();
        String filename=file.getName();
        try{
            if(file.exists()){
                if(file.length()>0){
                    writer=new FileWriter(filename,false);
                }
                writer=new FileWriter(filename);
                writer.write(str);
            }
            else{
                archive=new File(filename);
                archive.createNewFile();
                writer=new FileWriter(archive);
                writer.write(str);


            }
        }
        catch(IOException ex) {
                System.out.println("IOException occured while reading from file "+file);
        }
        finally {
            if (writer!= null) {
                try{
                    writer.close();
                }
                catch(IOException ex) {
                    System.out.println("IOException occured while reading from file "+file);
                }
            }
        }



    }
    public void equalize(){
        float []histogramArray=new float[256];
        histogramArray=Array.clone();

        for(int i=0;i<256;i++){
            histogramArray[i]=histogramArray[i]/pixels;
        }
        for(int i=1;i<256;i++){
            histogramArray[i]=histogramArray[i]+histogramArray[i-1];
        }
         for(int i=0;i<256;i++){
            equalizationArray[i]=(int) (histogramArray[i]*235);
        }
    }
    public short getEqualizedLuminocity(int luminocity){
        return(short) (equalizationArray[luminocity]);

    }

}
