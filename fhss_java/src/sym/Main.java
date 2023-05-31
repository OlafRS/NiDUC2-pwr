package sym;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import static java.lang.Math.ceil;

class Pair2 {

    boolean isEmpty;
    int packetId;

    public Pair2() {
        this.isEmpty= true;
        this.packetId = 0;
    }
}

class Packet {
    public int id;
    public int framesToSend;
    public int completedFrames;
    public int beginningTime;
    public int startingTime;
    public int completionTime;
    public int numCollisions;
    public int selectedChannel;
    public int time;
    public boolean isSending;
    public boolean isFinished;
    public boolean error;

    public Packet(int id) {
        Random rand = new Random();
        this.framesToSend = rand.nextInt(4) + 2; // Random number between 2 and 5
        this.startingTime = rand.nextInt(59)+1;
        this.beginningTime = this.startingTime;
        this.id = id;
        this.numCollisions = 0;
        this.isSending = false;
        this.completedFrames = 0;
        this.isFinished = false;
        this.error = false;
        this.time = 0;
    }
}

public class Main {
    private static int CHANNEL_COUNT;
    public static int frameTime;


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Packet> Packets = new ArrayList<>();

        System.out.println("Choose the time needed to send a frame (in seconds): ");
        frameTime = scan.nextInt();
        System.out.println("Choose the number of channels: ");
        CHANNEL_COUNT = scan.nextInt();
        System.out.println("Choose the number of packets: ");
        int numOfPackets = scan.nextInt();

        Pair2[] channels = new Pair2[CHANNEL_COUNT];
        for (int i=0 ; i<CHANNEL_COUNT ; i++)channels[i] = new Pair2();
        for (int i = 1; i<= numOfPackets; i++)
            Packets.add(new Packet(i));



        int time = 0;
        while (true) {
            System.out.println("Time: " + time + " seconds");

            //check if the packets can send their frames
            for (Packet packet : Packets) {

                if (packet.startingTime == time) {
                    if (!packet.error) packet.selectedChannel = getRandomChannel();
                    System.out.println("Packet " + packet.id + " tries to send frame nr " + (packet.completedFrames + 1) + " on channel " + packet.selectedChannel);
                    if (channels[packet.selectedChannel].isEmpty) {
                        packet.isSending = true;
                        channels[packet.selectedChannel].isEmpty = false;
                        channels[packet.selectedChannel].packetId = packet.id;
                        packet.error = false;
                    } else {
                        packet.startingTime += ceil(frameTime / 2.0F);
                        System.out.println("Collision on channel " + packet.selectedChannel);
                        packet.error = true;
                        packet.numCollisions++;
                    }
                } else if (packet.isSending) {
                    packet.time++;
                    if (packet.time >= frameTime) {
                        packet.time = 0;
                        packet.completedFrames++;
                        channels[packet.selectedChannel].isEmpty = true;
                        System.out.println("Packet " + packet.id + " finished sending frame nr " + packet.completedFrames);
                        if (packet.completedFrames == packet.framesToSend) {
                            packet.isFinished = true;
                            packet.completionTime = time;
                            packet.isSending = false;
                        } else {
                            packet.startingTime = time + 1;
                        }

                    }
                }
            }

            int pom = 0;
            for (Packet packet : Packets) {
                if (packet.isFinished) pom++;
            }
            if (pom==numOfPackets)break;


            // Wait for 1 second
            wait(1);
            time++;
        }//this is where the engine finishes running

        //creating a file
        try {
            File fileCr = new File("output.txt");
            if (fileCr.createNewFile()) {
                System.out.println("File created: " + fileCr.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //writing to the file
        try {
            FileWriter file = new FileWriter("output.txt");

            file.write(
                    "SIMULATION SUCCESSFUL\n" +
                            "\nChosen number of time needed to send a frame: " + frameTime +
                            "\nChosen number of channels: " + CHANNEL_COUNT +
                            "\nChosen number of packets: " + numOfPackets +
                            "\n\nTotal time of the simulation: " + time + " seconds" +
                            "\n\nData from each packet:\n\n"
            );

            for (Packet packet : Packets) {
                file.write(
                        "Packet id: " + packet.id +
                                "\nNumber of frames in the packet: " + packet.framesToSend +
                                "\nTotal time of sending everything: " + (packet.completionTime - packet.beginningTime) +
                                "\nNumber of collisions: " + packet.numCollisions + "\n\n"
                );
            }

            file.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private static int getRandomChannel() {
        Random rand = new Random();
        return rand.nextInt(CHANNEL_COUNT);
    }

    private static void wait(int x) {
        try {
            Thread.sleep(1000L *x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
