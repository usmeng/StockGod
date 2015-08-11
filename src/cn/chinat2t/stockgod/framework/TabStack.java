package cn.chinat2t.stockgod.framework;

import java.util.LinkedList;

public class TabStack {
	private LinkedList<String> views;
	
	public LinkedList<String> getViews() {
		return views;
	}

	public void setViews(LinkedList<String> views) {
		this.views = views;
	}
	
	public TabStack(){
		views = new LinkedList<String>();
	}
	
	public boolean isExist(String id){
		return views.contains(id);
	}
	
	public String top(){
		if(!views.isEmpty())
			return views.getFirst();
		return null;
	}
	
	public void pop(){
		if(!views.isEmpty()){
			views.removeFirst();
		}
	}
	
	public void pop(String id){
		if(isExist(id)){
			views.remove(id);
		}
	}
	
	public boolean isEmpty(){
		return views.isEmpty();
	}
	
	public void push(String view){
		views.addFirst(view);
	}
	
	public void clear(){
		views.clear();
	}
	
	public void popSome(int sum){
		for(int i = 0;i < sum;i++){
			pop();
		}
	}
	
	public int getTheSumToPop(String id){
		int index = views.indexOf(id);
		return index + 1;
	}
	
	public int getCurrentViewIndex(String id){
		return views.indexOf(id);
	}
	
	public void popSome(String id){
		popSome(getTheSumToPop(id));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(String id : views){
			builder.append(id);
			builder.append(",\t");
			
		}
		return builder.toString();
	}
	
	public int size(){
		return this.views.size();
	}
	
}
