package ch.taskmanager.application;

import ch.taskmanager.model.ForcePush;
import ch.taskmanager.model.Priority;
import ch.taskmanager.model.Process;
import ch.taskmanager.model.ProcessManagerException;

import java.util.Collection;


/**
 * This Handler class serves request from Client, handles Exceptions and
 * logs messages based on responses from Task Manager
 */
public enum Handler {
    H_INSTANCE;

    TaskManager taskManager = TaskManager.TM_INSTANCE;

    System.Logger LOGGER = System.getLogger(Handler.class.getName());

    /**
     * @param priority Priority of the process to be started
     * @param forcePush (optional) If the maximum capacity has been reached, forcePush flag provides
     *                  option to remove the oldest process with FIFO method
     *                  or oldest low priority process and push the new process
     * Handles ProcessManagerException thrown by Task Manager
     */
    public void addProcess(Priority priority, ForcePush... forcePush) {
        String processIdentifier = null;
        try {
            if (forcePush != null && forcePush.length > 0) {
                if (LOGGER.isLoggable(System.Logger.Level.DEBUG)) {
                    LOGGER.log(System.Logger.Level.DEBUG, "Force Push called with : " + forcePush[0] + " for priority "+ priority);
                }
                processIdentifier = taskManager.addProcess(priority, forcePush[0]);
            } else {
                processIdentifier = taskManager.addProcess(priority);
            }

            if (processIdentifier != null) {
                LOGGER.log(System.Logger.Level.INFO, "Process added successfully with PID : " + processIdentifier);
            }
        } catch (ProcessManagerException processManagerException) {
            handleException(processManagerException);
        }
    }

    /**
     * Adds a process with Low priority
     * if no priority specified
     */
    public void addProcess() {
        addProcess(Priority.LOW);
    }

    /**
     * @param identifier PID to be used to find and kill the process
     * Handles ProcessManagerException thrown by Task Manager
     */
    public void killProcessWithPID(String identifier) throws ProcessManagerException {
        try {
            boolean removed = taskManager.killProcessWithPID(identifier);
            if (removed) {
                LOGGER.log(System.Logger.Level.INFO, "Requested Process have been killed successfully.");
            } else {
                LOGGER.log(System.Logger.Level.ERROR, "Operation did not succeed, please try again or check if the process still exists!");
            }
        } catch (ProcessManagerException processManagerException) {
            handleException(processManagerException);
        }
    }

    /**
     * @param priority Priority of the processes to be killed
     * Handles ProcessManagerException thrown by Task Manager
     */
    public void killProcessWithPriority(Priority priority) throws ProcessManagerException {
        try {
            boolean removed = taskManager.killProcessWithPriority(priority);
            if (removed) {
                LOGGER.log(System.Logger.Level.INFO, "Requested Processes have been killed successfully.");
            } else {
                LOGGER.log(System.Logger.Level.ERROR, "Operation did not succeed, please try again or check if the processes with requested priority still exist!");
            }
        } catch (ProcessManagerException processManagerException) {
            handleException(processManagerException);
        }
    }

    public void killAllProcesses() {
        taskManager.killAllProcesses();
        LOGGER.log(System.Logger.Level.INFO, "All Processes have been killed successfully.");
    }

    /**
     * @param sortBy (optional) Attribute to sort the processes with if specified
     */
    public void listAllProcesses(Process.SortBy... sortBy) {


        StringBuffer stringBuffer = new StringBuffer("\n");
        Collection<Process> processes;
        if (sortBy != null && sortBy.length > 0) {
            LOGGER.log(System.Logger.Level.INFO, "Listing Processes sorted by " + sortBy[0]);
            processes = taskManager.listAllProcesses(sortBy[0]);
        } else {
            LOGGER.log(System.Logger.Level.INFO, "Listing processes default by creation ...");
            processes = taskManager.listAllProcesses();
        }

        for (Process process : processes) {
            stringBuffer.append(process.getProcessIdentifier()).append("\t\t").append(process.getPriority()).append("\t\t").append(process.getCreatedAt()).append("\n");
        }
        LOGGER.log(System.Logger.Level.INFO, stringBuffer.toString());
    }

    private void handleException(ProcessManagerException processManagerException) {
        LOGGER.log(System.Logger.Level.ERROR, "ProcessManagerException :: " + processManagerException.getMessage());
    }

}
