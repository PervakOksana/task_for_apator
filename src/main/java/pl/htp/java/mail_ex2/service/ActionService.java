package pl.htp.java.mail_ex2.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.htp.java.mail_ex2.constant.ConstantVariable;
import pl.htp.java.mail_ex2.model.Action;
import pl.htp.java.mail_ex2.model.User;
import pl.htp.java.mail_ex2.repository.ActionRepository;

@Service
public class ActionService {

	@Autowired
	private ActionRepository actionRepository;

	public List<Action> findAllAction() {
		checkTimeAction();
		List<Action> actionList = changeTime();
		return actionList;
	}

	public void saveAction(Action action, User user) {

		action.setUserId(user.getId());
		action.setUserLogin(user.getLogin());
		action.setUserName(user.getUserName());
		action.setTimeCurrent(LocalDateTime.now());
		action.setNumber(1000 + (int) (Math.random() * 9999));

		if (action.getStatus().equals(ConstantVariable.STATUS_VIP)
				&& !action.getPin().equals(ConstantVariable.STATUS_VIP_PIN)) {
			action.setStatus(ConstantVariable.WITHOUT_STATUS);
		}

		if (action.getStatus().equals(ConstantVariable.STATUS_SUDDEN)
				&& !action.getPin().equals(ConstantVariable.STATUS_SUDDEN_PIN)) {
			action.setStatus(ConstantVariable.WITHOUT_STATUS);
		}

		if (action.getStatus().equals(ConstantVariable.WITHOUT_STATUS)) {
			action.setTimeEnd(findLastAction().plusSeconds(120));
			action.setTimeStart(action.getTimeEnd().minusSeconds(120));
			actionRepository.save(action);
		}
		if (action.getStatus().equals(ConstantVariable.STATUS_VIP)) {

			if (hasActionSudden()) {

				action.setTimeStart(findLastActionSudden());
				action.setTimeEnd(action.getTimeStart().plusSeconds(120));
				actionRepository.save(action);
			}

			if (!hasActionSudden()) {
				action.setTimeStart(LocalDateTime.now());
				action.setTimeEnd(action.getTimeStart().plusSeconds(120));
				actionRepository.save(action);
			}
		}
		if (action.getStatus().equals(ConstantVariable.STATUS_SUDDEN)) {
			if (!hasActionVIP()) {
				action.setTimeStart(LocalDateTime.now());
				action.setTimeEnd(action.getTimeStart().plusSeconds(120));
				actionRepository.save(action);
			}

			if (hasActionVIP()) {
				action.setTimeStart(findFirstActionVIP());
				action.setTimeEnd(action.getTimeStart().plusSeconds(120));
				actionRepository.save(action);
			}

		}

	}

	public List<Action> changeTime() {
		checkTimeAction();
		List<Action> actionListSort = sortQueue(this.actionRepository.findAll());

		for (int i = 1; i < actionListSort.size(); i++) {

			if (actionListSort.get(i).getTimeStart().isBefore(actionListSort.get(i - 1).getTimeEnd())) {
				actionListSort.get(i).setTimeStart(actionListSort.get(i - 1).getTimeEnd());
				actionListSort.get(i).setTimeEnd(actionListSort.get(i).getTimeStart().plusSeconds(120));
			}

		}

		return actionListSort;
	}

	public List<Action> sortQueue(List<Action> actionList) {
		Collections.sort(actionList);
		return actionList;
	}

	public LocalDateTime findLastAction() {

		List<Action> actionList = findAllAction();
		LocalDateTime timeMax = LocalDateTime.now();

		if (actionList.size() > 0) {
			for (Action action : actionList) {

				if (action.getTimeEnd().isAfter(timeMax)) {
					timeMax = action.getTimeEnd();
				}
			}
		}

		return timeMax;
	}

	public LocalDateTime findLastActionSudden() {

		List<Action> actionList = findAllAction();

		LocalDateTime timeMax = LocalDateTime.now();

		if (hasActionSudden()) {
			for (Action action : actionList) {

				if (action.getTimeStart().isAfter(timeMax)
						&& action.getStatus().equals(ConstantVariable.STATUS_SUDDEN)) {
					timeMax = action.getTimeStart();
				}
			}
		}

		return timeMax;

	}

	public LocalDateTime findFirstActionVIP() {

		List<Action> actionList = findAllAction();

		LocalDateTime timeMin = actionList.get(actionList.size() - 1).getTimeStart();

		if (hasActionVIP()) {
			for (Action action : actionList) {

				if (action.getTimeStart().isBefore(timeMin) && action.getStatus().equals(ConstantVariable.STATUS_VIP)) {
					timeMin = action.getTimeStart();
				}
			}
		}

		return timeMin.minusSeconds(1);
	}

	public boolean hasActionSudden() {

		List<Action> actionList = findAllAction();
		boolean result = false;

		if (actionList.size() > 0) {
			for (Action action : actionList) {

				if (action.getStatus().equals(ConstantVariable.STATUS_SUDDEN)) {
					result = true;
				}
			}
		}

		return result;
	}

	public boolean hasActionVIP() {

		List<Action> actionList = findAllAction();
		boolean result = false;

		if (actionList.size() > 0) {
			for (Action action : actionList) {

				if (action.getStatus().equals(ConstantVariable.STATUS_VIP)) {
					result = true;
				}
			}
		}

		return result;
	}

	public void checkTimeAction() {

		List<Action> actionList = this.actionRepository.findAll();
		LocalDateTime time = LocalDateTime.now();

		if (actionList.size() > 0) {
			for (Action action : actionList) {

				if (action.getTimeEnd().isBefore(time)) {
					this.actionRepository.delete(action);
				}
			}
		}

	}

	public String timeForWait() {

		Action action = lastAction();
		LocalDateTime timeCurrent = LocalDateTime.now();
		int secs = (int) Duration.between(timeCurrent, action.getTimeStart()).toSeconds();
		long hour = secs / 3600, min = secs / 60 % 60, sec = secs / 1 % 60;
		return String.format("%02d:%02d:%02d", hour, min, sec);
	}

	public String timeForWait(String number) {

		Action action = findActionByNumber(number);
		LocalDateTime timeCurrent = LocalDateTime.now();

		int secs = (int) Duration.between(timeCurrent, action.getTimeStart()).toSeconds();
		long hour = secs / 3600, min = secs / 60 % 60, sec = secs / 1 % 60;
		return String.format("%02d:%02d:%02d", hour, min, sec);
	}

	public Action lastAction() {
		List<Action> actionList = findAllAction();
		long maxId = 0;
		for (Action action : actionList) {

			if (action.getId() > maxId) {
				maxId = action.getId();
			}
		}
		Action action = this.actionRepository.getById(maxId);
		return action;
	}

	public Action findActionByNumber(String number) {

		List<Action> actionList = findAllAction();
		Action actionFind = new Action();

		if (checkString(number)) {
			int numberFind = Integer.parseInt(number);
			for (Action action : actionList) {

				if (action.getNumber() == numberFind) {
					actionFind = action;
				}
			}
		}
		return actionFind;
	}

	public boolean checkString(String string) {
		try {
			Integer.parseInt(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public int amout(String number) {
		List<Action> actionList = findAllAction();
		int result = 0;
		if (checkString(number)) {
			for (int i = 0; i < actionList.size(); i++) {

				if (Integer.parseInt(number) == actionList.get(i).getNumber()) {

					result = i;
				}
			}
		}
		return result;
	}

	public int amout(Action action) {
		List<Action> actionList = findAllAction();
		int result = 0;
		if (action.getStatus().equals(ConstantVariable.STATUS_SUDDEN)) {
			for (Action actionN : actionList) {
				if (actionN.getStatus().equals(ConstantVariable.STATUS_SUDDEN)) {
					result++;
				}
			}
			result--;
		}
		if (action.getStatus().equals(ConstantVariable.STATUS_VIP)) {
			for (Action actionN : actionList) {
				if (actionN.getStatus().equals(ConstantVariable.STATUS_VIP)) {
					result++;
				}
			}
			result--;
		}
		if (action.getStatus().equals(ConstantVariable.WITHOUT_STATUS)) {
			result = actionList.size() - 1;
		}
		return result;
	}
}
