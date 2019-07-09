package server;

/**
 * Klasa uruchomieniowa dla serwera
 */
//Alan Franas
public class ServerStart
    {
    public static void main(String[] args)
        {
        try
            {
            new Thread(new Server()).start();
            ServerSwingGui gui = new ServerSwingGui();

            new Thread(() ->
            {
            try
                {
                while (true)
                    {
                    gui.refresh();
                    gui.setField("refreshing list");
                    Thread.sleep(3000);
                    }
                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            }).start();

            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        }
    }
