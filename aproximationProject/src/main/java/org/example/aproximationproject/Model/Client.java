package org.example.aproximationproject.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4999);
        System.out.println("Подключение к серверу...");
        // Отправка 4 чисел на сервер
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        double[] numbersToSend = {5, 16, 45, 134.5 , 55};
        for (double num : numbersToSend) {
            dos.writeDouble(num);
        }
        dos.flush();
        System.out.println("Числа отправлены на сервер.");

        // Получение массива от сервера
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        double[] receivedNumbers = new double[2];
        for (int i = 0; i < 2; i++) {
            receivedNumbers[i] = dis.readDouble();
        }
        System.out.println("y = " + receivedNumbers[0] + " * x + " + receivedNumbers[1]);
        socket.close();
    }
}

