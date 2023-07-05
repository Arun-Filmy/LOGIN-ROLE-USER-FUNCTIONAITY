package com.LoginAndForget.LFF.user;

import com.LoginAndForget.LFF.user.role.Roles;
import com.LoginAndForget.LFF.user.role.dto.RoleDataResponse;
import com.LoginAndForget.LFF.user.dto.UserData;
import com.LoginAndForget.LFF.user.dto.UserDto;
import com.LoginAndForget.LFF.user.service.NewUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserAndRoleController {

    @Autowired
    private NewUserService userService;

    @GetMapping("/fetch/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getRole/{id}")
    public ResponseEntity<RoleDataResponse> getAllRoles(@PathVariable Long id) {
        Optional<Roles> role = userService.getRoleByIdWithUserDetails(id);
        if (role.isPresent()) {
            Roles roleData = role.get();
            Set<User> users = roleData.getUsers();

            List<UserData> userDataList = new ArrayList<>();

            for (User user : users) {
                // Extract necessary information from Users entity and create a UserData object
                UserData userData = new UserData();
                userData.setId((user.getId()));
                userData.setUsername(user.getEmail());
                userData.setPassword(user.getPassword());
                userDataList.add(userData);
            }
            RoleDataResponse roleDataResponse = new RoleDataResponse();
            roleDataResponse.setRoleData(roleData);
            roleDataResponse.setUserDataList(userDataList);

            return ResponseEntity.ok(roleDataResponse);
        } else {
            return ResponseEntity.notFound().build();
        }


    }
}

