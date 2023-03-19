import tkinter as tk
import matplotlib.pyplot as plt
import random

# Stwórz listę częstotliwości od 900 do 1100 co 25Hz
freq_list = list(range(900, 1125, 25))

# Stwórz listę na poprzednie częstotliwości
prev_freqs = []

# Funkcja do losowego wybierania częstotliwości i aktualizacji wykresu
def freq_hop():
    # Losuj częstotliwość z listy
    freq = random.choice(freq_list)
    # Dodaj wylosowaną częstotliwość do listy poprzednich częstotliwości
    prev_freqs.append(freq)
    # Aktualizuj wykres
    plt.clf() # Wyczyść poprzedni wykres
    plt.xlim(0, len(prev_freqs)+1) # Ustaw zakres osi x
    plt.ylim(900, 1125) # Ustaw zakres osi y
    plt.plot(list(range(len(prev_freqs))), prev_freqs, 'b-') # Dodaj wykres poprzednich częstotliwości
    plt.plot(len(prev_freqs)-1, freq, 'rs') # Dodaj wypełniony kwadrat reprezentujący wylosowaną częstotliwość
    plt.grid(True) # Włącz siatkę
    plt.xlabel('Numer skoku') # Ustaw nazwę osi x
    plt.ylabel('Częstotliwość (Hz)') # Ustaw nazwę osi y
    plt.title('Frequency Hopping') # Ustaw tytuł wykresu
    plt.draw() # Narysuj wykres
    freq_list_var.set("Lista wszystkich częstotliwości: " + str(prev_freqs)) # Ustaw tekst w polu tekstowym

# Stwórz okno GUI
root = tk.Tk()
root.title("Skakanie częstotliwości")

# Stwórz ramkę dla przycisku
button_frame = tk.Frame(root)
button_frame.pack(side="bottom")

# Dodaj przycisk do ramki
button = tk.Button(button_frame, text="Skocz", command=freq_hop)
button.pack(side="top")

# Dodaj pole tekstowe do wyświetlania listy poprzednich częstotliwości
freq_list_var = tk.StringVar()
freq_list_label = tk.Label(root, textvariable=freq_list_var, wraplength=300)
freq_list_label.pack()

# Stwórz wykres
fig, ax = plt.subplots()
plt.xlim(0, 1)
plt.ylim(900, 1125)
plt.grid(True)

# Wyświetl wykres w oknie Matplotlib
plt.show()
