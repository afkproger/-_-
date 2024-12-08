import java.net.*;
import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4999);
        System.out.println("Сервер запущен. Ожидание подключения...");

        while (true) { // Цикл для обработки соединений
            Socket socket = serverSocket.accept();
            System.out.println("Клиент подключился.");

            // Чтение данных от клиента
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            double[] numbers = new double[5];
            for (int i = 0; i < 5; i++) {
                numbers[i] = dis.readDouble();
                System.out.println(numbers[i]);
            }

            double[] coefficients = Approximation.
                    calculateCoefficients((int)numbers[0], numbers[1], numbers[2], numbers[3], numbers[4]);
            // Отправка данных обратно клиенту
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            for (double num : coefficients) {
                dos.writeDouble(num);
            }
            dos.flush();

            System.out.println("Коэффиценты отправлены клиенту.");
            socket.close(); // Закрыть текущее соединение
        }
    }
}
