
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TaskList extends JFrame implements ActionListener{

    public static ArrayList<Todo> todoList = new ArrayList<Todo>();
    public static ArrayList<Category> categoryList = new ArrayList<Category>();
    public static ArrayList<Priority> priorityList = new ArrayList<Priority>();

    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem categoryMenuItem, priorityMenuItem, exitMenuItem, editMenuItem, deleteMenuItem;
    JPanel mainPanel, leftPanel, rightPanel;
    JTextField txtTitle, txtComplatePercent;
    JComboBox comboCategory, combpPriority;
    JTextArea txtNote;


    TaskList() {
        //Frame Settings
        this.setTitle("Todo List");
        
       

        //MenuBar Settings

        categoryMenuItem = new JMenuItem("Kategori İşlemleri"); 
        categoryMenuItem.addActionListener(this);       
        priorityMenuItem = new JMenuItem("Öncelik İşlemleri");
        priorityMenuItem.addActionListener(this);
        exitMenuItem = new JMenuItem("Çıkış");
        exitMenuItem.addActionListener(this);
        editMenuItem = new JMenuItem("Düzenle");
        editMenuItem.addActionListener(this);
        deleteMenuItem = new JMenuItem("Sil");
        deleteMenuItem.addActionListener(this);

        menuBar = new JMenuBar();       
        fileMenu = new JMenu("Dosya");
        fileMenu.add(categoryMenuItem);
        fileMenu.add(priorityMenuItem);
        fileMenu.add(exitMenuItem);

        editMenu = new JMenu("Düzen");
        editMenu.add(editMenuItem);
        editMenu.add(deleteMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);  

        this.setJMenuBar(menuBar);



        //Panel Settings

        mainPanel = new JPanel(new GridLayout(1,2));
        leftPanel= new JPanel(new GridBagLayout());
        

        rightPanel= new JPanel(new GridLayout(7,1));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(10,10,10,10));

        txtTitle =new JTextField();
        txtTitle.setBorder(BorderFactory.createTitledBorder("Başlık"));

        txtComplatePercent =new JTextField();
        txtComplatePercent.setBorder(BorderFactory.createTitledBorder("Tamamlandı ( % )"));

        txtNote =new JTextArea();
        txtNote.setBorder(BorderFactory.createTitledBorder("Note"));
        

        


        rightPanel.add(txtTitle);
        rightPanel.add(txtComplatePercent);
        rightPanel.add(txtNote);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);


        this.add(mainPanel);

            
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
    }
}
