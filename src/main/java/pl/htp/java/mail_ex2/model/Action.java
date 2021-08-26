package pl.htp.java.mail_ex2.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Action implements Comparable<Action>{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String userName;
    private String userLogin;
    private String status;
    private LocalDateTime timeCurrent;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private int number;
    private String pin;
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
		public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public LocalDateTime getTimeCurrent() {
		return timeCurrent;
	}
	public void setTimeCurrent(LocalDateTime timeCurrent) {
		this.timeCurrent = timeCurrent;
	}
	public LocalDateTime getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(LocalDateTime timeEnd) {
		this.timeEnd = timeEnd;
	}	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
	
	public LocalDateTime getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(LocalDateTime timeStart) {
		this.timeStart = timeStart;
	}	
	
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	@Override
	public int compareTo(Action action) {
		return timeStart.compareTo(action.timeStart);
		
	}
	@Override
	public String toString() {
		return "Action [id=" + id + ", userId=" + userId + ", userName=" + userName + ", userLogin=" + userLogin
				+ ", status=" + status + ", timeCurrent=" + timeCurrent + ", timeStart=" + timeStart + ", timeEnd="
				+ timeEnd + ", number=" + number + ", pin=" + pin + "]";
	}
}
