package user;

import java.io.IOException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import user.gui.MyScreen;
import user.gui.UserSwing;

//Alan Franas
/**
 * Klasa uruchomieniowa dla klienta
 */
public class UserMain
    {
    public static String path;
    public static void main(String[] args) throws IOException, InterruptedException
        {
        String userName = "marek";
        String userPath = "marek";
        path = userPath;
        System.out.println(path);
        if (args.length == 2)
            {
            userName = args[0];
            userPath = args[1];
            }

        MyScreen screen = new MyScreen();
        MyUser user = new MyUser(userName, userPath, screen);
        Executor executor = Executors.newCachedThreadPool();

        //watek gui

        executor.execute(() ->
        {
        UserSwing userSwing1 = new UserSwing(user);
        screen.setUserSwing(userSwing1);
        });

        //dodawanie pliku.
        executor.execute(() ->
        {
        try
            {
            Thread.sleep(5000);

            while (true)
                {
                user.refreshFileList();
                Thread.sleep(1000);
                user.getUsersFromServer();
                Thread.sleep(1000);
                }
            }
        catch (IOException | InterruptedException e)
            {
            e.printStackTrace();
            }

        });

        executor.execute(() ->
        {
        try
            {
            while (true)
                {
                user.cleanFileList();
                Thread.sleep(3000);
                }
            }
        catch (IOException | InterruptedException e)
            {
            e.printStackTrace();
            }

        });
        executor.execute(() ->
        {
        try
            {
            while (true)
                {
                user.refreshServerFileList();
                Thread.sleep(5000);
                }
            }
        catch (InterruptedException e)
            {
            e.printStackTrace();
            }

        });
        }
    }

