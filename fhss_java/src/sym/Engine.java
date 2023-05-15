package sym;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Engine {

    public static void main(String[] args)
    {
        Random rd = new Random();
        Scanner scan = new Scanner(System.in);

        ArrayList<Frame> Frames = new ArrayList<>();
        int numOfCollisions = 0;
        int timeToFinish = 0;
        int finishedFrames = 0;
        int neededHops;
        int length = 1000;
        boolean finished = false;

        System.out.println("Choose the number of channels: ");
        int numChannels = scan.nextInt();

        System.out.println("Choose the minimum number of hops: ");
        int numHops = scan.nextInt();

        System.out.println("Choose the number of transmitters/receivers: ");
        int numTr = scan.nextInt();
        for (int i=0 ; i<numTr ; i++)
        {
            Frames.add(new Frame(rd.nextInt(numChannels)));
        }

        System.out.println("Choose the size of the frames: ");
        int size = scan.nextInt();
        neededHops = length / size;


        while (!finished)
        {
            timeToFinish++;

            for(int i=0 ; i<Frames.size() ; i++)
            {
              if (!Frames.get(i).isFinished)    //check if the frame has reached the receiver
              {
                  int pom = (Frames.get(i).channel += (rd.nextInt(numChannels)+numHops))%numChannels;
                  Frames.get(i).channel = pom;
                  Frames.get(i).Path.add(new Pair(timeToFinish,pom));
                  if (i == 0)
                  {
                      Frames.get(i).hops++;
                      if (Frames.get(i).hops >= neededHops) //check if finished
                      {
                          Frames.get(i).isFinished = true;
                          finishedFrames++;
                      }
                  }
                  else {
                      for (int j = 0; j < i; j++) {
                          if (Frames.get(i).channel == Frames.get(j).channel && !Frames.get(j).isFinished)   //check for collisions
                          {
                              Frames.get(i).hops = 0;
                              Frames.get(i).Path.clear();
                              Frames.get(j).hops = 0;
                              Frames.get(j).Path.clear();
                              numOfCollisions++;
                          } else if (j==(i-1)){
                              Frames.get(i).hops++;
                              if (Frames.get(i).hops >= neededHops && Frames.get(i).Path.size() >= neededHops) //check if finished
                              {
                                  Frames.get(i).isFinished = true;
                                  finishedFrames++;
                              }
                          }
                      }
                  }


              } //end of checking the frame


            }//end of the iteration

            if (finishedFrames == numTr)finished = true;
        }

        //creating the file
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
                            "\nChosen number of channels: " + numChannels +
                            "\nChosen number of minimum hops: " + numHops +
                            "\nChosen number of transmitters/receivers: " + numTr +
                            "\nChosen size of the frame: " + size +
                            "\n\nNumber of collisions: " + numOfCollisions +
                            "\nTime to send every frame: " + timeToFinish +
                            "\n\nFinal paths of every frame:\n(unit of time ||| channel)\n"
            );

            for (int i=0 ; i<Frames.size() ; i++)
            {
                file.write(
                        "\nFrame number " + i + " :\n"
                );
                for (int j=0 ; j<neededHops ; j++)
                    file.write(
                            Frames.get(i).Path.get(j).time + " ||| " + Frames.get(i).Path.get(j).place + "\n"
                    );
            }


            file.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
