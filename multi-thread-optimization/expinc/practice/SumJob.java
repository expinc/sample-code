package expinc.practice;


public class SumJob implements Job
{
	protected int[]	data;
	protected int	begin;
	protected int	end;
	protected int	result;


	public SumJob(int[] data, int begin, int end)
	{
		this.data = data;
		this.begin = begin;
		this.end = end;
		result = 0;
	}


	@Override
	public void execute()
	{
		result = 0;
		for (int i = begin; i <= end; ++i)
		{
			result += data[i];
		}
	}
	
	
	public int getResult()
	{
		return result;
	}

}
