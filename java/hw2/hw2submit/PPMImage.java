/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : PPMImage
*/
package ce326.hw2;
import java.io.*;
import java.util.*;
import java.io.FileWriter;

public class PPMImage extends RGBImage{

    public PPMImage(java.io.File file) throws UnsupportedFileFormatException,FileNotFoundException{
        super(0,0,0);
        Scanner sc;

        File PPMfile;
        String filename=file.getAbsolutePath();
        PPMfile = new File (filename);
        String name;
        int numi;
        RGBPixel pixel;
        pixel = new RGBPixel((short)0,(short)0,(short)0);

        short num;


        try{


            sc = new Scanner(PPMfile);
            name=sc.next();
            super.width=sc.nextInt();
            super.height=sc.nextInt();
            super.colorDepth=sc.nextInt();
            arrayImage=new RGBPixel[height][width];


             for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    arrayImage[i][j]=new RGBPixel(pixel);
                }
            }

            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    num=sc.nextShort();

                    this.arrayImage[i][j].setRed(num);
                    this.arrayImage[i][j].setGreen(sc.nextShort());
                    this.arrayImage[i][j].setBlue(sc.nextShort());
                }

            }

            if(!filename.endsWith(".ppm")){
                throw new UnsupportedFileFormatException();
            }
        }
        finally{


        }


    }

    public PPMImage(RGBImage img){
        super(img);
    }
    public PPMImage(YUVImage img){
        super(img);
    }


    @Override
    public String toString(){
        String str;
        colorDepth = 255;
        str="P3"+"\n"+width+" "+height+"\n"+colorDepth+"\n";
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                str=str+arrayImage[i][j].getRed()+" "+arrayImage[i][j].getGreen()+" "+arrayImage[i][j].getBlue()+" ";
            }
            str=str+"\n";
        }
        return(str);
    }

    public void toFile(java.io.File file){
        FileWriter writer = null;
        File archive;
        String str;
        str=toString();

            try{
                if(file.exists()){
                    if(file.length()>0){
                        writer=new FileWriter(file.getAbsolutePath(),false);

                    }
                    writer=new FileWriter(file);
                    writer.write(str);
                }
                else{
                    archive=new File(file.getAbsolutePath());
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

}
