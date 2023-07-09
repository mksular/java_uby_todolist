
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.DateFormatter;

public class TodoList extends JFrame implements ActionListener {

    ArrayList<Todo> todoList = new ArrayList<Todo>();
    ArrayList<Category> categoryList = new ArrayList<Category>();
    ArrayList<Priority> priorityList = new ArrayList<Priority>();
    int selectedCategoryId = 0, selectedPriorityId = 0;
    boolean isEdit = false;

    Todo selectedTodo, tempSelectedTodo;

    String categoryName, priorityName;

    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem todoMenuItem, categoryMenuItem, priorityMenuItem, exitMenuItem, editMenuItem, deleteMenuItem;
    JPanel mainPanel, leftPanel, rightPanel;
    JTextField txtTitle, txtComplatePercent;
    JComboBox comboCategory, comboPriority;
    JTextArea txtNote;
    JScrollPane scrollTxtNote, scrollTodoListView;
    JFormattedTextField txtStartDate, txtEndDate;
    JButton btnAdd, btnEdit, btnDelete, btnCancel, btnUpdate;
    JList todoListView;

    DefaultListModel todoListModel;

    Vector categoryListModel, priorityListModel;

    MysqlConn conn;

    ResultSet res;

    TodoList() throws Exception {
        conn = new MysqlConn();
        getTodoList();
        getCategoryList();
        getPriorityList();
        // Frame Settings

        // MenuBar Settings
        todoMenuItem = new JMenuItem("Todo İşlemleri");
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

        categoryListModel = new Vector();
        setCategoryListModel();
        comboCategory = new JComboBox(categoryListModel);
        comboCategory.setName("comboCategory");
        comboCategory.setBorder(BorderFactory.createTitledBorder("Kategori"));
        comboCategory.setRenderer(new CategoryRenderer());
        comboCategory.addActionListener(this);

        priorityListModel = new Vector();
        setPriorityListModel();

        comboPriority = new JComboBox(priorityListModel);
        comboPriority.setName("comboPriority");
        comboPriority.setBorder(BorderFactory.createTitledBorder("Öncelik Seviyesi"));
        comboPriority.setRenderer(new PriorityRenderer());
        comboPriority.addActionListener(this);

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

        btnCancel = new JButton("İptal");
        btnCancel.setName("btnCancel");
        btnCancel.addActionListener(this);
        btnCancel.setVisible(false);

        btnUpdate = new JButton("Kaydet");
        btnUpdate.setName("btnUpdate");
        btnUpdate.addActionListener(this);
        btnUpdate.setVisible(false);

        boxButton.add(btnAdd);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnEdit);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnDelete);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnCancel);
        boxButton.add(new JLabel(" "));
        boxButton.add(btnUpdate);

        rightPanel.add(txtTitle);
        rightPanel.add(txtStartDate);
        rightPanel.add(txtEndDate);
        rightPanel.add(comboCategory);
        rightPanel.add(comboPriority);
        rightPanel.add(txtComplatePercent);
        rightPanel.add(scrollTxtNote);
        rightPanel.add(boxButton);

        todoListModel = new DefaultListModel();
        setTodoListModel();
        todoListView = new JList<>(todoListModel);
        todoListView.setCellRenderer(new TodoListCellRenderer());

        ListSelectionListener sl = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    Todo currentTodo = (Todo) todoListView.getSelectedValue();
                    selectedTodo = currentTodo;

                    btnAdd.setVisible(false);
                    btnEdit.setVisible(true);
                    btnDelete.setVisible(true);
                    btnCancel.setVisible(true);
                    setRightPanel();

                }
            }
        };

        todoListView.addListSelectionListener(sl);

        scrollTodoListView = new JScrollPane(todoListView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTodoListView.setBorder(BorderFactory.createTitledBorder("Yapılacaklar Listesi"));

        leftPanel.add(scrollTodoListView);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        this.add(mainPanel);

    }

    private void getTodoList() throws Exception {

        String sql = "Select * from todo";
        res = conn.getData(sql);
        while(res.next()){
            System.out.println(res.getString("title"));
            todoList.add(new Todo(res.getInt("id"), res.getString("title"), LocalDate.parse(res.getDate("startdate").toString()), LocalDate.parse(res.getDate("enddate").toString()), res.getInt("categoryid"), res.getInt("priorityid"), res.getInt("complatepercent"), res.getString("note")));
        }

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

    private void setTodoListModel() {

        tempSelectedTodo = selectedTodo;
        todoListModel.removeAllElements();
        selectedTodo = tempSelectedTodo;

        for (Todo object : todoList) {
            todoListModel
                    .addElement(new Todo(object.id, object.title, object.startDate, object.endDate, object.categoryId,
                            object.priorityId, object.complatePercent, object.note));
        }
    }

    private void setCategoryListModel() {
        categoryListModel.removeAllElements();
        categoryListModel.addElement(new Category(0, "Seçiniz"));

        for (Category object : categoryList) {
            categoryListModel.addElement(new Category(object.id, object.title));
        }

    }

    private void setPriorityListModel() {
        priorityListModel.removeAllElements();
        priorityListModel.addElement(new Priority(0, "Seçiniz"));
        for (Priority object : priorityList) {
            priorityListModel.addElement(new Priority(object.id, object.title));
        }

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
            if (button.getName() == "btnAdd") {

                Todo todo = new Todo(todoList.size() == 0 ? 1 : todoList.get(todoList.size() - 1).id + 1,
                        txtTitle.getText(), LocalDate.parse(txtStartDate.getText()),
                        LocalDate.parse(txtEndDate.getText()), selectedCategoryId, selectedPriorityId, 0,
                        txtNote.getText());

                todoList.add(todo);
                JOptionPane.showMessageDialog(null, "Kayıt Eklendi!");
                setTodoListModel();
                setRightPanel();
            }

            if (button.getName() == "btnCancel") {
                btnAdd.setVisible(true);
                btnDelete.setVisible(false);
                btnEdit.setVisible(false);
                btnCancel.setVisible(false);
                selectedTodo = null;
                todoListView.setSelectedIndex(-1);
                todoListView.clearSelection();
                setRightPanel();

            }

            if (button.getName() == "btnDelete") {

                // System.out.println(selectedTodo.title + " silinecek");

                // System.out.println("kayıt silinmeden önceki kayıt sayısı " +
                // todoList.size());

                for (Todo item : todoList) {
                    if (item.id == selectedTodo.id) {
                        todoList.remove(item);
                    }
                }

                // System.out.println("kayıt silindikten sonraki kayıt sayısı " +
                // todoList.size());
                JOptionPane.showMessageDialog(null, "Kayıt Silindi");

                todoListView.setSelectedIndex(-1);
                todoListView.clearSelection();
                setTodoListModel();
                selectedTodo = null;
                setRightPanel();
                System.out.println("buton görünürlükleri düzenleniyor");
                btnAdd.setVisible(true);
                btnDelete.setVisible(false);
                btnEdit.setVisible(false);
                btnCancel.setVisible(false);
                System.out.println("buton görünürlükleri düzenlendi");

            }

            if (button.getName() == "btnEdit") {
                isEdit = true;
                btnAdd.setVisible(false);
                btnDelete.setVisible(false);
                btnEdit.setVisible(false);
                btnCancel.setVisible(false);
                btnUpdate.setVisible(true);
                setRightPanel();

            }

            if (button.getName() == "btnUpdate") {
                isEdit = false;
                btnAdd.setVisible(false);
                btnDelete.setVisible(true);
                btnEdit.setVisible(true);
                btnCancel.setVisible(true);
                btnUpdate.setVisible(false);
                System.out.println(selectedTodo.title + " kaydı güncelleniyor");
                updateTodo();
                setRightPanel();

            }

        } else if (className == "javax.swing.JMenuItem") {

        }

    }

    private void updateTodo() {

        for (Todo item : todoList) {
            if (item.id == selectedTodo.id) {
                item.categoryId = selectedCategoryId;
                item.priorityId = selectedPriorityId;
                item.complatePercent = Integer.parseInt(txtComplatePercent.getText());
                item.title = txtTitle.getText();
                item.note = txtNote.getText();
                item.startDate = LocalDate.parse(txtStartDate.getText());
                item.endDate = LocalDate.parse(txtEndDate.getText());
                selectedTodo = item;
            }
        }

        // System.out.println("kayıt silindikten sonraki kayıt sayısı " +
        // todoList.size());
        JOptionPane.showMessageDialog(null, "Kayıt Güncellendi");

        /*
         * todoListView.setSelectedIndex(-1);
         * todoListView.clearSelection();
         */

        setTodoListModel();
        setRightPanel();
    }

    private void setRightPanel() {
        if (selectedTodo != null) {
            System.out.println("buton durumu düzenleniyor");
            if (isEdit) {
                txtTitle.enable();
                txtStartDate.enable();
                txtEndDate.enable();
                comboCategory.enable();
                comboPriority.enable();
                txtComplatePercent.enable();
                txtNote.enable();
            } else {
                txtTitle.disable();
                txtStartDate.disable();
                txtEndDate.disable();
                comboCategory.disable();
                comboPriority.disable();
                txtComplatePercent.disable();
                txtNote.disable();
            }
            System.out.println("buton durumu düzenlendi");

            txtTitle.setText(selectedTodo.title);

            txtStartDate.setText(selectedTodo.startDate.toString());
            // System.out.println(selectedTodo.startDate);

            txtEndDate.setText(selectedTodo.endDate.toString());

            for (Category category : categoryList) {
                if (selectedTodo.categoryId == category.id) {
                    categoryName = category.title;
                }
            }

            for (Priority priority : priorityList) {
                if (selectedTodo.priorityId == priority.id) {
                    priorityName = priority.title;
                }
            }

            comboCategory.getModel().setSelectedItem(new Category(selectedTodo.categoryId, categoryName));

            comboPriority.getModel().setSelectedItem(new Priority(selectedTodo.priorityId, priorityName));

            txtComplatePercent.setText(String.valueOf(selectedTodo.complatePercent));

            txtNote.setText(selectedTodo.note);
        } else {
            System.out.println("sadece resetleniyor");
            txtTitle.setText("");
            txtTitle.enable();
            txtNote.setText("");
            txtNote.enable();

            txtComplatePercent.setText("0");
            txtComplatePercent.enable();

            txtStartDate.enable();

            txtStartDate.setValue(new Date());

            txtEndDate.enable();

            txtEndDate.setValue(new Date());
            selectedCategoryId = 0;
            comboCategory.enable();

            comboCategory.getModel().setSelectedItem(new Category(0, "Seçiniz"));

            selectedPriorityId = 0;
            comboPriority.getModel().setSelectedItem(new Priority(0, "Seçiniz"));
            comboPriority.enable();
        }

    }

}
