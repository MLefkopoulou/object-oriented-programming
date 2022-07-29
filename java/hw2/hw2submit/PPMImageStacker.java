/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : PPMImageStacker
*/
package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PPMImageStacker{
      List<File> listOfFiles;
      RGBImage finallyImage;
    public PPMImageStacker(java.io.File dir) throws UnsupportedFileFormatException{
         File []array;
         File ppmDir;
         listOfFiles= new ArrayList<>();
         ppmDir = new File(dir.getAbsolutePath());
        try {
            if(!ppmDir.exists()){
                throw new UnsupportedFileFormatException("Directory "+dir.getName()+" does not exist!");
            }
            if(!ppmDir.isDirectory()){
                throw new UnsupportedFileFormatException(dir.getName()+" is not a directory!");
            }
        }
        finally{

            array = new File[(int)ppmDir.length()];
//            for(int i = 0; i< ppmDir.listFiles().length; i ++){
//                array[i]  = ppmDir.listFiles()[i];
//            }
            array = ppmDir.listFiles();
          for(int i =0; i<array.length; i++){
              listOfFiles.add(i,array[i]);

          }

        }


    }
    public void stack() throws UnsupportedFileFormatException, FileNotFoundException{
        PPMImage []arrayPPM;
        arrayPPM = null;
        arrayPPM = new PPMImage[listOfFiles.size()];
        for(int i = 0; i< listOfFiles.size(); i++){
              arrayPPM[i] = new PPMImage(listOfFiles.get(i));
        }
        finallyImage = new RGBImage(arrayPPM[0].getWidth(),arrayPPM[0].getHeight(),arrayPPM[0].getColorDepth());
//        finallyImage.width = arrayPPM[0].getWidth();
//        finallyImage.height = arrayPPM[0].getHeight();
//        finallyImage.colordepth = arrayPPM[0].getColorDepth();
//        finallyImage.arrayImage = new RGBPixel[finallyImage.height][finallyImage.width];
        for(int i = 0; i< listOfFiles.size(); i++){
            for(int row = 0; row< finallyImage.height; row ++){
                for(int col=0; col<finallyImage.width ; col++){
                    finallyImage.arrayImage[row][col].setRGB(finallyImage.arrayImage[row][col].getRGB() + arrayPPM[i].arrayImage[row][col].getRGB());
                   // finallyImage.arrayImage[row][col].setRed((short) (finallyImage.arrayImage[row][col].getRed() + arrayPPM[i].arrayImage[row][col].getRed()));
                     //finallyImage.arrayImage[row][col].setGreen((short) (finallyImage.arrayImage[row][col].getGreen() + arrayPPM[i].arrayImage[row][col].getGreen()));
                      //finallyImage.arrayImage[row][col].setBlue((short) (finallyImage.arrayImage[row][col].getBlue() + arrayPPM[i].arrayImage[row][col].getBlue()));
                }

            }

        }

        for(int row = 0; row< finallyImage.height; row ++){
                for(int col=0; col<finallyImage.width ; col++){
                    finallyImage.arrayImage[row][col].setRGB(finallyImage.arrayImage[row][col].getRGB()/listOfFiles.size());
                    //finallyImage.arrayImage[row][col].setRed((short) (finallyImage.arrayImage[row][col].getRed() /listOfFiles.size()));
                    //finallyImage.arrayImage[row][col].setGreen((short) (finallyImage.arrayImage[row][col].getGreen() /listOfFiles.size()));
                    //finallyImage.arrayImage[row][col].setBlue((short) (finallyImage.arrayImage[row][col].getBlue() /listOfFiles.size()));



                }

            }
    }
    public PPMImage getStackedImage(){
         PPMImage finalPPM = new PPMImage(finallyImage);
         return finalPPM;

    }
}
