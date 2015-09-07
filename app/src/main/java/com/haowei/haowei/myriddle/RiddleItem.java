package com.haowei.haowei.myriddle;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by haowei on 8/30/15.
 */
public class RiddleItem implements Serializable {
    private Long id;
    private int riddleId;
    private boolean solved;
    private boolean requestHint;
    private Calendar requestHintAt;
    private int friendsInvited;

    public RiddleItem(int riddleId, boolean solved, boolean requestHint, Calendar date,
                      int friendsInvited) {
        this.riddleId = riddleId;
        this.solved = solved;
        this.requestHint = requestHint;
        this.requestHintAt = date;
        this.friendsInvited = friendsInvited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRiddleId() {
        return riddleId;
    }

    public void setRiddleId(int riddleId) {
        this.riddleId = riddleId;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isRequestHint() {
        return requestHint;
    }

    public void setRequestHint(boolean requestHint) {
        this.requestHint = requestHint;
    }

    public Calendar getRequestHintAt() {
        return requestHintAt;
    }

    public void setRequestHintAt(Calendar requestHintAt) {
        this.requestHintAt = requestHintAt;
    }

    public int getFriendsInvited() {
        return friendsInvited;
    }

    public void setFriendsInvited(int friendsInvited) {
        this.friendsInvited = friendsInvited;
    }
}
