import numpy as np
import itertools
import matplotlib.pyplot as plt
import multiprocessing
import tkinter as tk
import threading

class WifiSimulation:
    def __init__(self):
        self.current_frequency = None
        self.frequencies = itertools.cycle([1, 10, 2, 7])
        self.host = None
        self.bits = None
        self.pseudo_random_sequence = None
        self.stop_event = threading.Event()
        
    def start_simulation(self):
        self.current_frequency = next(self.frequencies)
        self.pseudo_random_sequence = self.generate_pseudo_random_sequence()
        self.stop_event.clear()
        self.start_process()
        
    def generate_pseudo_random_sequence(self):
        np.random.seed(42) # Seed for reproducibility
        return np.random.randint(0, 2, size=1000)
        
    def start_process(self):
        p = multiprocessing.Process(target=self.simulation_process)
        # p.start()
        
    def simulation_process(self):
        while not self.stop_event.is_set():
            # Perform simulation here
            # ...
            # Update frequency and other variables as needed
            self.current_frequency = next(self.frequencies)
        
    def stop_simulation(self):
        self.stop_event.set()
        
class Application:
    def __init__(self, master):
        self.master = master
        self.wifi_simulation = WifiSimulation()
        
        # Create GUI elements
        self.freq_label = tk.Label(master, text="Frequency: ")
        self.freq_label.pack()
        
        self.freq_display = tk.Label(master, text="")
        self.freq_display.pack()
        
        self.graph_canvas = tk.Canvas(master, width=500, height=200, bg="white")
        self.graph_canvas.pack()
        
        self.next_button = tk.Button(master, text="Next Frequency", command=self.next_frequency)
        self.next_button.pack()
        
        self.start_button = tk.Button(master, text="Start Simulation", command=self.start_simulation)
        self.start_button.pack()
        
        self.stop_button = tk.Button(master, text="Stop Simulation", command=self.stop_simulation, state="disabled")
        self.stop_button.pack()
        
    def start_simulation(self):
        self.start_button.config(state="disabled")
        self.stop_button.config(state="normal")
        self.wifi_simulation.start_simulation()
        self.update_gui()
        
    def stop_simulation(self):
        self.start_button.config(state="normal")
        self.stop_button.config(state="disabled")
        self.wifi_simulation.stop_simulation()
        
    def next_frequency(self):
        self.wifi_simulation.current_frequency = next(self.wifi_simulation.frequencies)
        self.update_gui()
        
    def update_gui(self):
        self.freq_display.config(text=str(self.wifi_simulation.current_frequency) + " MHz")
        # Update graph with sinusoid at current frequency
        t = np.arange(0.0, 1.0, 0.0001)
        s = np.sin(2*np.pi*self.wifi_simulation.current_frequency*t)
        self.graph_canvas.delete("all")
        self.graph_canvas.create_line(0, 100, 500, 100, width=2, fill='gray')
        for i in range(1, 5):
            self.graph_canvas.create_line(0, i*40, 500, i*40, width=1, fill='lightgray')
        self.graph_canvas.create_line(0, 100 - 40*np.sin(0), 1, 100 - 40*np.sin(0), width=2, fill='red')
        for i in range(1, len(s)):
            self.graph_canvas.create_line(i*500/len(s), 100 - 40*np.sin(s[i-1]), (i+1)*500/len(s), 100 - 40*np.sin(s[i]), width=2, fill='red')
        self.master.after(1000, self.update_gui) # Update every second
root = tk.Tk()
app = Application(root)
root.mainloop()