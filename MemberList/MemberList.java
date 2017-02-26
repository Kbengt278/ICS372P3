package MemberList;

import Member.*;
import java.util.HashMap;

/**
 * MemberList Class
 * Creates an initial list of member objects
 */
public class MemberList {
    private HashMap<Integer, Member> memberList = new HashMap<Integer, Member>();

    public MemberList() {
        for (int i = 1; i < 5; i++) {
            memberList.put((Integer) i, new Member());
        }
    }

    public Member getMember(int id){
        return memberList.get((Integer)id);
    }

    public int getNumberMembers(){
        return memberList.size();
    }
}
