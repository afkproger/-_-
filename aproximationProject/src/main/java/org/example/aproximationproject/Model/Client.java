package org.example.aproximationproject.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Client {
    public static String getApproximationCoefficients(Map<Double, Double> inputData) throws IOException {
        Socket socket = new Socket("localhost", 4999);
        System.out.println("Подключение к серверу...");
        // Отправка 4 чисел на сервер
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        double[] numbersToSend = new double[5];
        calculateIntermediateValues(inputData, numbersToSend);
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

        socket.close();
        return "y = " + receivedNumbers[0] + " * x + " + receivedNumbers[1];
    }


    private static void calculateIntermediateValues(Map<Double, Double> inputData, double[] valueToSend) {
        int n = inputData.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double squareSumX = 0;
        for (Map.Entry<Double, Double> entry : inputData.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            squareSumX += x * x;

        }
        valueToSend[0] = n;
        valueToSend[1] = sumX;
        valueToSend[2] = sumY;
        valueToSend[3] = sumXY;
        valueToSend[4] = squareSumX;

    }
}

