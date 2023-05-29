package sym;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import static java.lang.Math.ceil;

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
        this.startingTime = rand.nextInt(9)+1;
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

        Pair[] channels = new Pair[CHANNEL_COUNT];
        for (int i=0 ; i<CHANNEL_COUNT ; i++)channels[i] = new Pair();
        for (int i = 1; i<= numOfPackets; i++)
            Packets.add(new Packet(i));



        int time = 0;
        while (true) {
            System.out.println("Time: " + time + " seconds");

            //check if the packets can send their frames
            for (int i=0 ; i<Packets.size() ; i++) {

                if (Packets.get(i).startingTime == time) {
                    if (!Packets.get(i).error)Packets.get(i).selectedChannel = getRandomChannel();
                    System.out.println("Packet " + Packets.get(i).id + " tries to send frame nr " + (Packets.get(i).completedFrames+1) + " on channel " + Packets.get(i).selectedChannel);
                    if (channels[Packets.get(i).selectedChannel].isEmpty) {
                        Packets.get(i).isSending = true;
                        channels[Packets.get(i).selectedChannel].isEmpty = false;
                        channels[Packets.get(i).selectedChannel].packetId = Packets.get(i).id;
                        Packets.get(i).error = false;
                    } else {
                        Packets.get(i).startingTime += ceil(frameTime/2.0F);
                        System.out.println("Collision on channel " + Packets.get(i).selectedChannel);
                        Packets.get(i).error = true;
                        Packets.get(i).numCollisions++;
                    }
                } else if (Packets.get(i).isSending) {
                   Packets.get(i).time++;
                   if (Packets.get(i).time>=frameTime) {
                       Packets.get(i).time = 0;
                       Packets.get(i).completedFrames++;
                       channels[Packets.get(i).selectedChannel].isEmpty = true;
                       System.out.println("Packet " + Packets.get(i).id + " finished sending frame nr " + Packets.get(i).completedFrames);
                       if (Packets.get(i).completedFrames == Packets.get(i).framesToSend) {
                           Packets.get(i).isFinished = true;
                           Packets.get(i).completionTime = time;
                           Packets.get(i).isSending = false;
                       } else {
                           Packets.get(i).startingTime = time + 1;
                       }

                   }
                }
            }

            int pom = 0;
            for (int i=0 ; i<Packets.size() ; i++) {
                if (Packets.get(i).isFinished)pom++;
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

            for (int i=0 ; i<Packets.size() ; i++) {
                file.write(
                        "Packet id: " + Packets.get(i).id +
                                "\nNumber of frames in the packet: " + Packets.get(i).framesToSend +
                              "\nTotal time of sending everything: " + (Packets.get(i).completionTime-Packets.get(i).beginningTime) +
                              "\nNumber of collisions: " + Packets.get(i).numCollisions + "\n\n"
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
