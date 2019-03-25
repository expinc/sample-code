package expinc.practice;


public class MergeSortJob implements Job
{
	protected int[]	data;
	protected int	begin;
	protected int	end;
	protected int[]	result;


	public MergeSortJob(int[] data, int begin, int end)
	{
		this.data = data;
		this.begin = begin;
		this.end = end;
		result = null;
	}


	@Override
	public void execute()
	{
		result = MergeSort.sort(data, begin, end);
	}


	public int[] getResult()
	{
		return result;
	}

}
