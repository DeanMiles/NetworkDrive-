package server.loadBalancer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import server.balancerForServer.TaskHandler;
import server.balancerForServer.Task;
import server.balancerForServer.TaskData;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "server.*")
public class TaskHandlerTest
    {
    Path path;
    TaskHandler taskHandler;
    Task task;
    TaskData dataForTask;

    @Before
    public void setUp() throws Exception
        {
        dataForTask = new TaskData("user", "file", null);

        path = Paths.get(System.getProperty("user.home")).resolve("test");

        taskHandler = new TaskHandler(new ArrayList<Path>()
            {{
            add(path);
            }});

        task = mock(Task.class);
        whenNew(Task.class).withArguments(any(LinkedBlockingQueue.class), any(PriorityBlockingQueue.class)).thenReturn(task);
        }

    @Test
    public void shouldCreateOneSaveHandler() throws Exception
        {
        taskHandler.saveFile(dataForTask);
        verifyNew(Task.class).withArguments(any(LinkedBlockingQueue.class),
                any(PriorityBlockingQueue.class));
        }

    @Test
    public void shouldReturn2AsPriorityBecauseUserSaveFileTwoTimes()
        {
        taskHandler.saveFile(dataForTask);
        taskHandler.saveFile(dataForTask);
        Assert.assertThat(dataForTask.getPriority(), is(2));
        }

    @Test
    public void shouldReturn1AsPriority()
        {
        taskHandler.saveFile(dataForTask);
        Assert.assertThat(dataForTask.getPriority(), is(1));
        }
    @Test
    public void shouldReturn1AsCount()
        {
        taskHandler.getCount("user");
        taskHandler.getCount("user2");
        Integer count = taskHandler.getCount("user");
        Integer count2 = taskHandler.getCount("user2");
        Assert.assertThat(count, is(1));
        Assert.assertThat(count2, is(1));
        }

    @Test
    public void shouldReturnFalseBecauseUser1HasBiggerPriority()
        {
        TaskData data1 = new TaskData("user", "file", null);
        TaskData data2 = new TaskData("user", "file", null);
        TaskData data3 = new TaskData("user1", "file", null);

        taskHandler.saveFile(data1);
        taskHandler.saveFile(data2);
        taskHandler.saveFile(data3);

        Assert.assertFalse(data3.compareTo(data2) > 0);
        }

    @Test
    public void shouldReturnFalseBecauseUser1DoesntHaveBiggerPriority()
        {
        TaskData data1 = new TaskData("user", "file", null);
        TaskData data2 = new TaskData("user", "file", null);
        TaskData data3 = new TaskData("user1", "file", null);
        TaskData data4 = new TaskData("user1", "file", null);
        TaskData data5 = new TaskData("user1", "file", null);

        taskHandler.saveFile(data1);
        taskHandler.saveFile(data2);
        taskHandler.saveFile(data3);
        taskHandler.saveFile(data4);
        taskHandler.saveFile(data5);


        Assert.assertFalse(data5.compareTo(data2) < 0);

        }
    }
