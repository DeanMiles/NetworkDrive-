package user;

import java.io.IOException;
//Alan Franas
public interface User
    {

    String getName();

    boolean refreshFileList() throws IOException;

    boolean refreshServerFileList() throws IOException, InterruptedException;

    boolean getUsersFromServer();

    boolean sendFileToServer(UserFile myFile);

    boolean sendFileToServer(String name, String to);
    }
