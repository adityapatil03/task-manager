package ch.taskmanager.application;

import ch.taskmanager.model.ForcePush;
import ch.taskmanager.model.Priority;
import ch.taskmanager.model.Process;
import ch.taskmanager.model.ProcessManagerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Task manager (Singleton) to handle processes with unique PID and a priority.
 */
public enum TaskManager {

    TM_INSTANCE;

    private static final Integer maximumCapacity = 6;

    public static Integer idCounter = 111110;

    private static ConcurrentLinkedQueue<Process> processes = new ConcurrentLinkedQueue<>();

    private static final Object lockQueue = new Object();

    System.Logger LOGGER = System.getLogger(TaskManager.class.getName());

    /**
     * @param priority Priority of the process to be started
     * @return Unique process identifier PID
     * @throws ProcessManagerException if Task Manager reaches maximum capacity
     */
    public String addProcess(Priority priority) throws ProcessManagerException {

        if (LOGGER.isLoggable(System.Logger.Level.DEBUG)) {
            LOGGER.log(System.Logger.Level.DEBUG, "Starting new process with priority : " + priority);
        }

        synchronized (lockQueue) {
            if (processes.size() < maximumCapacity) {
                Process process = new Process(priority);
                processes.add(process);
                return process.getProcessIdentifier();
            }

            throw new ProcessManagerException("Task Manager has reached maximum capacity, process could not be started!");
        }
    }

    /**
     * @param priority  Priority of the process to be started
     * @param forcePush If the maximum capacity has been reached, forcePush flag provides
     *                  option to remove the oldest process with FIFO method
     *                  or oldest low priority process and push the new process
     * @return Unique process identifier PID
     */
    public String addProcess(Priority priority, ForcePush... forcePush) {

        if (forcePush != null && forcePush.length > 0) {
            if (LOGGER.isLoggable(System.Logger.Level.DEBUG)) {
                LOGGER.log(System.Logger.Level.DEBUG, "Force Push called with : " + forcePush[0] + " for priority " + priority);
            }
            synchronized (lockQueue) {
                if (processes.size() == maximumCapacity) {
                    if (ForcePush.PUSH_FIFO.name().equals(forcePush[0].name())) {
                        processes.poll();
                    } else if (ForcePush.PUSH_PRIORITY.name().equals(forcePush[0].name())) {
                        if (priority.name().equals(Priority.HIGH.name()) || priority.name().equals(Priority.MEDIUM.name())) {
                            boolean isHigher = true;
                            Process lastLowPriorityProcess = null;
                            for (Process process : processes) {
                                if (priority.rank <= process.getPriority().rank) {
                                    isHigher = false;
                                    break;
                                }

                                if (lastLowPriorityProcess == null || process.getPriority().rank < lastLowPriorityProcess.getPriority().rank) {
                                    lastLowPriorityProcess = process;

                                }

                            }
                            if (isHigher && lastLowPriorityProcess != null) {
                                processes.remove(lastLowPriorityProcess);
                            }
                        }
                    }
                }
            }
        }
        return addProcess(priority);
    }

    /**
     * @param identifier PID to be used to find and kill the process
     * @return true if killed successfully otherwise false
     * @throws ProcessManagerException
     */
    public boolean killProcessWithPID(String identifier) throws ProcessManagerException {
        Process process = findProcessWithPID(identifier);
        return processes.remove(process);
    }

    /**
     * @param priority Priority of the processes to be killed
     * @return true if killed successfully otherwise false
     * @throws ProcessManagerException if no processes exist with specified priority
     */
    public boolean killProcessWithPriority(Priority priority) throws ProcessManagerException {

        List<Process> processesFound = new ArrayList<>();
        for (Process process : processes) {
            if (priority.rank == process.getPriority().rank) {
                processesFound.add(process);
            }
        }

        if (processesFound.isEmpty()) {
            throw new ProcessManagerException("No Processes could be found with priority : " + priority);
        }
        return processes.removeAll(processesFound);
    }

    public void killAllProcesses() {
        processes.clear();
    }

    private Process findProcessWithPID(String identifier) throws ProcessManagerException {
        for (Process process : processes) {
            if (identifier.equals(process.getProcessIdentifier())) {
                return process;
            }
        }
        throw new ProcessManagerException("No Process could be found with PID : " + identifier);
    }

    /**
     * @param sortBy Attribute to sort the processes with
     * @return List of processes within Task manager sorted
     */
    public List<Process> listAllProcesses(Process.SortBy sortBy) {

        List<Process> processesToSort = new ArrayList<>();
        processesToSort.addAll(processes);
        Collections.sort(processesToSort, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {

                if (Process.SortBy.priority.equals(sortBy)) {
                    return p2.getPriority().compareTo(p1.getPriority());
                } else if (Process.SortBy.createdAt.equals(sortBy)) {
                    return p1.getCreatedAt().compareTo(p2.getCreatedAt());
                } else {
                    return p1.getProcessIdentifier().compareTo(p2.getProcessIdentifier());
                }

            }
        });

        return processesToSort;
    }

    /**
     * @return List of all the processes within Task manager
     */
    public ConcurrentLinkedQueue<Process> listAllProcesses() {
        return processes;
    }


}

