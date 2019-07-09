package user.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;
import user.User;
import user.UserMain;

//Alan Franas
/**
 * reperezentuje wyswietlacz stanu usera
 */
public class UserSwing extends JFrame
    {

    private JList<String> fileList;
    private JList<String> userList;
    private DefaultListModel<String> fliesListData;
    private DefaultListModel<String> userListData;
    private User user;
    private JTextField action;
    private JTextField statistics;
    private JTextField stat1;
    private JTextField stat2;
    private JTextField stat3;
    private JTextField FileTittle;
    private JTextField UserTittle;
    private Lock lock = new ReentrantLock();
    private boolean flagMode = false;


    public UserSwing(User user)
        {
        super(user.getName());
        this.user = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(new Dimension(400, 760));

        fliesListData = new DefaultListModel<>();

        fileList = new JList(fliesListData); //data has type Object[]
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList.setVisibleRowCount(-1);
        fileList.setBounds(0, 30, 330, 390);
        fileList.setBackground(new Color(245,245,245));

        userListData = new DefaultListModel<>();

        userList = new JList(userListData);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userList.setVisibleRowCount(-1);
        userList.setBounds(330, 30, 70, 390);
        userList.setBackground(new Color(245,245,245));

        JTextField sep = new JTextField("STATISTICS");
        sep.setHorizontalAlignment(JTextField.CENTER);
        sep.setBounds(328, 30, 4, 388);
        sep.setBackground(new Color(245,245,245));

        JButton share = new JButton("Share");
        share.setBounds(0, 420, 400, 30);
        share.setBackground(Color.white);
        share.addActionListener(new AbstractAction()
            {
            @Override
            public void actionPerformed(ActionEvent e)
                {
                if (fileList.getSelectedValue() == null || userList.getSelectedValue() == null)
                    {
                    return;
                    }
                user.sendFileToServer(fileList.getSelectedValue(), userList.getSelectedValue());
                action.setText("wyslano " + fileList.getSelectedValue() + " do " + userList.getSelectedValue());
                }
            });


        /** Controls the amount of files on disc*/
        FileTittle = new JTextField("List of the files");
        FileTittle.setHorizontalAlignment(JTextField.CENTER);
        FileTittle.setBounds(0, 0, 330, 30);
        FileTittle.setFont(new Font("Arrial",Font.BOLD,13));

        UserTittle = new JTextField("Users");
        UserTittle.setHorizontalAlignment(JTextField.CENTER);
        UserTittle.setBounds(330, 0, 70, 30);
        UserTittle.setFont(new Font("Arrial",Font.BOLD,13));


        JButton mode = new JButton("Dark mode ");
        mode.setBounds(130, 640, 140, 30);
        mode.setBackground(Color.white);
        mode.addActionListener(new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                if(flagMode==false) {
                        //nocny
                        share.setBackground(Color.black);
                        share.setText("<html><Font Color=white>"+"SHARE"+"</Font><Font Color=black> </font></html>");

                        userList.setForeground(Color.WHITE);
                        userList.setBackground(new Color(43,43,43));
                        fileList.setForeground(Color.WHITE);
                        fileList.setBackground(new Color(43,43,43));

                        mode.setText("<html><Font Color=white>"+"DayMode"+"</Font><Font Color=black> </font></html>");
                        mode.setBackground(Color.BLACK);

                        action.setForeground(Color.white);
                        action.setBackground(new Color(43,43,43));
                        setBackground(Color.black);

                        statistics.setForeground(Color.white);
                        statistics.setBackground(new Color(43,43,43));

                        FileTittle.setForeground(Color.white);
                        FileTittle.setBackground(new Color(43,43,43));
                        UserTittle.setForeground(Color.white);
                        UserTittle.setBackground(new Color(43,43,43));

                        stat1.setForeground(Color.white);
                        stat1.setBackground(new Color(43,43,43));
                        stat2.setForeground(Color.white);
                        stat2.setBackground(new Color(43,43,43));
                        stat3.setForeground(Color.white);
                        stat3.setBackground(new Color(43,43,43));

                        stat2.setText("Nb. of files: "+fileList.getModel().getSize());
                        flagMode = true;
                    }
                    else {
                        share.setBackground(Color.white);
                        share.setText("<html><Font Color=black>"+"SHARE"+"</Font><Font Color=black> </font></html>");

                        userList.setBackground(new Color(245,245,245));
                        userList.setForeground(Color.BLACK);
                        fileList.setForeground(Color.black);
                        fileList.setBackground(new Color(245,245,245));

                        mode.setText("<html><Font Color=#2b2b2b>"+"NightMode"+"</Font><Font Color=black> </font></html>");
                        mode.setBackground(Color.white);

                        action.setForeground(Color.black);
                        action.setBackground(Color.white);

                        statistics.setForeground(Color.black);
                        statistics.setBackground(Color.white);

                        UserTittle.setForeground(Color.black);
                        UserTittle.setBackground(Color.white);
                        FileTittle.setForeground(Color.black);
                        FileTittle.setBackground(Color.white);

                        stat1.setForeground(Color.black);
                        stat1.setBackground(Color.white);
                        stat2.setForeground(Color.black);
                        stat2.setBackground(Color.white);
                        stat3.setForeground(Color.black);
                        stat3.setBackground(Color.white);

                        stat2.setText("Nb. of files: "+fileList.getModel().getSize());
                        add(sep);
                        flagMode = false;
                    }
                }
            });


        /** Controls the amount of memmory*/
        stat1 = new JTextField("Memorry");
        stat1.setBounds(0, 600, 134, 30);
        stat1.setFont(new Font("Arrial",Font.BOLD,13));
        stat1.setHorizontalAlignment(JTextField.CENTER);
        //Path path = Paths.get("C:\\Users\\Alan\\marys");
        Path path = Paths.get("C:\\Users\\Alan\\"+UserMain.path);
        File currentDir = new File(String.valueOf(path));
        double size = currentDir.length()/1024;
        stat1.setText("Used space: "+size+"MB");

        /** Controls the amount of files on disc*/
        stat2 = new JTextField("Number of Files");
        stat2.setHorizontalAlignment(JTextField.CENTER);
        stat2.setBounds(133, 600, 134, 30);
        stat2.setFont(new Font("Arrial",Font.BOLD,13));
        stat2.setText("Nb. of files: "+fileList.getModel().getSize());

        /** Controls the lest edition*/
        stat3 = new JTextField("Last Edition");
        stat3.setHorizontalAlignment(JTextField.CENTER);
        stat3.setBounds(266, 600, 134, 30);
        stat3.setFont(new Font("Arrial",Font.BOLD,13));
            /** banner*/
        statistics = new JTextField("STATISTICS");
        statistics.setHorizontalAlignment(JTextField.CENTER);
        statistics.setBounds(0, 572, 400, 30);
        statistics.setFont(new Font("Arrial",Font.BOLD,18));

        action = new JTextField("downloading files from server");
        action.setHorizontalAlignment(JTextField.CENTER);
        action.setBounds(0, 680, 400, 50);
        action.setFont(new Font("Arrial",Font.BOLD,18));

        /** Image */
        ImageIcon image = new ImageIcon("src\\main\\resources\\usb.png");
        JLabel label = new JLabel(image);
        label.setBounds(0,420,400,152);

        add(sep);
        add(label);
        add(FileTittle);
        add(UserTittle);
        add(stat1);
        add(stat2);
        add(stat3);
        add(statistics);
        setLayout(null);
        add(mode);
        add(fileList);
        add(userList);
        add(share);
        add(action);


        setVisible(true);

            /** Thread which control changes on file*/
        Thread thread = new Thread(() -> {
            WatchService watchService = null;
            try {
                watchService = FileSystems.getDefault().newWatchService();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //to moglo byc lepiej

            WatchKey watchKey = null;
            try {
                watchKey = path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true) {
                stat2.setText("Nb. of files: "+fileList.getModel().getSize());
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                    WatchEvent.Kind<Path> kind = watchEvent.kind();

                    System.out.println(watchEvent.context() + ", count: " +
                            watchEvent.count() + ", event: " + watchEvent.kind() + " was last modified at "
                            + path.toFile().lastModified());

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy-hh:mm:ss");
                    stat3.setText(dateFormat.format(path.toFile().lastModified()));
                }
            }

        });
        thread.start();



        }

    /**
     * doanie pliku do listy wyswietlanej
     *
     * @param fileName nawa pliku
     */
    public void showFile(String fileName)
        {
        lock.lock();
        fliesListData.addElement(fileName);
        action.setText("dodano plik " + fileName);
        lock.unlock();
        }

    /**
     * dodanie nazy usera do listy userow
     *
     * @param userName nazwa usera
     */
    public void addToUserList(String userName)
        {
        lock.lock();
        userListData.addElement(userName);
        action.setText("dodano uzytwownika " + userName);
        lock.unlock();
        }

    public void deleteFromUserList(String userName)
        {
        lock.lock();
        userListData.removeElement(userName);
        action.setText("usunieto uzytkownika " + userName);
        lock.unlock();
        }

    /**
     * usuwa plik z listy

     * @param fileName nazwa plku
     */
    public void deleteFile(String fileName)
        {
        lock.lock();
        fliesListData.removeElement(fileName);
        action.setText("usunieto plik " + fileName);
        lock.unlock();
        }

    public void cleanList()
        {
        lock.lock();
        userListData.clear();
        lock.unlock();
        }

    /**
     * @return licza wyswietlonych userow
     */
    public int userCount()
        {
        return userListData.size();
        }

    /**
     * pokazuje powiadomienie
     *
     * @param text text do wyswietlenia
     */
    public void writePrompt(String text)
        {
        lock.lock();
        action.setText(text);
        lock.unlock();
        }
    }
