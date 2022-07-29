/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : RGBPixel
*/
package ce326.hw2;

public class RGBPixel {

    private int RGB;

    public RGBPixel(short red, short green, short blue) {
        int color;
        RGB=0;
        color=red;
        color= color << 16;
        RGB=RGB|color;
        color=green;
        color=color<<8;
        RGB=RGB|color;
        color=blue;
        RGB=RGB|color;
    }
    public RGBPixel(RGBPixel pixel){

        this.RGB=pixel.RGB;
    }

    public RGBPixel(YUVPixel pixel){

        setRed(clip(( 298 * (pixel.getY() - 16)          + 409 * (pixel.getV() - 128) + 128) >> 8));
        setGreen(clip(( 298 * (pixel.getY() - 16) - 100 * (pixel.getU() - 128) - 208 * (pixel.getV() - 128) + 128) >> 8));
        setBlue(clip(( 298 * (pixel.getY() - 16) + 516 * (pixel.getU() - 128)          + 128) >> 8));
    }

    public short getRed(){
        short red;
        int color;
        color=RGB>>16;
        color=color & 0xFF ;
        red=(short) color;
        return(red);

    }
    public short getGreen(){
        short green;
        int color;

        color=RGB<<16;
        color=color>>24;

        color=color & 0xFF ;
        green=(short) color;
        return(green);

    }
    public short getBlue(){
        short blue;
        int color;

        color=RGB<<24;
        color=color>>24;
        color=color & 0xFF ;
        blue=(short) color;
        return(blue);


    }
    public void setRed(short red){
        int color;
        int blue=getBlue();
        int green=getGreen();

        RGB=0;
        color=red;
        color=color<<16;
        RGB=RGB|color;

        green=green<<8;
        RGB=RGB|green;

        RGB=RGB|blue;

    }
    public void setGreen(short green){
        int color;
        int blue;
        blue=getBlue();

        RGB=RGB>>16;
        RGB=RGB<<16;
        color=green;
        color=color<<8;
        RGB=RGB|color;

        RGB=RGB|blue;


    }
    public void setBlue(short blue){
        int color;
        RGB=RGB>>8;
        RGB=RGB<<8;
        color=blue;
        RGB=RGB|color;

    }
    public int getRGB(){
        return(RGB);

    }
    public void setRGB(int value){
        RGB=value;

    }
    public void setRGB(short red, short green, short blue){
        setRed(red);
        setBlue(blue);
        setGreen(green);
    }
    @Override
    public String toString(){

        return(getBlue()+" "+getGreen()+" "+getRed()+"\n");
    }

    private short clip(int i) {
        if(i<0){
            return 0;
        }
        if(i>255){
            return 255;
        }
        return (short) i;
    }

}
