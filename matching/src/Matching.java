import java.util.ArrayList;
import java.util.Hashtable; // copy from java.util.Hashtable
import java.io.*;

public class Matching
{
	private static Hashtable<Integer, AVLtreenode<MyNode>> hash; // copy from java.util.Hashtable
	private static BufferedReader fr;
	private static int size, count;
	public static void main(String args[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws Exception 
	{
		String s = new String(input.substring(2)); // 실제 스트링 부분
		if(input.startsWith("<")){
			DataIn(s); // 데이터 입력
		}
		else if(input.startsWith("@")){
			int key = Integer.parseInt(s);
			AVLtreenode<MyNode> temp = hash.get(key);
			size = count = size(temp);
			print(temp); // 해당하는 문자열을 출력
		}
		else if(input.startsWith("?")){
			if(!Find(s)){ // 패턴과 맞는 문자열의 주소를 출력
				System.out.println("(0, 0)");
			}
		}
		else{
			throw new Exception();
		}
	}
	private static int size(AVLtreenode<MyNode> tree){//tree가 가지고 있는 노드의 개수
		if(tree != null){
		return 1 + size(tree.getLeft()) + size(tree.getRight());
		}
		else return 0;
	}
	private static void print(AVLtreenode<MyNode> tree){//preorder로 출력
		if(tree != null){
			if(count == 1){
				System.out.println(tree.getItem().getstr()+"");//마지막 공백 없애주기
				count--;
			}
			else{
				System.out.print(tree.getItem().getstr()+" ");
				count--;
			}
			print(tree.getLeft());
			print(tree.getRight());
		}
		else if(size == count){
			System.out.println("EMPTY");
		}
	}
	private static void DataIn(String input) throws Exception{
			fr = new BufferedReader(new FileReader(input)); // 파일 입력
			hash = new Hashtable<Integer, AVLtreenode<MyNode>>();
			String s, tmp;
			int chk = 1;
			while((s = fr.readLine()) != null){ // 한 줄 씩 입력받음
				for(int i = 0; i < s.length()- 5; i++){
					tmp = s.substring(i,i+6);
					MyNode mynode = new MyNode(tmp, chk, i); // substring과 위치값이 있음
					int key = hashing(tmp); // substring을 hashing한다
					AVLtreenode<MyNode> tree = new AVLtreenode<>(); 
					
					if(hash.get(key) == null){ //키 값에 따른 AVLtree를 반환
						tree.setItem(mynode); // 내용이 없으면 헤드에 mynode를 넣음
					}
					else{
						tree = hash.get(key);
						if(tree.retrieve(mynode) != null){ // 이미 스트링이 트리에 존재하면
							MyNode tmpnode = tree.retrieve(mynode).getItem(); //링크드리스트에 추가
							while(tmpnode.getNext() != null){
								tmpnode = tmpnode.getNext();
							}
							tmpnode.setNext(mynode);
						}
						else{
							tree = tree.insert(tree, mynode);
						}
					}	
					hash.put(key, tree);
				}
				chk++;
			}
	}
	private static boolean Find(String pattern){
		ArrayList<MyNode> result = new ArrayList<>(); // 결과 리스트
		boolean chk = false;
		MyNode ptrn, node = null;
		ptrn = new MyNode(pattern.substring(0, 6), 0, 0);
		node = ptrn;
		int mod = pattern.length() % 6;
		for(int i = 6; i < pattern.length() - mod; i = i + 6){
			node.setNext(new MyNode(pattern.substring(i,i+6), 0, 0));
			node = node.getNext();
		}
		if(mod > 0){
			node.setNext(new MyNode(pattern.substring(pattern.length()-6), 0, 0));
		} // ptrn 링크드리스트에 패턴을 길이가 6인 서브스트링으로 분해
		else{
			mod = 6;
		}
		int key = hashing(ptrn.getstr());
		AVLtreenode<MyNode> keynode = hash.get(key);
		ArrayList<MyNode> check = new ArrayList<>();
		if(keynode == null || keynode.retrieve(ptrn) == null) return false;
		MyNode tempnode = keynode.retrieve(ptrn).getItem();
		result.add(tempnode); // 서브스트링이 존재하면 결과 리스트에 추가
		while(tempnode.getNext() != null){
			tempnode = tempnode.getNext();
			result.add(tempnode);
		}//첫 번째 6개
		int k = 0;
		for(node = ptrn.getNext(); node != null; node = node.getNext()){ //다음 서브스트링의 주소값이 처음 서브스트링과 연결되어있는지
			if(node.getNext() == null){ 								 //확인하고 연결되어있지 않다면 결과 리스트에서 삭제
				k += mod;
			}
			else{
				k += 6;
			}
			key = hashing(node.getstr());
			keynode = hash.get(key);
			tempnode = keynode.retrieve(node).getItem();
			check.add(tempnode);
			while(tempnode.getNext() != null){
				tempnode = tempnode.getNext();
				check.add(tempnode);
			}
			for(int i = 0; i < result.size();){
				for(int j = 0; j < check.size(); j++){
					if(result.get(i).getaddr()[0] == check.get(j).getaddr()[0] &&
							result.get(i).getaddr()[1] + k == check.get(j).getaddr()[1]){
						chk = true;
					}
				}
				if(!chk){
					result.remove(i);
				}
				else{
					i++;
				}
				chk = false;
			}
		}
		if(result.size() == 0){
			return false;
		}

		for(int i = 0; i < result.size(); i++){ // 결과리스트를 출력
			if(i == result.size() - 1){
				System.out.println("("+result.get(i).getaddr()[0]+", "+result.get(i).getaddr()[1]+")");
			}
			else{
				System.out.print("("+result.get(i).getaddr()[0]+", "+result.get(i).getaddr()[1]+") ");
			}
		}
		return true;
	}
	private static int hashing(String item){
		int result=0;
		for(int i=0; i < item.length(); i++){
			result += item.charAt(i);
		}
		return (result % 100); // sum of ASCII value mod 100
	}

}
