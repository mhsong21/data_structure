public class MyNode implements Comparable<MyNode>{ 
	String[] index = new String[3];
	Integer distance;
	MyNode parent;
	MyNode next;
	boolean visited;
	Integer transnum;
	public MyNode(){
		index = null;
		distance = 0;
		next = null;
		parent = null;
		visited = false;
		transnum = 0;
	}
	public MyNode(String[] str) {
		index[0] = str[0];
		index[1] = str[1];
		index[2] = str[2];
		distance = 0;
		next = null;
		parent = null;
		visited = false;
		transnum = 0;
	}
	public MyNode(String[] str, int dist) {
		index[0] = str[0];
		index[1] = str[1];
		index[2] = str[2];
		distance = dist;
		next = null;
		parent = null;
		visited = false;
		transnum = 0;
	}
	public void setdist(int dist){
		distance = dist;
	}
	public void setNext(MyNode tmp){
		next = tmp;
	}
	public void setParent(MyNode tmp){
		parent = tmp;
	}
	public void settransnum(int num){
		transnum = num;
	}
	public MyNode getNext(){
		return next;
	}
	public MyNode getparent(){
		return parent;
	}
	public Integer getDist(){
		return distance;
	}
	public String[] getindex(){
		return index;
	}
	public Integer gettransnum(){
		return transnum;
	}
	public void add(MyNode n){ // 맨 뒤에 추가
		MyNode temp = this;
		MyNode temp2 = new MyNode(n.getindex(), n.getDist());
		while(temp.getNext() != null){
			temp = temp.getNext();
		}
		temp.setNext(temp2);
	}
	public int compareTo(MyNode temp){
		if(transnum.compareTo(temp.gettransnum()) == 0)
		 return distance.compareTo(temp.getDist());
		else return transnum.compareTo(temp.gettransnum());
	}
}
