package MemberList;

import Member.Member;
import Member.MemberIdServer;

import java.io.Serializable;
import java.util.HashMap;

/**
 * MemberList Class
 * Creates an initial list of member objects
 */
public class MemberList implements Serializable {
    private HashMap<Integer, Member> memberList = new HashMap<>();
    private MemberIdServer iDserver = MemberIdServer.instance();

    public MemberList() {
    }

    public int addMember(Member mem) {
        mem = new Member();
        int id = iDserver.getId();
        memberList.put((Integer) id, mem);
        return id;
    }

    public Member getMember(int id) {
        return memberList.get((Integer) id);
    }

    public Member getMemberWithItem(String itemId) {
        for (Member member : memberList.values()) {
            if(member.hasItem(itemId)){
                return member;
            }
        }
        return null;
    }

    public int getNumberMembers() {
        return memberList.size();
    }
}
