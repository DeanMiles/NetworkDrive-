package user;

import java.nio.file.Path;
//Alan Franas
/**
 * klasa reprezentuje plik
 */
public class UserFile
    {
    private String name;
    private String owner;
    private String shareTo = "";
    public static Path path;

    public String getOwner()
        {
        return owner;
        }

    public Path getFilePath()
        {
        return path.resolve(name);
        }

    public void setOwner(String owner)
        {
        this.owner = owner;
        }

    public String getShareTo()
        {
        return shareTo;
        }

    public void setShareTo(String shareTo)
        {
        this.shareTo = shareTo;
        }

    public UserFile(String name)
        {
        this.name = name;
        }

    /**
     * @param name nazwa usera
     * @param owner owner pliku
     * @param shareTo user do ktorego wyslamy
     */
    public UserFile(String name, String owner, String shareTo)
        {
        this.name = name;
        this.owner = owner;
        this.shareTo = shareTo;
        }

    public String getName()
        {
        return name;
        }

    public void setName(String name)
        {
        this.name = name;
        }
    }
