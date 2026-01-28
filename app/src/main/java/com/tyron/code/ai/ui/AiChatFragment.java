package com.tyron.code.ai.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.tyron.code.R;
import com.tyron.code.ai.gemini.ApiKeyManager;
import com.tyron.code.ai.gemini.GeminiApiClient;
import com.tyron.code.ai.model.ChatMessage;

import java.io.File;

public class AiChatFragment extends Fragment {
    public static final String TAG = "AiChatFragment";
    
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private TextInputEditText messageInput;
    private ImageButton sendButton;
    private ProgressBar progressBar;
    private TextView emptyStateText;
    private ImageButton settingsButton;
    private ImageButton clearButton;
    
    private GeminiApiClient geminiClient;
    private ApiKeyManager apiKeyManager;
    private File currentProject;
    
    public static AiChatFragment newInstance(File projectRoot) {
        AiChatFragment fragment = new AiChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("project_root", projectRoot);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai_chat, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyStateText = view.findViewById(R.id.empty_state_text);
        settingsButton = view.findViewById(R.id.settings_button);
        clearButton = view.findViewById(R.id.clear_button);
        
        // Get project root from arguments
        if (getArguments() != null) {
            currentProject = (File) getArguments().getSerializable("project_root");
        }
        
        // Initialize AI client
        geminiClient = new GeminiApiClient(requireContext());
        if (currentProject != null) {
            geminiClient.setProjectRoot(currentProject);
        }
        apiKeyManager = new ApiKeyManager(requireContext());
        
        // Setup RecyclerView
        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(chatAdapter);
        
        // Setup listeners
        setupListeners();
        
        // Check if API key is configured
        checkApiKeyConfiguration();
        
        // Update empty state
        updateEmptyState();
    }
    
    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        
        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(s.length() > 0);
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        settingsButton.setOnClickListener(v -> showSettingsDialog());
        clearButton.setOnClickListener(v -> clearChat());
    }
    
    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) return;
        
        // Clear input
        messageInput.setText("");
        
        // Add user message to chat
        ChatMessage userMessage = new ChatMessage(message, true, System.currentTimeMillis());
        chatAdapter.addMessage(userMessage);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);
        
        // Send to Gemini
        geminiClient.sendMessage(message, new GeminiApiClient.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    
                    // Add AI response to chat
                    ChatMessage aiMessage = new ChatMessage(response, false, System.currentTimeMillis());
                    chatAdapter.addMessage(aiMessage);
                    chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                    
                    updateEmptyState();
                });
            }
            
            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                    
                    if (error.contains("API key")) {
                        showSettingsDialog();
                    }
                });
            }
        });
    }
    
    private void showSettingsDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_ai_settings, null);
        EditText apiKeyInput = dialogView.findViewById(R.id.api_key_input);
        Spinner modelSpinner = dialogView.findViewById(R.id.model_spinner);
        
        // Set current values
        String currentKey = apiKeyManager.getApiKey();
        if (currentKey != null) {
            apiKeyInput.setText(currentKey);
        }
        
        // Setup model spinner
        String[] models = apiKeyManager.getAvailableModels();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);
        
        String currentModel = apiKeyManager.getSelectedModel();
        for (int i = 0; i < models.length; i++) {
            if (models[i].equals(currentModel)) {
                modelSpinner.setSelection(i);
                break;
            }
        }
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Gemini AI Settings")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String apiKey = apiKeyInput.getText().toString().trim();
                    String selectedModel = (String) modelSpinner.getSelectedItem();
                    
                    if (!apiKey.isEmpty()) {
                        apiKeyManager.saveApiKey(apiKey);
                        apiKeyManager.saveSelectedModel(selectedModel);
                        geminiClient.reinitializeModel();
                        Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "API key cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Get API Key", (dialog, which) -> {
                    // Open browser to get API key
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(android.net.Uri.parse("https://aistudio.google.com/app/apikey"));
                    startActivity(intent);
                })
                .show();
    }
    
    private void clearChat() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear Chat")
                .setMessage("Are you sure you want to clear the chat history?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    chatAdapter.clearMessages();
                    geminiClient.clearHistory();
                    updateEmptyState();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void checkApiKeyConfiguration() {
        if (!geminiClient.isConfigured()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Configure Gemini AI")
                    .setMessage("Please set your Gemini API key to start using the AI assistant.")
                    .setPositiveButton("Configure", (dialog, which) -> showSettingsDialog())
                    .setNegativeButton("Later", null)
                    .show();
        }
    }
    
    private void updateEmptyState() {
        if (chatAdapter.getItemCount() == 0) {
            emptyStateText.setVisibility(View.VISIBLE);
            chatRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    public void setCurrentProject(File project) {
        this.currentProject = project;
        if (geminiClient != null) {
            geminiClient.setProjectRoot(project);
        }
    }
}
