package com.example.littleacechatbot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://guileless-blot-syrup.ngrok-free.dev/";

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;

    private List<ChatMessage> messageList;
    private ChatAdapter chatAdapter;
    private ApiService apiService;

    private String username;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        apiService = RetrofitClient.getClient(BASE_URL).create(ApiService.class);

        buttonSend.setOnClickListener(v -> sendMessage());

        loadChatHistory();
    }

    private void loadChatHistory() {
        apiService.getChatHistory(username, role).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                    if (!messageList.isEmpty()) {
                        recyclerViewChat.scrollToPosition(messageList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                // silently ignore — chat just starts empty if history fetch fails
            }
        });
    }

    private void sendMessage() {
        String userText = editTextMessage.getText().toString().trim();
        if (userText.isEmpty()) {
            return;
        }

        messageList.add(new ChatMessage(userText, ChatMessage.TYPE_USER));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
        editTextMessage.setText("");

        ChatRequest request = new ChatRequest(userText, username, role);

        apiService.sendMessage(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                String botReply;
                if (response.isSuccessful() && response.body() != null) {
                    botReply = response.body().getReply();
                } else {
                    botReply = "Sorry, something went wrong. Please try again.";
                }
                messageList.add(new ChatMessage(botReply, ChatMessage.TYPE_BOT));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewChat.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                messageList.add(new ChatMessage("Couldn't reach the server. Check your connection.", ChatMessage.TYPE_BOT));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewChat.scrollToPosition(messageList.size() - 1);
            }
        });
    }
}