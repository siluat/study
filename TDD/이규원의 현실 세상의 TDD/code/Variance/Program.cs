using System;

namespace Variance
{
    class Program
    {
        static void Main(string[] args) => Console.WriteLine(args.Length switch
        {
            0 => "입력된 데이터가 없습니다.",
            1 => "데이터가 부족해 분산을 계산할 수 없습니다. 2개 이상의 데이터를 입력해 주세요.",
            _ => GetVarianceOutput(args)
        });

        private static string GetVarianceOutput(string[] args)
        {
            double[] source = ParseArguments(args, args.Length);
            double mean = CalculateMean(args, source);
            double sumOfSquares = CalculateSumOfSquares(source, mean);
            double variance = sumOfSquares / (args.Length - 1);
            return $"분산: {variance}";
        }

        private static double CalculateSumOfSquares(double[] source, double mean)
        {
            double sumOfSquares = 0.0;
            for (int i = 0; i < source.Length; i++)
            {
                sumOfSquares += (source[i] - mean) * (source[i] - mean);
            }

            return sumOfSquares;
        }

        private static double CalculateMean(string[] args, double[] source)
        {
            double sum = 0.0;
            for (int i = 0; i < args.Length; i++)
            {
                sum += source[i];
            }

            double mean = sum / args.Length;
            return mean;
        }

        private static double[] ParseArguments(string[] args, int n)
        {
            double[] source = new double[n];
            for (int i = 0; i < n; i++)
            {
                source[i] = double.Parse(args[i]);
            }

            return source;
        }
    }
}
