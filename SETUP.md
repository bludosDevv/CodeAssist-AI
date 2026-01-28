# 🚀 CodeAssist-AI - Setup & Build Guide

## Prerequisites

### Required Software
- **Android Studio** 2021.3.1 (Dolphin) or later
- **JDK 11** or later
- **Android SDK** with API level 26-32
- **Git** for version control
- **Gradle** 7.0+ (included with project)

### System Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: Minimum 8GB (16GB recommended)
- **Storage**: 10GB free space
- **Internet**: Required for Gradle dependencies and Gemini API

## 📥 Installation

### Option 1: Clone from GitHub
```bash
git clone https://github.com/bludosDevv/CodeAssist-AI.git
cd CodeAssist-AI
```

### Option 2: Download ZIP
1. Go to https://github.com/bludosDevv/CodeAssist-AI
2. Click "Code" → "Download ZIP"
3. Extract to your desired location

## 🔨 Building the Project

### Using Android Studio

1. **Open Project**
   ```
   File → Open → Select the CodeAssist-AI directory
   ```

2. **Sync Gradle**
   - Android Studio will automatically start syncing
   - Wait for "Gradle sync finished" notification
   - If errors occur, click "Sync Now" in the notification bar

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```
   - APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on Device**
   - Connect your Android device via USB
   - Enable USB Debugging on your device
   - Click "Run" (▶️) button in Android Studio
   - Select your device from the list

### Using Command Line

1. **Make Gradlew Executable** (Linux/Mac only)
   ```bash
   chmod +x gradlew
   ```

2. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```

4. **Install on Connected Device**
   ```bash
   ./gradlew installDebug
   ```

5. **Run Tests**
   ```bash
   ./gradlew test
   ```

## 🔧 Configuration

### Gemini API Setup

1. **Get API Key**
   - Visit: https://aistudio.google.com/app/apikey
   - Sign in with Google account
   - Click "Create API Key"
   - Copy the generated key

2. **Configure in App**
   - Open CodeAssist-AI on your device
   - Open any project or create new one
   - Tap the Gemini FAB (bottom-right)
   - Tap settings icon (⚙️)
   - Paste your API key
   - Select preferred model
   - Tap "Save"

### Build Configuration

Edit `app/build.gradle` for custom configuration:

```gradle
android {
    compileSdkVersion 32
    
    defaultConfig {
        applicationId "com.tyron.code"
        minSdkVersion 26
        targetSdkVersion 32
        versionCode 21
        versionName "0.3.0 ALPHA"
    }
}
```

## 🏗️ Project Structure

```
CodeAssist-AI/
├── app/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/tyron/code/
│   │   │   │   ├── ai/          # 🆕 AI Assistant module
│   │   │   │   │   ├── gemini/  # Gemini API client
│   │   │   │   │   ├── model/   # Data models
│   │   │   │   │   ├── operations/  # File operations
│   │   │   │   │   └── ui/      # Chat UI components
│   │   │   │   ├── ui/          # App UI components
│   │   │   │   └── ...
│   │   │   └── res/             # Resources
│   │   │       ├── layout/      # XML layouts
│   │   │       ├── drawable/    # Images & icons
│   │   │       └── values/      # Strings, colors, etc.
│   │   └── test/                # Unit tests
│   └── build.gradle             # App dependencies
├── build-tools/                 # Custom build tools
├── java-completion/             # Java code completion
├── kotlin-completion/           # Kotlin code completion
├── code-editor/                 # Code editor module
├── .github/workflows/           # GitHub Actions CI/CD
├── build.gradle                 # Root build config
└── settings.gradle              # Module includes
```

## 🔍 Key Dependencies

### Gemini AI (NEW)
```gradle
implementation 'com.google.ai.client.generativeai:generativeai:0.1.2'
implementation 'com.google.guava:guava:31.1-android'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
```

### Android Core
```gradle
implementation 'androidx.appcompat:appcompat:1.4.1'
implementation 'androidx.core:core:1.7.0'
implementation 'com.google.android.material:material:1.5.0'
```

### Build Tools
- Custom Gradle implementation
- R8/ProGuard for optimization
- Java 11 compiler
- Kotlin 1.6.21

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### Test AI Integration
1. Open app on device
2. Create test project
3. Open AI Assistant
4. Configure API key
5. Test commands:
   ```
   Create a test file at test.txt
   Write "Hello AI!" to test.txt
   Read test.txt
   ```

## 📦 Creating Release Build

### Generate Signed APK

1. **Create Keystore** (first time only)
   ```bash
   keytool -genkey -v -keystore my-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias my-key-alias
   ```

2. **Configure Signing**
   
   Create `keystore.properties` in project root:
   ```properties
   storePassword=yourStorePassword
   keyPassword=yourKeyPassword
   keyAlias=my-key-alias
   storeFile=my-release-key.jks
   ```

3. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```

4. **APK Location**
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

## 🐛 Troubleshooting

### Gradle Sync Failed
```bash
# Clear Gradle cache
./gradlew clean
rm -rf .gradle/
./gradlew --refresh-dependencies
```

### Out of Memory Error
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### SDK Not Found
Set `ANDROID_HOME` environment variable:
```bash
# Linux/Mac
export ANDROID_HOME=$HOME/Android/Sdk

# Windows
setx ANDROID_HOME "C:\Users\YourName\AppData\Local\Android\Sdk"
```

### Build Tools Version Error
Update `build.gradle`:
```gradle
buildToolsVersion "30.0.2"
```

### Dependency Resolution Issues
```bash
./gradlew build --refresh-dependencies --stacktrace
```

## 🤝 Contributing

### Setting Up Development Environment

1. **Fork Repository**
   ```bash
   gh repo fork bludosDevv/CodeAssist-AI
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/my-new-feature
   ```

3. **Make Changes**
   - Follow existing code style
   - Add tests for new features
   - Update documentation

4. **Test Changes**
   ```bash
   ./gradlew test
   ./gradlew assembleDebug
   ```

5. **Submit Pull Request**
   ```bash
   git push origin feature/my-new-feature
   ```
   Then create PR on GitHub

### Code Style
- Follow Android Kotlin Style Guide
- Use meaningful variable names
- Add comments for complex logic
- Keep methods small and focused

## 📱 Installing on Device

### Via ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Via File Transfer
1. Copy APK to device
2. Open file manager
3. Tap APK file
4. Allow "Install from Unknown Sources" if prompted
5. Tap "Install"

## 🚀 Continuous Integration

### GitHub Actions
The project includes automated CI/CD:
- Builds on every push to main
- Runs tests automatically
- Generates APK artifacts
- View workflows: `.github/workflows/`

### Manual Workflow Trigger
```bash
gh workflow run "Android CI"
```

## 📚 Additional Resources

- **Android Developers**: https://developer.android.com/
- **Gemini API Docs**: https://ai.google.dev/docs
- **Gradle User Guide**: https://docs.gradle.org/
- **CodeAssist Discord**: https://discord.gg/pffnyE6prs

## 🆘 Getting Help

### Issues
Report bugs: https://github.com/bludosDevv/CodeAssist-AI/issues

### Community
- Discord: https://discord.gg/pffnyE6prs
- Telegram: https://t.me/codeassist_app

### Documentation
- [AI Guide](AI_GUIDE.md) - How to use AI Assistant
- [README_AI](README_AI.md) - AI feature overview
- [Contributing Guidelines](CONTRIBUTING.md)

---

**Built with ❤️ for the Android development community**
