package ForceGraphLayout;
import java.util.*;
public class FRAlgorithm {
    private Graph graph;
    private double area;
    private double koeficient;
    private double temperature;
    private int Iterations;
    private double constant;
    private double minMovementInt;
    private int width;
    private int height;
    private Random random;
    private boolean runProg;
    private double coolingRate;
    private double maxDispl=0;
    public FRAlgorithm(Graph graph,int iterations,int width,int height,int seed){
        this.random=new Random(seed);
        this.width=width;
        this.height=height;
        this.constant=seed;
        this.graph=graph;
        this.Iterations=iterations;
        this.area=width*height;
        this.koeficient=(Math.sqrt(area/graph.getNodes().size()));
        this.temperature= (double) Math.min(width, height) /100;
        this.coolingRate=0.98;
        //this.temperature=0.99;
        this.minMovementInt=0.1;
        this.runProg=false;
    }
    public boolean run(){
            Logger.log("I calculating Repulsive F",LogLevel.Info);

            calculateRepulsiveForces();
            Logger.log("I Calculating attractive F",LogLevel.Info);
            calculateAttractiveForces();

            Logger.log("I am cooling",LogLevel.Debug);
            double maxUpdate=updatePosition();
            cooling();



            Logger.log("I am updating pos",LogLevel.Debug);

        return maxUpdate>=minMovementInt&&temperature>=0.0000000001;


    }
    private void calculateRepulsiveForces(){
        for(Node v:graph.getNodes()){
            v.dispX=0;
            v.dispY=0;
            for(Node u:graph.getNodes()){
                if(u!=v){
                    double dx=v.x-u.x;
                    double dy=v.y-u.y;
                    double distance=Math.sqrt(dx * dx + dy * dy);
                    if(distance==0) distance=0.01;
                    if(distance>0) {
                        v.dispX += (dx / distance) * forceRep(distance);
                        v.dispY += (dy / distance) * forceRep(distance);
                    }
                }
            }
        }
    }
    private void calculateAttractiveForces(){
        for(Edge edge : graph.getEdgesUsed()){
            double dx=edge.start.x-edge.end.x;
            double dy=edge.start.y-edge.end.y;
            double distance=Math.sqrt(dx * dx + dy * dy);
                if(distance==0) distance=0.01;
            if(distance>0) {
                edge.start.dispX -= (dx / distance) * forceAttractive(distance);
                edge.start.dispY -= (dy / distance) * forceAttractive(distance);
                edge.end.dispX += (dx / distance) * forceAttractive(distance);
                edge.end.dispY += (dy / distance) * forceAttractive(distance);
            }
        }
    }
    private double updatePosition(){

        for (Node v:graph.getNodes()){
            double display= Math.sqrt(v.dispX * v.dispX + v.dispY * v.dispY);
            maxDispl=Math.max(maxDispl,display);
            //if (display==0) display=0.01;
            if(display > 0) {
                v.x += (v.dispX / display) * Math.min(display, temperature);
                v.y += (v.dispY / display) * Math.min(display, temperature);
            }
            //v.x = Math.max(10,Math.min(v.x,width-10));
            //v.y = Math.min((double) height / 2, Math.max(((double) (height * -1) / 2), v.y));
            //v.y=Math.max(10,Math.min(v.x,width-10));
            v.x=Math.max(1,Math.min(v.x,width-20));
            v.y=Math.max(1,Math.min(v.y,height-30));
        }
        return maxDispl;
    }
    private void cooling(){
        //temperature-=Math.max(temperature*0.95,0);
        temperature=Math.max(temperature*coolingRate,0);
        Logger.log("Temperature == "+temperature,LogLevel.Debug);
    }
    private double forceRep(double dist){
        return (koeficient * koeficient)/dist;
    }
    private double forceAttractive(double dist){
        return (dist * dist)/koeficient;
    }
}
