package server;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


import server.balancerForServer.TaskHandler;
import server.balancerForServer.TaskData;
//Alan Franas
/**
 * klasa reprezentuje watek dla klienta
 */
public class ThreadForServer implements Runnable
    {
    private Socket socket;
    private TaskHandler taskHandler;
    private TaskData task;

    /**
     * @param socket      socket z polaczenia z klientem
     * @param taskHandler organizer ruchu na dysku
     */
    public ThreadForServer(Socket socket, TaskHandler taskHandler)
        {
        this.socket = socket;
        this.taskHandler = taskHandler;
        }

    @Override
    public void run()
        {

        String head = getHeadFromUserRequest();
        String[] headString = head.split(":");

        if (userWantAction(headString, "save"))
            {
            saveFile(headString);
            }

        if (userWantAction(headString, "user"))
            {
            try
                {
                sendUsersAsString();
                }
            catch (IOException e)
                {
                Logger.getAnonymousLogger().log(Level.WARNING, "error with socket");
                }
            }

        if (userWantAction(headString, "file"))
            {
            ServerSwingGui.setField("sending files to client");
            try
                {
                findFileAndSend(headString[DataType.OWNER.value]);
                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            finally
                {
                TaskHandler.lockForLogs.unlock();
                }
            }
        }

    private void sendUsersAsString() throws IOException
        {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        String listUsers = TaskHandler.listUsersAsString();
        writer.println(listUsers);
        }

    private void saveFile(String[] headString)
        {
        task = new TaskData(
                headString[DataType.OWNER.value].trim(),
                headString[DataType.FILE_NAME.value].trim(),
                socket,
                headString[DataType.FRIEND.value]);

        taskHandler.saveFile(task);
        }

    private boolean userWantAction(String[] data, String action)
        {
        return data[DataType.TYPE.value].trim().equals(action);
        }

    private void findFileAndSend(String owner) throws IOException
        {
        TaskHandler.lockForLogs.lock();
        String user = owner.trim();
        Path path =
                Paths.get(System.getProperty("user.home")).resolve("log").resolve(user + ".txt");
        if (Files.exists(path))
            {
            trySendFileToUser(user, path);
            }
        else
            {
            Files.createFile(path);
            endOfList();
            }
        }

    private void trySendFileToUser(String user, Path path)
        {
        try
            {
            Integer count = taskHandler.getCount(user);

            Optional<Path> filePath =
                    Files.lines(path).map(e -> Paths.get(e)).skip(count).findFirst();

            if (filePath.isPresent())
                {
                sendFile(Files.newInputStream(filePath.get()), filePath.get().getFileName().toString());
                }
            else
                {
                endOfList();
                }
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        }

    private void endOfList() throws IOException
        {
        byte[] close = ("close:" + ":" + "\n").getBytes();
        socket.getOutputStream().write(close, 0, close.length);
        socket.close();
        }

    private void sendFile(InputStream in, String name)
        {
        try
            {
            byte[] head = ("file:" + name + ":" + "\n").getBytes();
            socket.getOutputStream().write(head, 0, head.length);


            int count;
            byte[] bytes = new byte[4096];
            while ((count = in.read(bytes)) > 0)
                {
                socket.getOutputStream().write(bytes, 0, count);
                }
            socket.close();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        }

    private String getHeadFromUserRequest()
        {
        byte[] charFromHead = new byte[1];
        byte[] head = new byte[1024];

        int i = 0;
        try
            {
            while (socket.getInputStream().read(charFromHead) > 0 & charFromHead[0] != '\n')
                {
                head[i] = charFromHead[0];
                i++;
                }

            }
        catch (IOException e)
            {
            Logger.getAnonymousLogger().log(Level.WARNING, "save file exception");
            }
        return new String(head);
        }

    enum DataType
        {
            OWNER(1), FILE_NAME(3), FRIEND(2), TYPE(0);

        int value;

        DataType(int value)
            {
            this.value = value;
            }
        }
    }

