package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.swing.*;
//Alan Franas
import server.balancerForServer.TaskHandler;

/**
 * klasa wyswietla stan serwera na ekran
 */
public class ServerSwingGui extends JFrame
{


    private JList<String> fileList1;
    private JList<String> fileList2;
    private JList<String> fileList3;
    private JList<String> fileList4;
    private JList<String> fileList5;

    private DefaultListModel<String> fliesListData1;
    private DefaultListModel<String> fliesListData2;
    private DefaultListModel<String> fliesListData3;
    private DefaultListModel<String> fliesListData4;
    private DefaultListModel<String> fliesListData5;
    private Path serverPath;

    /**
     * @param text umozliwia ustawienie komunikatu o aktualnej akcji
     */
    public static synchronized void setField(String text)
    {
        ServerSwingGui.field.setText(text);
    }

    private static JTextField field = new JTextField("server ready", JLabel.CENTER);

    public ServerSwingGui() throws IOException
    {
        super("Server OneDrive");
        field.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(new Dimension(1000, 600));

        JLabel jLabel0 = new JLabel("server 1",JLabel.CENTER);
        jLabel0.setBounds(0, 440, 200, 20);

        JLabel jLabel = new JLabel("server 2",JLabel.CENTER);
        jLabel.setBounds(200, 440, 200, 20);

        JLabel jLabel2 = new JLabel("server 3",JLabel.CENTER);
        jLabel2.setBounds(400, 440, 200, 20);

        JLabel jLabel3 = new JLabel("server 4",JLabel.CENTER);
        jLabel3.setBounds(600, 440, 200, 20);

        JLabel jLabel4 = new JLabel("server 5",JLabel.CENTER);
        jLabel4.setBounds(800, 440, 200, 20);


        serverPath = Paths.get(System.getProperty("user.home"));

        fliesListData1 = new DefaultListModel<>();

        fileList1 = new JList(fliesListData1); //data has type Object[]
        fileList1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList1.setVisibleRowCount(-1);
        fileList1.setBounds(0, 0, 200, 440);
        fileList1.setBackground(new Color(245,245,245));
        listFiles(serverPath, 0, fliesListData1);


        fliesListData2 = new DefaultListModel<>();
        fileList2 = new JList(fliesListData2); //data has type Object[]
        fileList2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList2.setVisibleRowCount(-1);
        fileList2.setBounds(200, 0, 200, 440);
        listFiles(serverPath, 1, fliesListData2);

        fliesListData3 = new DefaultListModel<>();
        fileList3 = new JList(fliesListData3); //data has type Object[]
        fileList3.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList3.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList3.setVisibleRowCount(-1);
        fileList3.setBounds(400, 0, 200, 440);
        fileList3.setBackground(new Color(245,245,245));
        listFiles(serverPath, 2, fliesListData3);

        fliesListData4 = new DefaultListModel<>();
        fileList4 = new JList(fliesListData4); //data has type Object[]
        fileList4.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList4.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList4.setVisibleRowCount(-1);
        fileList4.setBounds(600, 0, 200, 440);
        listFiles(serverPath, 3, fliesListData4);

        fliesListData5 = new DefaultListModel<>();
        fileList5 = new JList(fliesListData5); //data has type Object[]
        fileList5.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList5.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList5.setVisibleRowCount(-1);
        fileList5.setBounds(800, 0, 200, 440);
        fileList5.setBackground(new Color(245,245,245));
        listFiles(serverPath, 4, fliesListData5);

        field.setBounds(375, 500, 250, 50);
        field.setBorder(BorderFactory.createLineBorder(Color.lightGray));//obramowka
        field.setFont(new Font("Arial", Font.BOLD, 18));
        field.setHorizontalAlignment(JTextField.CENTER);

        setLayout(null);
        add(jLabel);
        add(jLabel0);
        add(jLabel2);
        add(jLabel3);
        add(jLabel4);
        add(fileList1);
        add(fileList2);
        add(fileList3);
        add(fileList4);
        add(fileList5);
        add(field);

        setVisible(true);
    }

    /**
     * aktualizuje graficzna zawartosc serwera
     *
     * @throws IOException
     */
    public void refresh() throws IOException
    {
        listFiles(serverPath, 0, fliesListData1);
        listFiles(serverPath, 1, fliesListData2);
        listFiles(serverPath, 2, fliesListData3);
        listFiles(serverPath, 3, fliesListData4);
        listFiles(serverPath, 4, fliesListData5);
    }

    private void listFiles(Path serverPath, int server, DefaultListModel<String> list) throws IOException
    {
        TaskHandler.lockForLogs.lock();
        list.clear();
        Files.list(serverPath.resolve("server" + server)).flatMap((e) ->
        {
            try
            {
                return Files.list(e);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            return null;
        }).map(e -> e.getFileName().toString()).filter(Objects::nonNull).forEach(e -> list.addElement(e));
        TaskHandler.lockForLogs.unlock();
    }
}
