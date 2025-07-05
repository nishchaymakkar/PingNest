# 📱 PingNest

**PingNest** is a modern real-time chat application built using **Kotlin** and **Jetpack Compose**. Designed with performance and adaptability in mind, it features seamless messaging via **WebSockets**, supports **STOMP**, and utilizes **Navigation 3** (currently in alpha) for a responsive, scalable UI across device types.

---

## 🚀 Features

- 💬 Real-time messaging using **WebSockets**
- 🌐 HTTP and STOMP support via **Ktor**
- ✨ **Jetpack Compose** UI with adaptive navigation using **Navigation 3**
- 📸 **Camera** integration to capture photos and videos
- 🖼️ **Photo & Video Picker** for media sharing from gallery
- 🔔 **Bubble Notifications** (Android 11+ support)
- ⚙️ Full-featured **Settings screens**:
  - Account
  - Language
  - Themes & Wallpapers
  - Privacy
  - Profile
  - Help
- ⚖️ Clean **MVVM architecture**
- 🔄 State persistence with **DataStore**
- 🧠 **Koin** for dependency injection
- 📶 Fully reactive with **StateFlow** and **Composable** functions
- 📱 Optimized for both phones and tablets

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
| Navigation          | Jetpack Navigation 3 (alpha)        |
| Media               | CameraX, PhotoPicker                |
| Notifications       | Bubbles API                         |

---

## 📀 Adaptive UI

PingNest now uses **Jetpack Navigation 3**, providing a modern and scalable navigation experience:

- 📃 **Single-pane view** for compact/mobile devices
- 🪟 **Two-pane adaptive layout** for tablets and larger screens

The new Navigation 3 API enables cleaner backstack control and better performance.

---

## 🔌 WebSocket & STOMP Integration

PingNest supports:

- **Standard WebSocket connections** for real-time messaging
- **STOMP protocol** for publish/subscribe functionality
- Robust error handling and reconnection strategies

---

## 📸 Media Sharing

- Launch the **camera** directly from chat to take and send photos/videos
- Use the **Photo Picker** to select media from the device gallery
- Media is integrated smoothly with message bubbles

---

## 🔔 Bubble Notifications

- Chat notifications are presented as **conversation bubbles** for a floating, multitask-friendly experience (Android 11+)
- Includes inline replies and deep linking

---

## ⚙️ Settings Screens

A complete **settings section** allows users to customize their experience:

- 🔐 **Privacy & Security**
- 🧑 **Profile & Account**
- 🎨 **Chat themes & wallpapers**
- 🌐 **Language selection**
- 🆘 **Help & Support**

All settings screens are built using **Compose** and follow a consistent UI/UX pattern.

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

- **ViewModel**: Exposes UI state and handles business logic
- **Repository**: Abstracts data sources (network, preferences)
- **Ktor Client**: Manages all networking layers

---

## 📂 Project Modules

- ui/
  - screens/
  - components/
  - viewmodel/
- data/
  - repository/
  - network/
    - http/
    - websocket/
  - di/
  - model/
  - utils/
    
---


## 💾 Data Persistence

- **Jetpack DataStore** handles persistent storage for user settings, preferences, and app state

---

## Screen Shots
<img src="https://github.com/user-attachments/assets/e7b02c69-4541-4042-8227-424a405c97b8" width="200" alt="Enter Chatroom Dialog" />
<img src="https://github.com/user-attachments/assets/8732f1ae-865c-4876-8b26-65831feccf53" width="200" alt="Chat List" />
<img src="https://github.com/user-attachments/assets/6e4132ab-a3bb-42a0-9397-1ab5c4c9e0d0" width="200" alt="Timeline"/>
<img src="https://github.com/user-attachments/assets/46ddb6bc-a01a-4cd1-b305-4229a99a7621" width="200" alt="Chat Room" />
<img src="https://github.com/user-attachments/assets/8d26ae71-2c29-40c0-be39-20003cbe1911" width="200" alt="Camera Screen" />
<img src="https://github.com/user-attachments/assets/dd99b634-9ced-40ed-b63c-5a220be01c82" width="200" alt ="Edit Video Screen"/>
<img src="https://github.com/user-attachments/assets/8c1eeac2-da9a-4c32-9fcb-56481575f82b" width="200" alt="Photo Picker Screen" />
<img src="https://github.com/user-attachments/assets/b81436f4-d98d-4bcd-9afd-6dc169793525" width="200" alt="Bubble Notifications" />
<img src="https://github.com/user-attachments/assets/fcab9015-65e9-4e3b-adc9-8a908fcdf704" width="200" alt="Bubble Conversation Screen" />
<img src="https://github.com/user-attachments/assets/6af80a85-466b-4796-88a4-b7fd313331d8" width="200" alt="Settings" />
<img src="https://github.com/user-attachments/assets/1657c958-f9a4-4c20-b2de-e3c2dd3ef7f3" width="200" alt="Chat Theme and Wallpaper Settings" />
<img src="https://github.com/user-attachments/assets/2fb2cd4f-dd17-4887-913f-49f949cf8a77" width="200" alt="App Language Settings"/>

---

## ⚙️ Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/nishchaymakkar/PingNest.git
   ```

2. **Open in Android Studio**:
   - Use the latest Android Studio version with **Compose Multiplatform** and Kotlin 1.9+ support.

3. **Run the app**:
   - Ensure your backend WebSocket/STOMP server is running.
   - Build and run the app on a real device or emulator (Android 11+ recommended for bubbles).

---

## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

Feel free to submit issues, feature requests, or pull requests. All contributions are welcome!
