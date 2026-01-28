# CodeAssist with Gemini AI Agent 🚀

<p align='center'>
<img width='300px' height='300px' src='https://raw.githubusercontent.com/KrishnaVyshak/CodeAssist/main/logo-dark.png#gh-dark-mode-only' >
<img width='300px' height='300px' src='https://raw.githubusercontent.com/KrishnaVyshak/CodeAssist/main/logo-light.png#gh-light-mode-only'>
</p>

[![stability-alpha](https://img.shields.io/badge/stability-alpha-f4d03f.svg)](https://github.com/mkenney/software-guides/blob/master/STABILITY-BADGES.md#alpha)
[![Chat](https://img.shields.io/badge/chat-on%20discord-7289da)](https://discord.gg/pffnyE6prs)

## 🤖 NEW: Gemini AI Agent Integration

CodeAssist now features a powerful **Gemini AI Assistant** that can help you code faster and smarter!

### ✨ AI Features

- **🧠 Intelligent Code Generation**: Ask the AI to create files, classes, and entire features
- **📝 Smart Code Editing**: AI can read, modify, and refactor your code
- **🔍 Project Understanding**: AI analyzes your project structure to provide context-aware suggestions
- **🐛 Bug Detection & Fixes**: Get help debugging issues and fixing errors
- **💡 Code Suggestions**: Real-time recommendations for improvements
- **📁 Full File Operations**: AI can create, read, update, and delete files/folders
- **🎯 Model Selection**: Choose from multiple Gemini models:
  - `gemini-1.5-flash` (Fast & efficient)
  - `gemini-1.5-flash-8b` (Ultra-fast)
  - `gemini-1.5-pro` (Most capable)
  - `gemini-2.0-flash-exp` (Experimental)

### 🎨 How to Use

1. **Open the AI Assistant**: 
   - Tap the **Gemini icon FAB** (floating button) at the bottom-right of your screen
   - The AI chat panel will slide in from the right

2. **Configure API Key**:
   - On first use, click the settings icon (⚙️)
   - Enter your Gemini API key (get it free from [Google AI Studio](https://aistudio.google.com/app/apikey))
   - Select your preferred model
   - Save settings

3. **Start Chatting**:
   - Type your request in the chat input
   - Examples:
     - "Create a new MainActivity.java file"
     - "Add a button click listener to my layout"
     - "Refactor this code to use RecyclerView"
     - "Fix the null pointer exception in LoginActivity"
     - "Generate a REST API client class"

4. **AI File Commands**:
   The AI can execute file operations automatically:
   - `[CREATE_FILE:path/to/file.java]` - Creates a new file
   - `[WRITE_FILE:path/to/file.java]content[/WRITE_FILE]` - Writes content to a file
   - `[READ_FILE:path/to/file.java]` - Reads and displays file content
   - `[DELETE_FILE:path/to/file.java]` - Deletes a file
   - `[CREATE_DIR:path/to/directory]` - Creates a directory
   - `[LIST_FILES:path/to/directory]` - Lists files in a directory

### 🎥 Screenshots

*Coming soon*

---

## 📱 About CodeAssist

**A javac APIs-based code editor that supports building Android apps on Android devices.**

### Features

- [x] APK Compilation
- [x] AAB Support
- [x] Java
- [x] Kotlin  
- [x] R8/ProGuard
- [x] Code Completions (Currently for Java only)  
- [x] Quick fixes (Import missing class and Implement Abstract Methods)  
- [x] Layout Preview (80%)
- [x] Automatic dependency resolution  
- [x] **🆕 Gemini AI Assistant**
- [ ] Layout Editor
- [ ] Debugger
- [ ] Lint 

---

## 🏗️ Building - Android Studio

1. Clone this repository:
   ```bash
   git clone https://github.com/[YOUR_USERNAME]/CodeAssist-AI.git
   cd CodeAssist-AI
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run on your Android device

### Building with Gradle Command Line

```bash
./gradlew assembleDebug
```

The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

- Pull requests must have a short description as a title and a more detailed one in the description
- Feature additions must include Unit/Instrumentation tests for future stability
- Follow the existing code style and architecture patterns

---

## 🔑 Getting a Gemini API Key

1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the key and paste it in CodeAssist's AI settings

**Note**: The Gemini API has a generous free tier, perfect for personal development!

---

## 🌐 CodeAssist Community

- Discord server: https://discord.gg/pffnyE6prs
- English Telegram: https://t.me/codeassist_app
- Russian Telegram: https://t.me/codeassist_chat

---

## 🙏 Special Thanks

- Rosemoe/CodeEditor 
- JavaNIDE
- Mike Anderson
- Java Language Server
- Ilyasse Salama
- **Google Gemini Team** for the powerful AI API

---

## 📄 License

See [LICENSE](LICENSE) file for details.

---

## 🔒 Privacy & Security

- Your API key is stored securely on your device using Android's SharedPreferences
- All AI requests are sent directly to Google's Gemini API
- No data is collected or stored by CodeAssist
- Your code remains private and secure

---

**Made with ❤️ by the CodeAssist community**

<img style="border-radius: 30px; width: 100%; height: 100%" src="https://krishnavyshak.github.io/info_codeassist_1.png">
