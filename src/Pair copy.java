public class Pair implements Comparable 
{
    private String key;
    private Integer val;
    
    public Pair(String key, Integer val) 
    {
	    this.key = key;
	    this.val = val;
    }

    public String key() 
    {
	    return key;
    }

    public Integer val() 
    {
	    return val;
    }

    public int compareTo(Object o) 
    {
	    return this.key.compareTo(((Pair)o).key());
    }

}
