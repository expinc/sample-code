package expinc.practice;

public class MergeSort
{
	public static int[] merge(int[] data1, int begin1, int end1, int[] data2, int begin2, int end2)
	{
		int[] result = new int[end1 - begin1 + 1 + end2 - begin2 + 1];
		int i = begin1;
		int j = begin2;
		int k = 0;
		while (i <= end1 || j <= end2)
		{
			if (i > end1)
				result[k++] = data2[j++];
			else if (j > end2)
				result[k++] = data1[i++];
			else
			{
				if (data1[i] < data2[j])
					result[k++] = data1[i++];
				else
					result[k++] = data2[j++];
			}
		}
		return result;
	}
	
	
	public static int[] sort(int[] data, int begin, int end)
	{
		int[] result = new int[end - begin + 1];
		if (end == begin)
			result[0] = data[begin];
		else
		{
			int mid = begin + (end - begin) / 2;
			int[] result1 = sort(data, begin, mid);
			int[] result2 = sort(data, mid + 1, end);
			result = merge(result1, 0, result1.length - 1, result2, 0, result2.length - 1);
		}
		return result;
	}
}
