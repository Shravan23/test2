package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Profile;
import com.acs560.freelancing.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    String name;
    String email;
    Profile profile;
}
