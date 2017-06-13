package com.ves.main.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Controller
@RequestMapping("/date")
public class DateController {
    @RequestMapping(method = RequestMethod.GET)
    public String date(ModelMap model, HttpServletRequest request) {

    	Date date = new Date();
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	df.setTimeZone(TimeZone.getTimeZone("Europe/Istanbul"));
    	
    	model.addAttribute("time", df.format(date));
    	
        return "date";
    }
}
