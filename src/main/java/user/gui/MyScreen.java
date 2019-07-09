package user.gui;
//Alan Franas
/**
 * Klasa reprezentuje interfejs do wyswietlanie informacji na ekran
 */
public class MyScreen implements Screen
    {
    private UserSwing userSwing;

    public void setUserSwing(UserSwing userSwing)
        {
        this.userSwing = userSwing;
        }

    public synchronized void cleanGui()
        {
        waitIfUserGuiNull();
        userSwing.writePrompt(" downloading user list " );
        userSwing.cleanList();
        }

    @Override
    public int userCount()
        {
        return userSwing.userCount();
        }

    private void waitIfUserGuiNull()
        {
        while (userSwing == null)
            {
            try
                {
                /*czekamy az gui sie robi*/
                Thread.sleep(1000);
                }
            catch (InterruptedException e)
                {
                e.printStackTrace();
                }
            }
        }

    public synchronized void addToFileList(String fileName)
        {
        waitIfUserGuiNull();
        userSwing.writePrompt("adding file " + fileName);
        userSwing.showFile(fileName);
        }

    @Override
    public void addToUserList(String userName)
        {
        waitIfUserGuiNull();
        userSwing.writePrompt("adding user " + userName);
        userSwing.addToUserList(userName);
        }

    @Override
    public synchronized void deleteFromFileList(String fileName)
        {
        waitIfUserGuiNull();
        userSwing.writePrompt("removing file " + fileName);
        userSwing.deleteFile(fileName);
        }

    @Override
    public void writePrompt(String text)
        {
        waitIfUserGuiNull();
        userSwing.writePrompt(text);
        }
    }
