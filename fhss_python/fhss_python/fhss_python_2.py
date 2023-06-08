import random
import time
import math

class Pair2:
    def __init__(self):
        self.isEmpty = True
        self.packetId = 0

class Packet:
    def __init__(self, id):
        self.id = id
        self.framesToSend = random.randint(2, 5)
        self.completedFrames = 0
        self.beginningTime = random.randint(1, 59)
        self.startingTime = self.beginningTime
        self.completionTime = 0
        self.numCollisions = 0
        self.selectedChannel = 0
        self.time = 0
        self.isSending = False
        self.isFinished = False
        self.error = False

class Main:
    CHANNEL_COUNT = 0
    frameTime = 0

    @staticmethod
    def main():
        Packets = []

        print("Choose the time needed to send a frame (in seconds): ")
        Main.frameTime = int(input())
        print("Choose the number of channels: ")
        Main.CHANNEL_COUNT = int(input())
        print("Choose the number of packets: ")
        numOfPackets = int(input())

        channels = [Pair2() for _ in range(Main.CHANNEL_COUNT)]
        for i in range(1, numOfPackets + 1):
            Packets.append(Packet(i))

        time = 0
        while True:
            print(f"Time: {time} seconds")

            for packet in Packets:
                if packet.startingTime == time:
                    if not packet.error:
                        packet.selectedChannel = Main.get_random_channel()
                    print(f"Packet {packet.id} tries to send frame nr {packet.completedFrames + 1} on channel {packet.selectedChannel}")
                    if channels[packet.selectedChannel].isEmpty:
                        packet.isSending = True
                        channels[packet.selectedChannel].isEmpty = False
                        channels[packet.selectedChannel].packetId = packet.id
                        packet.error = False
                    else:
                        packet.startingTime += math.ceil(Main.frameTime / 2.0)
                        print(f"Collision on channel {packet.selectedChannel}")
                        packet.error = True
                        packet.numCollisions += 1
                elif packet.isSending:
                    packet.time += 1
                    if packet.time >= Main.frameTime:
                        packet.time = 0
                        packet.completedFrames += 1
                        channels[packet.selectedChannel].isEmpty = True
                        print(f"Packet {packet.id} finished sending frame nr {packet.completedFrames}")
                        if packet.completedFrames == packet.framesToSend:
                            packet.isFinished = True
                            packet.completionTime = time
                            packet.isSending = False
                        else:
                            packet.startingTime = time + 1

            pom = sum(packet.isFinished for packet in Packets)
            if pom == numOfPackets:
                break

            Main.wait(1)
            time += 1

        try:
            with open("output.txt", "w") as file:
                file.write(
                    "SIMULATION SUCCESSFUL\n" +
                    f"\nChosen number of time needed to send a frame: {Main.frameTime}" +
                    f"\nChosen number of channels: {Main.CHANNEL_COUNT}" +
                    f"\nChosen number of packets: {numOfPackets}" +
                    f"\n\nTotal time of the simulation: {time} seconds" +
                    "\n\nData from each packet:\n\n"
                )

                for packet in Packets:
                    file.write(
                        f"Packet id: {packet.id}" +
                        f"\nNumber of frames in the packet: {packet.framesToSend}" +
                        f"\nTotal time of sending everything: {packet.completionTime - packet.beginningTime}" +
                        f"\nNumber of collisions: {packet.numCollisions}\n\n"
                    )

            print("Successfully wrote to the file.")
        except IOError:
            print("An error occurred.")

    @staticmethod
    def get_random_channel():
        return random.randint(0, Main.CHANNEL_COUNT - 1)

    @staticmethod
    def wait(x):
        time.sleep(1 * x)

Main.main()
