package org.example.aproximationproject.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Client {
    public static double[] getApproximationCoefficients(Map<Double, ArrayList<Double>> inputData , int parameter) throws IOException {
        Config config = Config.getInstance("src/main/resources/config.properties");

        String host = config.getString("app.host" , "");
        int port = config.getInt("app.port" , 0);
        if (Objects.equals(host, "") || Objects.equals(port , 0) ) {
            throw new RuntimeException("Ошибка конфигурации: отсутствуют необходимые параметры");
        }
        Socket socket = new Socket(host, port);
        System.out.println("Подключение к серверу...");
        // Отправка 4 чисел на сервер
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        double[] numbersToSend = new double[5];
        calculateIntermediateValues(inputData, parameter,numbersToSend);
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
        return receivedNumbers;
    }


    private static void calculateIntermediateValues(Map<Double, ArrayList<Double>> inputData, int parameter,double[] valueToSend) {
        int n = inputData.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double squareSumX = 0;
        for (Map.Entry<Double, ArrayList<Double>> entry : inputData.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue().get(parameter);
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

