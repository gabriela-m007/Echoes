# 🎵 Echoes – Dziennik Koncertowy

**Echoes** to nowoczesna aplikacja mobilna na system Android stworzona dla miłośników muzyki na żywo. Umożliwia prowadzenie osobistego pamiętnika koncertowego, łącząc własne wspomnienia z danymi pobieranymi z zewnętrznych muzycznych API.

---

## ✨ Funkcjonalności

### 🎤 Zapisywanie wspomnień
Dodawaj koncerty, zapisując:
- nazwę zespołu lub artysty,
- datę wydarzenia (z wykorzystaniem wygodnego kalendarza),
- miejsce koncertu,
- własną ocenę w skali 1–5 ⭐,
- osobiste notatki i wspomnienia.

### 🌐 Integracja z Last.fm API
Aplikacja automatycznie pobiera:
- biografię artysty,
- gatunki muzyczne i tagi.

### 🖼️ Integracja z Deezer API
Echoes pobiera oficjalne zdjęcia artystów w wysokiej jakości bezpośrednio z API Deezera.

### 💾 Lokalna baza danych
Wszystkie wpisy są przechowywane lokalnie na urządzeniu dzięki bazie danych Room, co pozwala korzystać z aplikacji również offline.

### 📅 Chronologiczne sortowanie
Koncerty są automatycznie wyświetlane od najnowszych do najstarszych wydarzeń.

### 🎨 Nowoczesny interfejs
Aplikacja oferuje estetyczny i przejrzysty interfejs:
- lawendowa kolorystyka,
- płynne przewijanie,
- czytelne komunikaty błędów,
- komponenty zgodne z Material Design 3.

---

## 🛠️ Technologie i architektura

Projekt został napisany w języku **Kotlin** z wykorzystaniem nowoczesnych narzędzi Androida:

- **Jetpack Compose** – deklaratywne tworzenie interfejsu użytkownika,
- **Room Database** – lokalne przechowywanie danych (SQLite),
- **Retrofit + GSON** – komunikacja z REST API i obsługa JSON,
- **Coroutines + StateFlow** – programowanie asynchroniczne i reaktywny UI,
- **Coil** – wydajne ładowanie obrazów z URL,
- **Material Design 3** – nowoczesne komponenty interfejsu.

---

## 🚀 Uruchomienie projektu

### 1. Sklonuj repozytorium
```bash
git clone https://github.com/gabriela-m007/Echoes.git
```

### 2. Otwórz projekt w Android Studio
Zalecana wersja: **Android Studio Iguana** lub nowsza.

### 3. Zsynchronizuj Gradle
Poczekaj na pobranie wszystkich wymaganych bibliotek i zależności.

### 4. Uruchom aplikację
Włącz emulator Androida lub podłącz fizyczne urządzenie i uruchom projekt.

---

## 🔑 Konfiguracja API

- **Deezer API** działa bez potrzeby używania klucza API.
- Testowy klucz **Last.fm API** jest już zawarty w projekcie.

Dzięki temu aplikacja jest gotowa do działania od razu po uruchomieniu.

---

## 📱 Screenshots


---

## 📄 Licencja
Projekt został stworzony w celach edukacyjnych i portfolio.
