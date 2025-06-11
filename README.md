# 📱 PingNest

**PingNest** is a modern real-time chat application built using **Kotlin** and **Jetpack Compose**. Designed with performance and adaptability in mind, it features seamless messaging via **WebSockets**, supports **STOMP**, and utilizes **adaptive navigation** for responsive UI across different device sizes.

---

## 🚀 Features

- 💬 Real-time messaging using **WebSockets**
- 🌐 HTTP and STOMP support via **Ktor**
- ✨ **Jetpack Compose** UI with **adaptive list-detail navigation**
- ⚖️ Clean **MVVM architecture**
- 🔄 State persistence with **DataStore**
- 🧠 **Koin** for dependency injection
- 📶 Fully reactive with **StateFlow** and **Composable** functions
- 📱 Tablet and phone friendly with **Navigation Pane** split views

---

## 💠 Tech Stack

| Layer                | Technology                         |
|---------------------|-------------------------------------|
| Language            | Kotlin                              |
| UI                  | Jetpack Compose                     |
| Architecture        | MVVM                                |
| Networking          | Ktor (HTTP, WebSocket, STOMP)       |
| Dependency Injection| Koin                                |
| State Management    | StateFlow, ViewModel, DataStore     |
| Navigation          | Adaptive Navigation Pane            |

---

## 📀 Adaptive UI

PingNest leverages Jetpack Compose's **Navigation Pane** to support both:

- 📃 **List View (Single-pane)** on smaller devices
- 🪟 **List + Detail View (Two-pane)** on larger screens/tablets

This makes the user experience fluid and intuitive regardless of screen size.

---

## 🔌 WebSocket & STOMP Integration

PingNest supports:

- **Standard WebSocket connections** for real-time messaging.
- **STOMP protocol** support for pub/sub style communication.
- Error handling and reconnection logic included for robust performance.

---

## 🧹 Architecture Overview

```
View (Compose UI)
   ↓
ViewModel (MVVM)
   ↓
Repository (Handles data)
   ↓
Ktor Client (HTTP/WS/STOMP)
```

- **ViewModel** handles business logic and exposes state to the UI.
- **Repository** abstracts data operations (network, local storage).
- **Ktor Client** manages all networking including WebSocket/STOMP communication.

---

## 📂 Project Modules

```
- ui/
  - screens/
  - components/
  - viewmodel/
-data/
  - repository/
  - network/
    - http/
    - websocket/
  - di/
  - model/
  - utils/
```

---

## 💾 Data Persistence

- **Jetpack DataStore** is used for storing user preferences and chat metadata in a type-safe and efficient way.

---

## ⚙️ Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/nishchaymakkar/PingNest.git
   ```

2. **Open in Android Studio**:
   - Use the latest version of Android Studio with Kotlin and Jetpack Compose support.

3. **Run the app**:
   - Make sure your backend WebSocket/STOMP server is running.
   - Build and run the app on an emulator or device.

---


## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

Feel free to submit issues, feature requests, or pull requests. All contributions are welcome!
