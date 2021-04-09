
public class EditArray 
{
	//adds single data piece to array
	public static int[] addInt(int addTo, int[] array)
	{
		int[] i = new int[array.length];
		i[i.length - 1] = addTo;
		return i;
	}
	public static String[] addString(String addTo, String[] array)
	{
		String[] i = new String[array.length];
		i[i.length - 1] = addTo;
		return i;
	}
	public static char[] addChar(char addTo, char[] array)
	{
		char[] i = new char[array.length];
		i[i.length - 1] = addTo;
		return i;
	}
	public static double[] addDouble(double addTo, double[] array)
	{
		double[] i = new double[array.length];
		i[i.length - 1] = addTo;
		return i;
	}

	//combines arrays of same data type
	public static int[] addIntArray(int[] array, int[] addTo)
	{
		int[] i = new int[addTo.length + array.length];
		for(int loopArray = 0; loopArray < array.length; loopArray++)
		{
			i[loopArray] = array[loopArray];
		}
		int newLoop = 0;
		for(int loopAddTo = array.length; loopAddTo < i.length; loopAddTo++)
		{
			i[loopAddTo] = addTo[newLoop++];
		}
		return i;
	}
	public static String[] addStringArray(String[] array, String[] addTo)
	{
		String[] i = new String[addTo.length + array.length];
		for(int loopArray = 0; loopArray < array.length; loopArray++)
		{
			i[loopArray] = array[loopArray];
		}
		int newLoop = 0;
		for(int loopAddTo = array.length; loopAddTo < i.length; loopAddTo++)
		{
			i[loopAddTo] = addTo[newLoop++];
		}
		return i;
	}
	public static double[] addDoubleArray(double[] array, double[] addTo)
	{
		double[] i = new double[addTo.length + array.length];
		for(int loopArray = 0; loopArray < array.length; loopArray++)
		{
			i[loopArray] = array[loopArray];
		}
		int newLoop = 0;
		for(int loopAddTo = array.length; loopAddTo < i.length; loopAddTo++)
		{
			i[loopAddTo] = addTo[newLoop++];
		}
		return i;
	}
	public static char[] addCharArray(char[] array, char[] addTo)
	{
		char[] i = new char[addTo.length + array.length];
		for(int loopArray = 0; loopArray < array.length; loopArray++)
		{
			i[loopArray] = array[loopArray];
		}
		int newLoop = 0;
		for(int loopAddTo = array.length; loopAddTo < i.length; loopAddTo++)
		{
			i[loopAddTo] = addTo[newLoop++];
		}
		return i;
	}
	public static String[] addStringArrayArray(String[] array, String[][] addTo)
	{
		int totalLength = 0;
		for(int loopLength = 0; loopLength < addTo.length; loopLength++)
		{
			totalLength = totalLength + addTo[loopLength].length;
		}
		totalLength = totalLength + array.length;
		String[] i = new String[totalLength];
		for(int loopFirstArray = 0; loopFirstArray < array.length; loopFirstArray++)
		{
			i[loopFirstArray] = array[loopFirstArray];
		}
		int startAt = array.length;
		for(int loopAddArrayOne = 0; loopAddArrayOne < addTo.length; loopAddArrayOne++)
		{
			for(int loopAddArrayTwo = 0; loopAddArrayTwo < addTo.length - 1; loopAddArrayTwo++)
			{
				i[startAt++] = addTo[loopAddArrayOne][loopAddArrayTwo];
			}
		}
		return i;
	}
	public static int[] addintArrayArray(int[] array, int[][] addTo)
	{
		int totalLength = 0;
		for(int loopLength = 0; loopLength < addTo.length; loopLength++)
		{
			totalLength = totalLength + addTo[loopLength].length;
		}
		totalLength = totalLength + array.length;
		int[] i = new int[totalLength];
		for(int loopFirstArray = 0; loopFirstArray < array.length; loopFirstArray++)
		{
			i[loopFirstArray] = array[loopFirstArray];
		}
		int startAt = array.length;
		for(int loopAddArrayOne = 0; loopAddArrayOne < addTo.length; loopAddArrayOne++)
		{
			for(int loopAddArrayTwo = 0; loopAddArrayTwo < addTo.length - 1; loopAddArrayTwo++)
			{
				i[startAt++] = addTo[loopAddArrayOne][loopAddArrayTwo];
			}
		}
		return i;
	}
	public static double[] addDoubleArrayArray(double[] array, double[][] addTo)
	{
		int totalLength = 0;
		for(int loopLength = 0; loopLength < addTo.length; loopLength++)
		{
			totalLength = totalLength + addTo[loopLength].length;
		}
		totalLength = totalLength + array.length;
		double[] i = new double[totalLength];
		for(int loopFirstArray = 0; loopFirstArray < array.length; loopFirstArray++)
		{
			i[loopFirstArray] = array[loopFirstArray];
		}
		int startAt = array.length;
		for(int loopAddArrayOne = 0; loopAddArrayOne < addTo.length; loopAddArrayOne++)
		{
			for(int loopAddArrayTwo = 0; loopAddArrayTwo < addTo.length - 1; loopAddArrayTwo++)
			{
				i[startAt++] = addTo[loopAddArrayOne][loopAddArrayTwo];
			}
		}
		return i;
	}
	public static char[] addCharArrayArray(char[] array, char[][] addTo)
	{
		int totalLength = 0;
		for(int loopLength = 0; loopLength < addTo.length; loopLength++)
		{
			totalLength = totalLength + addTo[loopLength].length;
		}
		totalLength = totalLength + array.length;
		char[] i = new char[totalLength];
		for(int loopFirstArray = 0; loopFirstArray < array.length; loopFirstArray++)
		{
			i[loopFirstArray] = array[loopFirstArray];
		}
		int startAt = array.length;
		for(int loopAddArrayOne = 0; loopAddArrayOne < addTo.length; loopAddArrayOne++)
		{
			for(int loopAddArrayTwo = 0; loopAddArrayTwo < addTo.length - 1; loopAddArrayTwo++)
			{
				i[startAt++] = addTo[loopAddArrayOne][loopAddArrayTwo];
			}
		}
		return i;
	}
}

