/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : RGBImage
*/
package ce326.hw2;

public class RGBImage implements ce326.hw2.Image{
    int width;
    int height;
    int colorDepth;
    RGBPixel [][]arrayImage;
    public static final int MAX_COLORDEPTH=255;

    public RGBImage(int width,int height,int colorDepth) {
        this.height=height;
        this.width=width;
        this.colorDepth=colorDepth;
        arrayImage=new RGBPixel[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }

    }
    public RGBImage(RGBImage copyImg){
        this.width=copyImg.getWidth();
        this.height=copyImg.getHeight();
        this.colorDepth=copyImg.getColorDepth();
        arrayImage=new RGBPixel[height][width];
         for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                arrayImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                setPixel(i,j,copyImg.getPixel(i,j));
            }
        }
    }
    public RGBImage(YUVImage YUVImg){
        width=YUVImg.width;
        height=YUVImg.height;
        RGBPixel pixel;
        arrayImage=new RGBPixel[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){

                arrayImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                pixel=new RGBPixel(YUVImg.arrayImage[i][j]);
                arrayImage[i][j]=pixel;
            }
        }
    }
    public int getWidth(){
        return(width);
    }
    public int getHeight(){
        return(height);
    }
    public int getColorDepth(){
        return(colorDepth);
    }
    RGBPixel getPixel(int row, int col){
        return(arrayImage[row][col]);
    }
    void setPixel(int row, int col,RGBPixel pixel){

        arrayImage[row][col].setRGB(pixel.getRGB());
    }


    @Override
    public void grayscale() {
        int grey;
        short red,green,blue;
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                blue=(short) (0.11*arrayImage[i][j].getBlue());
                red=(short) (0.3*arrayImage[i][j].getRed());
                green=(short) (0.57*arrayImage[i][j].getGreen());
                grey=blue+red+green;
                arrayImage[i][j].setBlue((short) grey);
                arrayImage[i][j].setRed((short) grey);
                arrayImage[i][j].setGreen((short) grey);

            }
        }
    }

    @Override
    public void doublesize() {
        int newHeight=2*height;
        int newWidth=2*width;
        RGBPixel [][]newImage=new RGBPixel[newHeight][newWidth];
         for(int i=0;i<newHeight;i++){
            for(int j=0;j<newWidth;j++){
                newImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                newImage[2*i][2*j]=arrayImage[i][j];
                newImage[2*i+1][2*j]=arrayImage[i][j];
                newImage[2*i][2*j+1]=arrayImage[i][j];
                newImage[2*i+1][2*j+1]=arrayImage[i][j];
            }
        }
        height=newHeight;
        width=newWidth;
        arrayImage=newImage.clone();

    }

    @Override
    public void halfsize() {
        int newHeight=height/2;
        int newWidth=width/2;
        short blue ,red,green;
        RGBPixel [][]newImage=new RGBPixel[newHeight][newWidth];
        for(int i=0;i<newHeight;i++){
            for(int j=0;j<newWidth;j++){
                newImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<newHeight;i++){
            for(int j=0;j<newWidth;j++){
                blue=(short) ((arrayImage[2*i][2*j].getBlue()+arrayImage[2*i+1][2*j].getBlue()+arrayImage[2*i][2*j+1].getBlue()+arrayImage[2*i+1][2*j+1].getBlue())/4);

                newImage[i][j].setBlue(blue);

                red=(short) ((arrayImage[2*i][2*j].getRed()+arrayImage[2*i+1][2*j].getRed()+arrayImage[2*i][2*j+1].getRed()+arrayImage[2*i+1][2*j+1].getRed())/4);
                newImage[i][j].setRed(red);

                green=(short) ((arrayImage[2*i][2*j].getGreen()+arrayImage[2*i+1][2*j].getGreen()+arrayImage[2*i][2*j+1].getGreen()+arrayImage[2*i+1][2*j+1].getGreen())/4);
                newImage[i][j].setGreen(green);

            }
        }
        height=newHeight;
        width=newWidth;
        arrayImage=newImage.clone();

    }

    @Override
    public void rotateClockwise() {
        int newHeight=width;
        int newWidth=height;
        RGBPixel [][]newImage=new RGBPixel[newHeight][newWidth];
         for(int i=0;i<newHeight;i++){
            for(int j=0;j<newWidth;j++){
                newImage[i][j]=new RGBPixel((short)0,(short)0,(short)0);
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                newImage[j][newWidth-(i+1)]=arrayImage[i][j];
            }
        }
        height=newHeight;
        width=newWidth;
        arrayImage=newImage.clone();
    }

}

