public class

Main { public static void main(String[] args) {
    // Выводим приглашение для пользователя
    System.out.print("Введите текст и нажмите <Enter>: ");

// Создаем Scanner для чтения ввода из консоли
    java.util.Scanner scanner = new java.util.Scanner(System.in);
// Читаем всю строку, которую ввел пользователь
    String text = scanner.nextLine();

// Выводим длину введенного текста
    System.out.println("Длина текста: " + text.length());

// Закрываем scanner (хороший тон)
    scanner.close();
}
}
