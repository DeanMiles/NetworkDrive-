package user.gui;
//Alan Franas
public interface Screen
    {
    void addToFileList(String fileName);

    void addToUserList(String userName);

    void deleteFromFileList(String fileName);

    void writePrompt(String text);

    void cleanGui();

    int userCount();

    }
