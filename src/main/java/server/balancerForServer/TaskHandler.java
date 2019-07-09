package server.balancerForServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import server.ServerSwingGui;

import static java.nio.file.StandardOpenOption.APPEND;
//Alan Franas
/**
 * Klasa organizujaca ruch na dysku, rozdziela pliki rowno na 5 dyskow
 */
public class TaskHandler
    {
    private LinkedBlockingQueue<Path> folderPaths;
    private static Path logPath;
    private PriorityBlockingQueue<TaskData> tasks;
    private Map<String, Integer> userPriority;
    public static volatile Lock lockForLogs = new ReentrantLock();
    private static Map<String, Integer> countDownloadedFiles = new HashMap<>();


    /**
     * @param folderPaths lista 5 serwerow
     */
    public TaskHandler(ArrayList<Path> folderPaths)
        {
        userPriority = new HashMap<>();
        logPath = Paths.get(System.getProperty("user.home")).resolve("log");
        tasks = new PriorityBlockingQueue<>();
        this.folderPaths = new LinkedBlockingQueue<>(folderPaths);
        }

    /**
     * @param task generuje task do zapisu pliku na dysk, wyznacza piorytet tasku
     */
    public synchronized void saveFile(TaskData task)
        {

        lockForLogs.lock();
        Integer priority = userPriority.getOrDefault(task.getFileOwner(), 0);
        userPriority.put(task.getFileOwner(), ++priority);
        task.setPriority(priority);
        tasks.add(task);
        new Thread(new Task(folderPaths, tasks)).start();
        lockForLogs.unlock();
        }

    /**
     * dodanie logow o zapisie na dysku
     *
     * @param user user dla ktorego zapisujemy
     * @param file plik, ktory zostal zapisany
     * @throws IOException
     */
    public static synchronized void addUserFilesRegister(String user, Path file) throws IOException
        {
        try
            {
            lockForLogs.lock();
            OutputStream os = Files.newOutputStream(logPath.resolve(user + ".txt"), APPEND);
            PrintWriter writer = new PrintWriter(os);
            writer.println(file.toString());
            writer.flush();
            writer.close();
            }
        finally
            {
            clearDownloadedList();//powiadami userow ze liczba plikow sie zmienila musza je
            // pobrac od nowa
            lockForLogs.unlock();
            }
        }

    /**
     * sprawdzamy czy user ma juz ten plik
     *
     * @param user     user dla ktorego sprawdzamy
     * @param fileName nazwa plku ktory sprawdzamy
     * @return czy uzytkownik posiada ten plik
     * @throws IOException
     */
    public static synchronized boolean fileExist(String user, String fileName) throws
            IOException
        {
        try
            {
            lockForLogs.lock();
            if (Files.exists(logPath.resolve(user + ".txt")))
                {
                return Files.lines(logPath.resolve(user + ".txt")).map(e -> Paths.get(e).getFileName().toString()).anyMatch(e -> e.equals(fileName));
                }
            }
        finally
            {
            lockForLogs.unlock();
            }
        return false;
        }

    /**
     * @return zwraca liste userow jako string
     * @throws IOException
     */
    //Metoda typu synchronized zajmuje zamek związany z obiektem, dla którego jest
    //wywoływana, będzie zatem wykluczać wykonanie innych metod typu synchronized lub bloków synchronized na tym obiekcie
    public static synchronized String listUsersAsString() throws IOException
        {
        lockForLogs.lock();
        ServerSwingGui.setField("pobieranie listy userow");
        StringBuilder stringBuilder = new StringBuilder();
        Files.list(logPath).map(e -> e.getFileName().toString().replaceAll(".txt", "")).forEach(e -> stringBuilder.append(e + ":"));
        lockForLogs.unlock();
        return stringBuilder.toString();
        }

    /**
     * @param user user dla ktorego sprawdzamy ile plikow pobral z serweras
     * @return ilosc pobranych plikow
     */
    public Integer getCount(String user)
        {
        lockForLogs.lock();
        Integer count = countDownloadedFiles.getOrDefault(user, 0);
        countDownloadedFiles.put(user, ++count);
        lockForLogs.unlock();
        return countDownloadedFiles.get(user) - 1;
        }

    public static void clearDownloadedList()
        {
        countDownloadedFiles.clear();
        }
    }
