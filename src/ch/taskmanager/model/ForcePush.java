package ch.taskmanager.model;

public enum ForcePush {

    PUSH_FIFO(0), PUSH_PRIORITY(1);

    public final int value;

    private ForcePush(int value) {
        this.value = value;
    }
}
