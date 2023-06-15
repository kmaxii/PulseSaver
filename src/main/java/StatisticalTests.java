import org.apache.commons.math3.stat.inference.TTest;

import java.util.List;

public class StatisticalTests {
    public static void performTTest(List<Double> sample1, List<Double> sample2) {
        double[] array1 = sample1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] array2 = sample2.stream().mapToDouble(Double::doubleValue).toArray();

        TTest tTest = new TTest();
        double pValue = tTest.tTest(array1, array2);

        System.out.println("VR-gruppen:");
        System.out.println("N = " + sample1.size());
        System.out.println("Medelvärde (mean) = " + calculateMean(sample1));
        System.out.println("Standardavvikelse (standard deviation) = " + calculateStandardDeviation(sample1));
        System.out.println();

        System.out.println("Datorskärmsgruppen:");
        System.out.println("N = " + sample2.size());
        System.out.println("Medelvärde (mean) = " + calculateMean(sample2));
        System.out.println("Standardavvikelse (standard deviation) = " + calculateStandardDeviation(sample2));
        System.out.println();

        System.out.println("För att utföra t-testet behöver vi även anta en signifikansnivå (vanligtvis angiven som α). Låt oss anta en signifikansnivå på 0.05.");
        System.out.println();

        System.out.println("Med dessa värden kan vi nu utföra t-testet och beräkna p-värdet för att avgöra om det finns en statistiskt signifikant skillnad mellan grupperna. Med hjälp av statistisk programvara eller tabeller får vi följande resultat:");
        double tValue = tTest.t(array1, array2);
        int degreesOfFreedom = sample1.size() + sample2.size() - 2;
        System.out.println("t-värde = " + tValue);
        System.out.println("Frihetsgrader = " + degreesOfFreedom);
        System.out.println("p-värde = " + pValue);
        System.out.println();

        System.out.println("Det beräknade p-värdet är " + pValue + ", vilket är " + (pValue > 0.05 ? "större" : "mindre") + " än vår valda signifikansnivå på 0.05. Det innebär att vi " + (pValue > 0.05 ? "inte" : "har") + " tillräckligt med bevis för att säga att det finns en statistiskt signifikant skillnad i maxpuls mellan personer som spelade VR och personer som spelade på datorskärm.");
    }

    private static double calculateMean(List<Double> values) {
        double sum = 0;
        int count = values.size();
        for (double value : values) {
            sum += value;
        }
        return sum / count;
    }

    private static double calculateStandardDeviation(List<Double> values) {
        double mean = calculateMean(values);
        double sumOfSquaredDeviations = 0;
        int count = values.size();
        for (double value : values) {
            double deviation = value - mean;
            sumOfSquaredDeviations += deviation * deviation;
        }
        double standardDeviation = Math.sqrt(sumOfSquaredDeviations / count);
        return standardDeviation;
    }
}
