package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.dto.AddressResponse;
import com.ogunkuade.employeemanagementsystem.dto.SearchResponse;
import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.feignclient.AddressClient;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


@Service
public class SearchService {


    private String searchedWorld;
    private List<SearchResponse> searchResponseList;
    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressClient addressClient;


    public String search(String wordToSearch){
        searchedWorld = wordToSearch;
        List<User> userList = userRepository.findUsersByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(wordToSearch, wordToSearch);
        if(wordToSearch.isBlank() || wordToSearch.isEmpty() || wordToSearch == null || wordToSearch.length() < 3){
            return "redirect:/search?invalidsearch";
        } else if(userList.size() < 1){
            return "redirect:/search?emptysearch";
        } else{
            searchResponseList = new ArrayList<>();
            ListIterator<User> iteratedUserList = userList.listIterator();
            User i;
            while(iteratedUserList.hasNext()){
                i = iteratedUserList.next();
                SearchResponse searchResponse = new SearchResponse();
                searchResponse.setId(i.getId());
                searchResponse.setUsername(i.getUsername());
                searchResponse.setFirstname(i.getFirstname());
                searchResponse.setLastname(i.getLastname());
                searchResponse.setGender(i.getGender());
                searchResponse.setEmail(i.getEmail());
                AddressResponse addressResponse = addressClient.gettingAddressById(i.getId());
                searchResponse.setLane1(addressResponse.getLane1());
                searchResponse.setLane2(addressResponse.getLane2());
                searchResponse.setZip(addressResponse.getZip());
                searchResponse.setState(addressResponse.getState());
                searchResponseList.add(searchResponse);
            }
            return "redirect:/search";
        }
    }


    public String searchResult(Model model){
        model.addAttribute("searchResponseList", searchResponseList);
        model.addAttribute("searchedWorld", searchedWorld);
        return "search_page";
    }




}
