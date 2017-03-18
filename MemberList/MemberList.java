package MemberList;

import Member.Member;
import Member.MemberIdServer;
import storage.Storage;

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
    
    /**
     * Adds a member to memberList with a library card number
     *
     * @param name Name of new member
     * @return String display text
     */
    public Member createMember(String name)
    {
    	int id = iDserver.getId();
	    Member member = new Member(id, name);
	    memberList.put((Integer) id, member);
        return member;
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
