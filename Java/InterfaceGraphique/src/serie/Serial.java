package serie;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class Serial
{
    public static boolean flag;
    SerialPort serialPort;
    public InputStream serialInStream;
    static String inputSerial;

    public Serial()
    {
        super();
        try {
            this.serialInStream = this.connect("/dev/ttyACM0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        serialPort.close();
    }


    InputStream connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
            return null;
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                this.serialPort = (SerialPort) commPort;
                this.serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = this.serialPort.getInputStream();

                return in;
            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
                return null;
            }
        }
    }


    public InputStream getSerialInStream() {
        return serialInStream;
    }

    public static class SerialReader implements Runnable
    {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run ()
        {
            try
            {
                while (!flag) {
                    int current = -1;
                    String message = "";
                    while (!flag && (current = this.in.read()) != '\n') {
                        message += (char) current;
                    }
                    inputSerial =  message.trim();
                    System.out.println(message);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /*public static class SerialWriter implements Runnable
    {
        OutputStream out;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }*/

    public String readMessage() {
        /*try
        {
            //(new Serial()).connect("/dev/ttyUSB0");
            int current = -1;
            String message = "";
            System.out.println("cacamou");
            if (serialInStream.read() != -1) {
                System.out.println("zizimou");
                while ((current = serialInStream.read()) != '\n') {
                    message += (char) current;
                }
                return message.trim();
            }
            return null;
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }*/
        /*Thread reader = new Thread(new SerialReader(serialInStream));
        reader.start();*/
        return this.inputSerial;
    }

    public void resetMessage() {
        this.inputSerial = null;
    }
}