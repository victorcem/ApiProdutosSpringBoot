package br.com.victordev.springboot.controllers;

import br.com.victordev.springboot.dtos.AuthRecordDto;
import br.com.victordev.springboot.dtos.LoginResponseRecordDto;
import br.com.victordev.springboot.dtos.RegisterRecordDto;
import br.com.victordev.springboot.models.user.UserModel;
import br.com.victordev.springboot.repositories.UserRepository;
import br.com.victordev.springboot.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userAuthRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseRecordDto> login(@RequestBody @Valid AuthRecordDto authRecordDto) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(authRecordDto.login(), authRecordDto.password());
        var auth = this.authenticationManager.authenticate(userNamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseRecordDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody @Valid RegisterRecordDto registerRecordDto){
        if(this.userAuthRepository.findByLogin(registerRecordDto.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(registerRecordDto.password());
        var newUser = new UserModel(registerRecordDto.login(), encryptedPassword, registerRecordDto.role());

        this.userAuthRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
