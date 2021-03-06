package ru.geekbrains.thirdquarter.springintro.client;

import java.util.Set;

public interface MessageReciever {

    void submitMessage(TextMessage message);

    void userConnected(String login);

    void userDisconnected(String login);

    void updateUserList(Set<String> users);
}
