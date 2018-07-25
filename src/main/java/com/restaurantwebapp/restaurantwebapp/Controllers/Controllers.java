package com.restaurantwebapp.restaurantwebapp.Controllers;

import com.restaurantwebapp.restaurantwebapp.Models.Menu;
import com.restaurantwebapp.restaurantwebapp.Models.MenuItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class Controllers {
    ArrayList<Menu> menus = new ArrayList<Menu>();
    HashMap<String, MenuItem> menuItems= new HashMap<String, MenuItem>();

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("menus",menus);
        model.addAttribute("menuItem", menuItems);
        return "index";
    }

    @RequestMapping(value="/createmenu", method = RequestMethod.GET)
    public String createmenudisplay(Model model) {
        model.addAttribute("menus",menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value="/createmenuitem", method = RequestMethod.GET)
    public String createmenuitemdisplay(Model model) {
        model.addAttribute("menus",menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenuitem";
    }

    @RequestMapping(value="/createmenuitem", method = RequestMethod.POST)
    public String createmenuitemdisplay(Model model,HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        if(name == "") {
            String error = "Operation failed! Named field is mandatory!";
            model.addAttribute("error", error);
            return "createmenuitem";
        }
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        float price = (request.getParameter("price") == "")? 0.0f : (Float.parseFloat(request.getParameter("price"))) ;
        if(menuItems.get(name) == null){
            MenuItem menuItem = new MenuItem(price, name, description, category);
            menuItems.put(name, menuItem);
        }
        model.addAttribute("menuItems", menuItems);
        return "createmenuitem";
    }

    @RequestMapping(value="deletemenuitem",method = RequestMethod.GET)
    public String deleteCreateMenuItem(Model model,HttpServletRequest request){
        String name = request.getParameter("name").trim();
        if(name != "" && menuItems.get(name) != null){
            menuItems.remove(name);
            return "redirect:/createmenuitem";
        }
        else{
            return "redirect:/";
        }
    }

    @RequestMapping(value="/addmenu", method = RequestMethod.GET)
    public String addmenudisplay(Model model) {
        model.addAttribute("menus",menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value="/addmenu", method = RequestMethod.POST)
    public String processaddmenudisplay(Model model, HttpServletRequest request) {
        String restuarant = request.getParameter("restuarant").trim();
        if(restuarant == "") return "redirect:/addmenu";
        menus.add(new Menu(restuarant));
        model.addAttribute("menus",menus);
        model.addAttribute("menuItem", menuItems);
        return "createmenu";
    }

    @RequestMapping(value="/addmenuitem", method = RequestMethod.GET)
    public String displayaddmenuitem(Model model){
        model.addAttribute("menus",menus);
        model.addAttribute("menuItems", menuItems);
        return "addmenuitem";
    }

    @RequestMapping(value="/addmenuitem", method = RequestMethod.POST)
    public String processaddmenuitem(Model model, @RequestParam(name = "menu", defaultValue = "") String menuname, @RequestParam(name="menuitem", defaultValue = "") String menuitemname ){
        if(menuitemname != "" && menuname != ""){
            MenuItem menuItem = menuItems.get(menuitemname);
            for(Menu menu: menus){
                if(menu.getRestuarant().equals(menuname)){
                    for(MenuItem existingMenuItem:menu.getMenuItems()){
                        if(existingMenuItem == menuItem) {
                            System.out.println("Exit without adding - Because, this is a duplicate entry");
                            return "redirect:/addmenuitem";
                        }
                    }
                    menu.addAnItem(menuItem);
                }
            }
        }
        return "redirect:/addmenuitem";
    }
}
