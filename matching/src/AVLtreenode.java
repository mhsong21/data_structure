
public class AVLtreenode<E extends Comparable<E>>{//compareTo를 사용할 수 있는 generic
	private E item;
	private AVLtreenode<E> leftChild;
	private AVLtreenode<E> rightChild;
	public AVLtreenode(){
		item = null;
		leftChild = null;
		rightChild = null;
	}
	public E getItem(){
		return item;
	}
	public void setItem(E newItem){
		item = newItem;
	}
	public AVLtreenode<E> getLeft(){
		return leftChild;
	}
	public AVLtreenode<E> getRight(){
		return rightChild;
	}
	public void setLeft(AVLtreenode<E> left){
		leftChild = left;
	}
	public void setRight(AVLtreenode<E> right){
		rightChild = right;
	}
	public int height(AVLtreenode<E> node){
		if(node != null){
			if(height(node.getLeft()) > height(node.getRight())){
				return 1 + height(node.getLeft());
			}
			else return 1 + height(node.getRight());
		}
		else return 0;
	}
	public int Dif(AVLtreenode<E> node){ // 자식 노드의 길이 차이
		if(node == null) return 0;
		else{
			return height(node.getLeft()) - height(node.getRight());
		}
	}
	public AVLtreenode<E> LL(AVLtreenode<E> node){
		AVLtreenode<E> B = node.getLeft();
		node.setLeft(B.getRight());
		B.setRight(node);
		return B;
	}
	public AVLtreenode<E> RR(AVLtreenode<E> node){
		AVLtreenode<E> B = node.getRight();
		node.setRight(B.getLeft());
		B.setLeft(node);
		return B;
	}
	public AVLtreenode<E> LR(AVLtreenode<E> node){
		AVLtreenode<E> B = node.getLeft();
		node.setLeft(RR(B));
		return LL(node);
	}
	public AVLtreenode<E> RL(AVLtreenode<E> node){
		AVLtreenode<E> B = node.getRight();
		node.setRight(LL(B));
		return RR(node); 
	}
	//AVLtree를 구성하기 위한 4가지 회전경우
	public AVLtreenode<E> MakeBalance(AVLtreenode<E> node){ //회전을 이용해서 Binary tree를 AVLtree로 만들기
		int dif = Dif(node);
		if(dif >= 2){
			if(Dif(node.getLeft()) >= 1){
				node = LL(node);
			}
			else{
				node = LR(node);
			}
		}
		else if(dif <= -2){
			if(Dif(node.getRight()) <= -1){
				node = RR(node);
			}
			else{
				node = RL(node);
			}
		}
		return node;
	}
	public AVLtreenode<E> insert(AVLtreenode<E> root, E newItem){ // 삽입할 때 마다 AVLtree 조건 확인(MakeBalance)
		if(root == null){
			root = new AVLtreenode<E>();
			root.setItem(newItem);
		}
		else if(root.getItem().compareTo(newItem) < 0){
			root.setRight(insert(root.getRight(),newItem));
			root = MakeBalance(root);
		}
		else{
			root.setLeft(insert(root.getLeft(),newItem));
			root = MakeBalance(root);
		}
		return root;
	}

	public void delete(E item){//don't need
	}
	public AVLtreenode<E> retrieve(E item){
		return retrieveItem(this, item);
	}
	public AVLtreenode<E> retrieveItem(AVLtreenode<E> tree, E item){
		if(tree == null) return null;
		else{
			if(item.compareTo(tree.getItem()) == 0) return tree;
			else if(tree.getItem().compareTo(item) < 0){
				return retrieveItem(tree.getRight(),item);
			}
			else{
				return retrieveItem(tree.getLeft(),item);
			}
		}
	}
	
}
