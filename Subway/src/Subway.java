import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.PriorityQueue;

public class Subway {
	private static Hashtable<String, MyNode> hash = new Hashtable<String, MyNode>();
	private static Hashtable<String, MyNode> transferchk = new Hashtable<String, MyNode>();
	private static Hashtable<String, MyNode> distance = new Hashtable<String, MyNode>();
	private static PriorityQueue<MyNode> queue = new PriorityQueue<MyNode>();
	private static BufferedReader fr;
	final static int INF = Integer.MAX_VALUE;
	public static void main(String args[]) throws Exception
	{
		DataIn("input.txt"); 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while (true)
		{
			String input = br.readLine();
			if (input.equals("QUIT"))
				break;
			
			command(input);
		}
	}
	public static void DataIn(String args) throws Exception{
		fr = new BufferedReader(new FileReader(args));
		MyNode node, node2, transfer, tempnode;
		String[] tmpstr;
		String s;
		// 역 처리
		while((s = fr.readLine()).length() != 0){ 
			tmpstr = s.split(" "); // 고유번호, 역 이름, 호선
			node = new MyNode(tmpstr);
			if((transfer = transferchk.get(tmpstr[1])) != null){//만약 환승역이라면
				tempnode = transfer;
				node.setdist(5);
				do{
					node2 = hash.get(tempnode.getindex()[0]);
					node2.setdist(5);
					node2.add(node);
					node.add(node2);
					tempnode = tempnode.getNext();
				}while(tempnode != null); // 환승역끼리 연결시켜준다
				transfer.add(node);
				hash.put(tmpstr[0], node);
			}
			else{ // 아니면 그냥 입력
				hash.put(tmpstr[0], node);
				tempnode = new MyNode(node.getindex());
				transferchk.put(tmpstr[1], tempnode);
			}
		}
		// 간선 처리
		while((s = fr.readLine()) != null){ 
			tmpstr = s.split(" "); // 시작노선, 도착노선, 시간
			node = hash.get(tmpstr[0]);
			node2 = hash.get(tmpstr[1]);
			node2.setdist(Integer.parseInt(tmpstr[2]));
			node.add(node2);
		}
	}
	public static void command(String input) throws Exception{
		int time = 2147483647;
		boolean MinTransfer = false;
		String rlt = "";
		String[] s = new String[3];
		s = input.split(" ");
		if(s.length == 3) // 최소환승
			MinTransfer = true;
		
		MyNode small = transferchk.get(s[0]);
		initialset(distance);
		// distance INF로 초기화
		MyNode start = distance.get(small.getindex()[0]);
		Dijkstra(start, MinTransfer);
		time = gettime(s[1], MinTransfer);
		rlt = getroute(s[1], MinTransfer);
			
		System.out.println(rlt);
		System.out.println(time);
	}
	private static void Dijkstra(MyNode temp, boolean chk){
		temp = transferchk.get(temp.getindex()[1]);
		MyNode start;
		while(temp != null){
			start = new MyNode(temp.getindex());
			distance.put(start.getindex()[0], start);//시작지점 설정
			queue.add(start);//BFS 시작
			temp = temp.getNext();
		}
		int weight, trans = 0;
		MyNode small, dist, adj;
		while(!queue.isEmpty()){
			small = queue.poll();
			if(small.visited == true)	continue;
			small.visited = true;
			dist = hash.get(small.getindex()[0]);
			temp = dist.getNext();
			while(temp != null){
				adj = distance.get(temp.getindex()[0]);
				weight = small.getDist() + temp.getDist();
				if(chk){//최소환승
					trans = small.gettransnum();
					if(small.getindex()[1].equals(adj.getindex()[1])) trans++;
					if(adj.gettransnum() > trans){
						adj.settransnum(trans);
						adj.setdist(weight);
						adj.setParent(small);
					}
					else if(adj.gettransnum() == trans && adj.getDist() > weight){
						adj.setdist(weight);
						adj.setParent(small);
					}
				}
				else{//최단거리
					if(adj.getDist() >= weight){
						adj.setdist(weight);
						adj.setParent(small);
					}
				}
				queue.add(adj);
				temp = temp.getNext();
			}
		}
	}
	private static void initialset(Hashtable<String, MyNode> distance){
		Enumeration<MyNode> items = hash.elements();
		MyNode item, temp;
		while(items.hasMoreElements()){
			item = items.nextElement();
			temp = new MyNode(item.getindex(), INF);
			temp.settransnum(INF);
			distance.put(temp.getindex()[0], temp);
		}
	}
	private static String getroute(String end, boolean chk){
		String rlt = "";
		MyNode smallest = transferchk.get(end);
		MyNode tempnode = distance.get(smallest.getindex()[0]);
		smallest = smallest.getNext();
		while(smallest != null){
			if(chk){
				if(tempnode.gettransnum() > distance.get(smallest.getindex()[0]).gettransnum()){
					tempnode = distance.get(smallest.getindex()[0]);
				}
				else if(tempnode.gettransnum() == distance.get(smallest.getindex()[0]).gettransnum() && 
						tempnode.getDist() > distance.get(smallest.getindex()[0]).getDist()){
					tempnode = distance.get(smallest.getindex()[0]);
				}
			}
			else if(tempnode.getDist() > distance.get(smallest.getindex()[0]).getDist()){
				tempnode = distance.get(smallest.getindex()[0]);
			}
			smallest = smallest.getNext();
		} // 도착역이 환승역일 때 오류 처리
		ArrayList<String> result = new ArrayList<>();
		while(tempnode.getDist() != 0){
			if(tempnode.getparent().getindex()[1].equals(tempnode.getindex()[1])){
				result.add(0, " [" + tempnode.getindex()[1] + "]");
			}
			else{
				result.add(0, " " + tempnode.getindex()[1]);
			}
			tempnode = tempnode.getparent();
		}//도착역에서부터 거꾸로 탐색
		result.add(0, tempnode.getindex()[1]+"");
		for(int i = 0; i < result.size(); i++){
			if(i < result.size() - 1 && result.get(i+1).startsWith(" [")){
				continue;
			}
			rlt += result.get(i);
		}
		return rlt;
	}
	private static int gettime(String end, boolean chk){
		MyNode smallest = transferchk.get(end);
		MyNode tempnode = distance.get(smallest.getindex()[0]);
		smallest = smallest.getNext();
		while(smallest != null){
			if(chk){
				if(tempnode.gettransnum() > distance.get(smallest.getindex()[0]).gettransnum()){
					tempnode = distance.get(smallest.getindex()[0]);
				}
				else if(tempnode.gettransnum() == distance.get(smallest.getindex()[0]).gettransnum() && 
						tempnode.getDist() > distance.get(smallest.getindex()[0]).getDist()){
					tempnode = distance.get(smallest.getindex()[0]);
				}
			}
			else if(tempnode.getDist() > distance.get(smallest.getindex()[0]).getDist()){
				tempnode = distance.get(smallest.getindex()[0]);
			}
			smallest = smallest.getNext();
		} // getroute와 같이 도착역이 환승가능역일 때 오류 처리
		return tempnode.getDist();
	}
}
