/*
programmers - AEM:  Lefkopoulou Eleni Maria - 2557
                    Papadouli Vasiliki - 2602
programm : Image Processing
file : YUVPixel
*/
package ce326.hw2;

public class YUVPixel {
     private int YUV;

     public YUVPixel(short Y, short U, short V){
        int color;
        YUV=0;
        color = Y << 16;
        YUV = YUV | color;
        color = U << 8;
        YUV = YUV | color;
        color = V;
        YUV = YUV | color;

     }
     public YUVPixel(YUVPixel pixel){
         this.YUV = pixel.YUV ;
     }
     public YUVPixel(RGBPixel pixel){
        setY( (short)(( (  66 * pixel.getRed() + 129 * pixel.getGreen() +  25 * pixel.getBlue() + 128) >> 8) +  16));
        setU((short)(( ( -38 * pixel.getRed() -  74 * pixel.getGreen() + 112 * pixel.getBlue() + 128) >> 8) + 128));
        setV((short)(( ( 112 * pixel.getRed() -  94 * pixel.getGreen() -  18 * pixel.getBlue() + 128) >> 8) + 128));


     }
    public short getY(){
      short Y;
        int color;
        color=YUV>>16;
        color=color & 0xFF ;
        Y=(short) color;
        return(Y);
    }
    public short getU(){
        short U;
        int color;

        color=YUV<<16;
        color=color>>24;

        color=color & 0xFF ;
        U=(short) color;
        return(U);
    }
    public short getV(){
        short V;
        int color;

        color=YUV<<24;
        color=color>>24;
        color=color & 0xFF ;
        V=(short) color;
        return(V);
    }
     public void setY(short Y){
       int color;
        int V=getV();
        int U=getU();

        YUV=0;
        color=Y;
        color=color<<16;
        YUV=YUV|color;

        U=U<<8;
        YUV=YUV|U;

         YUV=YUV|V;

    }
     public void setU(short U){
        int color;
        int V=getV();

        YUV=YUV>>16;
        YUV=YUV<<16;
        color=U;
        color=color<<8;
        YUV=YUV|color;

        YUV=YUV|V;

    }
    public void setV(short V){
        int color;
        YUV=YUV>>8;
        YUV=YUV<<8;
        color=V;
        YUV=YUV|color;

    }
}
