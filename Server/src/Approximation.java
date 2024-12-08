public class Approximation {
    static {
        System.loadLibrary("approximationlib");  // Имя библиотеки без пути и расширения
    }

    // Нативный метод, который возвращает новый массив коэффициентов
    public native double[] getCoefficients(int n, double sumX, double sumY, double sumXY, double squareSumX);

    public static double[] calculateCoefficients(int n, double sumX, double sumY, double sumXY, double squareSumX) {
        Approximation app = new Approximation();
        return app.getCoefficients(n, sumX, sumY, sumXY, squareSumX);
    }
}
