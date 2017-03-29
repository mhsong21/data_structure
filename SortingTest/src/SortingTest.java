import java.io.*;
import java.util.*;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int i, j, temp;
	
	private static int[] DoBubbleSort(int[] value)
	{
		for(i = 0; i < value.length - 1; i++){
			for(j = 0; j < value.length - 1 - i; j++){
				if(value[j] > value[j+1]){//원소 크기 비교
					temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;// Swap
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		for(i = 1; i < value.length; i++){
			temp = value[i];
			for(j = i - 1; (j >= 0) && (value[j] > temp); j--){
				value[j+1] = value[j];//shift
			}
			value[j+1] = temp;//insert
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for(i = 0; i < value.length; i++){
			heap.add(value[i]);//넣었다
		}
		for(i = 0; i < value.length; i++){
			value[i] = heap.poll();//빼기
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{
		int[] S1,S2;

		if(value.length > 1){
			if(value.length % 2 == 0){//짝수일때
				S1 = new int[value.length/2];
				S2 = new int[value.length/2];
				System.arraycopy(value, 0, S1, 0, (value.length / 2));
				System.arraycopy(value, value.length/2, S2, 0, value.length/2);
			}
			else{//홀수일때
				S1 = new int[value.length/2+1];
				S2 = new int[value.length/2];
				System.arraycopy(value, 0, S1, 0, (value.length / 2)+1);
				System.arraycopy(value, (value.length/2)+1, S2, 0, value.length/2);
			}
			S1 = DoMergeSort(S1);//재귀 호출
			S2 = DoMergeSort(S2);
			value = merge(S1,S2);// 합치면서 정렬
		}
		return value;
	}
	private static int[] merge(int[] a, int[] b){
		int[] result = new int[a.length + b.length];
		int i = 0;
		int j = 0;
		for(; i <= b.length; i++){
			if(i == b.length){
				System.arraycopy(a, j, result, i+j, result.length-i-j);
				break;
			}
			for(; j <= a.length; j++){
				if(j == a.length){
					System.arraycopy(b, i, result, i+j, result.length-i-j);
					i = b.length+1;
					break;
				}
				if(a[j] < b[i]){
					result[j+i] = a[j]; 
				}
				else{
					result[j+i] = b[i];
					break;
				}
			}
		}
		return result;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		int[] L, R;
		int x = 0; int cnt = 0; int pivot;
		if(value.length > 1){
			Random random = new Random();//랜덤으로 pivot 생성
			x = random.nextInt(value.length);
			pivot = value[x];
			for(i = 0; i < value.length; i++){
				if(value[i] > pivot)	cnt++;//배열 크기 지정하기 위해 카운트
			}
			L = new int[value.length-1-cnt]; R = new int[cnt];
			partition(value, pivot, L, R);//pivot을 기준으로 L과 R로 나누기
			L = DoQuickSort(L);//재귀 호출
			R = DoQuickSort(R);
			System.arraycopy(L, 0, value, 0, L.length);
			value[L.length] = pivot;
			System.arraycopy(R, 0, value, L.length+1, R.length);//합치기(Concatenation)
		}
		
		return (value);
	}
	private static void partition(int[] S, int pivot, int[] L, int[] R){
		int lt=0; int rt=0;
		boolean chk = false;
		for(i = 0; i < S.length; i++){
			if(S[i] > pivot){
				R[rt] = S[i];
				rt++;
			}
			else if(S[i] <= pivot){
				if(S[i] == pivot && !chk){
					chk = true;
					continue;
				}
				L[lt] = S[i];
				lt++;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		int[] Negval, Posval;
		int lt = 0; int rt = 0; int len = 0;
		for(i = 0; i < value.length; i++){
			if(value[i] < 0) len++;//배열 크기 지정하기 위해 카운트
		}
		if(len == 0){
			value = RadixSort(value);
			return (value);
		}
		else if(len == value.length){
			for(i = 0; i < value.length; i++){
				value[i] = -value[i];
			}
			value = RadixSort(value);
			int[] tempval = new int[value.length];
			for(i = 0; i < value.length; i++){
				tempval[i] = -value[value.length-1-i];//양수로 바꾼 Negval을 다시 음수로 바꾸고 순서 거꾸로
			}
			return (tempval);
		}
		else{
			Negval = new int[len];
			Posval = new int[value.length - len];
			for(i = 0; i < value.length; i++){
				if(value[i] < 0){
					Negval[lt] = -value[i];// 동일한 method적용을 위해 양수로 바꾼 뒤 계산
					lt++;
				}
				else{
					Posval[rt] = value[i];
					rt++;
				}
			}
			Negval = RadixSort(Negval);
			Posval = RadixSort(Posval);
			int[] tempval = new int[Negval.length];
			for(i = 0; i < Negval.length; i++){
				tempval[i] = -Negval[Negval.length-1-i];//양수로 바꾼 Negval을 다시 음수로 바꾸고 순서 거꾸로
			}	
			System.arraycopy(tempval, 0, value, 0, tempval.length);
			System.arraycopy(Posval, 0, value, tempval.length, Posval.length);//음수와 양수 배열 합치기
			return (value);
		}
	}
	private static int[] RadixSort(int[] S){
		// modification from https://ko.wikipedia.org/wiki/%EA%B8%B0%EC%88%98_%EC%A0%95%EB%A0%AC
		int[] tmp;
		int num;
		int p = MaxPlaceValue(S);//가장 큰 자릿수 세기
		int d = 1;
		int[] count;
		for(i = 0; i < p; i++){
			count = new int[10];
			tmp = new int[S.length];
			for(j = 0; j < S.length; j++){
				num = (S[j]/d)%10;
				count[num]++;//숫자가 몇번 나왔는지 세기
			}
			for(j = 1; j < 10; j++) count[j] += count[j-1];//배열에 대입을 위해 누적합을 계산
			for(j = S.length-1; j >= 0; j--){
				num = (S[j]/d)%10;
				tmp[count[num]-1] = S[j];//숫자의 위치에 따라 배치
				count[num]--;//쓴 숫자 카운트-1
			}
			S = tmp;//옮긴 배열 대입
			d = d * 10;//다음 자릿수로
		}
		return S;
	}
	private static int MaxPlaceValue(int[] S){
		int cnt = 1;
		int M = S[0];
		for(i = 1; i < S.length; i++){
			if(M < S[i]) M = S[i];
		}
		while(M > 10){
			M = M/10;
			cnt++;
		}
		return cnt;
	}
}