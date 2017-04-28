import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Luis Barbosa on 28/04/2017 from 15h30 to 16h40.
 */
public class NetworkJammer {

    private static void threadFunc(String address, int port) {
        byte[] bytes = new byte[0];
        int offset = 0;

        try {
            InetAddress group = InetAddress.getByName(address);
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, offset, bytes.length, group, port);

            while (true)
                multicastSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        String address;
        int port;
        int time;
        int numThreads;

        if (args.length != 4) {
            System.out.println("Usage: java NetworkJammer <multicast_address> <multicast_port> <how_many_milliseconds> <number_of_threads>\n(0 milliseconds to jam permanently)");

            address = "225.0.0.1";
            port = 2000;
            time = 30000;
            numThreads = 2;
        } else {
            address = args[0];
            port = Integer.parseInt(args[1]);
            time = Integer.parseInt(args[2]);
            numThreads = Integer.parseInt(args[3]);
        }

        StringBuilder stringBuilder = new StringBuilder("Performing with values: ");
        stringBuilder.append(address).append(" ").append(port).append(" ").append(time).append(" ").append(numThreads);
        System.out.println(stringBuilder);

        int delay = 0;
        for (int i = 0; i < numThreads; i++) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    threadFunc(address, port);
                }
            };
            timer.schedule(timerTask, delay);
        }

        if (time != 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }
    }
}
