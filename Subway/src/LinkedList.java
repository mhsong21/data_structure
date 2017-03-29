
public class LinkedList {
	MyNode node;
	int size;
	public LinkedList(MyNode head){
		node = head;
		size = 1;
	}
	public void add(MyNode n){
		MyNode temp = node;
		while(temp.getNext() != null){
			temp = temp.getNext();
		}
		temp.setNext(n);
		size++;
	}
	public int getsize(){
		return size;
	}
	public void delete(MyNode n){
		// don't need
	}
}
