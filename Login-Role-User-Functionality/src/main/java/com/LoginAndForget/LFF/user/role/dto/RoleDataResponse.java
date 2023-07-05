package com.LoginAndForget.LFF.user.role.dto;

import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.dto.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDataResponse {
    private Roles roleData;
    private List<UserData> userDataList;
}
