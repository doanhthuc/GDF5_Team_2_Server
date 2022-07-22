package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;

public class Bida extends JFrame {
    int w=800;
    int h=400;
    int l=50;
    int r=10;
    int n=30;
    static Ball Bl[] = new Ball[10000];
    BufferedImage B;
    Graphics G;
    public Bida()
    {
        Random rand= new Random();
        this.setTitle("BIDA");
        this.setSize(w+l*2, h+l*2);
        this.setDefaultCloseOperation(3);
        for(int i=0;i<n;i++)
        {
            double x= rand.nextDouble()*(w-2*r)+r+l;
            double y= rand.nextDouble()*(h-2*r)+r+l;
            double vx= rand.nextDouble()-0.5;
            double vy= rand.nextDouble()-0.5;
            Bl[i]=new Ball(x,y,vx,vy,r);
            Bl[i].start();
        }
        B = new BufferedImage(w+l*2, h+l*2,BufferedImage.TYPE_3BYTE_BGR);
        G = B.getGraphics();
        this.setVisible(true);
    }
    public static void main(String[] args) {
        new Bida();
    }
    public void paint(Graphics G1)
    {
        G.setColor(Color.WHITE);
        G.fillRect(0, 0,this.getWidth(),this.getHeight());
        G.setColor(Color.BLACK);
        G.fillRect(l,l,w,h);
        G.setColor(Color.RED);
        G.drawRect(l,l,w,h);
        for(int i=0;i<n-1;i++)
            for(int j=i+1;j<n;j++)
            {
                double x1=Bl[i].x;
                double y1=Bl[i].y;
                double x2=Bl[j].x;
                double y2=Bl[j].y;
                if (Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<=2*r)
                {
                    double temp=Bl[i].vx;
                    Bl[i].vx=Bl[j].vx;
                    Bl[j].vx=temp;
                    temp=Bl[i].vy;
                    Bl[i].vy=Bl[j].vy;
                    Bl[j].vy=temp;
                    Bl[i].x+=Bl[i].vx;
                    Bl[i].y+=Bl[i].vy;
                    Bl[j].x+=Bl[j].vx;
                    Bl[j].y+=Bl[j].vy;
                }
            }
        for (int i=0;i<n;i++)
        {
            double x= Bl[i].x;
            double y= Bl[i].y;
            G.setColor(Color.GREEN);
            G.drawOval((int)x-r,(int)y-r,(int)r*2,(int)r*2);
            G.setColor(Color.YELLOW);
            G.fillOval((int)x-r,(int)y-r,(int)r*2,(int)r*2);
        }
        G1.drawImage(B, 0, 0, this.getWidth(),this.getHeight(),null);
        this.repaint();
    }


}
class Ball extends Thread{
    double x,y,vx,vy;
    double r;
    static int w=800;
    static int h=400;
    static int l=50;
    static double a=0.001;
    public Ball(double x,double y,double vx,double vy,double r)
    {
        this.x=x;
        this.y=y;
        this.vx=vx;
        this.vy=vy;
        this.r=r;
    }
    public void run()
    {
        while(true)
        {this.x+=this.vx;
            this.y+=this.vy;
            this.vx+=this.vx*(-a);
            this.vy+=this.vy*(-a);
            if ((this.x<=r+l)|| (this.x+this.r)>=w+l) vx*=-1;
            if ((this.y<=r+l)|| (this.y+this.r)>=h+l) vy*=-1;
            try {
                Thread.sleep(7);
            } catch (InterruptedException e){};}
    }
}
