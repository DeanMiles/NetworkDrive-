package user;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;

import server.Server;
import user.gui.Screen;

@RunWith(MockitoJUnitRunner.class)
public class MyUserTest
    {
    MyUser user;
    @Mock
    Screen screen;
    static Thread thread;

    @BeforeClass
    public static void setUpServer() throws Exception
        {
        thread = new Thread(new Server());
        thread.start();
        }

    @AfterClass
    public static void tearDown() throws Exception
        {
        thread.stop();
        }

    @Before
    public void setUp() throws Exception
        {
        user = new MyUser("test", "test", screen);
        }


    @org.junit.Test
    public void shouldNotRefreshIfFileEmpty() throws Exception
        {
        Assert.assertFalse(user.refreshFileList());
        }

    @org.junit.Test
    public void shouldRefreshIfFileNotEmpty() throws Exception
        {
        Files.createFile(user.getUserPath().resolve(fileName()));
        Assert.assertTrue(user.refreshFileList());
        Files.delete(user.getUserPath().resolve(fileName()));
        }

    @Test
    public void shouldReturnTrueRefreshIfDeletedFile() throws IOException
        {
        Files.createFile(user.getUserPath().resolve(fileName()));
        Assert.assertTrue(user.refreshFileList());

        Files.delete(user.getUserPath().resolve(fileName()));
        Assert.assertTrue(user.cleanFileList());
        }

    private String fileName()
        {
        return "te.txe";
        }
    }
