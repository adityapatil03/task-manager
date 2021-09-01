package ch.taskmanager.model;

public enum Priority {

    LOW(0), MEDIUM(1), HIGH(2);

    public final int rank;

    private Priority(int rank) {
        this.rank = rank;
    }

}
