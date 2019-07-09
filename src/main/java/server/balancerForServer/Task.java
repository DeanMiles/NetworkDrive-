package server.balancerForServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerSwingGui;
import static java.lang.Math.abs;
//Alan Franas

/**
 * Klasa realizuje zapis jednego pliku, pobiera sciezki do serwerow i pule taskow.
 */
public class Task implements Runnable
    {
    private LinkedBlockingQueue<Path> paths;
    private PriorityBlockingQueue<TaskData> tasks;
    private OutputStream out;
    private InputStream in;
    byte[] bytes = new byte[4096];

    /**
     * @param paths Lista serwerow
     * @param tasks Lista zadani do zapisania
     */
    public Task(LinkedBlockingQueue<Path> paths, PriorityBlockingQueue<TaskData> tasks)
        {
        this.paths = paths;
        this.tasks = tasks;
        }

    @Override
    public void run()
        {
        try
            {
            Path path = paths.take();//czeka az bedzie woly server
            TaskData task = tasks.take();//pobranie taska o naj. piorytecie
            Path filePath = makeFile(path, task);
            in = task.getClientSocket().getInputStream();
            out = null;

            if (!TaskHandler.fileExist(task.getFileOwner(), task.getFileName()))
                {
                wait(task);
                saveAndCopy(path, task, filePath);
                }
            else if (task.getShareTo().length() > 0 && !TaskHandler.fileExist(task.getShareTo(),
                    task.getFileName()))
                {
                wait(task);
                saveOnlyToOtherUser(path, task);
                }

            if (out != null)
                {
                out.close();
                }
            paths.put(path);
            task.getClientSocket().close();
            task.getTaskDone().set(true);
            }
        catch (Exception e)
            {
            e.printStackTrace();
            Logger.getAnonymousLogger().log(Level.WARNING, "problem with saving");
            }

        }

    private void saveOnlyToOtherUser(Path path, TaskData task) throws IOException
        {
        out =
                Files.newOutputStream(path.resolve(task.getShareTo()).resolve(task.getFileName()));
        writeFromUser();
        TaskHandler.addUserFilesRegister(task.getShareTo(),
                path.resolve(task.getShareTo()).resolve(task.getFileName()));
        }

    private void saveAndCopy(Path path, TaskData task, Path filePath) throws IOException
        {
        out = Files.newOutputStream(filePath, StandardOpenOption.CREATE);
        writeFromUser();
        TaskHandler.addUserFilesRegister(task.getFileOwner(), filePath);

        if (task.getShareTo().length() > 0 && !TaskHandler.fileExist(task.getShareTo(), task.getFileName()))
            {
            Files.copy(filePath, path.resolve(task.getShareTo()).resolve(task.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);
            TaskHandler.addUserFilesRegister(task.getShareTo(), path.resolve(task.getShareTo()).resolve(task.getFileName()));
            }
        }

    private void writeFromUser() throws IOException
        {
        int count;//ilosc danych odczytu

        while ((count = in.read(bytes)) > 0)
            {
            out.write(bytes, 0, count);
            }
        }

    private Path makeFile(Path path, TaskData task) throws IOException
        {
        if (!Files.exists(path.resolve(task.getFileOwner())))
            {
            Files.createDirectories(path.resolve(task.getFileOwner()));
            }

        if (!Files.exists(path.resolve(task.getShareTo())) && task.getShareTo().length() > 0)
            {
            Files.createDirectories(path.resolve(task.getShareTo()));
            }

        Path filePath = path.resolve(task.getFileOwner()).resolve(task.getFileName());

        return filePath;
        }

    private void wait(TaskData task)
        {
        ServerSwingGui.setField(task.getFileOwner() + " saves file" + task.getFileName() + " has " +
                "piorytet " + task.getPriority());
        try
            {
            Thread.sleep(abs(new Random().nextInt()) % 1000 * 5);
            }
        catch (InterruptedException e)
            {
            e.printStackTrace();
            }
        }
    }
