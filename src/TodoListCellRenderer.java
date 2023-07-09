import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class TodoListCellRenderer extends JLabel implements ListCellRenderer<Todo> {

    public TodoListCellRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Todo> list, Todo value, int index, boolean isSelected,
            boolean cellHasFocus) {

        Todo todo = (Todo) value;
        setText(todo.title +" "+ todo.id);
       

        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());

        }
        return this;
    }

}
