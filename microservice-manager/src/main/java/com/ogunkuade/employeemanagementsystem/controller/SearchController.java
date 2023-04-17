package com.ogunkuade.employeemanagementsystem.controller;


import com.ogunkuade.employeemanagementsystem.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public String searching(@RequestParam(required = false) String wordToSearch){
        return searchService.search(wordToSearch);
    }

    @GetMapping("/search")
    public String searchingResult(Model model){
        return searchService.searchResult(model);
    }

}
