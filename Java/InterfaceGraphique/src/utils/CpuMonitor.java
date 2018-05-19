package utils;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class CpuMonitor {

    private MBeanServer mbs;
    private ObjectName name;

    public CpuMonitor(){
        this.mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            this.name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    public double getProcessCpuLoad(){
        AttributeList list = null;
        try {
            list = this.mbs.getAttributes(this.name, new String[]{"ProcessCpuLoad"});
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }

        if (list.isEmpty()){
            return Double.NaN;
        }
        Attribute att = (Attribute)list.get(0);
        Double value  = (Double)att.getValue();

        if (value == -1.0) {
            return Double.NaN;
        }
        return ((int)(value * 1000) / 10.0);
    }
}
