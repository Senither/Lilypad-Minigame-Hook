package com.senither.lilypad.minigame.network;

import com.google.gson.Gson;
import com.senither.lilypad.minigame.LilypadMinigameHook;
import com.senither.lilypad.minigame.api.LilypadMinigameAPI;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public class NetworkManager implements Runnable {

    private final LilypadMinigameHook plugin;
    private final Gson gson;

    private String channel;

    public NetworkManager(LilypadMinigameHook plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        this.channel = "GLOBAL";

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 60, 20);
    }

    public void setGameChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        HashMap<String, Object> response = new HashMap<>();

        response.put("type", "schedule");
        response.put("channel", channel);
        response.put("data", LilypadMinigameAPI.invokePlaceholders());

        messageRequest("Lilypad-Minigame", gson.toJson(response), plugin.getLobbies());
    }

    public void messageRequest(String channel, String message, List<String> servers) {
        try {
            this.reconnectToNetwork();

            MessageRequest request = new MessageRequest(servers, channel, message);

            plugin.getConnect().request(request);
        } catch (UnsupportedEncodingException | RequestException ex) {
            throw new RuntimeException("Error while sending a message request." + ex);
        } catch (Throwable ex) {
            throw new RuntimeException("Error whilst redirecting a player. The connection seems to have been closed and won't open again." + ex);
        }
    }

    public boolean teleportRequest(String player, String server) {
        try {
            this.reconnectToNetwork();

            RedirectRequest request = new RedirectRequest(server, player);
            FutureResult future = plugin.getConnect().request(request);

            return future.await().getStatusCode().equals(StatusCode.SUCCESS);
        } catch (RequestException | InterruptedException ex) {
            throw new RuntimeException("Error whilst redirecting a player." + ex);
        } catch (Throwable ex) {
            throw new RuntimeException("Error whilst redirecting a player. The connection seems to have been closed and won't open again." + ex);
        }
    }

    private void reconnectToNetwork() throws Throwable {
        if (!plugin.getConnect().isConnected()) {
            plugin.getConnect().connect();
        }
    }
}
