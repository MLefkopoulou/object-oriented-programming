/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : YUVImage
*/
package ce326.hw2;
import java.util.*;
import java.io.*;

public class YUVImage {
    int width;
    int height;
    YUVPixel [][]arrayImage;
    public YUVImage(int width, int height){
        this.height=height;
        this.width=width;
        arrayImage=new YUVPixel[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j]=new YUVPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j].setY((short)16);
                arrayImage[i][j].setU((short)128);
                arrayImage[i][j].setV((short)128);
            }
        }
    }

    public YUVImage(YUVImage copyImg){
        this.width=copyImg.width;
        this.height=copyImg.height;

        arrayImage=new YUVPixel[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j]=new YUVPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j].setY(copyImg.arrayImage[i][j].getY());
                arrayImage[i][j].setU(copyImg.arrayImage[i][j].getU());
                arrayImage[i][j].setV(copyImg.arrayImage[i][j].getV());


            }
        }

    }
    public YUVImage(RGBImage RGBImg){
        height=RGBImg.getHeight();
        width=RGBImg.getWidth();
        YUVPixel [][]newArray =new YUVPixel[height][width];

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                newArray[i][j]=new YUVPixel(RGBImg.getPixel(i,j));
            }
        }
        arrayImage=newArray.clone();


    }
    public YUVImage(java.io.File file) throws UnsupportedFileFormatException,FileNotFoundException{
        String str;
        String nameImage;
        File YUVfile;
        String filename=file.getAbsolutePath();
        Scanner sc;

        try{
            YUVfile = new File (filename);
            sc = new Scanner(YUVfile);
            if(!filename.endsWith(".yuv")){
                throw new UnsupportedFileFormatException("Is not YUV!!!");
            }
            nameImage=sc.next();
            this.width=sc.nextInt();
            this.height=sc.nextInt();
            arrayImage=new YUVPixel[height][width];
             for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j]=new YUVPixel((short)0,(short)0,(short)0);
            }
        }
            for(int i=0;i<this.height;i++){
                for(int j=0;j<this.width;j++){
                    arrayImage[i][j].setY((short)sc.nextInt());
                    arrayImage[i][j].setU((short)sc.nextInt());
                    arrayImage[i][j].setV((short)sc.nextInt());
                }
            }
        }
        finally{

        }


    }
    @Override
    public String toString(){
        String str;
        str="P3"+"\n"+width+" "+height+"\n";
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                str=str+arrayImage[i][j].getY()+" "+arrayImage[i][j].getU()+" "+arrayImage[i][j].getV()+" ";
            }
            str=str+"\n";
        }

        return(str);
    }
    public void toFile(java.io.File file){
        FileWriter writer=null;
        File archive;
        String str;
        str=toString();
        String filename=file.getAbsolutePath();
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
        YUVImage image=new YUVImage(width,height);

        image.arrayImage=arrayImage.clone();
        Histogram img =new Histogram(image);
        img.equalize();
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                image.arrayImage[i][j].setY(img.getEqualizedLuminocity(arrayImage[i][j].getY()));
            }
        }

        arrayImage=image.arrayImage.clone();
    }

}

