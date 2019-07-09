package user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import server.Server;
import user.gui.Screen;

/**
 * klasa wykonuje task zlecony przez usera
 * miedzy innymi wysyla i pobiera
 *
 * @see TaskData
 */
//Alan Franas
public class TaskDispatcher implements Runnable
    {
    private Screen screen;
    private Socket socket;
    private TaskData task;
    private OperationType type;

    /**
     * @see TaskDispatcher#TaskDispatcher(Screen, TaskData, OperationType)
     */
    public TaskDispatcher(Screen screen, TaskData task)
        {
        this(screen, task, OperationType.SEND_FILE);
        }

    /**
     * @param screen interfejs do wyswietlanai na ekran
     * @param task   zadanie do wykonania
     * @param type   typ zadania
     */
    public TaskDispatcher(Screen screen, TaskData task, OperationType type)
        {
        this.task = task;
        this.screen = screen;
        socket = new Socket();
        this.type = type;
        }

    /**
     * @see TaskDispatcher#TaskDispatcher(Screen, TaskData, OperationType)
     */
    public TaskDispatcher(Screen screen, OperationType type)
        {
        this.screen = screen;
        socket = new Socket();
        this.type = type;
        }

    @Override
    public void run()
        {
        try
            {
            connection();
            }
        catch (IOException e)
            {
            screen.writePrompt("server overpower");
            }
        }

    private void connection() throws IOException
        {
        socket.connect(new InetSocketAddress("localhost", Server.PORT), 1000);


        OutputStream out = socket.getOutputStream();
        InputStream in = null;

        if (type == OperationType.SEND_FILE)
            {
            in = Files.newInputStream(task.getFilePath());
            sendFile(out, in);
            }
        if (type == OperationType.GET_USERS)
            {
            in = socket.getInputStream();
            sendRequestForUser(out);
            refreshUserList(in);
            }
        if (type == OperationType.GET_FILES)
            {
            in = socket.getInputStream();
            sendRequestForFile(out);

            String[] headLine = getFileName();

            if (!headLine[0].equals("close"))
                {
                closeOption(in, headLine[1]);
                }
            else
                {
                task.getUser().setReady();
                }
            }

        out.close();
        in.close();
        socket.close();
        }

    private void closeOption(InputStream in, String fileName1) throws IOException
        {
        String fileName = fileName1;
        OutputStream fileOut = Files.newOutputStream(task.getUser().getUserPath().resolve(fileName),
                StandardOpenOption.CREATE);
        readFile(in, fileOut);
        fileOut.close();
        }

    private void readFile(InputStream in, OutputStream fout) throws IOException
        {
        int count;//ilosc danych odczytu
        byte[] bytes = new byte[4096];

        while ((count = in.read(bytes)) > 0)
            {
            fout.write(bytes, 0, count);
            }
        }

    private String[] getFileName()
        {
        return getHead().split(":");
        }

    private void refreshUserList(InputStream in)
        {
        Scanner scanner = new Scanner(in);
        List<String> list =
                Arrays.stream(scanner.nextLine().split(":")).collect(Collectors.toList());

        if (list.size() != screen.userCount())
            {
            screen.cleanGui();
            list.forEach(e -> screen.addToUserList(e));
            }
        }

    private void sendFile(OutputStream out, InputStream in) throws IOException
        {
        sendHead(out);
        sendBody(out, in);
        }

    private void sendHead(OutputStream out) throws IOException
        {
        byte[] head =
                ("save:" + task.getUserName() + ":" + task.getSendTo() + ":" + task.getFilePath().getFileName() + "\n").getBytes();
        out.write(head, 0, head.length);
        }

    private void sendRequestForUser(OutputStream out) throws IOException
        {
        byte[] head = "user:\n".getBytes();
        out.write(head, 0, head.length);
        }

    private void sendRequestForFile(OutputStream out) throws IOException
        {
        byte[] head = ("file:" + task.getUserName() + "\n").getBytes();
        out.write(head, 0, head.length);
        }

    private void sendBody(OutputStream out, InputStream in) throws IOException
        {
        readFile(in, out);
        }

    enum OperationType
        {
            SEND_FILE, GET_USERS, GET_FILES;
        }

    private String getHead()
        {
        byte[] head = new byte[1024];
        byte[] charFromHead = new byte[1];
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
            e.printStackTrace();
            }
        return new String(head);
        }
    }
