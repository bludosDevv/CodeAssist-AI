package com.tyron.code.ai.gemini;

import android.content.Context;
import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.tyron.code.ai.model.ChatMessage;
import com.tyron.code.ai.operations.FileOperationsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiApiClient {
    private static final String TAG = "GeminiApiClient";
    private final Context context;
    private final ApiKeyManager apiKeyManager;
    private GenerativeModelFutures model;
    private final List<ChatMessage> conversationHistory;
    private final Executor executor;
    private FileOperationsManager fileOpsManager;
    
    public interface ResponseCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    public GeminiApiClient(Context context) {
        this.context = context;
        this.apiKeyManager = new ApiKeyManager(context);
        this.conversationHistory = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
        initializeModel();
    }
    
    public void setProjectRoot(File projectRoot) {
        this.fileOpsManager = new FileOperationsManager(projectRoot);
    }
    
    private void initializeModel() {
        String apiKey = apiKeyManager.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            Log.w(TAG, "No API key set");
            return;
        }
        
        String modelName = apiKeyManager.getSelectedModel();
        GenerativeModel gm = new GenerativeModel(modelName, apiKey);
        this.model = GenerativeModelFutures.from(gm);
        Log.d(TAG, "Model initialized: " + modelName);
    }
    
    public void reinitializeModel() {
        initializeModel();
    }
    
    public void sendMessage(String userMessage, ResponseCallback callback) {
        if (model == null) {
            callback.onError("API key not configured. Please set your Gemini API key in settings.");
            return;
        }
        
        // Add user message to history
        conversationHistory.add(new ChatMessage(userMessage, true, System.currentTimeMillis()));
        
        // Build context with file operations capability
        String enhancedPrompt = buildEnhancedPrompt(userMessage);
        
        Content content = new Content.Builder()
                .addText(enhancedPrompt)
                .build();
        
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String responseText = result.getText();
                
                // Process any file operations in the response
                String processedResponse = processFileOperations(responseText);
                
                // Add AI response to history
                conversationHistory.add(new ChatMessage(processedResponse, false, System.currentTimeMillis()));
                
                callback.onSuccess(processedResponse);
            }
            
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Error generating content", t);
                callback.onError("Error: " + t.getMessage());
            }
        }, executor);
    }
    
    private String buildEnhancedPrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();
        
        // Add system context
        prompt.append("You are an AI coding assistant integrated into CodeAssist Android IDE. ");
        prompt.append("You can help with code generation, debugging, refactoring, and file operations.\n\n");
        
        // Add project context if available
        if (fileOpsManager != null) {
            prompt.append("Current Project Structure:\n");
            prompt.append(fileOpsManager.getProjectStructure());
            prompt.append("\n\n");
        }
        
        // Add conversation context (last 5 messages)
        int startIdx = Math.max(0, conversationHistory.size() - 5);
        for (int i = startIdx; i < conversationHistory.size(); i++) {
            ChatMessage msg = conversationHistory.get(i);
            prompt.append(msg.isUser() ? "User: " : "Assistant: ");
            prompt.append(msg.getMessage()).append("\n");
        }
        
        // Add file operation instructions
        prompt.append("\nFile Operation Commands (use these in your response):\n");
        prompt.append("- [CREATE_FILE:path/to/file.java] - Create a new file\n");
        prompt.append("- [WRITE_FILE:path/to/file.java]content[/WRITE_FILE] - Write content to file\n");
        prompt.append("- [READ_FILE:path/to/file.java] - Read file content\n");
        prompt.append("- [DELETE_FILE:path/to/file.java] - Delete a file\n");
        prompt.append("- [CREATE_DIR:path/to/directory] - Create a directory\n");
        prompt.append("- [LIST_FILES:path/to/directory] - List files in directory\n\n");
        
        // Add current user message
        prompt.append("User: ").append(userMessage).append("\n");
        prompt.append("Assistant: ");
        
        return prompt.toString();
    }
    
    private String processFileOperations(String response) {
        if (fileOpsManager == null) {
            return response;
        }
        
        StringBuilder result = new StringBuilder(response);
        
        try {
            // Process CREATE_FILE
            processCommand(result, "CREATE_FILE", (path) -> {
                try {
                    fileOpsManager.createFile(path);
                    return "✓ File created: " + path;
                } catch (Exception e) {
                    return "✗ Failed to create file: " + e.getMessage();
                }
            });
            
            // Process CREATE_DIR
            processCommand(result, "CREATE_DIR", (path) -> {
                boolean success = fileOpsManager.createDirectory(path);
                return success ? "✓ Directory created: " + path : "✗ Failed to create directory";
            });
            
            // Process WRITE_FILE
            int writeStart = result.indexOf("[WRITE_FILE:");
            while (writeStart != -1) {
                int pathEnd = result.indexOf("]", writeStart);
                if (pathEnd == -1) break;
                
                String path = result.substring(writeStart + 12, pathEnd);
                int contentEnd = result.indexOf("[/WRITE_FILE]", pathEnd);
                if (contentEnd == -1) break;
                
                String content = result.substring(pathEnd + 1, contentEnd);
                
                try {
                    fileOpsManager.writeFile(path, content);
                    result.replace(writeStart, contentEnd + 13, "✓ File written: " + path + "\n");
                } catch (Exception e) {
                    result.replace(writeStart, contentEnd + 13, "✗ Failed to write file: " + e.getMessage() + "\n");
                }
                
                writeStart = result.indexOf("[WRITE_FILE:", writeStart + 1);
            }
            
            // Process DELETE_FILE
            processCommand(result, "DELETE_FILE", (path) -> {
                boolean success = fileOpsManager.delete(path);
                return success ? "✓ Deleted: " + path : "✗ Failed to delete";
            });
            
            // Process READ_FILE
            processCommand(result, "READ_FILE", (path) -> {
                try {
                    String content = fileOpsManager.readFile(path);
                    return "File content of " + path + ":\n```\n" + content + "\n```";
                } catch (Exception e) {
                    return "✗ Failed to read file: " + e.getMessage();
                }
            });
            
            // Process LIST_FILES
            processCommand(result, "LIST_FILES", (path) -> {
                List<String> files = fileOpsManager.listFiles(path);
                return "Files in " + path + ":\n" + String.join("\n", files);
            });
            
        } catch (Exception e) {
            Log.e(TAG, "Error processing file operations", e);
        }
        
        return result.toString();
    }
    
    private void processCommand(StringBuilder text, String command, CommandProcessor processor) {
        String marker = "[" + command + ":";
        int start = text.indexOf(marker);
        
        while (start != -1) {
            int end = text.indexOf("]", start);
            if (end == -1) break;
            
            String path = text.substring(start + marker.length(), end);
            String replacement = processor.process(path);
            
            text.replace(start, end + 1, replacement);
            start = text.indexOf(marker, start + replacement.length());
        }
    }
    
    interface CommandProcessor {
        String process(String input);
    }
    
    public List<ChatMessage> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
    
    public void clearHistory() {
        conversationHistory.clear();
    }
    
    public boolean isConfigured() {
        return apiKeyManager.hasApiKey();
    }
}
