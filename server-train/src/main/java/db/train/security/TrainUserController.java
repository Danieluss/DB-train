package db.train.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class TrainUserController {

    @Autowired
    private TrainUserRepository repo;
    @Autowired
    private TrainUserService service;

    @ModelAttribute("user")
    public TrainUserDTO userRegistrationDto() {
        return new TrainUserDTO();
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUserAccount(@ModelAttribute("user") @Valid TrainUserDTO userDTO,
                                      BindingResult result){

        TrainUser existing = repo.findByUsername(userDTO.getUsername());
        if (existing != null){
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        existing = repo.findByEmail(userDTO.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()){
            return "registration";
        }

        service.addEntity(TrainUser.from(userDTO));
        return "redirect:/login?registrationSuccess";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }
}
