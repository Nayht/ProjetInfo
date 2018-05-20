package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CpuMonitor {

    private int nbPasses;
    private int nbPassesBeforeUpdate;
    private double[] splitValuesLastChecked;
    private File file;

    public CpuMonitor(){
        this.nbPasses=0;
        this.nbPassesBeforeUpdate=50;
        this.splitValuesLastChecked=new double[20];
        this.file=new File("/proc/stat");
    }

    public double getCpuLoad(){
        this.nbPasses+=1;
        BufferedReader reader;
        String firstLine="";
        try {
            reader = new BufferedReader(new FileReader(this.file));
            firstLine = reader.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
        if (this.nbPasses>this.nbPassesBeforeUpdate) {
            String[] split = firstLine.split(" ");

            double hundredPercent=0;
            double idle=0;
            for (int i=2; i<split.length; i++){
                hundredPercent+=Double.parseDouble(split[i])-this.splitValuesLastChecked[i];
                if (i==5){
                    idle=Double.parseDouble(split[5])-this.splitValuesLastChecked[5];
                }
                this.splitValuesLastChecked[i]=Double.parseDouble(split[i]);
            }
            double cpuLoad=1-(idle/hundredPercent);

            this.nbPasses=0;
            return Math.round(cpuLoad*100);
        }
        else{
            return -1;
        }
    }
}
