package server.balancerForServer;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;
//Alan Franas
/**
 * Klasa reprezentuje dane potrzebne do zapisania pliku na dysk serwera
 */

public class TaskData implements Comparable<TaskData>
    {
    private String fileOwner;
    private String fileName;
    private Socket clientSocket;
    private String shareTo = "";
    private Integer priority;

    private final AtomicReference<Boolean> taskDone = new AtomicReference<>(false);
    /**
     * @param fileOwner nazwa uzytkownika
     * @param fileName nazwa zapisywango pliku
     * @param clientSocket   referencja na clientSocket uzytkownika
     * @param shareTo   nazwa uzytkownika ktoremu udostepniamy plik
     */
    public TaskData(String fileOwner, String fileName, Socket clientSocket, String shareTo)
        {
        this.fileOwner = fileOwner;
        this.fileName = fileName;
        this.clientSocket = clientSocket;
        this.shareTo = shareTo;
        }

    /**
     * jezeli nie udostepniamy innym to uzywamy tego konstruktora
     *
     * @param fileOwner nazwa uzytkownika
     * @param fileName nazwa zapisywango pliku
     * @param clientSocket   referencja na clientSocket uzytkownika
     */
    public TaskData(String fileOwner, String fileName, Socket clientSocket)
        {
        this.fileOwner = fileOwner;
        this.fileName = fileName;
        this.clientSocket = clientSocket;
        }

    /**
     * @return zwraca true jezeli plik jest przeslany tz. task jest wykonany
     */
    public AtomicReference<Boolean> getTaskDone()
        {
        return taskDone;
        }

    //potrzbne do wyznacznia pioryteow
    @Override
    public int compareTo(TaskData o)
    {
    return this.priority - o.priority;
    }

    public String getFileOwner()
        {
        return fileOwner;
        }

    public void setFileOwner(String fileOwner)
        {
        this.fileOwner = fileOwner;
        }

    public String getFileName()
        {
        return fileName;
        }

    public void setFileName(String fileName)
        {
        this.fileName = fileName;
        }

    public Socket getClientSocket()
        {
        return clientSocket;
        }

    public void setClientSocket(Socket clientSocket)
        {
        this.clientSocket = clientSocket;
        }

    public String getShareTo()
        {
        return shareTo;
        }

    public void setShareTo(String shareTo)
        {
        this.shareTo = shareTo;
        }

    public Integer getPriority()
        {
        return priority;
        }

    public void setPriority(Integer priority)
        {
        this.priority = priority;
        }
    }
