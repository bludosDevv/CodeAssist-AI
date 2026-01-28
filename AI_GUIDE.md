# 🤖 Gemini AI Assistant - User Guide

## Quick Start

### 1. Access the AI Assistant
- Look for the **Gemini icon FAB** (floating action button) at the bottom-right corner of your screen
- Tap it to open the AI chat panel that slides in from the right

### 2. First Time Setup
When you open the AI Assistant for the first time:
1. A dialog will prompt you to configure your Gemini API key
2. Click "Get API Key" to open Google AI Studio in your browser
3. Sign in with your Google account and create a free API key
4. Copy the API key and paste it back into CodeAssist
5. Select your preferred model (default: gemini-1.5-flash)
6. Click "Save"

### 3. Start Coding with AI!

## 💬 Chat Commands & Examples

### Creating Files
```
User: Create a new MainActivity.java file in src/main/java/com/example/myapp/

AI: [CREATE_FILE:src/main/java/com/example/myapp/MainActivity.java]
    ✓ File created: src/main/java/com/example/myapp/MainActivity.java
```

### Writing Code
```
User: Write a simple Android Activity with a button that shows a toast

AI: [WRITE_FILE:src/main/java/com/example/myapp/MainActivity.java]
package com.example.myapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> 
            Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show()
        );
    }
}
[/WRITE_FILE]
    ✓ File written: src/main/java/com/example/myapp/MainActivity.java
```

### Reading Existing Code
```
User: Show me the content of MainActivity.java

AI: [READ_FILE:src/main/java/com/example/myapp/MainActivity.java]
    File content of src/main/java/com/example/myapp/MainActivity.java:
    ```
    package com.example.myapp;
    ...
    ```
```

### Creating Layouts
```
User: Create a layout XML with a button centered on screen

AI: [CREATE_FILE:src/main/res/layout/activity_main.xml]
[WRITE_FILE:src/main/res/layout/activity_main.xml]
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="Click Me" />
</RelativeLayout>
[/WRITE_FILE]
    ✓ File written: src/main/res/layout/activity_main.xml
```

### Refactoring Code
```
User: Refactor MainActivity to use ViewBinding instead of findViewById

AI: Let me update your MainActivity to use ViewBinding...
[WRITE_FILE:src/main/java/com/example/myapp/MainActivity.java]
...updated code with ViewBinding...
[/WRITE_FILE]
    ✓ File written: src/main/java/com/example/myapp/MainActivity.java
```

### Debugging Help
```
User: I'm getting a NullPointerException in MainActivity line 15

AI: Let me check your code...
[READ_FILE:src/main/java/com/example/myapp/MainActivity.java]
...analyzes code...
The issue is that you're calling findViewById before setContentView. 
Here's the fix:
[WRITE_FILE:src/main/java/com/example/myapp/MainActivity.java]
...fixed code...
[/WRITE_FILE]
```

### Creating Complete Features
```
User: Create a RecyclerView with a list of items showing name and description

AI: I'll create a complete RecyclerView implementation for you!
[CREATE_FILE:src/main/java/com/example/myapp/Item.java]
[WRITE_FILE:src/main/java/com/example/myapp/Item.java]
...Item model class...
[/WRITE_FILE]

[CREATE_FILE:src/main/java/com/example/myapp/ItemAdapter.java]
[WRITE_FILE:src/main/java/com/example/myapp/ItemAdapter.java]
...RecyclerView adapter...
[/WRITE_FILE]

[CREATE_FILE:src/main/res/layout/item_layout.xml]
[WRITE_FILE:src/main/res/layout/item_layout.xml]
...Item layout XML...
[/WRITE_FILE]

Now update your MainActivity to use this RecyclerView...
```

## 🎯 Best Practices

### 1. Be Specific
❌ "Fix my code"
✅ "Fix the NullPointerException in MainActivity.java line 15"

### 2. Provide Context
❌ "Add a button"
✅ "Add a login button to activity_login.xml that calls the login() method when clicked"

### 3. Use File Paths
❌ "Update MainActivity"
✅ "Update src/main/java/com/myapp/MainActivity.java to add error handling"

### 4. Ask for Explanations
✅ "Explain how this RecyclerView adapter works"
✅ "What's the difference between Fragment and Activity?"

### 5. Request Multiple Operations
✅ "Create a User model class, a UserAdapter, and update MainActivity to display users"

## 🎨 Advanced Features

### Project Understanding
The AI automatically analyzes your project structure and can:
- Understand existing code patterns
- Maintain consistent naming conventions
- Follow your project's architecture
- Import and use existing classes

### Context-Aware Suggestions
The AI remembers your conversation and can:
- Reference previous messages
- Build upon earlier code
- Track changes across multiple files
- Understand project dependencies

### Multi-Step Operations
The AI can execute complex workflows:
```
User: Create a complete MVVM architecture for a note-taking app

AI: I'll set up MVVM architecture for you!
1. Creating data layer...
2. Creating repository...
3. Creating ViewModel...
4. Creating UI components...
5. Setting up dependency injection...
```

## ⚙️ Settings

### Accessing Settings
1. Open AI chat panel
2. Tap the settings icon (⚙️) in the header
3. Modify your preferences

### Available Settings
- **API Key**: Your Gemini API key
- **Model Selection**: Choose from:
  - `gemini-1.5-flash` - Fast and efficient (recommended)
  - `gemini-1.5-flash-8b` - Ultra-fast for simple tasks
  - `gemini-1.5-pro` - Most capable for complex operations
  - `gemini-2.0-flash-exp` - Experimental latest features

### Changing Models
Different models for different needs:
- **Quick edits**: Use flash-8b
- **General coding**: Use flash (default)
- **Complex refactoring**: Use pro
- **Experimental features**: Use 2.0-flash-exp

## 🔐 Privacy & Security

- Your API key is stored **securely** on your device
- All requests go **directly** to Google's Gemini API
- **No data** is collected or stored by CodeAssist
- Your code remains **private** on your device
- Network requests are **encrypted** via HTTPS

## ❓ Troubleshooting

### "API key not configured"
- Tap settings icon and enter your API key
- Get a free key from: https://aistudio.google.com/app/apikey

### "Error: 401 Unauthorized"
- Your API key is invalid or expired
- Get a new API key from Google AI Studio

### "Error: Rate limit exceeded"
- You've reached the free tier limit
- Wait a few minutes or upgrade your Gemini API plan

### AI not understanding context
- Clear chat history (trash icon) and start fresh
- Be more specific in your requests
- Provide full file paths

### File operations not working
- Ensure your project is properly loaded
- Check that file paths are correct
- Verify you have write permissions

## 🎓 Learning Resources

### Example Projects
Ask the AI to create complete example projects:
- "Create a todo list app with SQLite database"
- "Make a weather app with API integration"
- "Build a chat app with Firebase"

### Code Snippets
Request common patterns:
- "Show me how to implement SharedPreferences"
- "Create a custom RecyclerView adapter"
- "Make a retrofit API client"

### Best Practices
Ask for guidance:
- "What's the best way to handle AsyncTasks?"
- "How should I structure my Android project?"
- "Explain the Activity lifecycle"

## 🚀 Tips & Tricks

1. **Use conversation memory**: The AI remembers previous messages
2. **Chain operations**: "Now add error handling" after creating code
3. **Ask for alternatives**: "Show me another way to do this"
4. **Request documentation**: "Add comments to this code"
5. **Batch operations**: "Update all Activities to use the new theme"

## 📞 Support

Need help?
- Discord: https://discord.gg/pffnyE6prs
- Telegram: https://t.me/codeassist_app
- GitHub Issues: https://github.com/bludosDevv/CodeAssist-AI/issues

---

**Happy Coding with AI! 🎉**
