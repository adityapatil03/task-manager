package ch.taskmanager;

import ch.taskmanager.application.Handler;
import ch.taskmanager.model.Process;
import ch.taskmanager.model.ForcePush;
import ch.taskmanager.model.Priority;

public class Driver {

    public static void main(String[] args)
    {
        Handler taskHandler = Handler.H_INSTANCE;

        // sample cases
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.LOW);
        taskHandler.addProcess(Priority.LOW);
        taskHandler.addProcess(Priority.LOW);
        taskHandler.addProcess(Priority.MEDIUM);
        taskHandler.addProcess(Priority.MEDIUM);

        taskHandler.listAllProcesses();

        taskHandler.killProcessWithPriority(Priority.HIGH);

        taskHandler.listAllProcesses();

        taskHandler.addProcess(Priority.LOW);
        taskHandler.addProcess(Priority.MEDIUM);
        taskHandler.addProcess(Priority.MEDIUM);

        taskHandler.listAllProcesses();

        taskHandler.addProcess(Priority.LOW, ForcePush.PUSH_FIFO);

        taskHandler.listAllProcesses();

        taskHandler.addProcess(Priority.MEDIUM, ForcePush.PUSH_PRIORITY); // issue!

        taskHandler.listAllProcesses();

        taskHandler.addProcess(Priority.HIGH,ForcePush.PUSH_PRIORITY);

        taskHandler.listAllProcesses();

        taskHandler.listAllProcesses(Process.SortBy.priority);
        taskHandler.listAllProcesses();

        // All High priority case
        taskHandler.killAllProcesses();
        taskHandler.listAllProcesses();
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);
        taskHandler.addProcess(Priority.HIGH);

        taskHandler.listAllProcesses();
        taskHandler.addProcess(Priority.HIGH,ForcePush.PUSH_PRIORITY);
        taskHandler.listAllProcesses();


    }
}
