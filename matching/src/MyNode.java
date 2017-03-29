public class MyNode implements Comparable<MyNode>{ // compareTo를 쓸 수 있는 링크드리스트 구현
	String str;
	int[] addr = new int[2];
	MyNode next;
	public MyNode(String tmp, int chk, int i) {
		str = tmp;
		addr[0] = chk;
		addr[1] = i+1;
	}
	public String getstr(){
		return str;
	}
	public int[] getaddr(){
		return addr;
	}
	public void setstr(String string){
		str = string;
	}
	public void setAddr(int[] adr){
		addr = adr;
	}
	public void setNext(MyNode tmp){
		next = tmp;
	}
	public MyNode getNext(){
		return next;
	}
	public int compareTo(MyNode node){
		return this.getstr().compareTo(node.getstr());
	}
}
