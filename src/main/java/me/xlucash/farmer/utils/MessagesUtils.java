package me.xlucash.farmer.utils;

import cz.foresttech.api.ColorAPI;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MessagesUtils {

    public static String colorize(String message) {
        return message != null ? ColorAPI.colorize(message) : "";
    }

    public static List<String> colorizeList(@NotNull List<String> messages) {
        messages.replaceAll(MessagesUtils::colorize);
        return messages;
    }

    public static String formatMessage(@NotNull String message, String placeholder, String value) {
        return message.replace(placeholder, value);
    }

    public static String formatMessage(@NotNull String message, String placeholder, Number value) {
        return message.replace(placeholder, value.toString());
    }

    public static String formatMessage(String message, @NotNull Map<String, String> placeholders) {
        return placeholders.entrySet().stream()
                .reduce(message, (formattedMessage, entry) ->
                                formattedMessage.replace("{" + entry.getKey() + "}", entry.getValue()),
                        (finalMessage, element) -> finalMessage
                );
    }
}
