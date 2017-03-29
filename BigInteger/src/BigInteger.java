import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
 
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("([+-]?)(\\d+)([+*-])([+-]?)(\\d+)");
    private int[] nums;
    private char sign;
    public BigInteger(int[] arr)
    {
     sign = '+';
     nums = arr;
    }
    
    public BigInteger add(BigInteger big)
    {
     if(!this.biggerthan(big)){
    	 return big.add(this);
     }
     int l = this.nums.length - big.nums.length;
     int[] sum = new int[this.nums.length+1];
     BigInteger result = new BigInteger(sum);
     for(int i=this.nums.length-1; i >= 0; i--){
      if(i >= l){
       result.nums[i+1] += this.nums[i] + big.nums[i - l];
      }
      else if(i < l){
       result.nums[i+1] += this.nums[i];
      }
      if(result.nums[i+1] >= 10){
       result.nums[i]++;
       result.nums[i+1] -= 10;
      }
     }
     return result;
    }
 
    public BigInteger subtract(BigInteger big)
    {
     if(!this.biggerthan(big)){
      return big.subtract(this);
     }
     int l = this.nums.length - big.nums.length;
     int[] sum = new int[this.nums.length];
     BigInteger result = new BigInteger(sum);
     for(int i=this.nums.length-1; i >= 0; i--){
      if(i >= l){
       result.nums[i] += this.nums[i] - big.nums[i-l];
      }
      else if(i < l){
       result.nums[i] = this.nums[i];
      }
      if(result.nums[i] < 0){
       this.nums[i-1]--;
       result.nums[i] += 10;
      }
     }
     return result;
    }
 
    public BigInteger multiply(BigInteger big)
    {
     if(!this.biggerthan(big)){
      return big.multiply(this);
     }
     int[] sum = new int[this.nums.length+big.nums.length];
     BigInteger result = new BigInteger(sum);
     for(int i=big.nums.length-1; i >= 0; i--){
    	 for(int j=this.nums.length-1; j >= 0; j--){
    		 result.nums[j+i+1] += this.nums[j] * big.nums[i];
    		 while(result.nums[j+i+1] >= 10){
    			 result.nums[j+i]++;
    			 result.nums[j+i+1] -= 10;
    		 }
    	 }
     }
     return result;
    }
    
    public boolean biggerthan(BigInteger big)
    {
     if(this.nums.length > big.nums.length){
      return true;
     }
     else if(this.nums.length < big.nums.length){
    	 return false;
     }
     for(int i=0; i < this.nums.length; i++){
      if(this.nums[i] > big.nums[i]){
    	  return true;
      }
      else if(this.nums[i] < big.nums[i]){
    	  return false;
      }
     }
     return true;
    }
    @Override
    public String toString()
    {
     String result = "";
     if(this.sign == '-'){
      result = "-";
     }
     int l=0;
     for(int i=0;;i++){
    	 if(l == this.nums.length)	return "0";
    	 if(this.nums[i] == 0)	 l++;
    	 else	 break;
     }
     for(int i=l; i < this.nums.length; i++){
    	 result += Character.forDigit(this.nums[i], 10);
     }
     return result;  
    }
 
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
     boolean chk;
     input = input.replaceAll("\\s","");
     Matcher m = EXPRESSION_PATTERN.matcher(input);
     String str1 = "";
     String str2 = "";
     char c ;
     char c1;
     char c2;
     m.matches();
     if (m.group(1).equals("-")) {
    	 c1 = '-';
     } else {
    	 c1 = '+';
     }
     str1 = m.group(2);
     c = m.group(3).charAt(0);
     if (m.group(4).equals("-")) {
    	 c2 = '-';
     } else {
    	 c2 = '+';
     }
     str2 = m.group(5);
     int[] nums1 = new int[str1.length()];//string to int[]
     for(int i=0; i < str1.length(); i++){
      nums1[i] = str1.charAt(i) - '0';
     }
     int[] nums2 = new int[str2.length()];
     for(int i=0; i < str2.length(); i++){
      nums2[i] = str2.charAt(i) - '0';
     }
     
     BigInteger num1 = new BigInteger(nums1);
     BigInteger num2 = new BigInteger(nums2);
     BigInteger result;
     // sign select
     chk = num1.biggerthan(num2);
     if((c1 == '+' && c == '+' && c2 == '+') || (c1 == '+' && c == '-' && c2 == '-')){
      result = num1.add(num2);
     }
     else if((c1 == '-' && c == '+' && c2 == '-') || (c1 == '-' && c == '-' && c2 == '+')){
      result = num1.add(num2);
      result.sign = '-';
     }
     else if((c1 == '+' && c == '-' && c2 == '+') || (c1 == '+' && c == '+' && c2 == '-')){   
    	 result = num1.subtract(num2);
    	 if(!chk){
    		 result.sign = '-';
         }
     }
     else if((c1 == '-' && c == '+' && c2 == '+') || (c1 == '-' && c == '-' && c2 == '-')){
         result = num1.subtract(num2);
    	 if(chk){
    		 result.sign = '-';
    	 }
     }
     else{
    	 result = num1.multiply(num2);
    	 if(c1 == '+' && c2 == '-' || c1 == '-' && c2 == '+'){
    		 result.sign = '-';
      	}
     }
     return result;
    }
 
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
 
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
 
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
 
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }
 
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}