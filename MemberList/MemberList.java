package MemberList;

import Member.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * MemberList Class
 * Creates an initial list of member objects
 */
public class MemberList implements Serializable{
    private HashMap<Integer, Member> memberList = new HashMap<Integer, Member>();
    private MemberIdServer iDserver = MemberIdServer.instance();

    public MemberList(){};

    public Member getMember(int id){
        return memberList.get((Integer)id);
    }

    public int getNumberMembers(){
        return memberList.size();
    }

    public int addMember(Member mem) {
        mem = new Member();
        int id = iDserver.getId();
        memberList.put((Integer) id, mem);
        return id;
    }
}
