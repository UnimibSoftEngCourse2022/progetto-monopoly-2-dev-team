package it.monopoly.controller.command;

public interface CommandBuilderDispatcher {
    <T> T createCommandBuilder(Class<T> className);
}
