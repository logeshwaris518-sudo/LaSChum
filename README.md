# LittleAce Chatbot – Android Frontend (LaS)

LaS ("LittleAce Sports" chatbot) is a mobile chat assistant built for LittleAce Sports, designed to answer queries from Schools and Parents about the company. This repository contains **only the Android frontend** of the project — the backend that powers the actual AI responses is kept private (see below).

## Overview

The app provides a simple, real-time-feeling chat interface. Users pick a role — School or Parent — and then chat with "LaS," which is meant to respond with information about LittleAce Sports. All AI reasoning and business-specific knowledge live in a separate backend service; this repo only contains the screens, chat UI, and networking code that talk to that backend.

## Features

- **Role selection screen** — user chooses School or Parent before entering the chat.
- **Chat interface** — message bubbles rendered via a custom `ChatAdapter` and `RecyclerView`, styled to distinguish user messages from bot replies.
- **Networking layer** — uses Retrofit to send user messages to the backend and receive AI-generated replies.
- **Message model** — `ChatMessage.java` defines the structure (sender, content, timestamp) used to populate the chat list.

## Project Structure

```
com.example.littleacechatbot/
├── MainActivity.java          # Hosts the chat screen, handles sending/receiving messages
├── RoleSelectionActivity.java # Entry screen — user picks School or Parent
├── ChatMessage.java           # Data model for a single chat message
├── ChatAdapter.java           # RecyclerView adapter that renders chat bubbles
└── res/                       # Layouts, strings, and other Android resources
```

## Tech Stack

**Frontend (this repo)**
- Android (Java)
- Retrofit — for making API calls to the backend
- RecyclerView + custom `ChatAdapter` — for displaying the chat message list

**Backend (not included in this repo)**
- Spring Boot (Java)
- Groq AI API — generates the actual chatbot responses
- Business-specific prompts and data belonging to LittleAce Sports

## ⚠️ Why the Backend Isn't Included

The Spring Boot backend that powers LaS's responses is **intentionally excluded** from this repository, because it contains:
- Proprietary business logic and prompt design specific to LittleAce Sports
- Client- and business-related information that shouldn't be public

As a direct result, **cloning and running this frontend alone will not produce working chat responses** — there is no publicly available backend for it to talk to. This is a deliberate choice, not an oversight: the frontend is shared here for portfolio and reference purposes, while the backend logic remains private.

If you're evaluating this project (e.g., for a demo, review, or collaboration) and want to see it working end-to-end, please reach out directly rather than expecting it to run out of the box.

## Setup (Frontend Only)

1. Clone this repository.
2. Open the project in Android Studio.
3. In `MainActivity.java`, locate the backend URL constant and replace the placeholder with your own backend's URL if you have one running:
   ```java
   private static final String BASE_URL = "https://your-backend-url-here/";
   ```
4. Build and run on an emulator or physical device.

**Note:** Without a live, compatible backend running at that URL, the app will launch and the UI will work, but no real chat responses will be returned.

## What This Repo Is (and Isn't)

- ✅ A working example of an Android chat UI with Retrofit networking and RecyclerView-based messaging.
- ✅ A reference for how the frontend of this project was structured.
- ❌ Not a fully functional chatbot out of the box.
- ❌ Not a place to find LittleAce Sports' proprietary business logic or data.

## License

See [LICENSE.md](./LICENSE.md) for full usage terms. In short: the frontend code here is shared for portfolio and reference purposes with some restrictions; the backend is proprietary, withheld, and not covered by this license at all.

## Contact

For questions, demo requests, or backend/collaboration inquiries, please reach out to the author directly.
