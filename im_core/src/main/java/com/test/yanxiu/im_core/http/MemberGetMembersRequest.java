package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

public class MemberGetMembersRequest extends ImRequestBase {
    private String method = "member.getMembers";

    public String imMemberIds;      // IM用户id，多个用逗号分隔
}
