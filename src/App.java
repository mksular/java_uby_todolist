public class App {
    public static void main(String[] args) throws Exception {
        TodoList todoList = new TodoList();
        todoList.setSize(480, 420);
        todoList.setVisible(true);
        todoList.setTitle("Todo List");
        todoList.setResizable(false);
    }
}
