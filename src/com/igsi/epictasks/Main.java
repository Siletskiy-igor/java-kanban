import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("первая задача" , "описание первой задачи", TaskStatus.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("вторая задача", "описание второй задачи", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task2);


        Epic epic1 = new Epic("первый эпик", "описание первого эпика");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("первая подзадача", "описание первой подзадачи", TaskStatus.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("вторая подзадача", "описание второй подзадачи", TaskStatus.NEW, epic1.getId());
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("второй эпик", "описание второго эпика");
        taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("третья подзадача", "описание третьей подзадачи", TaskStatus.IN_PROGRESS, epic2.getId());
        taskManager.createSubtask(subtask3);

        taskManager.removeTaskById(task1.getId());
        taskManager.removeEpicById(epic2.getId());

        System.out.println(taskManager.getTasks());
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());

    }
}
