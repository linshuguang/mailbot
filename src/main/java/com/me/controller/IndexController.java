package com.me.controller;

/**
 * Created by kenya on 2017/12/14.
 */

import com.me.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
        //return "index";
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show() {
        //return new ModelAndView("index");
        return "show";
    }

    @RequestMapping(value = "/testservice", method = RequestMethod.GET)
    public String testservice() {
        //return new ModelAndView("index");
        return "testservice";
    }


    /*
    @PostMapping(value = "/user")
    @ResponseBody
    public ResponseEntity<Object> saveUser(@Valid User user,
                                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.OK);
        } else {
            if (users.stream().anyMatch(it -> user.getEmail().equals(it.getEmail()))) {
                return new ResponseEntity<>(
                        Collections.singletonList("Email already exists!"),
                        HttpStatus.CONFLICT);
            } else {
                users.add(user);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }
    */


}
