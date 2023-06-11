
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.DateFormatter;

public class TodoList extends JFrame implements ActionListener {

    ArrayList<Todo> todoList = new ArrayList<Todo>();
    ArrayList<Category> categoryList = new ArrayList<Category>();
    ArrayList<Priority> priorityList = new ArrayList<Priority>();
    int selectedCategoryId = 0, selectedPriorityId = 0;

    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem todoMenuItem, categoryMenuItem, priorityMenuItem, exitMenuItem, editMenuItem, deleteMenuItem;
    JPanel mainPanel, leftPanel, rightPanel;
    JTextField txtTitle, txtComplatePercent;
    JComboBox comboCategory, combpPriority;
    JTextArea txtNote;
    JScrollPane scrollTxtNote, scrollTodoListView;
    JFormattedTextField txtStartDate, txtEndDate;
    JButton btnAdd, btnEdit, btnDelete;
    JList todoListView;

    TodoList() {
        getTodoList();
        getCategoryList();
        getPriorityList();
        // Frame Settings

        // MenuBar Settings
        todoMenuItem = new JMenuItem("Kategori İşlemleri");
        todoMenuItem.addActionListener(this);
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
        fileMenu.add(todoMenuItem);
        fileMenu.add(categoryMenuItem);
        fileMenu.add(priorityMenuItem);
        fileMenu.add(exitMenuItem);

        editMenu = new JMenu("Düzen");
        editMenu.add(editMenuItem);
        editMenu.add(deleteMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        this.setJMenuBar(menuBar);

        // Panel Settings
        mainPanel = new JPanel(new GridLayout(1, 2));
        leftPanel = new JPanel(new GridLayout(1, 1));

        rightPanel = new JPanel(new GridLayout(8, 1));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtTitle = new JTextField();
        txtTitle.setBorder(BorderFactory.createTitledBorder("Başlık"));

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter df = new DateFormatter(format);
        txtStartDate = new JFormattedTextField(df);
        txtStartDate.setBorder(BorderFactory.createTitledBorder("Başlangıç tarihi"));
        txtStartDate.setValue(new Date());

        txtEndDate = new JFormattedTextField(df);
        txtEndDate.setBorder(BorderFactory.createTitledBorder("Bitiş tarihi"));
        txtEndDate.setValue(new Date());

        comboCategory = new JComboBox(getCategoryModel());
        comboCategory.setName("comboCategory");
        comboCategory.setBorder(BorderFactory.createTitledBorder("Note"));
        comboCategory.setRenderer(new CategoryRenderer());
        comboCategory.addActionListener(this);

        combpPriority = new JComboBox(getPriorityModel());
        combpPriority.setName("combpPriority");
        combpPriority.setBorder(BorderFactory.createTitledBorder("Öncelik Seviyesi"));
        combpPriority.setRenderer(new PriorityRenderer());
        combpPriority.addActionListener(this);

        txtComplatePercent = new JTextField("0");
        txtComplatePercent.setBorder(BorderFactory.createTitledBorder("Tamamlandı ( % )"));

        txtNote = new JTextArea();
        txtNote.setLineWrap(true);
        scrollTxtNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTxtNote.setBorder(BorderFactory.createTitledBorder("Note"));

        Box boxButton = Box.createHorizontalBox();

        btnAdd = new JButton("Ekle");
        btnAdd.addActionListener(this);
        btnAdd.setName("btnAdd");
        btnEdit = new JButton("Düzenle");
        btnEdit.setName("btnEdit");
        btnEdit.addActionListener(this);
        btnEdit.setVisible(false);
        btnDelete = new JButton("Sil");
        btnDelete.setName("btnDelete");
        btnDelete.addActionListener(this);
        btnDelete.setVisible(false);

        boxButton.add(btnAdd);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnEdit);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnDelete);

        rightPanel.add(txtTitle);
        rightPanel.add(txtStartDate);
        rightPanel.add(txtEndDate);
        rightPanel.add(comboCategory);
        rightPanel.add(combpPriority);
        rightPanel.add(txtComplatePercent);
        rightPanel.add(scrollTxtNote);
        rightPanel.add(boxButton);

        todoListView = new JList<>(getTodoModel());
        todoListView.setCellRenderer(new TodoListCellRenderer());

        scrollTodoListView = new JScrollPane(todoListView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTodoListView.setBorder(BorderFactory.createTitledBorder("Yapılacaklar Listesi"));

        leftPanel.add(scrollTodoListView);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        this.add(mainPanel);

    }

    private void getTodoList() {
        todoList.add(new Todo(1, "Spor yap", LocalDate.now(), LocalDate.now(), 1, 1, 0, "MAC Kanyonda yap"));
        todoList.add(new Todo(1, "Mesaie kal", LocalDate.now(), LocalDate.now(), 2, 3, 0, "18-22 mesaisi"));
    }

    class CategoryRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value != null) {
                Category item = (Category) value;
                // System.out.println(item.getTitle());
                setText(item.getTitle());
            }
            return this;
        }
    }

    class PriorityRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value != null) {
                Priority item = (Priority) value;
                setText(item.getTitle());
            }
            return this;
        }
    }

    private Vector getTodoModel() {
        Vector model = new Vector();

        for (Todo object : todoList) {
            model.addElement(new Todo(object.id, object.title, object.startDate, object.endDate, object.categoryId,
                    object.priorityId, object.complatePercent, object.note));
        }
        return model;
    }

    private Vector getCategoryModel() {
        Vector model = new Vector();
        model.addElement(new Category(0, "Seçiniz"));

        for (Category object : categoryList) {
            model.addElement(new Category(object.id, object.title));
        }
        return model;
    }

    private Vector getPriorityModel() {
        Vector model = new Vector();
        model.addElement(new Priority(0, "Seçiniz"));
        for (Priority object : priorityList) {
            model.addElement(new Priority(object.id, object.title));
        }
        return model;
    }

    private void getCategoryList() {
        categoryList.add(new Category(1, "Kişisel"));
        categoryList.add(new Category(2, "Sosyal"));
        categoryList.add(new Category(3, "İş"));
        categoryList.add(new Category(4, "Okul"));
        categoryList.add(new Category(5, "Diğer"));
    }

    private void getPriorityList() {
        priorityList.add(new Priority(1, "Düşük"));
        priorityList.add(new Priority(2, "Normal"));
        priorityList.add(new Priority(3, "Yüksek"));
        priorityList.add(new Priority(4, "Acil"));
        priorityList.add(new Priority(5, "Kritik"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String className = e.getSource().getClass().getName();

        System.out.println(className);

        if (className == "javax.swing.JComboBox") {
            JComboBox element = (JComboBox) e.getSource();

            if (element.getName() == "comboCategory") {
                Category selectedItem = (Category) element.getSelectedItem();
                selectedCategoryId = selectedItem.id;
            }

            if (element.getName() == "comboPriority") {
                Priority selectedItem = (Priority) element.getSelectedItem();
                selectedPriorityId = selectedItem.id;
            }

        } else if (className == "javax.swing.JButton") {
            JButton button = (JButton) e.getSource();
            if(button.getName()=="btnAdd"){

                Todo todo=new Todo(todoList.size()==0?1:todoList.get(todoList.size()-1).id++, txtTitle.getText(), LocalDate.parse(txtStartDate.getText()), LocalDate.parse(txtEndDate.getText()), selectedCategoryId, selectedPriorityId, 0, txtNote.getText());
            
                todoList.add(todo);
            }


            System.out.println("buton tıklandı");
        } else if (className == "javax.swing.JMenuItem") {
            System.out.println("menü item tıklandı");
        }

    }

}
