package com.starion.webapp.controllers;

import com.starion.webapp.models.Post;
import com.starion.webapp.models.User;
import com.starion.webapp.repositories.PostRepository;
import com.starion.webapp.services.UserService;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteController {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserService userService;

  @GetMapping("/site")
  public String siteMain(Model model) {
    Iterable<Post> posts = postRepository.findAll();
    model.addAttribute("posts", posts);
    return "site-main";
  }

  @GetMapping("/site/add")
  public String siteAdd(Model model) {
    return "site-add";
  }

  @PostMapping("/site/add")
  public String sitePostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
    Post post = new Post(title, anons, full_text);
    postRepository.save(post);
    return "redirect:/site";
  }

  @GetMapping("/site/{id}")
  public String siteDetails(@PathVariable(value = "id") long id, Model model) {
    if (!postRepository.existsById(id)){
      return "redirect:/site";    }
    Optional<Post> post = postRepository.findById(id);
    ArrayList<Post> res = new ArrayList<>();
    post.ifPresent(res::add);
    model.addAttribute("post", res);
    return "site-details";
  }

  @GetMapping("/site/{id}/edit")
  public String siteEdit(@PathVariable(value = "id") long id, Model model) {
    if (!postRepository.existsById(id)){
      return "redirect:/site";
    }
    Optional<Post> post = postRepository.findById(id);
    ArrayList<Post> res = new ArrayList<>();
    post.ifPresent(res::add);
    model.addAttribute("post", res);
    return "post-edit";
  }

  @PostMapping("/site/{id}/edit")
  public String sitePostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
    Post post = postRepository.findById(id).orElseThrow();
    post.setTitle(title);
    post.setAnons(anons);
    post.setFull_text(full_text);
    postRepository.save(post);

    return "redirect:/site";
  }

  @PostMapping("/site/{id}/delete")
  public String sitePostDelete(@PathVariable(value = "id") long id, Model model) {
    Post post = postRepository.findById(id).orElseThrow();
    postRepository.delete(post);

    return "redirect:/site";
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());

    return "sign_up";
  }

  @PostMapping("/register")
  public String processRegister(User user, Model model) {

    if (!userService.checkEmail(user)){
      model.addAttribute("emailError", "Пользователь с таким email уже существует");
      return "sign_up";
    }
    if (!userService.saveUser(user)){
      model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
      return "sign_up";
    }

    return "sign_up_success";
  }
}
