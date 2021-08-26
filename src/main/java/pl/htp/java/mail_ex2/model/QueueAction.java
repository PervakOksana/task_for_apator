package pl.htp.java.mail_ex2.model;

public class QueueAction {
	
	private User user;
	private Action action;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	@Override
	public String toString() {
		return "QueueAction [user=" + user + ", action=" + action + "]";
	}
	
	

}
